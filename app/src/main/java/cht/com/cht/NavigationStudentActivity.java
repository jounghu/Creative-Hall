package cht.com.cht;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import cht.com.cht.model.School;
import cht.com.cht.model.User;
import cht.com.cht.service.SchoolServiceHandler;
import cht.com.cht.service.VerifyServiceHanlder;
import cht.com.cht.systemService.MyService;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.FromStringUtil;
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
 * Created by Administrator on 2016/11/22.
 */
public class NavigationStudentActivity extends AppCompatActivity implements View.OnClickListener {


    private User user;
    private Toolbar mToolBar;

    //学生和老师控件
    private TextView mSchoolName;
    private EditText mName;
    private EditText mIdentity;

    private ImageView mAddPicture;
    private RecyclerView mRecyclerView;

    private List<School> mSchools;

    private TextView mSendBtn;

    private SweetAlertDialog dialog;


    private PhotoPickerAdapter mPhotoAdapter;
    private ArrayList<String> mPhotoPath = new ArrayList<String>();
    private int currentClickId = -1;

    //保存表单数据
    private  String mSchoolStr;
    private String  mNameStr;
    private String mIdentityStr;


    private Intent serviceIntent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_stu_tea_verify);
        initStuView();
        initRecycler();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getBundleExtra("userInfo");
        user = (User) bundle.getSerializable("user");
        LogUtil.e(Constants.TAG, "onResume: "+user);
    }

    private void initRecycler() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        mPhotoAdapter = new PhotoPickerAdapter(mPhotoPath, this);
        mRecyclerView.setAdapter(mPhotoAdapter);
    }

    private void initEvent() {
        mSchoolName.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);


        mAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickView(v.getId());
            }
        });


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PhotoPreview.builder()
                        .setPhotos(mPhotoPath)
                        .setCurrentItem(position)
                        .start(NavigationStudentActivity.this);
            }
        }));

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
            if (currentClickId != -1) onClickView(currentClickId);
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            Toast.makeText(this, "No read storage permission! Cannot perform the action.", Toast.LENGTH_SHORT).show();
        }
    }



    public void onClickView(@IdRes int viewId) {
        switch (viewId) {
            case R.id.id_stu_verify_addpicture:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setGridColumnCount(3)
                        .start(this);
                break;

        }
        currentClickId = viewId;
    }


    private void initSchoolData() {
        SchoolServiceHandler.getSingleton().getSchoolService().getSchoolInfo().compose(RxHelper.<List<School>>handleResult())
                .subscribe(new RxSubscribe<List<School>>(NavigationStudentActivity.this, "正在请求学校信息...") {
                    @Override
                    protected void _onNext(List<School> schools) {
                        mSchools = schools;
                        showAlert();
                    }

                    @Override
                    protected void _onError(String message) {
                        ToastUtil.showToast(NavigationStudentActivity.this,message);
                    }
                });

    }

    private void initStuView() {
        mSchoolName = (TextView) findViewById(R.id.id_stu_verify_schoolname);
        mName = (EditText) findViewById(R.id.id_stu_verify_name);
        mIdentity = (EditText) findViewById(R.id.id_stu_verify_identity);


        mSendBtn = (TextView) findViewById(R.id.id_stu_verify_sendbtn);
        mAddPicture = (ImageView) findViewById(R.id.id_stu_verify_addpicture);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_stu_verify_recycleview);


        mToolBar = (Toolbar) findViewById(R.id.id_navigation_activity_verify_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent i = new Intent(NavigationStudentActivity.this, MainActivity.class);
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
                Intent i = new Intent(NavigationStudentActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_stu_verify_schoolname:
                initSchoolData();
                break;
            case R.id.id_stu_verify_sendbtn:
                getData();
                if(checkStr(mSchoolStr,mNameStr,mIdentityStr)){
                    LogUtil.e(Constants.TAG,"id_stu_verify_sendbtn  :"+mPhotoPath);
                    if(mPhotoPath.size()>0){
                        // TODO: 2016/11/25 跳转之前保存用户信息到数据库
                        upLoad(String.valueOf(user.getId()),mSchoolStr,mNameStr,mIdentityStr,mPhotoPath);
                    }else{
                        SweetDialogUtil.showSweetDialog(NavigationStudentActivity.this, SweetAlertDialog.ERROR_TYPE,"提示","请拍摄照片");
                        return;
                    }
                }
        }
    }




    private boolean checkStr(String mSchoolStr, String mNameStr, String mIdentityStr) {
        if(TextUtils.equals("",mSchoolStr)){
            SweetDialogUtil.showSweetDialog(NavigationStudentActivity.this, SweetAlertDialog.ERROR_TYPE,"提示","请选择学校");
            return false;
        }
        if(TextUtils.equals("",mNameStr)){
            SweetDialogUtil.showSweetDialog(NavigationStudentActivity.this, SweetAlertDialog.ERROR_TYPE,"提示","请填写你的真实名字");
            return false;
        }

        if(TextUtils.equals("",mIdentityStr)){
            SweetDialogUtil.showSweetDialog(NavigationStudentActivity.this, SweetAlertDialog.ERROR_TYPE,"提示","请填写你的身份证号");
            return false;
        }

        if(!FromStringUtil.isIDCard(mIdentityStr)){
            SweetDialogUtil.showSweetDialog(NavigationStudentActivity.this, SweetAlertDialog.ERROR_TYPE,"提示","请填写正确的身份证号");
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(serviceIntent!=null){
            stopService(serviceIntent);
        }


    }

    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(NavigationStudentActivity.this);
        dialog.setTitle("你的学校:");
        String[] schoolName = new String[mSchools.size() - 1];
        int index = 0;
        for (School school : mSchools
                ) {
            if (school.getId() != 1) {
                schoolName[index] = school.getSchool_name();
                index++;
            }

        }
        dialog.setItems(schoolName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSchoolName.setText(mSchools.get(which + 1).getSchool_name());
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public boolean shouldShowRequestPermissionRationale(@NonNull String permission) {
        // No need to explain to user as it is obvious
        return false;
    }

    public void getData() {
        mSchoolStr = mSchoolName.getText().toString().trim();
        mNameStr = mName.getText().toString().trim();
        mIdentityStr = mIdentity.getText().toString().trim();
    }


    private void upLoad(String userid,String schoolStr, String name, String identity, List<String> photoPath) {
        LogUtil.e(Constants.TAG,"upLoad strat");
        final Map<String, RequestBody> map = new HashMap<>();
        RequestBody useridBody = RequestBody.create(MediaType.parse("text/plain"),userid);
        RequestBody schoolBody = RequestBody.create(MediaType.parse("text/plain"),schoolStr);
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"),name);
        RequestBody identityBody = RequestBody.create(MediaType.parse("text/plain"),identity);
        for(int i=0;i<photoPath.size();i++){
            LogUtil.e(Constants.TAG,"photo path is:"+photoPath.get(i));
            map.put("file"+i+";filename=\""+photoPath.get(i),RequestBody.create(MediaType.parse("image/*"),new File(photoPath.get(i))));
        }
        VerifyServiceHanlder.getSingleton(this).getService().upload(useridBody,schoolBody,nameBody,identityBody,map).compose(RxHelper.<Message>handleResult()).subscribe(new RxSubscribe<Message>(NavigationStudentActivity.this,"正在上传证件照...") {
            @Override
            protected void _onNext(Message message) {
                dialog = new SweetAlertDialog(NavigationStudentActivity.this,SweetAlertDialog.WARNING_TYPE);
                dialog.setTitleText("注意");
                dialog.setContentText("你的证件照已提交，我们工作人员会在一个工作日内，完成审核。谢谢！");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        dialog.dismiss();
                        serviceIntent= new Intent(NavigationStudentActivity.this, MyService.class);
                        Bundle b = new Bundle();
                        user.setStatus(3);
                        b.putSerializable("user",user);
                        b.putInt("update_save",1);
                        serviceIntent.putExtras(b);
                        startService(serviceIntent);

                        Intent i = new Intent(NavigationStudentActivity.this,MainActivity.class);
                        i.putExtra("phone",user.getPhone());
                        i.putExtra("verify_type",3);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.modal_in,R.anim.modal_out);
                    }
                });
                dialog.show();
            }

            @Override
            protected void _onError(String message) {
                ToastUtil.showToast(NavigationStudentActivity.this,message);
            }
        });

    }
}
