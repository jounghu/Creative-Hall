package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/22.
 */
public class SchoolServiceHandler {
    private static SchoolServiceHandler singleton;

    private SchoolService schoolService;


    public SchoolServiceHandler() {
        RetrofitManager.builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.schoolService = retrofit.create(SchoolService.class);
    }

    public SchoolService getSchoolService() {
        return schoolService;
    }

    public void setSchoolService(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    public static SchoolServiceHandler getSingleton() {

        if (singleton == null) {
            synchronized (RegisteServiceHandler.class) {
                if (singleton == null) {
                    singleton = new SchoolServiceHandler();
                }
            }
        }
        return singleton;

    }
}
