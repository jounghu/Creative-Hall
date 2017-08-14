package cht.com.cht.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.ImagePagerActivity;
import cht.com.cht.R;

/**
 * Created by Administrator on 2016/11/27.
 */
public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ViewHolder> {

    private List<String> mPhotos;
    private LayoutInflater inflater;
    private Context context;


    public PhotoGridAdapter(Context context, List<String> mPhotos) {
        this.context = context;
        this.mPhotos = mPhotos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.recycle_item_grid_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mGridImg.setImageURI(mPhotos.get(position));
        holder.mGridImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgageBrower(position,mPhotos);

            }
        });
    }

    private void imgageBrower(int position, List<String> mPhotos) {
        Intent i = new Intent(context, ImagePagerActivity.class);
        Bundle b = new Bundle();
        b.putInt("position",position);
        b.putStringArrayList("mPhotos", (ArrayList<String>) mPhotos);
        i.putExtras(b);
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView mGridImg;
        public ViewHolder(View itemView) {
            super(itemView);
            mGridImg = (SimpleDraweeView) itemView.findViewById(R.id.id_recycle_item_grid_photo);

        }


    }
}
