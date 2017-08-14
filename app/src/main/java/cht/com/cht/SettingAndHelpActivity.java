package cht.com.cht;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.SharePreferenceUtil;

/**
 * Created by Administrator on 2017/3/4.
 */
public class SettingAndHelpActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mReg;
    private TextView mVer;
    private TextView mRePassword;
    private TextView mAbout;
    private TextView mExit;

    private void assignViews() {
        mToolbar = (Toolbar) findViewById(R.id.id_setting_toolbar);
        mReg = (TextView) findViewById(R.id.id_setting_reg);
        mVer = (TextView) findViewById(R.id.id_setting_ver);
        mRePassword = (TextView) findViewById(R.id.id_setting_repassword);
        mAbout = (TextView) findViewById(R.id.id_setting_about);
        mExit = (TextView) findViewById(R.id.id_setting_exit);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_help);
        assignViews();
        initEvent();
    }

    private void initEvent() {
        mReg.setOnClickListener(this);
        mVer.setOnClickListener(this);
        mRePassword.setOnClickListener(this);
        mAbout.setOnClickListener(this);
        mExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_setting_reg:
                //跳转指南activity 这里传递msg过去即可
                Intent regIntent = new Intent(this,GuideActivity.class);
                regIntent.putExtra("guideTitle","如何注册用户?");
                regIntent.putExtra("guideContent","注册模块采用手机号进行注册，您只需要输入手机号，进行短信验证即可。后台使用mob短信sdk进行短信验证，我们会秉承中国个人隐私权利保护法，对用户的手机号进行保密。");
                regIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(regIntent);
                break;
            case R.id.id_setting_ver:
                Intent verIntent = new Intent(this,GuideActivity.class);
                verIntent.putExtra("guideTitle","如何认证?");
                verIntent.putExtra("guideContent","如果是学生和老师需要填写学校，身份证号码，并且需要上传你的证件照。公司机构认证，需要填写公司名字以及上传公司证件，我们后台管理员会对上传的信息进行核实。注意只有核实过的用户才有权限进行相关的操作。");
                verIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(verIntent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.id_setting_repassword:
                Intent it = new Intent(this, MobSMSActivity.class);
                it.putExtra("flag", Constants.Mob.FORGET_FLAG);
                startActivity(it);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.id_setting_about:
                Intent aboutIntent = new Intent(this,AboutActivity.class);
                startActivity(aboutIntent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
            case R.id.id_setting_exit:
                //清空数据库 和 shareperference 内容 跳转至登陆界面
                clearAllData();
                break;
        }
    }

    private void clearAllData() {
        SharePreferenceUtil.clear(MyApplication.getContext());//清除所有内容
        MyAppDB.getSingleton(MyApplication.getContext()).clearAll();
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

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
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
