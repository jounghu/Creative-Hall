package cht.com.cht.widgt;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;

/**
 * Created by Administrator on 2016/12/1.
 */
public class RecyclerViewLoadMore {
    static LoadMore loadMore;

    public static void addOnScrollListener(final Fragment fragment, RecyclerView recyclerView){
        loadMore = (LoadMore) fragment;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {


            boolean isScrollToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int lastVisable = layoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();

                    if(lastVisable == (totalItemCount-1)&& !isScrollToLast){
                        loadMore.loadMore();
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtil.e(Constants.TAG,"onScrolled : "+dx+"   "+dy);
                if (dx > 0) {
                    isScrollToLast = true;
                } else {
                    isScrollToLast = false;
                }
            }
        });

    }



    public interface LoadMore{
         void loadMore();
    }

}
