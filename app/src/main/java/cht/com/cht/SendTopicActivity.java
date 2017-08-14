package cht.com.cht;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cht.com.cht.adapter.PhotoPickerAdapter;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.helper.RxSubscribe;
import cht.com.cht.model.Message;
import cht.com.cht.model.User;
import cht.com.cht.service.SendTopicServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SweetDialogUtil;
import cht.com.cht.utils.ToastUtil;
import cht.com.cht.widgt.RecyclerItemClickListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/11/30.
 */
public class SendTopicActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private Toolbar mToolbar;
    private TextView mSendBtn;
    private EditText mTitle;
    private EditText mContent;
    private ImageView mImageView;
    private RecyclerView mRecyclerview;

    private TextView mToolbarTitle;
    private TextView mSendTopicTitle;

    private PhotoPickerAdapter mPhotoAdapter;
    private ArrayList<String> mPhotoPath = new ArrayList<String>();
    private int currentClickId = -1;
    private int type = 0;


    private String titleStr;
    private String contentStr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_sendtopic_item);
        initViews();
        initRecyclerView();
        initPhotoView();
    }

    private void initPhotoView() {
        mSendBtn.setOnClickListener(this);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickView(v.getId());
            }
        });

        mRecyclerview.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PhotoPreview.builder()
                        .setPhotos(mPhotoPath)
                        .setCurrentItem(position)
                        .start(SendTopicActivity.this);
            }
        }));
    }

    private void clickView(int id) {
        switch (id) {
            case R.id.id_fragment_activity_sendtopic_addpicture:
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setGridColumnCount(3)
                        .start(this);
                break;
        }
        currentClickId = id;
    }

    private void initRecyclerView() {
        mRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        mPhotoAdapter = new PhotoPickerAdapter(mPhotoPath, this);
        mRecyclerview.setAdapter(mPhotoAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable("user");
        type = bundle.getInt("topic_type",-1);
        LogUtil.e(Constants.TAG, "type:"+type+"onResume: "+user);

        if(type==1){
            mToolbarTitle.setText("发布活动");
            mSendTopicTitle.setText("活动");
        }else if(type == 2){
            mToolbarTitle.setText("发布项目");
            mSendTopicTitle.setText("项目");
        }
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.id_fragment_activity_sendtopic_toolbar);

        mSendBtn = (TextView) findViewById(R.id.id_fragment_activity_sendtopic_sendbtn);
        mTitle = (EditText) findViewById(R.id.id_fragment_activity_sendtopic_title);
        mContent = (EditText) findViewById(R.id.id_fragment_activity_sendtopic_content);
        mImageView = (ImageView) findViewById(R.id.id_fragment_activity_sendtopic_addpicture);
        mRecyclerview = (RecyclerView) findViewById(R.id.id_fragment_activity_sendtopic_photos);

        mToolbarTitle = (TextView) findViewById(R.id.id_fragment_activity_sendtopic_toolbar_title);
        mSendTopicTitle = (TextView) findViewById(R.id.id_fragment_activity_sendtopic_type_title);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            mPhotoPath.clear();

            if (photos != null) {

                mPhotoPath.addAll(photos);
            }
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        LogUtil.e(Constants.TAG, "requestCode " + requestCode + "permissions " + permissions + " grantResults " + grantResults);
        // super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (currentClickId != -1) clickView(currentClickId);
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(this, "No read storage permission! Cannot perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.id_fragment_activity_sendtopic_sendbtn:
                    getData();
                    CheckData(titleStr,contentStr);
                    sendTopic(user.getId()+"",titleStr,contentStr,1,mPhotoPath);

                    break;
            }
    }

    private void sendTopic(String user_id,String title,String content,int type,List<String> photos){
        RequestBody userIDBody = RequestBody.create(MediaType.parse("text/plain"),user_id);
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"),title);
        RequestBody contentBody = RequestBody.create(MediaType.parse("text/plain"),content);
        RequestBody typeIDBody = RequestBody.create(MediaType.parse("text/plain"),type+"");
        final Map<String, RequestBody> map = new HashMap<>();
        for(int i=0;i<photos.size();i++){
            map.put("file"+i+";filename=\""+photos.get(i),RequestBody.create(MediaType.parse("image/*"),new File(photos.get(i))));
        }

        SendTopicServiceHandler.getSingleton(this).getService().sendTopic(userIDBody,titleBody,contentBody,typeIDBody,map).compose(RxHelper.<Message>handleResult())
        .subscribe(new RxSubscribe<Message>(SendTopicActivity.this,"正在发送...") {
            @Override
            protected void _onNext(Message message) {
                Intent i = new Intent(SendTopicActivity.this,MainActivity.class);
                finish();
                startActivity(i);
                overridePendingTransition(R.anim.push_no,R.anim.push_left_out);
            }

            @Override
            protected void _onError(String message) {
                ToastUtil.showToast(SendTopicActivity.this,message);
            }
        })
        ;
    }


    private void CheckData(String titleStr, String contentStr) {


        if(TextUtils.equals("",titleStr)){
            SweetDialogUtil.showSweetDialog(SendTopicActivity.this, SweetAlertDialog.ERROR_TYPE,"提示","请填写标题");
            return;
        }


        if(TextUtils.equals("",contentStr)){
            SweetDialogUtil.showSweetDialog(SendTopicActivity.this, SweetAlertDialog.ERROR_TYPE,"提示","内容没写啊！亲");
            return;
        }
    }

    private void getData() {
        titleStr = mTitle.getText().toString().trim();
        contentStr = mContent.getText().toString().trim();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(SendTopicActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(SendTopicActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





}
