package cht.com.cht.service;


import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.model.BaseModel;
import cht.com.cht.model.User;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/16.
 */
public interface LoginCheckService {
    //http://127.0.0.101:8080/cht/servlet/LoginCheckServlet
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_LONG)
    @FormUrlEncoded
    @POST("/cht/servlet/LoginCheckServlet")
    Observable<BaseModel<User>>checkUser(@Field("phone") String username
            , @Field("password") String password
    );

}
