package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/20.
 */
public class ForgetPasswordServiceHanlder {
    private static ForgetPasswordServiceHanlder singleton;
    private ForgetPasswordService service;


    private ForgetPasswordServiceHanlder(){
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ForgetPasswordService.class);
    }


    public ForgetPasswordService getService() {
        return service;
    }

    public void setService(ForgetPasswordService service) {
        this.service = service;
    }

    public static ForgetPasswordServiceHanlder getInstance(){
        if(singleton==null){
            synchronized (ForgetPasswordServiceHanlder.class){
                if(singleton==null){
                    singleton = new ForgetPasswordServiceHanlder();
                }
            }
        }
        return singleton;
    }
}
