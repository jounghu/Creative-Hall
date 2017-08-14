package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/5.
 */
public class CollectionNumServiceHandler {
    private static CollectionNumServiceHandler singleton;
    private CollectionNumCountService service;
    private CollectionInfoService collectionService;
    private CollectionDeleteService deleteService;
    private CollectionUpdateService updateService;


    private CollectionNumServiceHandler(){
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(CollectionNumCountService.class);
        collectionService  = retrofit.create(CollectionInfoService.class);
        deleteService = retrofit.create(CollectionDeleteService.class);
        updateService = retrofit.create(CollectionUpdateService.class);
    }

    public CollectionNumCountService getService() {
        return service;
    }

    public static CollectionNumServiceHandler getInstance(){
        if(singleton==null){
            synchronized (CollectionNumServiceHandler.class){
                if(singleton==null){
                    singleton = new CollectionNumServiceHandler();
                }
            }
        }
        return singleton;
    }

    public CollectionInfoService getCollectionService() {
        return collectionService;
    }

    public CollectionDeleteService getDeleteService() {
        return deleteService;
    }

    public CollectionUpdateService getUpdateService() {
        return updateService;
    }
}
