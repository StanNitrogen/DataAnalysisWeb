package com.songtech.servlet;

import com.songtech.jdbc.JDBCObject;
import com.songtech.jdbc.JDBCUitls;
import com.songtech.json.YJSON;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Create By YINN on 2018/2/8 10:05
 * Description :
 */
public class DetailQueryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        JDBCObject jdbc = JDBCUitls.AnalysisBaseJDBC();

        String result = "{}";
        if (StringUtils.isNotBlank(id)){
            String sql = "select * from analysis_message where attach_id = '" + id + "' order by status";
            try {
                List<Map<String,Object>> list = jdbc.query(sql);
                result = YJSON.toStringJSON(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
//        response.setHeader("Content-type: text/html; charset=utf8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
