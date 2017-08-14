package cht.com.cht.service;

import java.util.Map;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.model.BaseModel;
import cht.com.cht.model.Message;
import okhttp3.RequestBody;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/24.
 */
public interface VerifyService {

    //http://127.0.0.101:8080/cht/servlet/VerifyServlet
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_LONG)
    @Multipart
    @POST("/cht/servlet/VerifyServlet")
    Observable<BaseModel<Message>> upload(@Part("user_id")RequestBody userid, @Part("school")RequestBody school, @Part("name") RequestBody name, @Part("identity") RequestBody identity , @PartMap Map<String, RequestBody> map);
}
