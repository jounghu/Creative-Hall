package cht.com.cht;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.adapter.CollectionAdapter;
import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.model.TopicInfo;
import cht.com.cht.model.User;
import cht.com.cht.service.CollectionNumServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.SharePreferenceUtil;
import cht.com.cht.utils.ToastUtil;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/5.
 */
public class CollectionActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private Toolbar mToolbar;
    private SwipeRefreshLayout mRefreshlayout;
    private RecyclerView mRecylerview;
    private List<TopicInfo> topics = new ArrayList<>();
    private CollectionAdapter mAdapter;
    private User user;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
        initData();
    }

    private void initData() {
        String phoneStr = (String) SharePreferenceUtil.get(MyApplication.getContext(), Constants.SharePerferenceConfig.USER_TOKEN, "");
        user = MyAppDB.getSingleton(MyApplication.getContext()).readUser(phoneStr);
        CollectionNumServiceHandler.getInstance().getCollectionService().getCollectionInfo(user.getId()).compose(RxHelper.<List<TopicInfo>>handleResult())
                .subscribe(new Subscriber<List<TopicInfo>>() {
                    @Override
                    public void onCompleted() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshlayout.setRefreshing(false);
                            }
                        },2000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshlayout.setRefreshing(false);
                            }
                        },2000);
                        ToastUtil.showToast(CollectionActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(List<TopicInfo> topicInfos) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshlayout.setRefreshing(false);
                            }
                        },2000);
                        mAdapter.addNewData(topicInfos);
                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void initView() {
        mRecylerview = (RecyclerView) findViewById(R.id.id_activity_collection_recyclerview);
        mToolbar = (Toolbar) findViewById(R.id.id_activity_collection_toolbar);

        mRefreshlayout = (SwipeRefreshLayout) findViewById(R.id.id_activity_collection_refreshlayout);
        mRefreshlayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        mRefreshlayout.setRefreshing(true);
        mRefreshlayout.setOnRefreshListener(this);

        mRecylerview.setLayoutManager(new android.support.v7.widget.LinearLayoutManager(this, android.support.v7.widget.LinearLayoutManager.VERTICAL, false));
        mAdapter = new CollectionAdapter(topics, this);

        mRecylerview.setAdapter(mAdapter);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(CollectionActivity.this, MainActivity.class);
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
                Intent i = new Intent(CollectionActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mRefreshlayout.setRefreshing(true);
        refreshData();
    }

    private void refreshData() {
        CollectionNumServiceHandler.getInstance().getCollectionService().getCollectionInfo(user.getId()).compose(RxHelper.<List<TopicInfo>>handleResult())
                .subscribe(new Subscriber<List<TopicInfo>>() {
                    @Override
                    public void onCompleted() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshlayout.setRefreshing(false);
                            }
                        },2000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshlayout.setRefreshing(false);
                            }
                        },2000);
                        ToastUtil.showToast(CollectionActivity.this, e.getMessage());
                    }

                    @Override
                    public void onNext(List<TopicInfo> topicInfos) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefreshlayout.setRefreshing(false);
                            }
                        },2000);
                        mAdapter.clearAll();
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setData(topicInfos);
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }
}
