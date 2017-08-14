package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/3.
 */
public class UpdateZanServiceHandler {
    private static UpdateZanServiceHandler singleton;
    private UpdateZanService service;

    public UpdateZanServiceHandler(){
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(Constants.BASE_URL)
                            .client(RetrofitManager.mOkHttpClient)
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

        service =  retrofit.create(UpdateZanService.class);

    }

    public UpdateZanService getService() {
        return service;
    }

    public static UpdateZanServiceHandler getInstance(){
        if(singleton==null){
            synchronized (UpdateZanServiceHandler.class){
                if(singleton==null){
                    singleton=new UpdateZanServiceHandler();
                }
            }
        }
        return singleton;
    }
}
