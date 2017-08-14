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
import cht.com.cht.model.CommentInfo;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;

/**
 * Created by Administrator on 2016/12/3.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentInfo> datas;
    private Context context;
    private LayoutInflater inflater;

    private onCommentItemListener listener;


    public void addCommentItemListener( onCommentItemListener listener){
        this.listener = listener;
    }

    public CommentAdapter(List<CommentInfo> datas, Context context) {
        this.datas = datas;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.comment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentInfo commentInfo = datas.get(position);
        LogUtil.e(Constants.TAG,commentInfo+"");
        holder.mHeadImg.setImageURI(commentInfo.getFrom_user_img());
        holder.mNickName.setText(commentInfo.getFrom_user_nickname());
        holder.mDetailTime.setText(commentInfo.getFrom_user_comment_time()+"");
        if(commentInfo.getTo_user_nickname()==null||commentInfo.getTo_user_nickname().equals("")){

                holder.mContent.setText(commentInfo.getComment_content());
        }else{

            holder.mContent.setText("回复:"+commentInfo.getTo_user_nickname()+":"+commentInfo.getComment_content());
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    public void addNew(List<CommentInfo> commentInfos){
        for (CommentInfo comm:commentInfos
             ) {
            if(!datas.contains(comm)){
                datas.add(comm);
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView mHeadImg;
        public TextView mNickName;
        public TextView mDetailTime;
        public TextView mContent;
        public ViewHolder(View itemView) {
            super(itemView);
            mHeadImg = (SimpleDraweeView) itemView.findViewById(R.id.id_comment_detail_head_img);
            mNickName = (TextView) itemView.findViewById(R.id.id_comment_detail_nickname);
            mDetailTime = (TextView) itemView.findViewById(R.id.id_comment_detail_time);
            mContent = (TextView) itemView.findViewById(R.id.id_comment_detail_content);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCommentItemClick(getAdapterPosition(),datas.get(getAdapterPosition()));
                }
            });
        }


    }

    public interface onCommentItemListener{
        void onCommentItemClick(int position,CommentInfo commentInfo);
    }


}
