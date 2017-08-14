package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/5.
 */
public class CollectionInsertServiceHandler {
    private CollectionInsertService service;

    private static CollectionInsertServiceHandler singleton;


    public CollectionInsertServiceHandler(){
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
      service =   retrofit.create(CollectionInsertService.class);

    }

    public CollectionInsertService getService() {
        return service;
    }

    public static CollectionInsertServiceHandler getInstance(){
        if(singleton==null){
            synchronized (CollectionInsertServiceHandler.class){
                if(singleton==null){
                    singleton = new CollectionInsertServiceHandler();
                }
            }
        }
        return singleton;
    }
}
