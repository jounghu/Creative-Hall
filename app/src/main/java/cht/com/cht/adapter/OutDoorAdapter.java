package cht.com.cht.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cht.com.cht.R;
import cht.com.cht.model.OutDoor;

/**
 * Created by Administrator on 2017/2/20.
 */
public class OutDoorAdapter  extends RecyclerView.Adapter<OutDoorAdapter.ViewHolder>{
    private List<OutDoor> mData;
    private Context context;
    private LayoutInflater inflater;
    private OutDoorItemListener listener;

    public OutDoorItemListener getListener() {
        return listener;
    }

    public void setListener(OutDoorItemListener listener) {
        this.listener = listener;
    }

    public OutDoorAdapter(List<OutDoor> data, Context context) {
        this.mData = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.fragment_outdoor_recy_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OutDoor outDoor = mData.get(position);
        holder.mTitle.setText(outDoor.getTitle());
        holder.mContent.setText(outDoor.getContent());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void clearAll(){
       if(!mData.isEmpty()){
           mData.clear();
       }
    }

    public void addAll(List<OutDoor> datas){
       mData.addAll(datas);
    }

    public void addNewItem(List<OutDoor> datas){
        for (int i=0;i<datas.size();i++){
            if(!mData.contains(datas.get(i))){
                mData.add(datas.get(i));
            }
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public TextView mContent;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = (TextView) itemView.findViewById(R.id.id_fragment_outdoor_title);
            mContent = (TextView) itemView.findViewById(R.id.id_fragment_outdoor_content);
        }

        @Override
        public void onClick(View v) {
            listener.onItmeClick(mData.get(getAdapterPosition()));
        }
    }

    public interface OutDoorItemListener {
         void onItmeClick(OutDoor outdoor);
    }
}
