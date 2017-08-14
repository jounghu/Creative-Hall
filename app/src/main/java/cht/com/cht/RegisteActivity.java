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
import android.widget.LinearLayout;
import android.widget.TextView;

import cht.com.cht.helper.RxHelper;
import cht.com.cht.helper.RxSubscribe;
import cht.com.cht.model.User;
import cht.com.cht.service.RegisteServiceHandler;
import cht.com.cht.systemService.MyService;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;
import cht.com.cht.utils.SnackbarUtil;
import cht.com.cht.utils.SweetDialogUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/11/18.
 */
public class RegisteActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mRegisteToolbar;
    private TextView mRegisteToolbarTitle;

    private TextView mUsername;
    private TextView mNickname;
    private EditText mPassword;
    private EditText mConfirm;
    private Button mRegisteButton;
    private LinearLayout mShowSnakbar;

    private TextView mRegisteRule;

    private String mPhoneStr;
    private String mPasswordStr;
    private String mConfirmStr;
    private String mNicknameStr;

    private  Intent serviceIntent;
    @Override
    protected void onResume() {
        super.onResume();
        mPhoneStr =  getIntent().getStringExtra("phoneNum");
        LogUtil.e(Constants.TAG,"RegisteActivity:"+mPhoneStr);
        mUsername.setText(mPhoneStr);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        initViews();
        initEvent();
    }

    private void initData() {

        mPasswordStr = mPassword.getText().toString().trim();
        mConfirmStr = mConfirm.getText().toString().trim();
        mNicknameStr = mNickname.getText().toString().trim();
    }

    private void initEvent() {

        mRegisteButton.setOnClickListener(this);
        mRegisteRule.setOnClickListener(this);
    }

    private void initViews() {
        mRegisteToolbar = (Toolbar) findViewById(R.id.id_registe_toolbar);
        mRegisteToolbarTitle = (TextView) findViewById(R.id.id_registe_toolbar_text);

        mUsername = (TextView) findViewById(R.id.id_registe_username);
        mPassword = (EditText) findViewById(R.id.id_registe_password);
        mConfirm = (EditText) findViewById(R.id.id_regiset_confirm);
        mRegisteButton = (Button) findViewById(R.id.id_registe_button);
        mShowSnakbar = (LinearLayout) findViewById(R.id.id_registe_showsnackbar);
        mRegisteRule = (TextView) findViewById(R.id.id_registe_rule);

        mNickname = (TextView) findViewById(R.id.id_registe_nickname);


        setSupportActionBar(mRegisteToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_registe_button:
                initData();
               if(chekText(mNicknameStr,mPasswordStr,mConfirmStr)){
                   RegisteServiceHandler.getSingleton().getRegisteService().checkUser(mNicknameStr,mPasswordStr,mPhoneStr).compose(RxHelper.<User>handleResult())
                           .subscribe(new RxSubscribe<User>(RegisteActivity.this,"正在注册...") {
                               @Override
                               protected void _onNext(final User user) {
                                   serviceIntent = new Intent(RegisteActivity.this, MyService.class);
                                   Bundle b = new Bundle();
                                   b.putSerializable("user",user);
                                   b.putInt("update_save",2);
                                   serviceIntent.putExtras(b);
                                   startService(serviceIntent);
                                   SharePreferenceUtil.put(RegisteActivity.this,Constants.SharePerferenceConfig.FIRST_LOGIN,false);
                                   SharePreferenceUtil.put(RegisteActivity.this,Constants.SharePerferenceConfig.USER_TOKEN,user.getPhone());
                                   Intent i = new Intent(RegisteActivity.this,MainActivity.class);
                                   i.putExtra("phone",user.getPhone());
                                   startActivity(i);
                                   finish();
                                   overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                               }

                               @Override
                               protected void _onError(String message) {
                                   SnackbarUtil.show(mPassword, message, 0);
                               }
                           });
               }

                break;
            case R.id.id_registe_rule:
                Intent i = new Intent(RegisteActivity.this, RegisteRuleActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }






    private boolean chekText(String mNicknameStr, String mPasswordStr, String mConfirmStr) {
        LogUtil.e(Constants.TAG,"mNicknameStr"+ mNicknameStr +"mPasswordStr  "+mPasswordStr+ "mConfirmStr "+mConfirmStr);

        if(TextUtils.equals("", mNicknameStr)||mNicknameStr==null){
            SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE, "提示", "请给自己起一个昵称");
            return false;
        }

        if (TextUtils.equals("", mConfirmStr) || TextUtils.equals("", mPasswordStr)) {
            SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE, "提示", "密码不能为空");
            return false;
        }

        if (!TextUtils.equals(mConfirmStr, mPasswordStr)) {
            SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE, "提示", "两次密码输入不一致！");
            mConfirm.setText("");
            return false;
        }
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(serviceIntent!=null){
            stopService(serviceIntent);
        }
    }
}
