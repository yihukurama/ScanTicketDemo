package com.yihukurama.app.scanticketdemo.opensource.api.http.transfor;


import com.yihukurama.app.scanticketdemo.utils.md5.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by dengshuai on 16/1/7.
 * 把用于请求的参数转化为Json串
 */
public class Params2JsonObject {



    public static JSONObject yunduanPosApi(Map<String,String> params){
        JSONObject object = new JSONObject();
        JSONObject object2 = new JSONObject();
        try {
            object.put("m", "db");
            object.put("oper", "query");
            object.put("sqlid", "9901");


            String action = params.get("action");
            String pos_id = params.get("pos_id");
            String qrcode = params.get("qrcode");
            object2.put("action", action);
            object2.put("qrcode", qrcode);
            object2.put("partner", "yunduan");
            object2.put("pos_id", pos_id);
            String sign = MD5Utils.encrypt(action+"yunduan-test"+pos_id+qrcode,true);
            object2.put("sign",sign);//算法=md5(action+token+pos_id+qrcode)


            object.put("params", object2);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return object;

    }


}
