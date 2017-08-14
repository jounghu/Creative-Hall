package cht.com.cht.service;

import java.util.List;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.model.BaseModel;
import cht.com.cht.model.FavoriteInfo;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/7.
 */
public interface FavoriteItemService {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE+RetrofitManager.CACHE_STALE_LONG)
    @POST("/cht/servlet/FavoriteServlet")
    Observable<BaseModel<List<FavoriteInfo>>> getFavorite();


}
