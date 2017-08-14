package cht.com.cht;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import cht.com.cht.model.OutDoor;

/**
 * Created by Administrator on 2017/2/21.
 */
public class OutDoorDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView mTitle;
    private TextView mContent;
    private OutDoor data;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_outdoor_detail);

        initView();
        initData();
        setupView();

    }

    private void setupView() {
        mTitle.setText(data.getTitle());
        mContent.setText(data.getContent());
    }

    private void initData() {
         data = (OutDoor) getIntent().getSerializableExtra("outdoor");
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.id_fragment_outdoor_detail_title);
        mContent = (TextView) findViewById(R.id.id_fragment_outdoor_detail_content);
        toolbar = (Toolbar) findViewById(R.id.id_activity_detaile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
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
