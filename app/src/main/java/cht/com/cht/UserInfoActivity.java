package cht.com.cht;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cht.com.cht.db.MyAppDB;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.helper.RxSubscribe;
import cht.com.cht.model.User;
import cht.com.cht.service.UpdateNicknameServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.ToastUtil;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/2/28.
 */
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int PHOTOPATH_FINISH = 0x1232312;
    private static final int UPDATE_NICKNAME_CODE = 12;
    private static final int FINISH_UPDATE = 123;
    private Toolbar toolbar;
    private SimpleDraweeView mHeadImg;
    private RelativeLayout mNickNameRl, mGenderRl;
    private TextView mNickName, mGender, mReg,mSend;
    private User user;

    private String newNicknameStr = "";
    private String genderStr = "";

    private ArrayList<String> mPhotoPath = new ArrayList<String>();
    private int currentClickId = -1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PHOTOPATH_FINISH:
                    refreshHeadImg(msg);
                    break;
                case UPDATE_NICKNAME_CODE:
                    String newNickname = (String) msg.obj;
                    mNickName.setText(newNickname);
                    break;
                case FINISH_UPDATE:
                    Intent i = new Intent(UserInfoActivity.this, MainActivity.class);
                    i.putExtra("phone", user.getPhone());
                    startActivity(i);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_change);

        initView();
        recievideDatas();
        configViews();
        initEvent();

        //得到photopath 上传到服务器 然后替换头像的地址 然后再返回用户的所有信息，然后再修改本地数据库中的信息

        // refreshHeadImg();


    }

    private void refreshHeadImg(Message msg) {
        String uri = "";
        LogUtil.e(Constants.TAG, "mPhotoPath :" + mPhotoPath.isEmpty());
        ArrayList<String> photoPaths = (ArrayList<String>) msg.obj;
        if (!photoPaths.isEmpty()) {
            uri = "file:" + File.separator + File.separator + mPhotoPath.get(0);
        }
        LogUtil.e(Constants.TAG, uri);
        mHeadImg.setImageURI(uri);
    }


    private void initEvent() {
        mHeadImg.setOnClickListener(this);
        mNickNameRl.setOnClickListener(this);
        mGenderRl.setOnClickListener(this);
        mSend.setOnClickListener(this);
    }

    private void configViews() {
        if (user != null) {
            mHeadImg.setImageURI(user.getHead_img());
            mNickName.setText(user.getNickname());
            int genderType = user.getGender();
            // 0代表男 1 代表女
            if (genderType == 0) {
                mGender.setText("男");
            } else {
                mGender.setText("女");
            }

            // 0 代表未认证 1 代表审核中 2 代表已经通过认证
            int regStatus = user.getStatus();
            if (regStatus == 0) {
                mReg.setText("审核中");
                mReg.setTextColor(getResources().getColor(R.color.yellow));
            } else if (regStatus == 1) {
                mReg.setText("已认证");
                mReg.setTextColor(getResources().getColor(R.color.primary_light));
            } else {
                mReg.setText("未认证");
                mReg.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }

    private void recievideDatas() {
        user = (User) getIntent().getSerializableExtra("user");
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.id_activity_collection_toolbar);
        mHeadImg = (SimpleDraweeView) findViewById(R.id.id_activity_userinfo_change_headimg);
        mNickNameRl = (RelativeLayout) findViewById(R.id.id_activity_userinfo_nickname_rl);
        mNickName = (TextView) findViewById(R.id.id_activity_userinfo_change_nickname);
        mGenderRl = (RelativeLayout) findViewById(R.id.id_activity_userinfo_gender_rl);
        mGender = (TextView) findViewById(R.id.id_activity_userinfo_change_gender);
        mReg = (TextView) findViewById(R.id.id_activity_userinfo_change_regstatus);
        mSend = (TextView) findViewById(R.id.id_update_nickname_send);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //上传修改好的信息
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadData() {
        LogUtil.e(Constants.TAG, "uploadData: " + mPhotoPath.size());
        if (mPhotoPath.size() > 0) {
            newNicknameStr = mNickName.getText().toString().trim();
            genderStr = mGender.getText().toString().trim();
            String user_id = user.getId() + "";
            Map<String, RequestBody> map = new HashMap<>();
            RequestBody idBody = RequestBody.create(MediaType.parse("text/plain"), user_id);
            RequestBody nicknameBody = RequestBody.create(MediaType.parse("text/plain"), newNicknameStr);
            RequestBody genderBody = RequestBody.create(MediaType.parse("text/plain"), genderStr);
            for (int i = 0; i < mPhotoPath.size(); i++) {
                LogUtil.e(Constants.TAG, "photo path is:" + mPhotoPath.get(i));
                map.put("file" + i + ";filename=\"" + mPhotoPath.get(i), RequestBody.create(MediaType.parse("image/*"), new File(mPhotoPath.get(i))));
            }
            UpdateNicknameServiceHandler.getInstance().getService().update(idBody, nicknameBody, genderBody, map).compose(RxHelper.<User>handleResult()).subscribe(new RxSubscribe<User>(UserInfoActivity.this, "正在修改...") {
                @Override
                protected void _onNext(final User user) {
                    MyAppDB.getSingleton(UserInfoActivity.this).updateUserNickname(user);
                    Message msg = new Message();
                    msg.what = FINISH_UPDATE;
                    handler.sendMessage(msg);
                    ToastUtil.showToast(UserInfoActivity.this,"提交成功");
                }

                @Override
                protected void _onError(String message) {
                    Message msg = new Message();
                    msg.what = FINISH_UPDATE;
                    handler.sendMessage(msg);
                    ToastUtil.showToast(UserInfoActivity.this,"抱歉提交失败");
                }
            });

        } else {
            //这里指的是正常提交的 就是没有选择照片的
            newNicknameStr = mNickName.getText().toString().trim();
            genderStr = mGender.getText().toString().trim();
            int user_id = user.getId();
            UpdateNicknameServiceHandler.getInstance().getGenderService().updateNicknameGender(user_id,genderStr,newNicknameStr).compose(RxHelper.<User>handleResult()).subscribe(new RxSubscribe<User>(UserInfoActivity.this, "正在修改...") {
                @Override
                protected void _onNext(User user) {
                    MyAppDB.getSingleton(UserInfoActivity.this).updateUserNickname(user);
                    Message msg = new Message();
                    msg.what = FINISH_UPDATE;
                    handler.sendMessage(msg);
                    ToastUtil.showToast(UserInfoActivity.this,"提交成功");
                }

                @Override
                protected void _onError(String message) {
                    Message msg = new Message();
                    msg.what = FINISH_UPDATE;
                    handler.sendMessage(msg);
                    ToastUtil.showToast(UserInfoActivity.this,"抱歉提交失败");
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //上传修改好的信息
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onClickView(@IdRes int viewId) {
        switch (viewId) {
            case R.id.id_activity_userinfo_change_headimg:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(this);
                break;
        }
        currentClickId = viewId;
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
            if (currentClickId != -1) onClickView(currentClickId);
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(this, "No read storage permission! Cannot perform the action.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        // No need to explain to user as it is obvious
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_activity_userinfo_change_headimg:
                onClickView(v.getId());
                break;

            case R.id.id_activity_userinfo_nickname_rl:
                //修改昵称
                String nicknameStr = mNickName.getText().toString().trim();
                Intent i = new Intent(this, NickNameUpdateActivity.class);
                i.putExtra("nickname", nicknameStr);
                startActivityForResult(i, UPDATE_NICKNAME_CODE);
                break;
            case R.id.id_activity_userinfo_gender_rl:
                //修改性别
                showSelectDialog();
                break;
            case R.id.id_update_nickname_send:
                uploadData();
                break;

        }
    }

    private void showSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setItems(new String[]{"男", "女"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mGender.setText("男");
                } else {
                    mGender.setText("女");
                }
            }
        });
        builder.setTitle("性别");
        builder.show();
    }

    //回掉PhotoPath

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_NICKNAME_CODE && resultCode == RESULT_OK) {
            String newNickname = "";
            if (data != null) {
                newNickname = data.getStringExtra("newNickname");
            }
            Message msg = new Message();
            msg.what = UPDATE_NICKNAME_CODE;
            msg.obj = newNickname;
            handler.sendMessage(msg);
        }

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
            Message msg = new Message();
            msg.obj = mPhotoPath;
            msg.what = PHOTOPATH_FINISH;
            handler.sendMessage(msg);
        }
    }
}
