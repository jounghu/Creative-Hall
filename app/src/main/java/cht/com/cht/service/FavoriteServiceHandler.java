package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/7.
 */
public class FavoriteServiceHandler {
    private static FavoriteServiceHandler singleton;
    private FavoriteItemService service;
    private FavoriteInfoService infoService;
    private FavoriteInsertService insertService;
    private FavoriteNumService numService;

    private FavoriteServiceHandler() {
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(FavoriteItemService.class);
        infoService = retrofit.create(FavoriteInfoService.class);
        insertService = retrofit.create(FavoriteInsertService.class);
        numService = retrofit.create(FavoriteNumService.class);
    }


    public FavoriteItemService getService() {
        return service;
    }

    public static FavoriteServiceHandler getInstance(){
        if(singleton==null){
            synchronized (FavoriteServiceHandler.class){
                if(singleton==null){
                    singleton = new FavoriteServiceHandler();
                }
            }
        }
        return singleton;
    }


    public FavoriteInfoService getInfoService() {
        return infoService;
    }

    public FavoriteInsertService getInsertService() {
        return insertService;
    }

    public FavoriteNumService getNumService() {
        return numService;
    }
}
