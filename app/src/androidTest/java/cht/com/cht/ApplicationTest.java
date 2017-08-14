package cht.com.cht;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.HashMap;
import java.util.Map;

import cht.com.cht.application.MyApplication;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;
import cht.com.cht.utils.SharePreferenceUtil;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);

        Map<Integer,String> hashMap = new HashMap<>();
        hashMap.put(1,"23213");
        hashMap.put(2,"23213");
        hashMap.put(3,"23213");
        hashMap.put(4,"23213");
        hashMap.put(5,"23213");
        SharePreferenceUtil.putList(MyApplication.getContext(),hashMap,"test");
        Map<Integer, String> test = SharePreferenceUtil.getList(MyApplication.getContext(), "test");
        LogUtil.e(Constants.TAG,"ExampleUnitTest :"+test.toString());
    }
}