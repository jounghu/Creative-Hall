package cht.com.cht.service;

import android.content.Context;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/24.
 */
public class VerifyServiceHanlder {
    private static VerifyServiceHanlder singleton;
    private VerifyService service;
    private Context context;


    public VerifyServiceHanlder(Context context) {
        this.context = context;
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(VerifyService.class);
    }


    public static VerifyServiceHanlder getSingleton(Context context){
        if(singleton==null){
            synchronized (VerifyServiceHanlder.class){
                if(singleton==null){
                    singleton= new VerifyServiceHanlder(context);
                }
            }
        }
        return singleton;
    }

    public VerifyService getService() {
        return service;
    }
}
