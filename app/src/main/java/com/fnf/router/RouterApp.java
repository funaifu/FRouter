package com.fnf.router;

import android.app.Application;

/**
 * Created by ZhangShuai on 2018/6/26.
 */
public class RouterApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FRouter.init(this);
    }
}
