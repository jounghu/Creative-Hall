package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/18.
 */
public class RegisteServiceHandler {


    private RegisteService registeService;

    private static RegisteServiceHandler singleton;



    public static RegisteServiceHandler getSingleton() {

        if (singleton == null) {
            synchronized (RegisteServiceHandler.class) {
                if (singleton == null) {
                    singleton = new RegisteServiceHandler();
                }
            }
        }
        return singleton;

    }


    private RegisteServiceHandler() {
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        registeService = retrofit.create(RegisteService.class);
    }

    public RegisteService getRegisteService() {
        return registeService;
    }

    public void setRegisteService(RegisteService registeService) {
        this.registeService = registeService;
    }
}
