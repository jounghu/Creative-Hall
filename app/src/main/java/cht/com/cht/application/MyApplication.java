package cht.com.cht.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Locale;

import cht.com.cht.utils.Constants;
import cn.jpush.android.api.JPushInterface;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/11/17.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initFresco();//初始化 fresco 图片加载
        initMobSmss();//初始化 mob 短信平台
        initLangrage(); //国际化
        initJpush();//初始化Jpush推送平台


    }

    private void initJpush() {
        //// TODO: 2016/12/7 正式上线记得改为false  然后后台ip记得改
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
      //  JPushInterface.setDefaultPushNotificationBuilder();
    }

    private void initMobSmss() {
        SMSSDK.initSDK(this, Constants.Mob.APP_KEY, Constants.Mob.APP_SECRET);
    }

    private void initFresco() {
        Fresco.initialize(this);
    }

    private void initLangrage() {
        Resources resources =getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        config.locale = Locale.CHINA; //美式英语
        resources.updateConfiguration(config, dm);
    }

    public static Context getContext(){
        return context;
    }
}
