package cht.com.cht.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.R;
import cht.com.flowtaglibrary.OnInitSelectedPosition;

/**
 * Created by HanHailong on 15/10/19.
 */
public class TagAdapter<T> extends BaseAdapter implements OnInitSelectedPosition {


    private final Context mContext;
    private final List<T> mDataList;

    private static List<String> mSelectTag = new ArrayList<>();




    public TagAdapter(Context context) {
        this.mContext = context;
        mDataList = new ArrayList<>();
    }

    /**
     * 设置用户选中的标签
     * @param mSelectTag
     */
    public static void setmSelectTag(List<String> mSelectTag) {
        TagAdapter.mSelectTag = mSelectTag;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, null);

        TextView textView = (TextView) view.findViewById(R.id.tv_tag);
        T t = mDataList.get(position);

        if (t instanceof String) {
            textView.setText((String) t);
        }
        return view;
    }

    public void onlyAddAll(List<T> datas) {
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<T> datas) {
        mDataList.clear();
        onlyAddAll(datas);
    }


    @Override
    public boolean isSelectedPosition(int position) {
//        if (position % 2 == 0) {
//            return true;
//        }
        //返回true就会变成蓝色
        for (String tag:mSelectTag
             ) {
            if(mDataList.get(position).equals(tag)){
                return true;
            }
        }
        return false;
    }
}
