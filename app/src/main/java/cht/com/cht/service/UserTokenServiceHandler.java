package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/26.
 */
public class UserTokenServiceHandler {
    private UserTokenService service;
    private static UserTokenServiceHandler singleton;

    public UserTokenServiceHandler() {
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service =   retrofit.create(UserTokenService.class);
    }

    public UserTokenService getService() {
        return service;
    }


    public static UserTokenServiceHandler getInstance(){
        if(singleton==null){
            synchronized (CheckPhoneServiceHandler.class){
                if(singleton==null){
                    singleton = new UserTokenServiceHandler();
                }
            }
        }
        return singleton;
    }
}
