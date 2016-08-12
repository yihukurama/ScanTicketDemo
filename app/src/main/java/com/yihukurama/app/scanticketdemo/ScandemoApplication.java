package com.yihukurama.app.scanticketdemo;

import android.app.Application;

import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

/**
 * Created by dengshuai on 16/7/30.
 */
public class ScandemoApplication extends Application {
    @Override
    public void onCreate() {

        super.onCreate();

        NoHttp.initialize(this);
        Logger.setTag("nohttp");
        Logger.setDebug(true);// 开始NoHttp的调试模式, 这样就能看到请求过程和日志
    }
}
