package com.fnf.router;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @ProjectName: FRouter
 * @Package: com.fnf.router
 * @ClassName: FRouter
 * @Description: java类作用描述
 * @Author: Fu_NaiFu
 * @CreateDate: 2019/8/10 18:17
 * @UpdateUser: 更新者：Fu_NaiFu
 * @UpdateDate: 2019/8/10 18:17
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class FRouter {
    public static HashMap<String, Class<?>> activityMap = new HashMap<>();

    public static void init(Context context) {
        try {
            Class clz = Class.forName("com.process.router.RouteMap");
            if (clz != null) {
                Log.i("FRouter", "--------------------------execute");
                Method initMapMethod = clz.getMethod("initActivityMap", HashMap.class);
                initMapMethod.invoke(null, activityMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("FRouter", "-----------------------" + e);
        }

    }


    public static void startActivity(Context context, String path, Bundle bundle) {
        if (context == null || path == null) {
            return;
        }
        Class atClazz = activityMap.get(path);
        if (atClazz != null) {
            System.out.println(atClazz.getCanonicalName());
            Intent intent = new Intent(context, atClazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            context.startActivity(intent);
        } else {
            System.out.println("=====Activity is null.");
        }


    }
}
