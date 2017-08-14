package cht.com.cht;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.adapter.TabFragmentAdapter;
import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.fragment.ActivityFragment;
import cht.com.cht.fragment.GameFragment;
import cht.com.cht.fragment.OutSchoolFragment;
import cht.com.cht.fragment.ProjectFragment;
import cht.com.cht.helper.RxHelper;
import cht.com.cht.model.Message;
import cht.com.cht.model.User;
import cht.com.cht.service.CollectionNumServiceHandler;
import cht.com.cht.service.FavoriteServiceHandler;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int COLLECTION_NUM_FINISH = 0x121324;
    private static final int FAVORITE_NUM_FINISH = 0x12122;
    private static final int UPDATE_NICKNAME = 0x12;


    private DrawerLayout mTabDrawerLayout;
    private CoordinatorLayout mContentCoordinatorLayout;
    private AppBarLayout mContentAppBarLayout;
    private Toolbar mContentToolbar;
    private TextView mContentTextView;
    private SimpleDraweeView mContentHeadImg;
    private TabLayout mContentTabLayout;
    private ViewPager mContentViewPager;
    private NavigationView mTabNavigationView;


    //navigation红点textview
    private TextView mNavigation_Verify_tv;
    private TextView mNavigation_Message_tv;
    private TextView mNavigation_Collection_tv;
    private TextView mNavigation_Favorite_tv;
    //收藏数
    private int mCollectionNum = 0;
    //喜欢数
    private int mFavoriteNum = 0;

    //navigation head 头像以及昵称
    private SimpleDraweeView mHeadImg;
    private TextView mNickName;

    private String[] mTitles;
    private List<Fragment> mViews;

    private User user;

    private int verifyType;//这里表示认证状态
    private SweetAlertDialog verifyDialog;


    private TabFragmentAdapter mTabAdapter;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case COLLECTION_NUM_FINISH:
                    LogUtil.e(Constants.TAG,"mCollectionNum"+mCollectionNum);
                    mNavigation_Collection_tv.setText(mCollectionNum+"");
                    break;
                case FAVORITE_NUM_FINISH:
                    LogUtil.e(Constants.TAG,"FAVORITE_NUM_FINISH "+mFavoriteNum);
                    mNavigation_Favorite_tv.setText(mFavoriteNum+"");
                    break;
                case UPDATE_NICKNAME:
                    mHeadImg.setImageURI(user.getHead_img());
                    mContentHeadImg.setImageURI(user.getHead_img());
                    mNickName.setText(user.getNickname());
                    mContentTextView.setText(user.getNickname());
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initHeadView();
        initDatas();
        configViews();
        initEvent();
    }

    private void initEvent() {
        mHeadImg.setOnClickListener(this);
    }

    private void initHeadView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view=  inflater.inflate(R.layout.activity_main_navigation_head,null,false);
        mHeadImg = (SimpleDraweeView) view.findViewById(R.id.id_navigation_head_img);
        mNickName = (TextView) view.findViewById(R.id.id_navigation_head_textview);
        mTabNavigationView.addHeaderView(view);
    }

    private void configViews() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //显示首页标题昵称
        mContentTextView.setText(user.getNickname());
        mContentHeadImg.setImageURI(user.getHead_img());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mTabDrawerLayout, mContentToolbar, R.string.open, R.string.close);
        toggle.syncState();
        mTabDrawerLayout.addDrawerListener(toggle);
        //实现navigation菜单点击事件
        onNavgationViewMenuItemSelected(mTabNavigationView);

        mHeadImg.setImageURI(user.getHead_img());
        mNickName.setText(user.getNickname());


        mTabAdapter = new TabFragmentAdapter(getSupportFragmentManager(),mTitles,mViews);
        mContentViewPager.setAdapter(mTabAdapter);
        mContentTabLayout.setupWithViewPager(mContentViewPager);


    }

    @Override
    protected void onResume() {
        super.onResume();
        String phoneStr = (String) SharePreferenceUtil.get(MyApplication.getContext(),Constants.SharePerferenceConfig.USER_TOKEN,"");
        user = MyAppDB.getSingleton(MyApplication.getContext()).readUser(phoneStr);
        int i = (int) SharePreferenceUtil.get(MyApplication.getContext(),"verify_status",-1);
        if(user.getStatus()==1&&i==-1) {
            showPass();
        }
        initializeCountDrawer();
        refreshNickname();//更新nickname
    }

    private void refreshNickname() {
        android.os.Message msg = handler.obtainMessage();
        msg.what = UPDATE_NICKNAME;
        handler.sendMessage(msg);
    }

    private void initializeCountDrawer() {
        mNavigation_Verify_tv.setGravity(Gravity.CENTER_VERTICAL);
        mNavigation_Verify_tv.setTypeface(null, Typeface.BOLD);
        mNavigation_Verify_tv.setTextColor(getResources().getColor(R.color.colorAccent));

        if (user.getStatus() == 0) {
            if(verifyType!=-1&&verifyType==3){
                mNavigation_Verify_tv.setText("审核中");
            }else{
                mNavigation_Verify_tv.setText("未认证");
            }
        } else if(user.getStatus()==1) {
            mNavigation_Verify_tv.setText("已认证");
        }else{
            mNavigation_Verify_tv.setText("审核中");
        }

        //TODO message是否收到消息
        mNavigation_Message_tv.setGravity(Gravity.CENTER_VERTICAL);
        mNavigation_Message_tv.setTypeface(null, Typeface.BOLD);
        mNavigation_Message_tv.setTextColor(getResources().getColor(R.color.colorAccent));
        mNavigation_Message_tv.setText("99+");

        //关注消息
        mNavigation_Favorite_tv.setGravity(Gravity.CENTER_VERTICAL);
        mNavigation_Favorite_tv.setTypeface(null, Typeface.BOLD);
        mNavigation_Favorite_tv.setTextColor(getResources().getColor(R.color.colorAccent));
        mNavigation_Favorite_tv.setText("0");
        getFavoriteCount();

        mNavigation_Collection_tv.setGravity(Gravity.CENTER_VERTICAL);
        mNavigation_Collection_tv.setTypeface(null, Typeface.BOLD);
        mNavigation_Collection_tv.setTextColor(getResources().getColor(R.color.colorAccent));
        //getCollectionCount();取得所有collection的数量
        getCollectionCount();



    }

    private void getFavoriteCount() {
        FavoriteServiceHandler.getInstance().getNumService().getUserFavoriteNum(user.getId()).compose(RxHelper.<Message>handleResult())
                .subscribe(new Subscriber<Message>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Message message) {
                        mFavoriteNum = message.getMsg();
                        android.os.Message msg = handler.obtainMessage();
                        msg.what = FAVORITE_NUM_FINISH;
                        handler.sendMessage(msg);
                    }
                });

    }

    private void showNotClick() {
        verifyDialog = new SweetAlertDialog(MainActivity.this,SweetAlertDialog.WARNING_TYPE);
        verifyDialog.setTitleText("注意");
        verifyDialog.setContentText("你的证件照已提交，我们正在审核中。谢谢！");
        verifyDialog.setCancelable(true);
        verifyDialog.setCanceledOnTouchOutside(false);
        verifyDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                verifyDialog.dismiss();
            }
        });
        verifyDialog.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        verifyType = getIntent().getIntExtra("verify_type",-1);

        String phone  = getIntent().getStringExtra("phone");
        if(phone==null||phone.equals("")){
            List<User> users = MyAppDB.getSingleton(MainActivity.this).readUser();
            user = users.get(0);
        }else{
            user = MyAppDB.getSingleton(MainActivity.this).readUser(phone);
        }
        LogUtil.e(Constants.TAG,"onNewIntent---------"+user);


    }

    private void onNavgationViewMenuItemSelected(final NavigationView mTabNavigationView) {
        //显示侧滑标题和头像


        mTabNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_navigation_menu_index:
                        break;
                    case R.id.id_navigation_menu_verify:

                        if (user.getStatus() == 0) {
                            showAlert();
                        } else if(user.getStatus()==1) {
                            showPass();
                        }else if(user.getStatus()==3){
                            showNotClick();
                        }
                        break;
                    case R.id.id_navigation_menu_message:
                        //TODO 关闭导航栏 跳转消息 messageactivity
                        break;
                    case R.id.id_navigation_menu_focuse:
                        Intent i = new Intent(MainActivity.this,ForcusActivity.class);
                        i.putExtra("user",user);
                        startActivity(i);
                        overridePendingTransition(R.anim.push_no,R.anim.push_right_out);
                        break;
                    case R.id.id_navigation_menu_collection:
                        Intent intent = new Intent(MainActivity.this,CollectionActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_no,R.anim.push_right_out);
                        break;
                    case R.id.id_navigation_menu_setting:
                        Intent settingIntent = new Intent(MainActivity.this,SettingAndHelpActivity.class);
                        startActivity(settingIntent);
                        overridePendingTransition(R.anim.push_no,R.anim.push_right_out);
                        break;
                    case R.id.id_navigation_menu_feedback:
                        Intent feedbackIntent = new Intent(MainActivity.this,FeedBackActivity.class);
                        startActivity(feedbackIntent);
                        overridePendingTransition(R.anim.push_no,R.anim.push_right_out);
                        break;


                }
                item.setCheckable(true);
                mTabDrawerLayout.closeDrawers();
                return true;
            }


        });

    }

    private void showPass() {
        SharePreferenceUtil.put(MyApplication.getContext(),"verify_status",1);
        verifyDialog = new SweetAlertDialog(MainActivity.this,SweetAlertDialog.WARNING_TYPE);
        verifyDialog.setTitleText("注意");
        verifyDialog.setContentText("你的证件照已通过审核了！");
        verifyDialog.setCancelable(true);
        verifyDialog.setCanceledOnTouchOutside(false);
        verifyDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                verifyDialog.dismiss();
            }
        });
        verifyDialog.show();
    }
    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("你认证的类型");
        dialog.setItems(new String[]{"学生", "老师", "机构"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogUtil.e(Constants.TAG, "onClick: "+which);
                if (which == 0 || which == 1) {
                    //跳转学生老师界面
                    Intent i = new Intent(MainActivity.this, NavigationStudentActivity.class);
                    startActivity(i);
                    Bundle b = new Bundle();
                    b.putSerializable("user", user);
                    i.putExtra("userInfo", b);
                    startActivity(i);
                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                } else {
                    //跳转机构界面
                    Intent i = new Intent(MainActivity.this, NavigationComplanyActivity.class);
                    startActivity(i);
                    Bundle b = new Bundle();
                    b.putSerializable("user", user);
                    i.putExtra("userInfo", b);
                    startActivity(i);
                    overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

                }
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }


    private void initDatas() {

        mTitles = getResources().getStringArray(R.array.tab_titles);
        mViews = new ArrayList<>();
        //这里初始化Fragment记得使用懒加载
        mViews.add(new ActivityFragment());
        mViews.add(new ProjectFragment());
        mViews.add(new GameFragment());
        mViews.add(new OutSchoolFragment());



        String phone = getIntent().getStringExtra("phone");

        if(phone==null||phone.equals("")){
            List<User> users = MyAppDB.getSingleton(MainActivity.this).readUser();
            user = users.get(0);
        }else{
            user = MyAppDB.getSingleton(MainActivity.this).readUser(phone);
        }
        LogUtil.e(Constants.TAG,"initDatas:  "+user);


    }

    private void initViews() {
        mTabDrawerLayout = (DrawerLayout) findViewById(R.id.id_main_tab_drawerlayout);
        mTabNavigationView = (NavigationView) findViewById(R.id.id_main_tab_navigationview);

        mContentCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.id_main_content_coordinatorlayout);
        mContentAppBarLayout = (AppBarLayout) findViewById(R.id.id_main_content_appbarlayout);
        mContentToolbar = (Toolbar) findViewById(R.id.id_main_content_toolbar);
        mContentTextView = (TextView) findViewById(R.id.id_main_content_toolbar_title);
        mContentHeadImg = (SimpleDraweeView) findViewById(R.id.id_main_toolbar_img);
        mContentTabLayout = (TabLayout) findViewById(R.id.id_main_content_tablayout);
        mContentViewPager = (ViewPager) findViewById(R.id.id_main_content_viewpager);

        //初始化红点
        mNavigation_Verify_tv = (TextView) MenuItemCompat.getActionView(mTabNavigationView.getMenu().
                findItem(R.id.id_navigation_menu_verify));
        mNavigation_Message_tv = (TextView) MenuItemCompat.getActionView(mTabNavigationView.getMenu().
                findItem(R.id.id_navigation_menu_message));

        mNavigation_Collection_tv = (TextView) MenuItemCompat.getActionView(mTabNavigationView.getMenu().
                findItem(R.id.id_navigation_menu_collection));
        mNavigation_Favorite_tv = (TextView) MenuItemCompat.getActionView(mTabNavigationView.getMenu().
                findItem(R.id.id_navigation_menu_focuse));



        //初始化头像
        mHeadImg = (SimpleDraweeView) findViewById(R.id.id_navigation_head_img);
        mNickName = (TextView) findViewById(R.id.id_navigation_head_textview);
        setSupportActionBar(mContentToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.id_menu_exit:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getCollectionCount() {
        CollectionNumServiceHandler.getInstance().getService().getCollectionNum(user.getId()).compose(RxHelper.<Message>handleResult())
                .subscribe(new Subscriber<Message>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                            LogUtil.e(Constants.TAG,""+e.getMessage());
                    }

                    @Override
                    public void onNext(Message message) {
                        mCollectionNum
                                 = message.getMsg();
                        android.os.Message msg = handler.obtainMessage();
                        msg.what = COLLECTION_NUM_FINISH;
                        handler.sendMessage(msg);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_navigation_head_img:
                //// TODO: 2017/2/28 修改昵称
                Intent i = new Intent(this,UserInfoActivity.class);
                i.putExtra("user",user);
                startActivity(i);
                break;

        }
    }
}
