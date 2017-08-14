package cht.com.cht;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cht.com.cht.application.MyApplication;
import cht.com.cht.systemService.UpdateUserService;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;

/**
 * Created by Administrator on 2016/11/17.
 */
public class SplashActivity extends AppCompatActivity {
    private boolean isLogin;
    private String userToken;
    private   Intent serviceIntent;
    private Handler handler = new Handler(){};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               isLogin = (boolean) SharePreferenceUtil.get(MyApplication.getContext(), Constants.SharePerferenceConfig.FIRST_LOGIN,true);
                userToken = (String) SharePreferenceUtil.get(MyApplication.getContext(), Constants.SharePerferenceConfig.USER_TOKEN,"");
                LogUtil.e(Constants.TAG,isLogin+""+"userPhoneNum +"+userToken);
                if(isLogin){

                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
                }else{
                    if(!userToken.equals("")){
                        serviceIntent = new Intent(SplashActivity.this, UpdateUserService.class);
                        serviceIntent.putExtra("UserToken",userToken);
                        startService(serviceIntent);
                    }
                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
                }

            }
        },3000);
    }


}
