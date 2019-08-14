package com.fnf.router;

import android.app.Application;

/**
 * Created by ZhangShuai on 2018/6/26.
 */
public class RouterApp extends Application {

    private static RouterApp routerApp;


    public static RouterApp getRouterApp() {
        return routerApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        routerApp = this;
        FRouter.init(this);
    }
}
