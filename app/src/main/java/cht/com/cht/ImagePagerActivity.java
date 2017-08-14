package cht.com.cht;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;

/**
 * Created by Administrator on 2016/11/29.
 */
public class ImagePagerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private TextView mCount;
    private ViewPager mImageBrowser;
    private SimpleDraweeView mImage;

    private LayoutInflater inflater;

    private int position;
    private List<String> mPhotos;

    private List<View> mViews;

    private RelativeLayout rl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);
        initView();
        initData();
        SetView();


    }

    private void SetView() {

        mImageBrowser.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                mImage = (SimpleDraweeView) view.findViewById(R.id.id_recycle_item_grid_photo);
                rl = (RelativeLayout) view.findViewById(R.id.id_activity_image_browser_rl);
                rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        overridePendingTransition(R.anim.push_no, R.anim.push_right_out);
                    }
                });
                mImage.setImageURI(mPhotos.get(position));

                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViews.get(position));
            }
        });
        mImageBrowser.setOffscreenPageLimit(3);
        mImageBrowser.addOnPageChangeListener(this);
        mImageBrowser.setCurrentItem(position);
        mCount.setText((position + 1) + "/" + mPhotos.size());
    }

    private void initData() {
        mViews = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        mPhotos = bundle.getStringArrayList("mPhotos");
        for (int i = 0; i < mPhotos.size(); i++) {
            View view = inflater.inflate(R.layout.image_broser_item, null, false);
            mViews.add(view);
        }

    }

    private void initView() {
        mCount = (TextView) findViewById(R.id.id_activity_image_browser_num);
        mImageBrowser = (ViewPager) findViewById(R.id.id_activity_image_browser_viewpager);
        inflater = LayoutInflater.from(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        LogUtil.e(Constants.TAG, "onPageSelected " + position);
        mCount.setText((position + 1) + "/" + mPhotos.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }





}
