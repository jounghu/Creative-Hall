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
 * Created by Administrator on 2017/3/3.
 */
public interface UpdateNicknameGender {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_LONG)
    @FormUrlEncoded
    @POST("/cht/servlet/UpdateNicknameGenderServlet")
    Observable<BaseModel<User>> updateNicknameGender(@Field("id")int  id,@Field("nickname")String username,@Field("gender") String gender);
}
