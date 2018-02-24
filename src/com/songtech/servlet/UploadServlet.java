package com.songtech.servlet;

import com.songtech.Anaylysis;
import com.songtech.jdbc.JDBCObject;
import com.songtech.jdbc.JDBCUitls;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Create By YINN on 2018/2/2 10:27
 * Description : workbook-upload & analysis workbook Servlet
 */
public class UploadServlet extends HttpServlet {

    // 上传文件存储目录
    private static final String UPLOAD_DIRECTORY = "upload";

    private static final String SEP = "/";

    // 上传配置
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 检测是否为多媒体上传
        if (!ServletFileUpload.isMultipartContent(request)) {
            // 如果不是则停止
            PrintWriter writer = response.getWriter();
            writer.println("Error: 表单必须包含 enctype=multipart/form-data");
            writer.flush();
            return;
        }

        // 配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存临界值 - 超过后将产生临时文件并存储于临时目录中
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // 设置临时存储目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setHeaderEncoding("UTF-8");
        // 设置最大文件上传值
        upload.setFileSizeMax(MAX_FILE_SIZE);

        // 设置最大请求值 (包含文件和表单数据)
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // 构造临时路径来存储上传的文件
        // 这个路径相对当前应用的目录
        String uploadPath = getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;


        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        try {
            // 解析请求的内容提取文件数据
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);

            if (formItems != null && formItems.size() > 0) {

                JDBCObject jdbc = JDBCUitls.AnalysisBaseJDBC();

                String modelPath = request.getSession().getServletContext().getRealPath("/") + "/resource/数据字典模板.xlsx";
                // 迭代表单数据
                for (FileItem item : formItems) {
                    // 处理不在表单中的字段
                    if (!item.isFormField()) {
                        //文件名
                        String fileName = new File(item.getName()).getName();
                        //文件存储绝对路径 父级目录
                        String parentPath = uploadPath + File.separator;
//                        File storeFile = new File(parentPath + fileName);

                        Anaylysis al = new Anaylysis(modelPath);
                        //输出结果
                        Workbook workbook = al.resolve(item.getInputStream());
                        //后缀
                        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                        //附件id
                        String attach_id = UUID.randomUUID().toString().replace("-", "");
                        //真实名
                        String saveName = attach_id + "." + suffix;

                        //文件转储
                        File storeFile = new File(parentPath + saveName);
                        OutputStream os = new FileOutputStream(storeFile);

                        workbook.write(os);

                        os.flush();
                        os.close();

                        //数据入库
                        saveAttach(jdbc, attach_id, fileName, SEP + UPLOAD_DIRECTORY + SEP + saveName, storeFile.length());
                        saveErrorMsg(jdbc, al.getStatisMap(), attach_id);

////                      // 保存文件到硬盘
//                        item.write(storeFile);
                        request.setAttribute("message",
                                "数据分析成功!");
                    }
                }
                jdbc.destroy();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("message",
                    "数据分析失败,请检查您的excel是否合乎规范!");
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        String jsonStr = "{\"success\":\"1\"}";
//        String jsonStr = "{\"success\":\"1\",\"type\":\"虫子\"}";
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        // 跳转到 message.jsp
//        getServletContext().getRequestDispatcher("/index.jsp").forward(
//                request, response);
    }

    /**
     * 保存附件信息
     * */
    private void saveAttach(JDBCObject jdbc, String attach_id, String file_name, String file_path, long size) throws SQLException {
        String sql = "INSERT INTO analysis_attach ("
                + "attach_id,file_name,file_path,file_size,create_time)VALUES('"
                + attach_id + "','"
                + file_name + "','"
                + file_path + "','"
                + size + "',"
                + "now() )";
//        System.out.println("附件:" + sql);
        jdbc.execute(sql);
    }

    /**
     * 保存错误信息
     * */
    private void saveErrorMsg(JDBCObject jdbc, Map<Integer, Map<String, Integer>> errorMap, String attach_id) throws SQLException {
        for (int i = 0; i < errorMap.size(); i++) {
            Map<String, Integer> eMap;
            if (errorMap != null && errorMap.get(i) != null) {
                eMap = errorMap.get(i);
                String sql = "INSERT INTO analysis_message ( attach_id, status, is_not_null, total, sql_error, inn_total, sql_total, consistence, create_time )\n" +
                        "VALUES('"
                        + attach_id + "','"
                        + i + "','"
                        + (eMap.get("isNotNullErrorNum") == null ? "0" : eMap.get("isNotNullErrorNum")) + "','"
                        + (eMap.get("total") == null ? "0" : eMap.get("total")) + "','"
                        + (eMap.get("sqlErrorSum") == null ? "0" : eMap.get("sqlErrorSum")) + "','"
                        + (eMap.get("innTotal") == null ? "0" : eMap.get("innTotal")) + "','"
                        + (eMap.get("sqlTotal") == null ? "0" : eMap.get("sqlTotal")) + "','"
                        + (eMap.get("consistence") == null ? "0" : eMap.get("consistence")) + "',"
                        + "now() )";
//                System.out.println("错误信息：" + sql);
                jdbc.execute(sql);
            }
        }
    }

}
