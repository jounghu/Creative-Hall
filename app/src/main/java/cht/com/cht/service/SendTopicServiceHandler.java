package cht.com.cht.service;

import android.content.Context;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/30.
 */
public class SendTopicServiceHandler {


        private static SendTopicServiceHandler singleton;
        private  SendTopicService service;
        private Context context;


        public SendTopicServiceHandler(Context context) {
            this.context = context;
            RetrofitManager.builder();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(RetrofitManager.mOkHttpClient)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(SendTopicService.class);
        }


        public static SendTopicServiceHandler getSingleton(Context context){
            if(singleton==null){
                synchronized (SendTopicServiceHandler.class){
                    if(singleton==null){
                        singleton= new SendTopicServiceHandler(context);
                    }
                }
            }
            return singleton;
        }

        public SendTopicService getService() {
            return service;
        }

}
