package cht.com.cht.service;

import java.util.Map;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.model.BaseModel;
import cht.com.cht.model.User;
import okhttp3.RequestBody;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by Administrator on 2017/3/2.
 */
public interface UpdateNicknameService {
    //http://127.0.0.101:8080/cht/servlet/VerifyServlet
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_LONG)
    @Multipart
    @POST("/cht/servlet/UpdateUserInfoServlet")
    Observable<BaseModel<User>> update(@Part("user_id") RequestBody userid, @Part("nickname")RequestBody nickname, @Part("gender") RequestBody gender,@PartMap Map<String, RequestBody> map);

}
