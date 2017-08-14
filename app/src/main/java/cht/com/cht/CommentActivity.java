package cht.com.cht;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.adapter.CommentAdapter;
import cht.com.cht.adapter.PhotoGridAdapter;
import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.helper.RxSubscribe;
import cht.com.cht.model.CommentInfo;
import cht.com.cht.model.Message;
import cht.com.cht.model.TopicInfo;
import cht.com.cht.model.User;
import cht.com.cht.service.CollectionInsertServiceHandler;
import cht.com.cht.service.CollectionNumServiceHandler;
import cht.com.cht.service.CommentDetailServiceHandler;
import cht.com.cht.service.SendCommentServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;
import cht.com.cht.utils.SweetDialogUtil;
import cht.com.cht.utils.ToastUtil;
import cht.com.cht.widgt.DividerItemDecoration;
import cht.com.cht.widgt.LinearLayoutManager;
import cht.com.cht.widgt.MyRecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/1.
 */
public class CommentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CommentAdapter.onCommentItemListener, View.OnClickListener {

    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefresh;
    private SimpleDraweeView mHeadImg;
    private TextView mNickName;
    private TextView mTopicTime;
    private TextView mTopicTitle;
    private TextView mTopicContent;
    private RecyclerView mTopicPhotos;
    private TextView mCommentNum;
    private MyRecyclerView mCommentList;
    private EditText mCommentInput;
    private TextView mCommentSend;

    private TopicInfo topicInfo;
    private User user;

    private List<CommentInfo> mCommentDatas = new ArrayList<>();
    private CommentAdapter mCommentAdapter;

    private AlertDialog.Builder dialog;
    private InputMethodManager im;
    private String mCommentContentStr;

    private int mCommentId;
    private int mCommentParentId;

