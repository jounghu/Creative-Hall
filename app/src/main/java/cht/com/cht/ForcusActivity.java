package cht.com.cht;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cht.com.cht.adapter.FavoriteAdapter;
import cht.com.cht.adapter.TagAdapter;
import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.helper.RxSubscribe;
import cht.com.cht.model.FavoriteInfo;
import cht.com.cht.model.User;
import cht.com.cht.service.FavoriteServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.ToastUtil;
import cht.com.cht.widgt.LinearLayoutManager;
import cht.com.flowtaglibrary.FlowTagLayout;
import cht.com.flowtaglibrary.OnTagSelectListener;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/7.
 */
public class ForcusActivity extends AppCompatActivity {
    private static final int TAG_FINISH = 1123;
    private static final int MSG_SET_TAGS = 20021;
    private static final int SERVLER_SET_TAGS = 2001;



    private Toolbar mToolbar;
    private FlowTagLayout mFlowTagLayout;
    private RecyclerView mRecyclerView;
    private TagAdapter<String> mTagAdapter;
    private List<String> mTags =  new ArrayList<>();
    private  List<String> allUserFavorite = new ArrayList<>();
    private List<FavoriteInfo> favoriteInfos = new ArrayList<>();
    private List<FavoriteInfo> selectFavoriteInfo = new ArrayList<>();
    private User user;

    private FavoriteAdapter mFavoriteAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TAG_FINISH:
                    favoriteInfos = (List<FavoriteInfo>) msg.obj;
                    LogUtil.e(Constants.TAG,"favoriteInfos: "+favoriteInfos);
                    for (FavoriteInfo fa:favoriteInfos
                         ) {
                        mTags.add(fa.getFavorite_item());
                    }
                    mTagAdapter.setmSelectTag(allUserFavorite);
                    mTagAdapter.onlyAddAll(mTags);
                    break;
                case MSG_SET_TAGS:
                    if(mTags!=null&&mTags.size()>0){
                       List<Integer> indexs = (List<Integer>) msg.obj;
                        Set<String> tags = new HashSet<>();
                        for (int i:indexs
                             ) {
                            tags.add(mTags.get(i));
                            selectFavoriteInfo.add(favoriteInfos.get(i));
                        }
                        JPushInterface.setTags(ForcusActivity.this,tags,mAliasCallback);
                    }
                    break;

                case SERVLER_SET_TAGS:
                    mFavoriteAdapter.addNewTag(selectFavoriteInfo);
                    mFavoriteAdapter.notifyDataSetChanged();
                    Set<String> tags = (Set<String>) msg.obj;
                    String str = "";
                    //这里没有值得时候，直接传一个；
                    if(tags==null||tags.size()<=0){
                        str = ";";
                    }
                    for (String tag:tags
                            ) {
                        str = str + tag + ";";
                    }

                    LogUtil.e(Constants.TAG,"SERVLER_SET_TAGS: "+str);
                    insertIntoServer(str);

                    break;
            }
        }
    };



    private void insertIntoServer(final String str) {
        if(str==null||str.equals("")){
            return;
        }
        FavoriteServiceHandler.getInstance().getInsertService().insertFavorite(user.getId(),str).compose(RxHelper.<cht.com.cht.model.Message>handleResult())
                .subscribe(new Subscriber<cht.com.cht.model.Message>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(cht.com.cht.model.Message message) {
                        Intent i = new Intent(Constants.FAVORITE_UPFATE_BRODCAST_ACTION);
                        i.putExtra("tags",str);
                        i.putExtra("user_id",user.getId());
                        sendBroadcast(i);
                    }
                });

    }




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcuse);
        initView();
        initTagData();
        initEvent();
    }

    private void initEvent() {



        mFlowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                if (selectedList != null && selectedList.size() >= 0) {
                   //这里给Jpush设置标签
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, selectedList));
                }
            }
        });

    }

    private void initTagData() {
        user = (User) getIntent().getSerializableExtra("user");
        LogUtil.e(Constants.TAG,"ForcusActivity initTagData:"+user);
        allUserFavorite = MyAppDB.getSingleton(MyApplication.getContext()).getAllUserFavorite(user.getId());
        FavoriteServiceHandler.getInstance().getService().getFavorite().compose(RxHelper.<List<FavoriteInfo>>handleResult())
                .subscribe(new RxSubscribe<List<FavoriteInfo>>(ForcusActivity.this, "请稍后...") {
                    @Override
                    protected void _onNext(List<FavoriteInfo> strings) {
                        Message msg = mHandler.obtainMessage();
                        msg.obj = strings;
                        msg.what = TAG_FINISH;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtil.showToast(ForcusActivity.this, message);
                    }
                });
    }

    private void initView() {
        mFlowTagLayout = (FlowTagLayout) findViewById(R.id.id_activity_forcuse_tags);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_activity_forcuse_recyclerview);
        mToolbar = (Toolbar) findViewById(R.id.id_activity_forcuse_toolbar);

        mTagAdapter = new TagAdapter<>(this);
        mFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mFlowTagLayout.setAdapter(mTagAdapter);



        mFavoriteAdapter = new FavoriteAdapter(selectFavoriteInfo,this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, android.support.v7.widget.LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mFavoriteAdapter);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(ForcusActivity.this, MainActivity.class);
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
                Intent i = new Intent(ForcusActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    LogUtil.i(Constants.TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    //这里向服务器发送保存状态
                    Message msg = mHandler.obtainMessage();
                    msg.what = SERVLER_SET_TAGS;
                    msg.obj = tags;
                    mHandler.sendMessage(msg);
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    LogUtil.e(Constants.TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    LogUtil.e(Constants.TAG, logs);
            }
            ToastUtil.showToast(ForcusActivity.this,logs);
        }
    };

}
