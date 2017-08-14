package cht.com.cht.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.GameFragmentDetailActivity;
import cht.com.cht.R;
import cht.com.cht.adapter.FragmentGameAdapter;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.model.GameInfo;
import cht.com.cht.service.TopicServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.ToastUtil;
import cht.com.cht.widgt.LinearLayoutManager;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/17.
 */
public class GameFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, FragmentGameAdapter.GameItemClickListener {
    private static final int  GAME_INFO_FINISH = 0x1232;
    private static final int GAME_INFO_REFRESH = 0x2343;

    // TODO: 2016/12/14 type为3 项目内容 用地点加主办方，默认用户为官方用户，图片为首页图片
    private SwipeRefreshLayout mRefresh;
    private RecyclerView mRecyclerView;

    private List<GameInfo> datas = new ArrayList<>();
    private FragmentGameAdapter mAdapter;

    private int type = 3;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GAME_INFO_FINISH:
                    List<GameInfo> newData = (List<GameInfo>) msg.obj;
                    LogUtil.e(Constants.TAG,"GAME_INFO_FINISH: " + newData);
                    mAdapter.clearAllItem();
                    mAdapter.addAllItem(newData);
                    mAdapter.notifyDataSetChanged();
                    break;
                case GAME_INFO_REFRESH:
                    List<GameInfo> newDatas = (List<GameInfo>) msg.obj;
                    mAdapter.addNewItem(newDatas);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, null, false);
        mRefresh = (SwipeRefreshLayout) view.findViewById(R.id.id_fragment_game_refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_fragment_game_recyclerview);

        mRefresh.setOnRefreshListener(this);
        mRefresh.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mRefresh.setRefreshing(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), android.support.v7.widget.LinearLayoutManager.VERTICAL,false));
        mAdapter = new FragmentGameAdapter(getActivity(),datas);
        mRecyclerView.setAdapter(mAdapter);


        getGameInfo();

        mAdapter.setItemListener(this);
    }

    private void getGameInfo() {
        TopicServiceHandler.getInstance().getGameInfoService().getGameInfos(type,1).compose(RxHelper.<List<GameInfo>>handleResult())
                .subscribe(new Subscriber<List<GameInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                              mRefresh.setRefreshing(false);
                            }
                        },2000);
                        ToastUtil.showToast(getActivity(),"无法访问，请检查网络!");
                    }

                    @Override
                    public void onNext(List<GameInfo> gameInfos) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefresh.setRefreshing(false);
                            }
                        },2000);
                        Message msg = handler.obtainMessage();
                        msg.what = GAME_INFO_FINISH;
                        msg.obj = gameInfos;
                        handler.sendMessage(msg);
                    }
                });

    }

    @Override
    public void onRefresh() {
            mRefresh.setRefreshing(true);
            refreshData();
    }

    private void refreshData() {

        TopicServiceHandler.getInstance().getGameInfoService().getGameInfos(type,1).compose(RxHelper.<List<GameInfo>>handleResult())
                .subscribe(new Subscriber<List<GameInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefresh.setRefreshing(false);
                            }
                        },2000);
                        ToastUtil.showToast(getActivity(),"无法访问，请检查网络!");
                    }

                    @Override
                    public void onNext(List<GameInfo> gameInfos) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mRefresh.setRefreshing(false);
                            }
                        },2000);
                        Message msg = handler.obtainMessage();
                        msg.what = GAME_INFO_REFRESH;
                        msg.obj = gameInfos;
                        handler.sendMessage(msg);
                    }
                });
    }

    @Override
    public void gameItemClick(int postion, GameInfo gameInfo) {
        Intent i = new Intent(getActivity(), GameFragmentDetailActivity.class);
        i.putExtra("gameInfo",gameInfo);
        startActivity(i);

    }
}
