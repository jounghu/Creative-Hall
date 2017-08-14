package cht.com.cht.systemService;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cht.com.cht.application.MyApplication;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.model.User;
import cht.com.cht.service.UserTokenServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/26.
 */
public class UpdateUserService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 用来更新user对象，并保存到数据库中，我们下次进来的时候直接提示消息在splash中启动该服务
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        String userToken = (String) SharePreferenceUtil.get(MyApplication.getContext(),Constants.SharePerferenceConfig.USER_TOKEN,"");
        UserTokenServiceHandler.getInstance().getService().getUserToken(userToken).compose(RxHelper.<User>handleResult()).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.e(Constants.TAG,e.getMessage());
            }

            @Override
            public void onNext(User user) {
                LogUtil.e(Constants.TAG,"saveIntent start-------------");
                Intent saveIntent = new Intent(UpdateUserService.this,MyService.class);
                Bundle b = new Bundle();
                b.putSerializable("user",user);
                b.putInt("update_save",1);
                saveIntent.putExtras(b);
                startService(saveIntent);
            }
        });


        return START_REDELIVER_INTENT;
    }
}
