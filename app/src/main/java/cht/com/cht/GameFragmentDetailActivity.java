package cht.com.cht;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.adapter.CommentAdapter;
import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.helper.RxSubscribe;
import cht.com.cht.model.CommentInfo;
import cht.com.cht.model.GameInfo;
import cht.com.cht.model.User;
import cht.com.cht.service.CommentDetailServiceHandler;
import cht.com.cht.service.SendCommentServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;
import cht.com.cht.utils.SweetDialogUtil;
import cht.com.cht.utils.ToastUtil;
import cht.com.cht.widgt.LinearLayoutManager;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/14.
 */
public class GameFragmentDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, CommentAdapter.onCommentItemListener {
    private static final int COMMENT_FINISH = 0x21313;

    private Toolbar mToolBar;
    private SwipeRefreshLayout mRefreshLayout;
    private SimpleDraweeView mHead;
    private TextView mTitle;
    private TextView mFavoriteNum;
    private TextView mTime;
    private TextView mLocation;
    private TextView mOrgnization;
    private TextView mContent;
    private FloatingActionButton mWantto;
    private TextView mCommentNum;
    private RecyclerView mRecylerView;
    private EditText mInput;
    private TextView mSendComment;

    private User user;
    private GameInfo gameInfo;
    private CommentAdapter mAdapter;
    private List<CommentInfo> mCommentDatas = new ArrayList<>();
    private InputMethodManager im;

