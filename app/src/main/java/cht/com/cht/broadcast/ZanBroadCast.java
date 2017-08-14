package cht.com.cht.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cht.com.cht.helper.RxHelper;
import cht.com.cht.model.Message;
import cht.com.cht.service.UpdateZanServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/1.
 */
public class ZanBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int topic_id = intent.getIntExtra("topic_id", -1);
        int flag = intent.getIntExtra("flag",-1);
        if(topic_id!=-1){
            UpdateZanServiceHandler.getInstance().getService().updateZan(topic_id,flag).compose(RxHelper.<Message>handleResult())
                    .subscribe(new Subscriber<Message>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Message message) {
                            LogUtil.e(Constants.TAG,message+"");
                        }
                    });
        }
    }
}
