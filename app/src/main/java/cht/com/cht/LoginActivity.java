package cht.com.cht;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cht.com.cht.helper.RxHelper;
import cht.com.cht.helper.RxSubscribe;
import cht.com.cht.model.Collection;
import cht.com.cht.model.Message;
import cht.com.cht.model.User;
import cht.com.cht.service.CheckPhoneServiceHandler;
import cht.com.cht.service.CollectionNumServiceHandler;
import cht.com.cht.service.FavoriteServiceHandler;
import cht.com.cht.service.LoginCheckServiceHandler;
import cht.com.cht.systemService.MyService;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.FromStringUtil;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;
import cht.com.cht.utils.SnackbarUtil;
import cht.com.cht.utils.SweetDialogUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/11/17.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mUsername;
    private EditText mPassword;
    private Button mLogin;
    private Toolbar mLoginToolbar;
    private TextView mLoginToolBarTv;
    private TextView mShowSnak;
    private String mUsernameStr;
    private String mPasswordStr;


    private TextView mRegiste;
    private TextView mFroget;

    private User userInfo = null;

    private   Intent serviceIntent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initViews();

        initEvenet();
        mLoginToolBarTv.setText("登录");
        setSupportActionBar(mLoginToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private void initEvenet() {
        mLogin.setOnClickListener(this);
        mFroget.setOnClickListener(this);
        mRegiste.setOnClickListener(this);
    }


    private void initViews() {
        mUsername = (EditText) findViewById(R.id.id_login_username);
        mPassword = (EditText) findViewById(R.id.id_login_password);
        mLogin = (Button) findViewById(R.id.id_login_btn);
        mShowSnak = (TextView) findViewById(R.id.id_login_snak);
        mLoginToolbar = (Toolbar) findViewById(R.id.id_login_toolbar);
        mLoginToolBarTv = (TextView) findViewById(R.id.id_login_toolbar_text);

        mRegiste = (TextView) findViewById(R.id.id_login_registe);
        mFroget = (TextView) findViewById(R.id.id_login_forget);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_login_btn:
                mUsernameStr = mUsername.getText().toString().trim();
                mPasswordStr = mPassword.getText().toString().trim();
                if (TextUtils.equals("", mUsernameStr)) {
                    SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE, "提示", "手机号不能为空");
                    return;
                }

                if (!FromStringUtil.isChinaPhoneLegal(mUsernameStr)) {
                    SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE, "提示", "请填写正确的手机号");
                    mUsername.setText("");
                    return;
                }

                if (TextUtils.equals("", mPasswordStr)) {
                    SweetDialogUtil.showSweetDialog(this, SweetAlertDialog.ERROR_TYPE, "提示", "密码不能为空");
                    return;
                }


                CheckPhoneServiceHandler.getInstance().getCheckService().checkUser(mUsernameStr).compose(RxHelper.<Message>handleResult())
                        .subscribe(new RxSubscribe<Message>(LoginActivity.this, "请稍后...") {
                            @Override
                            protected void _onNext(Message phone) {

                                LogUtil.e(Constants.TAG, phone.getMsg() + "");
                                if (phone.getMsg() == 1) {
                                    LoginCheckServiceHandler.getSingleton().getLoginCheckService()
                                            .checkUser(mUsernameStr, mPasswordStr)
                                            .compose(RxHelper.<User>handleResult())
                                            .subscribe(new RxSubscribe<User>(LoginActivity.this, "正在登陆") {
                                                @Override
                                                protected void _onNext(User user) {
                                                    LogUtil.e(Constants.TAG, user.toString());
                                                    userInfo = user;
                                                    //通过服务保存对象到数据库
                                                    serviceIntent= new Intent(LoginActivity.this, MyService.class);
                                                    Bundle b = new Bundle();
                                                    b.putSerializable("user",user);
                                                    b.putInt("update_save",2);
                                                    serviceIntent.putExtras(b);
                                                    startService(serviceIntent);
                                                    SharePreferenceUtil.put(LoginActivity.this,Constants.SharePerferenceConfig.FIRST_LOGIN,false);
                                                    SharePreferenceUtil.put(LoginActivity.this,Constants.SharePerferenceConfig.USER_TOKEN,user.getPhone());

                                                    //这里发送一个广播，更新sqlite collection数据库信息。
                                                    toUpdateCollection();
                                                    //这里发送一个广播，更新 favorite数据库信息
                                                    toUpdateFavorite();

                                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                    i.putExtra("phone",user.getPhone());
                                                    startActivity(i);
                                                    finish();
                                                    overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                                                }

                                                @Override
                                                protected void _onError(String message) {
                                                    //    SweetDialogUtil.showSweetDialog(MyApplication.getContext(),SweetAlertDialog.ERROR_TYPE,"提示",message);
                                                    // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                                    SnackbarUtil.show(mLogin, message, 0);
                                                }
                                            });

                                } else {
                                    //在手机上面好像不管用
                                    SnackbarUtil.show(mLogin, "用户不存在", 0);
                                }
                            }

                            @Override
                            protected void _onError(String message) {

                            }
                        });
                break;

            case R.id.id_login_registe:

                Intent i = new Intent(LoginActivity.this, MobSMSActivity.class);
                i.putExtra("flag", Constants.Mob.REGISTE_FLAG);
                startActivity(i);
                // finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;

            case R.id.id_login_forget:
                Intent it = new Intent(this, MobSMSActivity.class);
                it.putExtra("flag", Constants.Mob.FORGET_FLAG);
                startActivity(it);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                break;
        }
    }

    private void toUpdateFavorite() {
        // TODO: 2016/12/7 把选择出来的构造成一个字符串进行传输，在这个activity退出之后，上传服务器 写服务器获取字符 
        FavoriteServiceHandler.getInstance().getInfoService().getFavoriteInfo(userInfo.getId()).compose(RxHelper.<List<String>>handleResult())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        String str = "";
                        if(strings==null||strings.size()<=0){
                            return;
                        }
                        for (String tag:strings
                             ) {
                            str = str + tag + ";";
                        }
                        LogUtil.e(Constants.TAG,"Mainactivity toUpdateFavorite "+str);
                        Intent i = new Intent(Constants.FAVORITE_UPFATE_BRODCAST_ACTION);
                        i.putExtra("tags",str);
                        i.putExtra("user_id",userInfo.getId());
                        sendBroadcast(i);
                    }
                });
    }

    private void toUpdateCollection() {
        CollectionNumServiceHandler.getInstance().getUpdateService().updateCollection(userInfo.getId()).compose(RxHelper.<List<Collection>>handleResult())
                .subscribe(new Subscriber<List<Collection>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.showToast(MyApplication.getContext(),"登录失败");
                    }

                    @Override
                    public void onNext(List<Collection> collections) {
                            if(collections==null||collections.size()<=0){
                                return;
                            }
                        LogUtil.e(Constants.TAG,"Loginactivity: "+collections);
                        Intent i = new Intent(Constants.COLLECTION_UPDATE_BRODCAST_ACTION);
                        i.putExtra("collections", (Serializable) collections);
                        sendBroadcast(i);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(serviceIntent!=null){
            stopService(serviceIntent);
        }

    }
}