    private AlertDialog.Builder dialog;
    private String mCommentContentStr = "";
    private boolean flag = true;//这里是直接回复，还是回复其他人
    private int mCommentId;
    private int mCommentParentId;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COMMENT_FINISH:
                    List<CommentInfo> datas = (List<CommentInfo>) msg.obj;
                    if(datas==null||datas.size()<=0){
                        mCommentNum.setText("已有0条评论");
                    }else{
                        mCommentNum.setText("已有"+datas.size()+"条评论");
                    }
                    mAdapter.addNew(datas);
                    mAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_game_detail);

        initViews();
        initData();
        setupViews();

    }

    private void setupViews() {
        mHead.setImageURI(gameInfo.getHead_img());
        mTitle.setText(gameInfo.getTitle());
        mFavoriteNum.setText(gameInfo.getZanNum() + "人感兴趣");
        mTime.setText(gameInfo.getTime().substring(0, gameInfo.getTime().length() - 5));
        mLocation.setText(gameInfo.getLocation());
        mOrgnization.setText(gameInfo.getOrgnization());
        mContent.setText(gameInfo.getContent());


    }

    private void initData() {
        String phoneStr = (String) SharePreferenceUtil.get(MyApplication.getContext(), Constants.SharePerferenceConfig.USER_TOKEN, "");
        user = MyAppDB.getSingleton(MyApplication.getContext()).readUser(phoneStr);
        gameInfo = (GameInfo) getIntent().getSerializableExtra("gameInfo");

        getComment();

    }

    private void getComment() {
        CommentDetailServiceHandler.getInstance().getService().getCommentInfos(gameInfo.getId()).compose(RxHelper.<List<CommentInfo>>handleResult())
                .subscribe(new Subscriber<List<CommentInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshLayout.setRefreshing(false);
                            }
                        }, 2000);
                        ToastUtil.showToast(GameFragmentDetailActivity.this, "错误，请检查网络连接");
                    }

                    @Override
                    public void onNext(List<CommentInfo> commentInfos) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshLayout.setRefreshing(false);
                            }
                        }, 2000);
                        Message msg = handler.obtainMessage();
                        msg.what = COMMENT_FINISH;
                        msg.obj = commentInfos;
                        handler.sendMessage(msg);
                    }
                });

    }

    private void initViews() {
        mToolBar = (Toolbar) findViewById(R.id.id_fragment_game_detail_toolbar);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_fragment_game_detail_refresh);
        mHead = (SimpleDraweeView) findViewById(R.id.id_fragment_game_head);
        mTitle = (TextView) findViewById(R.id.id_fragment_game_title);
        mFavoriteNum = (TextView) findViewById(R.id.id_fragment_game_favorite_num);
        mTime = (TextView) findViewById(R.id.id_fragment_game_time);
        mLocation = (TextView) findViewById(R.id.id_fragment_game_location);
        mOrgnization = (TextView) findViewById(R.id.id_fragment_game_organizer);
        mContent = (TextView) findViewById(R.id.id_fragment_game_detail_content);
        mWantto = (FloatingActionButton) findViewById(R.id.id_fragment_game_detail_want_to_btn);
        mCommentNum = (TextView) findViewById(R.id.id_fragment_game_detail_comemnt_num);
        mRecylerView = (RecyclerView) findViewById(R.id.id_fragment_game_detail_comment_list);
        mInput = (EditText) findViewById(R.id.id_fragment_game_detail_comemnt_input);
        mSendComment = (TextView) findViewById(R.id.id_fragment_game_detail_comement_send);

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mRefreshLayout.setRefreshing(true);


        mRecylerView.setLayoutManager(new LinearLayoutManager(this, android.support.v7.widget.LinearLayoutManager.VERTICAL, false));
        mAdapter = new CommentAdapter(mCommentDatas, this);
        mRecylerView.setAdapter(mAdapter);
        mAdapter.addCommentItemListener(this);


        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInput.clearFocus();
        mWantto.setOnClickListener(this);
        mSendComment.setOnClickListener(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(GameFragmentDetailActivity.this, MainActivity.class);
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
                Intent i = new Intent(GameFragmentDetailActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        refreshData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_fragment_game_detail_want_to_btn:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mWantto.setImageDrawable(getResources().getDrawable(R.drawable.want_to_press, this.getTheme()));
                } else {
                    mWantto.setImageDrawable(getResources().getDrawable(R.drawable.want_to_press));
                }
                Intent i = new Intent(Constants.ZAN_BRODCAST_ACTION);
                i.putExtra("topic_id",gameInfo.getId());
                i.putExtra("flag",0);
                this.sendBroadcast(i);
                mWantto.setClickable(false);
                break;
            case R.id.id_fragment_game_detail_comement_send:
                sendComment();

                mInput.setText("");
                im.hideSoftInputFromWindow(mInput.getWindowToken(),0);
                break;
        }
    }

    private void sendComment() {
        getCommentContent();
        if(flag){
            SendCommentServiceHandler.getSingleton().getService().sendComment("topic_"+gameInfo.getId(),mCommentContentStr,user.getId()+"",gameInfo.getId()+"")
                    .compose(RxHelper.<cht.com.cht.model.Message>handleResult()).subscribe(new RxSubscribe<cht.com.cht.model.Message>(GameFragmentDetailActivity.this,"发送中...") {
                @Override
                protected void _onNext(cht.com.cht.model.Message message) {
                    flag = true;
                    refreshData();
                    ToastUtil.showToast(GameFragmentDetailActivity.this,"评论成功！");
                }

                @Override
                protected void _onError(String message) {
                    LogUtil.e(Constants.TAG,message);
                }
            });
        }else{
            SendCommentServiceHandler.getSingleton().getService().sendComment(mCommentParentId+"",mCommentContentStr,user.getId()+"",mCommentId+"")
                    .compose(RxHelper.<cht.com.cht.model.Message>handleResult()).subscribe(new RxSubscribe<cht.com.cht.model.Message>(GameFragmentDetailActivity.this,"发送中...") {
                @Override
                protected void _onNext(cht.com.cht.model.Message message) {
                    flag = true;
                    ToastUtil.showToast(GameFragmentDetailActivity.this,"评论成功！");
                    refreshData();
                }

                @Override
                protected void _onError(String message) {
                    LogUtil.e(Constants.TAG,message);
                }
            });
        }


    }

    private void refreshData() {
        CommentDetailServiceHandler.getInstance().getService().getCommentInfos(gameInfo.getId()).compose(RxHelper.<List<CommentInfo>>handleResult())
                .subscribe(new Subscriber<List<CommentInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mRefreshLayout.setRefreshing(false);
                        LogUtil.e(Constants.TAG,e.getMessage());
                    }

                    @Override
                    public void onNext(List<CommentInfo> commentInfos) {
                        mRefreshLayout.setRefreshing(false);

                        mAdapter.addNew(commentInfos);
                        mCommentNum.setText("已有评论" + mAdapter.getItemCount() + "条");
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void getCommentContent() {
        mCommentContentStr = mInput.getText().toString().trim();

        if(TextUtils.equals("",mCommentContentStr)){
            SweetDialogUtil.showSweetDialog(GameFragmentDetailActivity.this, SweetAlertDialog.ERROR_TYPE,"提示","输入你的回复内容");
            return;
        }
    }

    @Override
    public void onCommentItemClick(int position,final CommentInfo comment) {
        mInput.requestFocus();
        LogUtil.e(Constants.TAG,"position:"+position+"  commentInfo: "+comment);
        dialog = new AlertDialog.Builder(this);

        dialog.setItems(new String[]{"回复", "举报"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case 0:
                        //弹出输入框
                        im.showSoftInput(mInput,InputMethodManager.SHOW_FORCED);
                        flag=false;
                        mCommentId = comment.getTo_user_id();
                        mCommentParentId = comment.getId();
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
}
