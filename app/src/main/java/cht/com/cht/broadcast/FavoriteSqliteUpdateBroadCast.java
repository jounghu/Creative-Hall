package cht.com.cht.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;

/**
 * Created by Administrator on 2016/12/7.
 */
public class FavoriteSqliteUpdateBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String tagStr = intent.getStringExtra("tags");
        MyAppDB db = MyAppDB.getSingleton(MyApplication.getContext());
        int user_id = intent.getIntExtra("user_id", -1);
        LogUtil.e(Constants.TAG,"FavoriteSqliteUpdateBroadCast: "+tagStr);
        if(tagStr.equals(";")){
            db.deleteAllUserFavorite(user_id);
        }else{
            String[] tags = tagStr.split(";");
            if(user_id!=-1){
                db.deleteAllUserFavorite(user_id);
                for (String tag:tags
                        ) {
                    db.insertFavorite(user_id,tag);
                }
            }
        }

    }
}
