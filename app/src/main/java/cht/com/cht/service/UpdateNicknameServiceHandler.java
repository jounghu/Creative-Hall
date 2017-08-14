package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/3/2.
 */
public class UpdateNicknameServiceHandler {
    private static UpdateNicknameServiceHandler sigleton;
    private  UpdateNicknameService service;
    private UpdateNicknameGender genderService;

    public UpdateNicknameServiceHandler(){
        RetrofitManager.builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(RetrofitManager.mOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service =  retrofit.create(UpdateNicknameService.class);
        genderService = retrofit.create(UpdateNicknameGender.class);

    }

    public UpdateNicknameService getService() {
        return service;
    }

    public UpdateNicknameGender getGenderService() {
        return genderService;
    }

    public static UpdateNicknameServiceHandler getInstance(){
        if(sigleton==null){
            synchronized (UpdateNicknameServiceHandler.class){
                if(sigleton==null){
                    sigleton=new UpdateNicknameServiceHandler();
                }
            }
        }
        return sigleton;
    }

}
