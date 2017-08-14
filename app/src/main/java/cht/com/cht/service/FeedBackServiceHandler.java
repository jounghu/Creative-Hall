package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/5.
 */
public class FeedBackServiceHandler {
    private static FeedBackServiceHandler singleton;

    private FeedBackService checkService;

    private FeedBackServiceHandler(){
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        checkService = retrofit.create(FeedBackService.class);
    }


    public FeedBackService getCheckService() {
        return checkService;
    }

    public static FeedBackServiceHandler getInstance(){
        if(singleton==null){
            synchronized (FeedBackServiceHandler.class){
                if(singleton==null){
                    singleton = new FeedBackServiceHandler();
                }
            }
        }
        return singleton;
    }
}
