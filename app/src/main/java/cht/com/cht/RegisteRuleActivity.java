package cht.com.cht;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cht.com.cht.utils.Constants;
import cht.com.cht.utils.SnackbarUtil;

/**
 * Created by Administrator on 2016/11/18.
 */
public class RegisteRuleActivity extends AppCompatActivity {

    private WebView mRegisteRule;
    private Toolbar mRegisteRuleToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe_rule);
        initViews();

        initEvent();
    }

    private void initEvent() {
        WebSettings settings = mRegisteRule.getSettings();
        settings.setJavaScriptEnabled(true);
        mRegisteRule.loadUrl(Constants.Registe.REGISTE_RULE);


        mRegisteRule.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadUrl(failingUrl);
                SnackbarUtil.show(view,description+"请检查网络设置！",0);
            }
        });

    }

    private void initViews() {
        mRegisteRule = (WebView) findViewById(R.id.id_registe_rule_detail);
        mRegisteRuleToolbar = (Toolbar) findViewById(R.id.id_registe_rule_toolbar);

        setSupportActionBar(mRegisteRuleToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                if (mRegisteRule.canGoBack()){
                    mRegisteRule.goBack();
                }
                finish();
                overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                return true;

        }

        return super.onKeyDown(keyCode, event);
    }
}
