package cht.com.cht.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import cht.com.cht.R;
import me.iwf.photopicker.utils.AndroidLifecycleUtils;

/**
 * Created by Administrator on 2016/11/23.
 */
public class PhotoPickerAdapter extends RecyclerView.Adapter<PhotoPickerAdapter.PhotoViewHolder> {
    private ArrayList<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;

    private Context mContext;

    public PhotoPickerAdapter(ArrayList<String> photoPaths, Context mContext) {
        this.photoPaths = photoPaths;
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.__picker_item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Uri uri = Uri.fromFile(new File(photoPaths.get(position)));

        boolean canLoadImage = AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.getContext());

        if (canLoadImage) {
            Glide.with(mContext)
                    .load(uri)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.__picker_ic_photo_black_48dp)
                    .error(R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(holder.ivPhoto);
        }
    }

    @Override
    public int getItemCount() {
        return photoPaths.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
            vSelected.setVisibility(View.GONE);
        }
    }

}
