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
 * Created by Administrator on 2016/11/30.
 */
public interface SendTopicService {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_LONG)
    @Multipart
    @POST("/cht/servlet/SendTopicServlet")
    Observable<BaseModel<Message>> sendTopic(@Part("user_id") RequestBody userid, @Part("title")RequestBody title, @Part("content") RequestBody name, @Part("type") RequestBody type , @PartMap Map<String, RequestBody> map);

}
