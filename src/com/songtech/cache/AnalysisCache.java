package com.songtech.cache;

import com.songtech.AnaUtil;
import com.songtech.jdbc.JDBCObject;
import com.songtech.jdbc.JDBCUitls;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create By YINN on 2018/2/9 14:10
 * Description :
 */
public class AnalysisCache extends HttpServlet {

    public void init() {

        System.out.println("这样在web容器启动的时候，就会执行这句话了！");

        String idSql = "select distinct dictgroup_id from sys_dictionary where dictgroup_id is not null order by dictgroup_id";

        JDBCObject jdbc = null;
        try {
            jdbc = new JDBCObject();
            List<Map<String, Object>> idList = jdbc.query(idSql);

            int size = idList.size();
            for (int i = 0; i < size; i++) {
                Map<String, Object> map = idList.get(i);
                String id = (String) map.get("dictgroup_id");
                if (StringUtils.isNotBlank(id)) {
                    String sql = "select dict_value from sys_dictionary where dictgroup_id = '" + id + "'";
                    List<Map<String, Object>> results = jdbc.query(sql);

                    if (results == null || results.size() < 1){
                        continue;
                    }

                    List<String> cacheList = new ArrayList<>();
                    for (Map<String,Object> m : results) {
                        cacheList.add((String) m.get("dict_value"));
                    }

                    CacheManager.putCache(id,cacheList);
                }
            }

            //读取配置文件
            String path = getServletContext().getRealPath("./") + File.separator+ "resource\\数据字典模板.xlsx";
            Map<Integer, Map<String, Map<String, Object>>> configsMap = AnaUtil.resolveConfig(path);
            CacheManager.putCache("configs",configsMap);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (jdbc != null)
            jdbc.destroy();
        }
    }

}
