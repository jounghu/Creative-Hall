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
import cht.com.cht.model.GameInfo;

/**
 * Created by Administrator on 2016/12/14.
 */
public class FragmentGameAdapter extends RecyclerView.Adapter<FragmentGameAdapter.ViewHolder> {
    private List<GameInfo> mdata;
    private LayoutInflater inflater;
    private Context context;
    private GameItemClickListener listener;

    public void setItemListener(GameItemClickListener listener){
        this.listener = listener;
    }

    public interface GameItemClickListener{
         void gameItemClick(int postion ,GameInfo gameInfo);
    }
    public FragmentGameAdapter(Context context, List<GameInfo> mdata) {
        this.context = context;
        this.mdata = mdata;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_game_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GameInfo gameInfo = mdata.get(position);

        holder.mHead.setImageURI(gameInfo.getHead_img());
        holder.mTitle.setText(gameInfo.getTitle());
        holder.mZan.setText(gameInfo.getZanNum()+"人感兴趣");
        holder.mTime.setText(gameInfo.getTime().substring(0,gameInfo.getTime().length()-5));
        holder.mLocation.setText(gameInfo.getLocation());
        holder.mOrgnization.setText(gameInfo.getOrgnization());
    }

    @Override
    public int getItemCount() {
        return mdata==null?0:mdata.size();
    }


    public void addNewItem(List<GameInfo> newData){
            for (int i=0;i<mdata.size();i++){
                for (GameInfo gameinfo:newData
                     ) {
                    if(!mdata.contains(gameinfo)){
                        mdata.add(gameinfo);
                    }
                }
            }
    }

    public void clearAllItem(){
        mdata.clear();
    }

    public void addAllItem(List<GameInfo> newData){
        mdata.addAll(newData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SimpleDraweeView mHead;
        public TextView mTitle;
        public TextView mZan;
        public TextView mTime;
        public TextView mLocation;
        public TextView mOrgnization;



        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mHead = (SimpleDraweeView) itemView.findViewById(R.id.id_fragment_game_head);
            mTitle = (TextView) itemView.findViewById(R.id.id_fragment_game_title);
            mZan = (TextView) itemView.findViewById(R.id.id_fragment_game_favorite_num);
            mTime = (TextView) itemView.findViewById(R.id.id_fragment_game_time);
            mLocation = (TextView) itemView.findViewById(R.id.id_fragment_game_location);
            mOrgnization = (TextView) itemView.findViewById(R.id.id_fragment_game_organizer);
        }

        @Override
        public void onClick(View v) {
            listener.gameItemClick(getAdapterPosition(),mdata.get(getAdapterPosition()));
        }
    }
}
