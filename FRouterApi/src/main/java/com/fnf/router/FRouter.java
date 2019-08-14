package com.fnf.router;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import dalvik.system.DexFile;

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

    public static void init(Application context) {
        try {
            Set<String> fileNameByPackageName = getFileNameByPackageName(context, "com.process.router");
            for (String s : fileNameByPackageName) {
                Log.i("FRouter", "------fileNameByPackageName---" + s);
                try {
                    Class clz = Class.forName(s);
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
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    public static Set<String> getFileNameByPackageName(Application context, final String packageName)
            throws PackageManager.NameNotFoundException, InterruptedException {
        final Set<String> classNames = new HashSet<>();
        List<String> paths = getSourcePaths(context);
        //使用同步计数器判断均处理完成
        final CountDownLatch countDownLatch = new CountDownLatch(paths.size());
        ThreadPoolExecutor threadPoolExecutor = DefaultPoolExecutor.newDefaultPoolExecutor(paths.size());
        for (final String path : paths) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    DexFile dexFile = null;
                    try {
                        //加载 apk中的dex 并遍历 获得所有包名为 {packageName} 的类
                        dexFile = new DexFile(path);
                        Enumeration<String> dexEntries = dexFile.entries();
                        while (dexEntries.hasMoreElements()) {
                            String className = dexEntries.nextElement();
                            if (!TextUtils.isEmpty(className) && className.startsWith(packageName)) {
                                classNames.add(className);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (null != dexFile) {
                            try {
                                dexFile.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        //释放一个
                        countDownLatch.countDown();
                    }
                }
            });
        }
        //等待执行完成
        countDownLatch.await();
        return classNames;
    }

    private static List<String> getSourcePaths(Context context) throws PackageManager.NameNotFoundException {
        ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
        List<String> sourcePaths = new ArrayList<>();
        sourcePaths.add(applicationInfo.sourceDir);
        //instant run
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null != applicationInfo.splitSourceDirs) {
                sourcePaths.addAll(Arrays.asList(applicationInfo.splitSourceDirs));
            }
        }
        return sourcePaths;
    }

}
