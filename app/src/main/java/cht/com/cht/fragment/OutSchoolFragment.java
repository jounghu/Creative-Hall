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

import cht.com.cht.OutDoorDetailActivity;
import cht.com.cht.R;
import cht.com.cht.adapter.OutDoorAdapter;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.model.OutDoor;
import cht.com.cht.service.OutDoorServiceHandler;
import cht.com.cht.utils.ToastUtil;
import cht.com.cht.widgt.LinearLayoutManager;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/17.
 */
public class OutSchoolFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OutDoorAdapter.OutDoorItemListener {
    private static final int type = 4;
    private static final int REFRESH_FLAG = 0x32;
    private static final int COMPLETE_FLAG = 0x324324;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<OutDoor> mDatas = new ArrayList<>();
    private OutDoorAdapter mAdapter;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COMPLETE_FLAG:
                    List<OutDoor> data = (List<OutDoor>) msg.obj;
                    mAdapter.clearAll();
                    mAdapter.addAll(data);
                    mAdapter.notifyDataSetChanged();
                    break;
                case REFRESH_FLAG:
                    List<OutDoor> datas = (List<OutDoor>) msg.obj;
                    mAdapter.addNewItem(datas);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    //// TODO: 2017/2/20 type为4的

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outdoor, null, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.id_fragment_outdoor_swipe);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_fragment_outdoor_recyclerview);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        mSwipeRefreshLayout.setRefreshing(true);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), android.support.v7.widget.LinearLayoutManager.VERTICAL, false));
        mAdapter = new OutDoorAdapter(mDatas, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        getOutDoorDatas();//这里获取校外信息

        mAdapter.setListener(this);
    }

    private void getOutDoorDatas() {
        OutDoorServiceHandler.getInstants().getService().getOutDoorInfo(type).compose(RxHelper.<List<OutDoor>>handleResult()).subscribe(new Subscriber<List<OutDoor>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
                ToastUtil.showToast(getActivity(), "无法访问，请检查网络!");
            }

            @Override
            public void onNext(List<OutDoor> outDoors) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);

                Message msg = new Message();
                msg.what = COMPLETE_FLAG;
                msg.obj = outDoors;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        refreshData();
    }

    private void refreshData() {
        OutDoorServiceHandler.getInstants().getService().getOutDoorInfo(type).compose(RxHelper.<List<OutDoor>>handleResult()).subscribe(new Subscriber<List<OutDoor>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(List<OutDoor> outDoors) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
                Message msg = new Message();
                msg.what = REFRESH_FLAG;
                msg.obj = outDoors;
                handler.sendMessage(msg);
            }
        });
    }


    @Override
    public void onItmeClick(OutDoor outdoor) {
        Intent i = new Intent(getActivity(), OutDoorDetailActivity.class);
        i.putExtra("outdoor",outdoor);
        startActivity(i);

    }
}
