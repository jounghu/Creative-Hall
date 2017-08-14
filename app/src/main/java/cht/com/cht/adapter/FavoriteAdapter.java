package cht.com.cht.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cht.com.cht.R;
import cht.com.cht.model.FavoriteInfo;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;

/**
 * Created by Administrator on 2016/12/7.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<FavoriteInfo> mSelectTags;
    private Context context;
    private LayoutInflater inflater;

    public FavoriteAdapter(List<FavoriteInfo> mSelectTags, Context context) {
        this.mSelectTags = mSelectTags;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }




    public void addNewTag(List<FavoriteInfo> newTag) {
        mSelectTags.clear();
        mSelectTags.addAll(newTag);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.favorite_avtivity_recy_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
             LogUtil.e(Constants.TAG,"FavoriteAdapter  onBindViewHolder"+mSelectTags.get(position).toString());
            holder.mHead.setImageURI(mSelectTags.get(position).getImg());
            holder.mDesc.setText(mSelectTags.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return mSelectTags.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView mHead;
        public TextView mDesc;
        public ViewHolder(View itemView) {
            super(itemView);
            mDesc = (TextView) itemView.findViewById(R.id.id_activity_forcuse_recy_title);
            mHead = (SimpleDraweeView) itemView.findViewById(R.id.id_activity_forcuse_recy_head);
        }
    }
}
