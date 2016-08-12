package com.yihukurama.app.scanticketdemo.opensource.api.http;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yihukurama.app.scanticketdemo.opensource.api.http.responebean.YunduanPosBean;
import com.yihukurama.app.scanticketdemo.opensource.api.http.transfor.Params2JsonObject;
import com.yihukurama.app.scanticketdemo.opensource.nohttp.CallServer;
import com.yihukurama.app.scanticketdemo.opensource.nohttp.listener.HttpListener;
import com.yihukurama.app.scanticketdemo.utils.LogUtil;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by dengshuai on 16/6/18.
 * 用于发送http请求，对用到的nohttp开源框架进行封装
 */
public class HttpApi {


    private Context context;
    private Handler mHandler;
    public HttpApi(Context context, Handler handle){
        this.mHandler = handle;
        this.context = context;
    }
//    public final static String API_PATH = "https://api.test.iecheck.com/dbservice/";
    public final static String API_PATH = "http://testapi.iecheck.com/dbservice/";

//    public final static String API_PATH = "http://119.29.251.110:8080/api/";





    public void yunduanPos(String action, String qrcode, String pos_id){

        Map<String,String> params = new HashMap<String,String>();
        params.put("action",action);
        params.put("qrcode",qrcode);
        params.put("pos_id",pos_id);
        //把参数转化成JsonObject
        JSONObject requestJsonObject = Params2JsonObject.yunduanPosApi(params);
        Log.i(LogUtil.TAG_NOHTTP,requestJsonObject.toString());
        //构造请求
        Request<String> mRequest= NoHttp.createStringRequest(API_PATH, RequestMethod.POST);
        mRequest.setDefineRequestBodyForJson(requestJsonObject);
        // 设置这个请求的tag，NoHttp的请求会为你保持这个tag，在成功或者失败时返回,这里设置responseBean方便自动转化
        mRequest.setTag(YunduanPosBean.class);
        //初始化自己的listener
        HttpListener httpListener = new BeseListener();
        // what: 用来区分请求，当多个请求使用同一个OnResponseListener时，在回调方法中会返回这个what，相当于handler的what一样
        // request: 请求对象，包涵Cookie、Head、请求参数、URL、请求方法
        // Listener 请求结果监听，回调时把what原样返回
        CallServer.getRequestInstance().add(context, RequestSinal.YUNDUNPOS, mRequest, httpListener, false, true);
    }


    /**
     * 成功响应服务器返回信息分发
     */
    private void dispatchResponeMessage(int requestSinal, Response<String> response) {
        Log.i(LogUtil.TAG_NOHTTP,"分发响应");
        Message msg = new Message();
        msg.what = requestSinal;
        Bundle b = new Bundle();
        b.putSerializable("RESPONSE", response.get());
        msg.setData(b);
        mHandler.dispatchMessage(msg);
    }

    private class BeseListener implements HttpListener<String> {

        @Override
        public void onSucceed(int what, Response<String> response) {
            Log.i(LogUtil.TAG_NOHTTP,response.toString());
                //把成功的请求结果分发出去
            dispatchResponeMessage(what,response);
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                //请求失败的处理
        }
    }
}
