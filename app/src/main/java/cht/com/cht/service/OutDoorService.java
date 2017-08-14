package cht.com.cht.service;

import java.util.List;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.model.BaseModel;
import cht.com.cht.model.OutDoor;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2017/2/20.
 */
public interface OutDoorService {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_LONG)
    @FormUrlEncoded
    @POST("cht/servlet/OutDoorServlet")
    Observable<BaseModel<List<OutDoor>>> getOutDoorInfo(@Field("type") int type);

}