    private int flag = 0x1231;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initViews();
        initData();
        setupView();
        initEvent();
    }

    private void initEvent() {
        mCommentSend.setOnClickListener(this);
    }

    private void setupView() {
        mHeadImg.setImageURI(topicInfo.getHeadImg());
        mNickName.setText(topicInfo.getNickName());
        mTopicTime.setText(topicInfo.getDate() + "");
        mTopicTitle.setText(topicInfo.getTitle());
        mTopicContent.setText(topicInfo.getContent());
        PhotoGridAdapter adapter = new PhotoGridAdapter(this, topicInfo.getPhotos());
        mTopicPhotos.setAdapter(adapter);

        mCommentAdapter.addCommentItemListener(this);



    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            topicInfo = (TopicInfo) bundle.getSerializable("topicInfo");
        }

        String phoneStr = (String) SharePreferenceUtil.get(MyApplication.getContext(),Constants.SharePerferenceConfig.USER_TOKEN,"");
        user = MyAppDB.getSingleton(MyApplication.getContext()).readUser(phoneStr);

        mCommentNum.setText("已有评论" + topicInfo.getComment() + "条");
        mRefresh.setRefreshing(false);
        mCommentAdapter = new CommentAdapter(mCommentDatas,CommentActivity.this);
        mCommentList.setAdapter(mCommentAdapter);

        initCommentData();
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.id_registe_toolbar);
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.id_activity_comment_comment_refresh);
        mHeadImg = (SimpleDraweeView) findViewById(R.id.id_recycle_item_head);
        mNickName = (TextView) findViewById(R.id.id_recycle_item_nickname);
        mTopicTime = (TextView) findViewById(R.id.id_recycle_item_time);
        mTopicTitle = (TextView) findViewById(R.id.id_recycle_item_title);
        mTopicContent = (TextView) findViewById(R.id.id_recycle_item_content);
        mTopicPhotos = (RecyclerView) findViewById(R.id.id_recycle_item_photo);
        mCommentNum = (TextView) findViewById(R.id.id_activity_comment_comment_num);
        mCommentList = (MyRecyclerView) findViewById(R.id.id_activity_comment_comment_list);
        mCommentInput = (EditText) findViewById(R.id.id_activity_comment_comment_input);
        mCommentSend = (TextView) findViewById(R.id.id_activity_comment_comment_send);




        mTopicPhotos.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        mCommentList.setLayoutManager(new LinearLayoutManager(this));
        mCommentList.addItemDecoration(new DividerItemDecoration(
               this, DividerItemDecoration.VERTICAL_LIST));


        mRefresh.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mRefresh.setRefreshing(true);
        mRefresh.setOnRefreshListener(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(CommentActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(CommentActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;

            case R.id.id_menu_collection:
                //显示判断是否收藏，如果收藏，则取消收藏，先删后台数据库，然后再删sqlite，否则收藏，存进sqlite
                boolean collection = MyAppDB.getSingleton(MyApplication.getContext()).isCollection(topicInfo.getId(), user.getId());
                if(collection){
                    //取消
                    CollectionNumServiceHandler.getInstance().getDeleteService().deleteCollection(topicInfo.getId(),user.getId()).compose(RxHelper.<Message>handleResult())
                            .subscribe(new RxSubscribe<Message>(CommentActivity.this,"正在取消...") {
                                @Override
                                protected void _onNext(Message message) {
                                    ToastUtil.showToast(CommentActivity.this,"取消成功");
                                    MyAppDB.getSingleton(MyApplication.getContext()).deleteCollection(topicInfo.getId(),user.getId());
                                    item.setIcon(R.mipmap.star);
                                }

                                @Override
                                protected void _onError(String message) {
                                    ToastUtil.showToast(CommentActivity.this,"取消失败");
                                }
                            });
                }else{


                CollectionInsertServiceHandler.getInstance().getService().insertCollection(topicInfo.getId(),user.getId()).compose(RxHelper.<Message>handleResult())

                        .subscribe(new RxSubscribe<Message>(CommentActivity.this,"正在加载") {
                            @Override
                            protected void _onNext(Message message) {
                                MyAppDB.getSingleton(MyApplication.getContext()).insertIntoCollection(topicInfo.getId(),user.getId());
                                item.setIcon(R.mipmap.star_select);
                                ToastUtil.showToast(CommentActivity.this,"收藏成功");
                            }

                            @Override
                            protected void _onError(String message) {
                                ToastUtil.showToast(CommentActivity.this,"收藏失败");
                                LogUtil.e(Constants.TAG,message);
                            }
                        });
                }
                return true;
            case R.id.id_menu_report:
                //// TODO: 2016/12/3 弹出对话框，进行举报
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_collection_menu, menu);
        LogUtil.e(Constants.TAG,topicInfo.getId()+"onCreateOptionsMenu"+user.getId());
        boolean collection = MyAppDB.getSingleton(MyApplication.getContext()).isCollection(topicInfo.getId(),user.getId());
        LogUtil.e(Constants.TAG,collection+"onCreateOptionsMenu");
        if(collection){
            menu.findItem(R.id.id_menu_collection).setIcon(R.mipmap.star_select);
        }
        return super.onCreateOptionsMenu(menu);
    }


    public void initCommentData() {
        CommentDetailServiceHandler.getInstance().getService().getCommentInfos(topicInfo.getId()).compose(RxHelper.<List<CommentInfo>>handleResult())
                .subscribe(new Subscriber<List<CommentInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefresh.setRefreshing(false);
                        LogUtil.e(Constants.TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(List<CommentInfo> commentInfos) {
                        mRefresh.setRefreshing(false);
                        mCommentNum.setText("已有评论" + commentInfos.size() + "条");
                        mCommentAdapter.addNew(commentInfos);
                        mCommentAdapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public void onRefresh() {
        mRefresh.setRefreshing(true);
        refreshData();
    }

    private void refreshData() {
        CommentDetailServiceHandler.getInstance().getService().getCommentInfos(topicInfo.getId()).compose(RxHelper.<List<CommentInfo>>handleResult())
                .subscribe(new Subscriber<List<CommentInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefresh.setRefreshing(false);
                        LogUtil.e(Constants.TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(List<CommentInfo> commentInfos) {
                        mRefresh.setRefreshing(false);

                        mCommentAdapter.addNew(commentInfos);
                        mCommentNum.setText("已有评论" + mCommentAdapter.getItemCount() + "条");
                        mCommentAdapter.notifyDataSetChanged();
                    }
                });


    }

    @Override
    public void onCommentItemClick(int position, final CommentInfo commentInfo) {
        //
        mCommentInput.requestFocus();
        LogUtil.e(Constants.TAG,"position:"+position+"  commentInfo: "+commentInfo);
        dialog = new AlertDialog.Builder(this);

        dialog.setItems(new String[]{"回复", "举报"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                    switch (which){
                        case 0:
                            //弹出输入框
                            im.showSoftInput(mCommentInput,InputMethodManager.SHOW_FORCED);
                            flag=0x23213123;
                            mCommentId = commentInfo.getTo_user_id();
                            mCommentParentId = commentInfo.getId();
                            LogUtil.v(Constants.TAG,"   mCommentId"+mCommentId);
                            break;
                        case 1:
                            // TODO: 2016/12/4 弹出举报对话框，进行举报
                            break;

                    }
            }
        });

        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_activity_comment_comment_send:
                getCommentConent();
                LogUtil.v(Constants.TAG,"onClick:flag"+flag);
                if(flag==0x1231){
                    //直接回复的

                    SendCommentServiceHandler.getSingleton().getService().sendComment("topic_"+topicInfo.getId(),mCommentContentStr,user.getId()+"",topicInfo.getUser_id()+"")
                            .compose(RxHelper.<Message>handleResult()).subscribe(new RxSubscribe<Message>(CommentActivity.this,"发送中...") {
                        @Override
                        protected void _onNext(Message message) {
                            flag = 0x1231;
                            ToastUtil.showToast(CommentActivity.this,"评论成功！");
                            refreshData();
                        }

                        @Override
                        protected void _onError(String message) {
                                LogUtil.e(Constants.TAG,message);
                        }
                    });
                }else{
                    SendCommentServiceHandler.getSingleton().getService().sendComment(mCommentParentId+"",mCommentContentStr,user.getId()+"",mCommentId+"")
                            .compose(RxHelper.<Message>handleResult()).subscribe(new RxSubscribe<Message>(CommentActivity.this,"发送中...") {
                        @Override
                        protected void _onNext(Message message) {
                            flag = 0x1231;
                            ToastUtil.showToast(CommentActivity.this,"评论成功！");
                            refreshData();
                        }

                        @Override
                        protected void _onError(String message) {
                            LogUtil.e(Constants.TAG,message);
                        }
                    });
                }
                mCommentInput.setText("");
                im.hideSoftInputFromWindow(mCommentInput.getWindowToken(),0);
                break;
        }
    }

    public void getCommentConent() {

        mCommentContentStr = mCommentInput.getText().toString().trim();

        if(TextUtils.equals("",mCommentContentStr)){
            SweetDialogUtil.showSweetDialog(CommentActivity.this, SweetAlertDialog.ERROR_TYPE,"提示","输入你的回复内容");
            return;
        }

    }
}
