package cht.com.cht;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.helper.RxSubscribe;
import cht.com.cht.model.User;
import cht.com.cht.service.FeedBackServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.SharePreferenceUtil;
import cht.com.cht.utils.ToastUtil;

/**
 * Created by Administrator on 2017/3/5.
 */
public class FeedBackActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    private static  final  int  SEND_FEEDBACK = 1231;
    private Toolbar mToolBar;
    private TextView mSend;
    private EditText mFeedBack;
    private TextView mCountText;
    private User user;
    private int mCountNum = 240;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SEND_FEEDBACK:
                    startSend(msg);

                    break;
            }
        }
    };

    private void startSend(Message msg) {
        String content = (String) msg.obj;
        int user_id = user.getId();
        String textCount = mCountText.getText().toString().trim();
        int countNum = Integer.parseInt(textCount);
        if(countNum==240){
            ToastUtil.showToast(FeedBackActivity.this,"请留下您的建议！");
            return;
        }else{
            FeedBackServiceHandler.getInstance().getCheckService().sendFeedBack(content,user_id).compose(RxHelper.<cht.com.cht.model.Message>handleResult()).subscribe(new RxSubscribe<cht.com.cht.model.Message>(FeedBackActivity.this,"请稍后...") {
                @Override
                protected void _onNext(cht.com.cht.model.Message message) {
                    ToastUtil.showToast(FeedBackActivity.this,"谢谢您的反馈！");
                    finish();
                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                }

                @Override
                protected void _onError(String message) {
                    ToastUtil.showToast(FeedBackActivity.this,"抱歉，服务器发生异常...");
                }
            });
        }
    }

    private void assignViews() {
        mCountText = (TextView) findViewById(R.id.id_feedback_count);
        mToolBar = (Toolbar) findViewById(R.id.id_feedback_toolbar);
        mSend = (TextView) findViewById(R.id.id_feedback_send);
        mFeedBack = (EditText) findViewById(R.id.id_feedback_et);
        String phoneStr = (String) SharePreferenceUtil.get(MyApplication.getContext(), Constants.SharePerferenceConfig.USER_TOKEN,"");
        user = MyAppDB.getSingleton(MyApplication.getContext()).readUser(phoneStr);

        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        assignViews();
        initEvent();


    }

    private void initEvent() {
        mFeedBack.addTextChangedListener(this);
        mSend.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mCountNum = mCountNum - count+before;
        mCountText.setText(mCountNum+"");
        mSend.setClickable(true);
        mSend.setTextColor(getResources().getColor(R.color.primary_light));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        Message msg = new Message();
        msg.what = SEND_FEEDBACK;
        msg.obj = mFeedBack.getText().toString().trim();
        handler.sendMessage(msg);
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
