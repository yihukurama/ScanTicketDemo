package com.yihukurama.app.scanticketdemo.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;

import java.util.List;

/**
 * Created by dengshuai on 16/1/6.
 *
 */
public class JsonUtil {
    static{
        //JSON全是大写时首字母不自动变小写
        TypeUtils.compatibleWithJavaBean = true;
    }
    private static final String tag = JsonUtil.class.getSimpleName();

    /**
     * 将json字符串转换成java对象
     * @param json
     * @param cls
     * @return
     */
    public static <T> T jsonToBean(String json, Class<T> cls) throws Exception{
        return JSON.parseObject(json, cls);
    }

    /**
     * 将json字符串转换成java List对象
     * @param json
     * @param cls
     * @return
     * @throws Exception
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) throws Exception {
        return JSON.parseArray(json, cls);
    }

    /**
     * 将bean对象转化成json字符串
     * @param obj
     * @return
     * @throws Exception
     */
    public static String beanToJson(Object obj) throws Exception{
        String result = JSON.toJSONString(obj);

        return result;
    }
}
