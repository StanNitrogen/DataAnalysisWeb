package com.songtech.jdbc;

import java.sql.*;
import java.util.*;

/**
 * Create By YINN on 2018/1/26 10:09
 * Description :
 */
public class JDBCObject {

    private String ConnectionPath = "jdbc:mysql://120.79.28.47:3306/cmcc_stable?useUnicode=true&characterEncoding=UTF-8";
    private String username = "root";
    private String password = "1234";

    Connection conn = null;
    Statement st = null;

    public JDBCObject() throws Exception {
        init();
        st = conn.createStatement();
    }

    public JDBCObject(String connectionPath, String username, String password) throws Exception {
        ConnectionPath = connectionPath;
        this.username = username;
        this.password = password;
        init();
        st = conn.createStatement();
    }

    public static void main(String[] args) throws Exception {
        JDBCObject jdbcObject = new JDBCObject();
        List l = jdbcObject.query("select dict_value from sys_dictionary where dictgroup_id = '038'");
    }

    public boolean execute(String sql) throws SQLException {
        return this.getSt().execute(sql);
    }

    public int update(String sql) throws SQLException {
        return this.getSt().executeUpdate(sql);
    }

    //"select dict_value from sys_dictionary where dictgroup_id = '038'"
    public List<Map<String,Object>> query(String sql) throws Exception {
        ResultSet rs = this.getSt().executeQuery(sql);
        ResultSetMetaData resultMetaData = rs.getMetaData();

        int cols = resultMetaData.getColumnCount();
        //get the count of all the coulums ,this will be 5
        List<Map<String,Object>> currentRow = new ArrayList();
        while (rs.next()) {
            Map<String,Object> map = new HashMap<>();
            for (int j = 1; j <= cols; j++) {
                switch (resultMetaData.getColumnType(j))
                //translate the column of table type to java type then write to vector
                {
                    case Types.VARCHAR:
                        map.put(resultMetaData.getColumnName(j),rs.getString(resultMetaData.getColumnName(j)));
                        break;
                    case Types.INTEGER:
                        map.put(resultMetaData.getColumnName(j),new Integer(rs.getInt(resultMetaData.getColumnName(j))));
                        break;
                    case Types.TIMESTAMP:
                        map.put(resultMetaData.getColumnName(j),rs.getDate(resultMetaData.getColumnName(j)));
                        break;
                    case Types.DOUBLE:
                        map.put(resultMetaData.getColumnName(j),rs.getDouble(resultMetaData.getColumnName(j)));
                        break;
                    case Types.FLOAT:
                        map.put(resultMetaData.getColumnName(j),rs.getFloat(resultMetaData.getColumnName(j)));
                        break;
                    case Types.CLOB:
                        map.put(resultMetaData.getColumnName(j),rs.getBlob(resultMetaData.getColumnName(j)));
                        break;
                    default:
                        map.put(resultMetaData.getColumnName(j),rs.getString(resultMetaData.getColumnName(j)));
                }
            }
            currentRow.add(map);
        }
        return currentRow;
    }

    //获取conn
    public void init() throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(ConnectionPath, username, password);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //释放conn
    public void destroy() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //遍历数据库表
    public List<String> ergodicTables() throws SQLException {
        List<String> tables = new ArrayList<String>();
        DatabaseMetaData dbMetaData = conn.getMetaData();
        ResultSet rs = dbMetaData.getTables(null, null, null, new String[]{"TABLE"});

        StringBuffer sbTables = new StringBuffer();
        while (rs.next()) {
            sbTables.append("表名：" + rs.getString("TABLE_NAME") + "<br/>");
            sbTables.append("表类型：" + rs.getString("TABLE_TYPE") + "<br/>");
            sbTables.append("表所属数据库：" + rs.getString("TABLE_CAT") + "<br/>");
            sbTables.append("表所属用户名：" + rs.getString("TABLE_SCHEM") + "<br/>");
            sbTables.append("表备注：" + rs.getString("REMARKS") + "<br/>");
            sbTables.append("------------------------------<br/>");

            tables.add(rs.getString("TABLE_NAME"));
        }

        return tables;
    }

    public Connection getConn() {
        return conn;
    }

    public Statement getSt() {
        return st;
    }

    public String getConnectionPath() {
        return ConnectionPath;
    }

    public void setConnectionPath(String connectionPath) {
        ConnectionPath = connectionPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
