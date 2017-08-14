package cht.com.cht.service;

import cht.com.cht.helper.RetrofitManager;
import cht.com.cht.model.BaseModel;
import cht.com.cht.model.Message;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/12/4.
 */
public interface SendCommentService {
    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_LONG)
    @FormUrlEncoded
    @POST("/cht/servlet/CommentInsertServlet")
    Observable<BaseModel<Message>> sendComment
            (@Field("parent_id") String parent_id,
             @Field("comment_content") String content,
             @Field("from_user_id") String fromuser_id,
             @Field("to_user_id") String to_user_id);
}
