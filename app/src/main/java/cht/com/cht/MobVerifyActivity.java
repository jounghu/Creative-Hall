package cht.com.cht;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SnackbarUtil;
import cht.com.cht.utils.SweetDialogUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2016/11/20.
 */
public class MobVerifyActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mMobVerifyToolBar;
    private EditText mMobVerifyCode;
    private Button mMobVerifyButton;
    private TextView mMobTime;
    private Button mMobAgain;
    private int timeCount = 60;

    private long activity_flag;
    private String mPhoneNum;
    private String mCodeStr;
    private boolean isVerify = true;

    private SweetAlertDialog sweetAlertDialog;

    private Handler handlerText = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 10000:
                   if (timeCount > 0) {
                       timeCount--;
                       handlerText.sendEmptyMessageDelayed(10000, 1000);
                   }else{
                       timeCount = 60;
                       mMobAgain.setVisibility(View.VISIBLE);
                   }
                   mMobTime.setText("重新发送" + timeCount + "s");

                   break;
           }

        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            //短信发送成功 result -1
          LogUtil.e(Constants.TAG,"event"+event + "result "+result);
         if(result==SMSSDK.RESULT_COMPLETE){

             //验证 event 3
             if(event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){

                 if (activity_flag == Constants.Mob.FORGET_FLAG) {
                     Intent i = new Intent(MobVerifyActivity.this, ForgetActivity.class);
                     i.putExtra("phoneNum", mPhoneNum);
                     startActivity(i);
                     finish();
                     overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                 }else
                 if (activity_flag == Constants.Mob.REGISTE_FLAG) {
                     Intent i = new Intent(MobVerifyActivity.this, RegisteActivity.class);
                     i.putExtra("phoneNum", mPhoneNum);
                     startActivity(i);
                     finish();
                     overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                 }


             }  else if(event == 2){
                 //收到消息
                 if(sweetAlertDialog!=null&&sweetAlertDialog.isShowing()){
                     sweetAlertDialog.dismiss();
                 }
                 handlerText.sendEmptyMessageDelayed(10000, 1000);
             }

         }else{
             try {
                 Throwable throwable = (Throwable) data;
                 throwable.printStackTrace();
                 JSONObject object = new JSONObject(throwable.getMessage());
                 String des = object.optString("detail");//错误描述
                 int status = object.optInt("status");//错误代码

                 if(status==477){
                     if(sweetAlertDialog!=null&&sweetAlertDialog.isShowing()){
                         sweetAlertDialog.dismiss();
                     }
                     SweetDialogUtil.showSweetDialog(MobVerifyActivity.this,SweetAlertDialog.ERROR_TYPE,"提示","你今天验证次数达到上限！");

                 }else if(status==519){
                     if(sweetAlertDialog!=null&&sweetAlertDialog.isShowing()){
                         sweetAlertDialog.dismiss();
                     }
                     SweetDialogUtil.showSweetDialog(MobVerifyActivity.this,SweetAlertDialog.ERROR_TYPE,"提示","请求发送验证码次数超出限制!");
                 }else if(status == 520){
                     if(sweetAlertDialog!=null&&sweetAlertDialog.isShowing()){
                         sweetAlertDialog.dismiss();
                     }
                     SweetDialogUtil.showSweetDialog(MobVerifyActivity.this,SweetAlertDialog.ERROR_TYPE,"提示","验证码错误，请重新输入");
                 }

                 if (status > 0 && !TextUtils.isEmpty(des)) {
                     if(sweetAlertDialog!=null&&sweetAlertDialog.isShowing()){
                         sweetAlertDialog.dismiss();
                     }
                     SnackbarUtil.show(mMobAgain,"验证码错误，请重新输入",2);
                     mMobVerifyCode.setText("");
                     return;
                 }
             } catch (Exception e) {
                 //do something
             }
         }

        }
    };

    private EventHandler myEventHandler = new EventHandler() {

        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            handler.sendMessage(msg);


        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        cn.smssdk.SMSSDK.registerEventHandler(myEventHandler);
        getVerifyCode();
        sweetAlertDialog = new SweetAlertDialog(MobVerifyActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("提示");
        sweetAlertDialog.setCancelText("请稍后...");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.show();
    }

    private void getVerifyCode() {
        SMSSDK.getVerificationCode("86", mPhoneNum);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(myEventHandler);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mob_verify_item);
        initView();
        initEvent();
    }


    private void initEvent() {

        mMobTime.setClickable(false);

        mMobVerifyButton.setOnClickListener(this);
        mMobTime.setOnClickListener(this);
        mMobAgain.setOnClickListener(this);
    }

    private void initView() {
        mMobVerifyToolBar = (Toolbar) findViewById(R.id.id_mob_verify_toolbar);
        mMobVerifyCode = (EditText) findViewById(R.id.id_mob_verify_code);
        mMobVerifyButton = (Button) findViewById(R.id.id_mob_verify_button);
        mMobTime = (TextView) findViewById(R.id.id_mob_verify_time);
        mMobAgain = (Button) findViewById(R.id.id_mob_verify_again);
        setSupportActionBar(mMobVerifyToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activity_flag = getIntent().getLongExtra("flag", 2213L);

        mPhoneNum = getIntent().getStringExtra("phoneNum");

        LogUtil.e(Constants.TAG, "activity_flag:" + activity_flag);
        LogUtil.e(Constants.TAG, "mPhoneNum:" + mPhoneNum);
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
            case R.id.id_mob_verify_button:
                mCodeStr = mMobVerifyCode.getText().toString().trim();
                checkCodeStr(mCodeStr);
                isVerify = true;
                SMSSDK.submitVerificationCode("86", mPhoneNum, mCodeStr);
                break;
            case R.id.id_mob_verify_again:
                if (mPhoneNum != null) {
                    SMSSDK.getVerificationCode("86", mPhoneNum);
                    Message msg = new Message();
                    msg.what =10000;
                    handlerText.sendMessage(msg);
                }
                mMobAgain.setVisibility(View.GONE);
                break;
        }
    }

    private void checkCodeStr(String mCodeString) {
        if (mCodeString == null || mCodeString.equals("")) {
            SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE, "提示", "请输入验证码");
            return;
        }
    }
}
