package cht.com.cht.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONObject;

import cht.com.cht.OutDoorDetailActivity;
import cht.com.cht.model.OutDoor;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2016/12/7.
 */
public class JpushRecevier extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        LogUtil.d(TAG, "onReceive - " + intent.getAction() + ", extras: ");

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            LogUtil.d(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            LogUtil.d(TAG, "接受到推送下来的通知");

            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            LogUtil.d(TAG, "用户点击打开了通知");

            openNotification(context, bundle);

        } else {
            LogUtil.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        LogUtil.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        LogUtil.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        LogUtil.d(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String myTitle = "";
        String mContent = "";
        try {
            JSONObject extrasJson = new JSONObject(extras);
            //// TODO: 2016/12/10 这里是jpush后台附加的消息
            myTitle = extrasJson.optString(Constants.Jpush.JPUSH_title);
            mContent = extrasJson.optString(Constants.Jpush.JPUSH_content);

        } catch (Exception e) {
            LogUtil.w(TAG, "Unexpected: extras is not a valid json" + e.getMessage());
            return;
        }

        LogUtil.e(Constants.TAG, "openNotification:" + myTitle + "\n  mContent: " + mContent);
        //// TODO: 2016/12/10  跳转不同页面
//        if (Constants.Jpush.JPUSH_TYPE.equals(myValue)) {
        Intent mIntent = new Intent(context, OutDoorDetailActivity.class);
        Bundle b = new Bundle();
        OutDoor out = new OutDoor(myTitle,mContent);
        mIntent.putExtra("outdoor",out);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);
//        }
    }
}
