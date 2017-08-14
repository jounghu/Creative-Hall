package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/2/20.
 */
public class OutDoorServiceHandler {
    private OutDoorService service;
    private static OutDoorServiceHandler singleton;

    private OutDoorServiceHandler() {
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(OutDoorService.class);
    }

    public OutDoorService getService() {
        return service;
    }
    public static OutDoorServiceHandler getInstants(){
        if(singleton==null){
            synchronized (OutDoorServiceHandler.class){
                if(singleton==null){
                    singleton = new OutDoorServiceHandler();
                }
            }
        }
        return singleton;
    }
}
