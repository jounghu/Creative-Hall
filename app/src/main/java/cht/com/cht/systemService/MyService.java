package cht.com.cht.systemService;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.model.User;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;

/**
 * Created by Administrator on 2016/11/22.
 */
public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle userInfo = intent.getExtras();

        int update_save = userInfo.getInt("update_save");

        LogUtil.e(Constants.TAG, "onStartCommand: "+update_save );
        final User user = (User) userInfo.getSerializable("user");
        LogUtil.e(Constants.TAG, "onStartCommand: "+ user.toString() );
        //执行更新
        if(update_save==1){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean is =  MyAppDB.getSingleton(MyApplication.getContext()).updateUser(user);
                    LogUtil.e(Constants.TAG,"success ????"+is);
                }
            }).start();
        }
        //执行保存
        else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                  MyAppDB.getSingleton(MyApplication.getContext()).saveUser(user);
                }
            }).start();
        }
        return START_REDELIVER_INTENT;
    }
}
