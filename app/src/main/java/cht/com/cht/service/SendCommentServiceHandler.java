package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/4.
 */
public class SendCommentServiceHandler {

    private static  SendCommentServiceHandler singleton;
    private SendCommentService service;

    private SendCommentServiceHandler(){
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(SendCommentService.class);
    }

    public SendCommentService getService() {
        return service;
    }

    public static SendCommentServiceHandler getSingleton(){
        if(singleton==null){
            synchronized (SendCommentServiceHandler.class){
                if(singleton==null){
                    singleton= new SendCommentServiceHandler();
                }
            }
        }
        return singleton;
    }
}
