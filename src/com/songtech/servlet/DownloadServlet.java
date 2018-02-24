package com.songtech.servlet;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Create By YINN on 2018/2/1 18:04
 * Description :
 */
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //处理请求
        String path = request.getParameter("path");
        String filePath = getServletContext().getRealPath("./") + File.separator;
        if (StringUtils.isNotBlank(path)){
            filePath += path;
        }
        //读取要下载的文件
        File f = new File(filePath);
        if (f.exists()) {
            FileInputStream fis = new FileInputStream(f);
            String filename = URLEncoder.encode(f.getName(), "utf-8"); //解决中文文件名下载后乱码的问题
            byte[] b = new byte[fis.available()];
            fis.read(b);
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
            //获取响应报文输出流对象
            ServletOutputStream out = response.getOutputStream();
            //输出
            out.write(b);
            out.flush();
            out.close();
        }
    }
}
