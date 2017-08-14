package cht.com.cht;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/2.
 */
public class NickNameUpdateActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mCancle;
    private TextView mFinish;
    private EditText mNicknameEt;
    private String mNicknameStr = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nickname);
        initView();
        receviedData();
        initEvent();
    }

    private void initEvent() {
        mCancle.setOnClickListener(this);
        mFinish.setOnClickListener(this);
    }

    private void receviedData() {
        mNicknameStr =  getIntent().getStringExtra("nickname");
        mNicknameEt.setText(mNicknameStr);
    }

    private void initView() {
        mCancle = (TextView) findViewById(R.id.id_update_nickname_cancle);
        mFinish = (TextView) findViewById(R.id.id_update_nickname_finish);
        mNicknameEt = (EditText) findViewById(R.id.id_update_nickname_et);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_update_nickname_cancle:
                this.finish();
                overridePendingTransition(R.anim.push_up_out,R.anim.push_up_in);
                break;
            case R.id.id_update_nickname_finish:
                if(TextUtils.equals(mNicknameStr,mNicknameEt.getText().toString().trim())){
                    this.finish();
                    overridePendingTransition(R.anim.push_up_out,R.anim.push_up_in);
                }else{
                    String newNickname = mNicknameEt.getText().toString().trim();
                    Intent i = new Intent();
                    i.putExtra("newNickname",newNickname);
                    this.setResult(Activity.RESULT_OK,i);
                    this.finish();
                    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                }
                break;
        }
    }


}
