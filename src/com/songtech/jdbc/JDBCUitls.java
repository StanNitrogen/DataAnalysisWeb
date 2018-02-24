package com.songtech.jdbc;

/**
 * Create By YINN on 2018/2/8 10:07
 * Description :
 */
public class JDBCUitls {
    public static JDBCObject JDBCFactory(String path, String username, String password) {
        try {
            return new JDBCObject(path,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JDBCObject AnalysisBaseJDBC(){
        try {
            return new JDBCObject("jdbc:mysql://120.79.28.47:3306/cmcc_data_analysis?useUnicode=true&characterEncoding=UTF-8",
                    "root", "1234");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
