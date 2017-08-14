package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/18.
 */
public class LoginCheckServiceHandler {
    private LoginCheckService loginCheckService;

    private static LoginCheckServiceHandler singleton;



    public static LoginCheckServiceHandler getSingleton() {

        if (singleton == null) {
            synchronized (LoginCheckServiceHandler.class) {
                if (singleton == null) {
                    singleton = new LoginCheckServiceHandler();
                }
            }
        }
        return singleton;

    }


    private LoginCheckServiceHandler() {
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        loginCheckService = retrofit.create(LoginCheckService.class);
    }

    public LoginCheckService getLoginCheckService() {
        return loginCheckService;
    }

    public void setLoginCheckService(LoginCheckService loginCheckService) {
        this.loginCheckService = loginCheckService;
    }
}
