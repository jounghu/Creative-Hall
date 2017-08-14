package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/11/28.
 */
public class TopicServiceHandler {
    private static TopicServiceHandler singleton;
    private TopicService service;
    private GameInfoService gameInfoService;

    public TopicServiceHandler() {
        RetrofitManager.builder();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(TopicService.class);
        gameInfoService = retrofit.create(GameInfoService.class);
    }


    public static TopicServiceHandler getInstance(){
        if(singleton==null){
            synchronized (TopicServiceHandler.class){
                if(singleton==null){
                    singleton=new TopicServiceHandler();
                }
            }
        }
        return singleton;
    }


    public TopicService getService() {
        return service;
    }

    public GameInfoService getGameInfoService() {
        return gameInfoService;
    }
}
