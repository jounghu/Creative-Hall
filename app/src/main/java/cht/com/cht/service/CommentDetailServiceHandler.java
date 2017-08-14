package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/3.
 */
public class CommentDetailServiceHandler {
    private static CommentDetailServiceHandler singleton;
    private CommentDetailService service;


    public CommentDetailServiceHandler(){
        RetrofitManager.builder();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        service =   retrofit.create(CommentDetailService.class);
    }


    public CommentDetailService getService() {
        return service;
    }

    public static CommentDetailServiceHandler getInstance(){
        if(singleton==null){
            synchronized (CommentDetailServiceHandler.class){
                if(singleton==null){
                    singleton = new CommentDetailServiceHandler();
                }
            }
        }
        return singleton;
    }
}
