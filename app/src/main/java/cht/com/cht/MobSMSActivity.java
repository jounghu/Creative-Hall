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
import cht.com.cht.model.Message;
import cht.com.cht.service.CheckPhoneServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.FromStringUtil;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SweetDialogUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2016/11/20.
 */
public class MobSMSActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mMobToolbar;
    private EditText mPhone;
    private Button mMobButton;


    private long activity_flag;
    private String mPhoneNum;
    private boolean isSend;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_smss_item);
        initView();
        initEvent();


    }

    private void initEvent() {
        mMobButton.setOnClickListener(this);

    }

    private void initView() {
        mMobToolbar = (Toolbar) findViewById(R.id.id_mob_toolbar);
        mPhone = (EditText) findViewById(R.id.id_mob_phone);
        mMobButton = (Button) findViewById(R.id.id_mob_button);
        activity_flag = getIntent().getLongExtra("flag", 2213L);

        LogUtil.e(Constants.TAG, "activity_flag:" + activity_flag);
        setSupportActionBar(mMobToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



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
            case R.id.id_mob_button:
             //   ToastUtil.showToast(this,"click");
                mPhoneNum = mPhone.getText().toString().trim();
                checkPhone(mPhoneNum);
                CheckPhoneServiceHandler.getInstance().getCheckService().checkUser(mPhoneNum).compose(RxHelper.<Message>handleResult())
                        .subscribe(new RxSubscribe<Message>(MobSMSActivity.this,"请稍后...") {
                            @Override
                            protected void _onNext(Message message) {
                                if(message.getMsg()==1){
                                    if(activity_flag==Constants.Mob.FORGET_FLAG){
                                        Intent i = new Intent(MobSMSActivity.this,MobVerifyActivity.class);
                                        i.putExtra("flag",activity_flag);
                                        i.putExtra("phoneNum",mPhoneNum);
                                        startActivity(i);
                                        finish();
                                        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                                    }else{
                                        SweetDialogUtil.showSweetDialog(MobSMSActivity.this, SweetAlertDialog.ERROR_TYPE, "提示", "手机号已经被注册");
                                    }

                                }else{
                                       Intent i = new Intent(MobSMSActivity.this,MobVerifyActivity.class);
                                       i.putExtra("flag",activity_flag);
                                       i.putExtra("phoneNum",mPhoneNum);
                                       startActivity(i);
                                       finish();
                                       overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                                }
                            }

                            @Override
                            protected void _onError(String message) {
                                SweetDialogUtil.showSweetDialog(MobSMSActivity.this, SweetAlertDialog.ERROR_TYPE, "提示", message);
                            }
                        });

                break;
        }

    }

    private void checkPhone(String mPhoneNum) {
        if (TextUtils.equals("", mPhoneNum)) {
            SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE, "提示", "手机号不能为空");
            return;
        }

        if (!FromStringUtil.isChinaPhoneLegal(mPhoneNum)) {
            SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE, "提示", "请输入正确的手机号码");
        }
    }
}
