package com.songtech.servlet;

import com.songtech.jdbc.JDBCObject;
import com.songtech.jdbc.JDBCUitls;
import com.songtech.json.YJSON;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Create By YINN on 2018/2/2 10:39
 * Description :
 */
public class QueryServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String excelName = new String(request.getParameter("excelName").getBytes("iso8859-1"), "UTF-8");
        try {
            JDBCObject jdbc = JDBCUitls.AnalysisBaseJDBC();

            //完整信息查询
            String sql = "select a.is_not_null,a.total,a.sql_error,a.consistence, \n" +
                                "ROUND(is_not_null * 100 / inn_total,4) inn_rate, \n" +
                                "Round(sql_error * 100 / sql_total,4) sql_rate, \n" +
                                "Round(consistence * 100 / total,4) consistence_rate, \n" +
                                "b.* from  \n" +
                                "(SELECT \n" +
                                        "attach_id, \n" +
                                        "IFNULL( SUM( is_not_null ), 0 ) is_not_null, \n" +
                                        "IFNULL( SUM( total ), 0 ) total, \n" +
                                        "IFNULL( sum( sql_error ), 0 ) sql_error, \n" +
                                        "IFNULL( SUM( consistence ), 0 ) consistence,\n" +
                                        "IFNULL( SUM( inn_total ), 0 ) inn_total,\n" +
                                        "IFNULL( SUM( sql_total ), 0 ) sql_total\n" +
                                "FROM \n" +
                                    "analysis_message  \n" +
                                "GROUP BY \n" +
                                    "attach_id) a, \n" +
                            "analysis_attach b \n" +
                        "where a.attach_id = b.attach_id";
            if (StringUtils.isNotBlank(excelName)) {
                sql += " and file_name like '%" + excelName + "%'";
            }

            List<Map<String, Object>> results = jdbc.query(sql);

            jdbc.destroy();
            request.setAttribute("result", results);
            //跳转到 index.jsp
            getServletContext().getRequestDispatcher("/index.jsp").forward(
                    request, response);
        } catch (Exception e) {

            getServletContext().getRequestDispatcher("/index.jsp").forward(
                    request, response);
            e.printStackTrace();
        }
    }

}
