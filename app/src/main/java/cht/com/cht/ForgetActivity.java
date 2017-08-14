package cht.com.cht;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cht.com.cht.helper.RxHelper;
import cht.com.cht.helper.RxSubscribe;
import cht.com.cht.model.User;
import cht.com.cht.service.ForgetPasswordServiceHanlder;
import cht.com.cht.systemService.MyService;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.SharePreferenceUtil;
import cht.com.cht.utils.SnackbarUtil;
import cht.com.cht.utils.SweetDialogUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/11/18.
 */
public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mFrogetToolbar;
    private EditText mFrogetPassword;
    private EditText mFrogetConfrim;
    private Button mForgetButton;

    private String mPasswrodStr;
    private String mPasswrodConfirm;
    private String mPhoneStr;

    private  Intent serviceIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forget);
        initView();
        initEvent();
    }

    private void initEvent() {
        mForgetButton.setOnClickListener(this);
    }

    private void initView() {
        mFrogetToolbar = (Toolbar) findViewById(R.id.id_forget_toolbar);
        mFrogetPassword = (EditText) findViewById(R.id.id_forget_password);
        mFrogetConfrim = (EditText) findViewById(R.id.id_foget_confirm);
        mForgetButton = (Button) findViewById(R.id.id_forget_button);

        setSupportActionBar(mFrogetToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPhoneStr = getIntent().getStringExtra("phoneNum");
    }

    private void getEditText(){
        mPasswrodStr = mFrogetPassword.getText().toString().trim();
        mPasswrodConfirm = mFrogetConfrim.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_forget_button:
                getEditText();
               if(checkPassword()){
                   ForgetPasswordServiceHanlder.getInstance().getService().changePassword(mPhoneStr,mPasswrodStr).compose(RxHelper.<User>handleResult())
                           .subscribe(new RxSubscribe<User>(ForgetActivity.this,"正在找回密码...") {

                               @Override
                               protected void _onNext(User user) {
                                   serviceIntent = new Intent(ForgetActivity.this, MyService.class);
                                   Bundle b = new Bundle();
                                   b.putSerializable("user",user);
                                   b.putInt("update_save",1);
                                   serviceIntent.putExtras(b);
                                   startService(serviceIntent);
                                   SharePreferenceUtil.put(ForgetActivity.this,Constants.SharePerferenceConfig.FIRST_LOGIN,false);
                                   SharePreferenceUtil.put(ForgetActivity.this,Constants.SharePerferenceConfig.USER_TOKEN,user.getPhone());
                                   Intent i = new Intent(ForgetActivity.this,MainActivity.class);
                                   i.putExtra("phone",user.getPhone());
                                   startActivity(i);
                                   finish();
                                   overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                               }



                               @Override
                               protected void _onError(String message) {
                                   SnackbarUtil.show(mForgetButton,message,0);
                               }
                           });

               }
                break;
        }
    }


    private boolean checkPassword() {
        if (TextUtils.equals("", mPasswrodConfirm)||TextUtils.equals("", mPasswrodStr)) {
            SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE,"提示","密码不能为空");
            return false;
        }

        if (!TextUtils.equals(mPasswrodConfirm,mPasswrodStr)){
            SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE,"提示","两次密码输入不一致！");
            mFrogetConfrim.setText("");
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(serviceIntent!=null){
            stopService(serviceIntent);
        }
    }
}
