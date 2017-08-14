package cht.com.cht.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.R;
import cht.com.cht.SendTopicActivity;
import cht.com.cht.adapter.FragmentActivityTitleAdapter;
import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.helper.NetUtil;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.model.TopicInfo;
import cht.com.cht.model.User;
import cht.com.cht.service.TopicServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;
import cht.com.cht.utils.SweetDialogUtil;
import cht.com.cht.utils.ToastUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/17.
 */
public class ActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, FragmentActivityTitleAdapter.LoadMore {

    private FloatingActionButton mSendTopicBtn;
    private SwipeRefreshLayout mReFreshLayout;
    private RecyclerView mRecyclerView;
    private FragmentActivityTitleAdapter mAdapter;
    private List<TopicInfo> mTopics = new ArrayList<>();
    private User user;
    private Handler mHandler = new Handler();
    private int page = 2;
     private int default_page = 1;
    private int type = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mReFreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_fragment_activity_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_fragment_activity_recycle);
        mSendTopicBtn = (FloatingActionButton) view.findViewById(R.id.id_fragment_activity_sendtopic);
        mSendTopicBtn.setOnClickListener(this);
        mReFreshLayout.setOnRefreshListener(this);
        mReFreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mReFreshLayout.setRefreshing(true);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new FragmentActivityTitleAdapter(mTopics, getActivity(), mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setCallbackLoad(ActivityFragment.this);
        initData();


    }

    private void initData() {
        mTopics = new ArrayList<>();

        TopicServiceHandler.getInstance().getService().getTopicByType(type, default_page).compose(RxHelper.<List<TopicInfo>>handleResult()).subscribe(new Subscriber<List<TopicInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mReFreshLayout.setRefreshing(false);
                if(!NetUtil.isNetworkAvailable(MyApplication.getContext())){
                    ToastUtil.showToast(MyApplication.getContext(),"无可用网络");

                }
                LogUtil.e(Constants.TAG, "onError :  " + e.getMessage());
            }

            @Override
            public void onNext(final List<TopicInfo> topicInfos) {

                LogUtil.e(Constants.TAG, "onNext:  " + topicInfos);
                mTopics = topicInfos;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mReFreshLayout.setRefreshing(false);
                        if(mTopics!=null&&mTopics.size()>0){
                            mAdapter.addNewData(mTopics);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }, 2000);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        String phoneStr = (String) SharePreferenceUtil.get(MyApplication.getContext(), Constants.SharePerferenceConfig.USER_TOKEN, "");
        user = MyAppDB.getSingleton(MyApplication.getContext()).readUser(phoneStr);
        LogUtil.e(Constants.TAG, "activity fragment :" + user);

    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private void refreshData() {
        TopicServiceHandler.getInstance().getService().getTopicByType(type, default_page).compose(RxHelper.<List<TopicInfo>>handleResult()).subscribe(new Subscriber<List<TopicInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mReFreshLayout.setRefreshing(false);
                if(!NetUtil.isNetworkAvailable(MyApplication.getContext())){
                    ToastUtil.showToast(MyApplication.getContext(),"无可用网络");
                }
            }

            @Override
            public void onNext(List<TopicInfo> topicInfos) {
                page = 2;
                mAdapter.clearAll();
                mAdapter.notifyDataSetChanged();
                mReFreshLayout.setRefreshing(false);
                mAdapter.setData(topicInfos);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadMoreData() {
        TopicServiceHandler.getInstance().getService().getTopicByType(type, page).compose(RxHelper.<List<TopicInfo>>handleResult()).subscribe(new Subscriber<List<TopicInfo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                mReFreshLayout.setRefreshing(false);
                LogUtil.e(Constants.TAG, "onError :  " + e.getMessage());
            }

            @Override
            public void onNext(List<TopicInfo> topicInfos) {
                if (topicInfos==null||topicInfos.size()<=0){
                    ToastUtil.showToast(getActivity(),"无更多数据");
                    page = 2;
                    mReFreshLayout.setRefreshing(false);
                    return;
                }
                page++;
                mReFreshLayout.setRefreshing(false);
                mAdapter.addNewData(topicInfos);
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onClick(View v) {
        //先判断用户是否认证
        if (user != null) {
            if (user.getStatus() == 0) {
                //没有认证
                SweetDialogUtil.showSweetDialog(getActivity(), SweetAlertDialog.ERROR_TYPE, "提示", "抱歉你没有认证！请前往认证界面认证。");
                return;
            } else if (user.getStatus() == 3) {
                //审核
                SweetDialogUtil.showSweetDialog(getActivity(), SweetAlertDialog.ERROR_TYPE, "提示", "抱歉你提交的信息在认证当中，请耐心等待。");
                return;
            } else {
                Intent i = new Intent(getActivity(), SendTopicActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("user", user);
                b.putInt("topic_type",type);
                i.putExtras(b);
                startActivity(i);
            }
        }


    }

    @Override
    public void loadMore() {
        loadMoreData();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyItemRemoved(mAdapter.getItemCount());
            }
        },2000);

    }
}
