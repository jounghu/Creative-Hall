package cht.com.cht.service;

import java.util.List;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.model.BaseModel;
import cht.com.cht.model.GameInfo;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/14.
 */
public interface GameInfoService {

    @Headers(RetrofitManager.CACHE_CONTROL_AGE+RetrofitManager.CACHE_STALE_LONG)
    @FormUrlEncoded
    @POST("/cht/servlet/GameServlet")
    Observable<BaseModel<List<GameInfo>>> getGameInfos(@Field("type") int type, @Field("page") int page);
}
