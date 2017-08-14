package cht.com.cht.service;

import java.util.List;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.model.BaseModel;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/7.
 */
public interface FavoriteInfoService {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE+RetrofitManager.CACHE_STALE_LONG)
    @FormUrlEncoded
    @POST("/cht/servlet/FavoriteInfoServlet")
    Observable<BaseModel<List<String>>> getFavoriteInfo(@Field("user_id") int user_id);
}
