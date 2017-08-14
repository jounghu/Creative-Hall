package cht.com.cht;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;

/**
 * Created by Administrator on 2017/3/4.
 */
public class GuideActivity extends AppCompatActivity {
    private TextView mToolbarTv;
    private Toolbar mToolbar;
    private TextView mContent;
    private String guideTitle = "";
    private String guideContent = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        assignViews();
        initData();
        setupViews();
    }

    private void setupViews() {
        LogUtil.e(Constants.TAG,this.getClass()+guideTitle+"\n"+guideContent);
        mToolbarTv.setText(guideTitle);
        mContent.setText("\u3000\u3000"+guideContent+"");
    }

    private void initData() {
        guideTitle =   getIntent().getStringExtra("guideTitle");
        guideContent =  getIntent().getStringExtra("guideContent");

    }

    private void assignViews() {
        mToolbar = (Toolbar) findViewById(R.id.id_guide_toolbar);
        mToolbarTv = (TextView) findViewById(R.id.id_guide_toolbar_text);
        mContent = (TextView) findViewById(R.id.id_guide_content);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
