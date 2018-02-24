package com.songtech.json;

import java.awt.image.renderable.RenderableImage;
import java.util.List;
import java.util.Map;

/**
 * Create By YINN on 2018/2/7 16:28
 * Description :
 */
public class YJSON {
    private static final String L = "[";
    private static final String R = "]";
    private static final String LE = "{";
    private static final String RI = "}";
    private static final String SP = "\"";
    private static final String COLON = ":";
    private static final String COMMA = ",";

    public static String toStringJSON(List<Map<String, Object>> list) {
        if (list == null)
            return null;

        StringBuffer sb = new StringBuffer("");
        int listSize = list.size();
        if (listSize > 1){
            sb.append(L);
        }

        for (int i=0;i<list.size();i++){
            Map<String,Object> map = list.get(i);
            int index = 0;
            int siz = map.size() - 1;
            for (Map.Entry<String,Object> entry : map.entrySet()){

                if (index == 0){
                    sb.append(LE);
                }

                sb.append(SP);
                sb.append(entry.getKey());
                sb.append(SP);
                sb.append(COLON);
                sb.append(SP);
                sb.append(entry.getValue());
                sb.append(SP);

                if (index < siz){
                    sb.append(COMMA);
                }
                if (index == siz){
                    sb.append(RI);
                }
                index++;
            }
            if (i < listSize - 1){
                sb.append(COMMA);
            }

        }

        if (listSize > 1){
            sb.append(R);
        }
        return sb.toString();
    }
}
