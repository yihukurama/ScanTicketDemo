/**
 * @package 	com.iecheck.app.data.controller.globalrequest
 * @file 		RequestHandler.java
 * @time 		2014年10月29日
 * @author 		Administrator
 **/
package com.yihukurama.app.scanticketdemo.opensource.api.http;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yihukurama.app.scanticketdemo.opensource.api.http.responebean.BaseResponseBean;
import com.yihukurama.app.scanticketdemo.opensource.api.http.responebean.YunduanPosBean;
import com.yihukurama.app.scanticketdemo.utils.JsonUtil;
import com.yihukurama.app.scanticketdemo.utils.LogUtil;

/**
 * @author DengShuai
 *
 */
public class RequestHandler extends Handler {
	private Context mContext;
	private Activity baseActivity;
	public RequestHandler(Context mContext){
		this.mContext = mContext;
		try{
			baseActivity = (Activity)mContext;
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	@Override
	public void handleMessage(Message msg) {
		Log.i(LogUtil.TAG_NOHTTP,"处理服务器响应");
		if(msg==null || msg.getData()==null ||  msg.getData().get("RESPONSE")==null){
			if(msg!=null)
				ErrorAction(-1);
			Log.i("yunpos","请求错误");
			return;
		}
		Bundle b = msg.getData();
		String responseString = (String)b.get("RESPONSE");
		BaseResponseBean baseBean = null;
		Log.i(LogUtil.TAG_NOHTTP,"json"+responseString);
		try {
			baseBean = JsonUtil.jsonToBean(responseString,BaseResponseBean.class);
		} catch (Exception e) {
			e.printStackTrace();
			//json转化为类错误
			Log.i("debug","服务器返回基类转化错误");
			return;
		}


		switch (msg.what) {


			case RequestSinal.YUNDUNPOS://获取卡券列表


				YunduanPosBean yunduanPosBean = null;

				Log.i("debug",responseString);
				try {
					yunduanPosBean = JsonUtil.jsonToBean(responseString,YunduanPosBean.class);
				} catch (Exception e) {
					e.printStackTrace();
					//json转化为类错误
					Log.i("debug","服务器返回实体类转化错误");
					break;
				}
				Log.i("debug","转化完成");
				yunduanPosAction(yunduanPosBean);
				break;



			default:

				break;
		}


		super.handleMessage(msg);
	}

	protected void yunduanPosAction(YunduanPosBean yunduanPosBean) {

	}



	/**
	 * 网络错误等要处理的事情
	 * @param sinal 
	 */
	protected void ErrorAction(int sinal) {}
	
}
