package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/20.
 */
public class CheckPhoneServiceHandler {
    private static CheckPhoneServiceHandler singleton;

    private CheckPhoneService checkService;

    private CheckPhoneServiceHandler(){
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        checkService = retrofit.create(CheckPhoneService.class);
    }


    public CheckPhoneService getCheckService() {
        return checkService;
    }

    public static CheckPhoneServiceHandler getInstance(){
        if(singleton==null){
            synchronized (CheckPhoneServiceHandler.class){
                if(singleton==null){
                    singleton = new CheckPhoneServiceHandler();
                }
            }
        }
        return singleton;
    }
}
