package cht.com.cht.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cht.com.cht.CommentActivity;
import cht.com.cht.R;
import cht.com.cht.application.MyApplication;
import cht.com.cht.model.TopicInfo;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;

/**
 * Created by Administrator on 2016/11/27.
 */
public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {

    private List<TopicInfo> data;
    private Context context;
    private LayoutInflater inflater;
    private PhotoGridAdapter mPhotoAdapter;

    public void setData(List<TopicInfo> data) {
        this.data = data;
    }

    public void clearAll(){
       for(int i=0;i<data.size();i++){
           data.remove(i);
       }
    }


    public CollectionAdapter(List<TopicInfo> data, Context context) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.recycle_item, parent, false), context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        initTopic(holder, position);
    }

    private void initTopic(ViewHolder holder, int position) {
        TopicInfo topic = data.get(position);
        List<String> photos = topic.getPhotos();
        LogUtil.e(Constants.TAG, "FragmentAdapter BindViewHolder " + photos);
        holder.mHeadImg.setImageURI(topic.getHeadImg());
        holder.mNickName.setText(topic.getNickName());
        //// TODO: 2016/11/27 这里要根据时间来判断
        holder.mDateTime.setText(topic.getDate().toString());
        holder.mTitle.setText(topic.getTitle());
        holder.mContent.setText(topic.getContent());
        if (photos == null) {
            holder.mPhotoShow.setVisibility(View.GONE);
            // holder.mPhotoShow.setLayoutManager();
            // holder.mPhotoShow.setAdapter();
        } else {
            if (photos.size() == 4 || photos.size() == 1 || photos.size() == 2) {
                holder.mPhotoShow.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            } else {
                holder.mPhotoShow.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            }

            holder.mPhotoShow.setVisibility(View.VISIBLE);
            mPhotoAdapter = new PhotoGridAdapter(context, photos);
            holder.mPhotoShow.setAdapter(mPhotoAdapter);

        }
        holder.mZanNum.setText(topic.getZan() + "");
        holder.mCommentNum.setText(topic.getComment() + "");
    }


    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size();
    }

    public List<TopicInfo> getData() {
        return data;
    }


    public void addNewData(List<TopicInfo> newTopicInfo) {

        LogUtil.e(Constants.TAG, "newTopicInfo :  " + newTopicInfo);

        for (TopicInfo topic : newTopicInfo) {

            //这里判断的是引用，要重写hashcode方法
            if (!data.contains(topic)) {
                LogUtil.e(Constants.TAG, data.contains(topic) + "");
                data.add(topic);
            }

        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SimpleDraweeView mHeadImg;
        public TextView mNickName;
        public TextView mDateTime;
        public TextView mTitle;
        public TextView mContent;
        public RecyclerView mPhotoShow;
        public ImageView mZan;
        public TextView mZanNum;
        public ImageView mComment;
        public TextView mCommentNum;
        private Context context;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            mHeadImg = (SimpleDraweeView) itemView.findViewById(R.id.id_recycle_item_head);
            mNickName = (TextView) itemView.findViewById(R.id.id_recycle_item_nickname);
            mDateTime = (TextView) itemView.findViewById(R.id.id_recycle_item_time);
            mTitle = (TextView) itemView.findViewById(R.id.id_recycle_item_title);
            mContent = (TextView) itemView.findViewById(R.id.id_recycle_item_content);
            mPhotoShow = (RecyclerView) itemView.findViewById(R.id.id_recycle_item_photo);
            mZan = (ImageView) itemView.findViewById(R.id.id_recycle_item_zan);
            mZanNum = (TextView) itemView.findViewById(R.id.id_recycle_item_zannum);
            mComment = (ImageView) itemView.findViewById(R.id.id_recycle_item_comment);
            mCommentNum = (TextView) itemView.findViewById(R.id.id_recycle_item_commentnum);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(data.get(getAdapterPosition()));
                }
            });

            mZan.setOnClickListener(this);
            mComment.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.id_recycle_item_zan:
                    int flag = -1;
                    int topic_id = (int) SharePreferenceUtil.get(MyApplication.getContext(), "topic_id",0x12323123);
                    int zanNum = data.get(getAdapterPosition()).getZan();
                    if(topic_id == 0x12323123){
                        //这里表示第一次进来？
                        mZanNum.setText("" + (zanNum + 1));
                        mZan.setImageResource(R.drawable.zan_select);
                        data.get(getAdapterPosition()).setZan((zanNum+1));
                        flag = 0;
                        SharePreferenceUtil.put(MyApplication.getContext(),"topic_id",data.get(getAdapterPosition()).getId());
                    }else if(topic_id == data.get(getAdapterPosition()).getId()){
                        mZanNum.setText("" + (zanNum - 1));
                        mZan.setImageResource(R.drawable.zan_normal);
                        data.get(getAdapterPosition()).setZan((zanNum-1));
                        flag = 1;
                        SharePreferenceUtil.remove(MyApplication.getContext(),"topic_id");
                    }

                    //发送点赞了到服务器,使用广播通知
                    Intent i = new Intent(Constants.ZAN_BRODCAST_ACTION);
                    i.putExtra("topic_id",data.get(getAdapterPosition()).getId());
                    i.putExtra("flag",flag);
                    context.sendBroadcast(i);
                    break;
                case R.id.id_recycle_item_comment:
                    //跳转详情界面
                    startActivity(data.get(getAdapterPosition()));
                    break;
            }

        }
    }


    private void startActivity(TopicInfo topicInfo){
        Intent i = new Intent(context,CommentActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("topicInfo",topicInfo);
        i.putExtras(b);
        context.startActivity(i);
    }
}
