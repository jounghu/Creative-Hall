package cht.com.cht.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/19.
 */
public class SharePreferenceUtil {
    public static String FILLNAME = "config";


    /**
     * 存入map对象，用jsonObject形式放进去
     * @param context
     * @param map
     * @param key
     */
    public static void putList(Context context,Map<Integer,Boolean> map,String key){
        SharedPreferences sp = context.getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        JSONObject json = new JSONObject();
        for (Map.Entry<Integer,Boolean> entry:
             map.entrySet()) {
            try {
                json.put(entry.getKey()+"",entry.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String objectStr = json.toString();
        edit.putString(key,objectStr);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit);
    }

    public static Map<Integer,Boolean> getList(Context context,String key) {
        Map<Integer,Boolean> hashMap = new HashMap<>();
        SharedPreferences sp = context.getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);

        String string = sp.getString(key, null);
       if(string!=null){
           try {
               JSONObject object = new JSONObject(string);
               Iterator<String> keys = object.keys();
               while(keys.hasNext()){
                   String next = keys.next();
                   hashMap.put(Integer.parseInt(next), (Boolean) object.get(next));
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
        return hashMap;
    }

    /**
     * 存入某个key对应的value值
     *
     * @param context
     * @param key
     * @param value
     */
    public static void put(Context context, String key, Object value) {
        SharedPreferences sp = context.getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (Long) value);
        }
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit);
    }

    /**
     * 得到某个key对应的值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static Object get(Context context, String key, Object defValue) {
        SharedPreferences sp = context.getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        }
        return null;
    }

    /**
     * 返回所有数据
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.remove(key);
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit);
    }

    /**
     * 清除所有内容
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILLNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        SharedPreferencesCompat.EditorCompat.getInstance().apply(edit);
    }



}
