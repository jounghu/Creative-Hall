package cht.com.cht;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Map<Integer,String> hashMap = new HashMap<>();
        hashMap.put(1,"23213");
        hashMap.put(1,"23214");
        hashMap.put(3,"23213");
        hashMap.put(4,"23213");
        hashMap.put(5,"23213");
//        SharePreferenceUtil.putList(MyApplication.getContext(),hashMap,"test");
//        Map<Integer, String> test = SharePreferenceUtil.getList(MyApplication.getContext(), "test");
//        LogUtil.e(Constants.TAG,"ExampleUnitTest :"+test.toString());

        for (Map.Entry<Integer,String> entry:
                hashMap.entrySet()) {

            System.out.println(entry.getKey()+","+entry.getValue());
        }

    }
}