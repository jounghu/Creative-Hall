package cht.com.cht.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cht.com.cht.model.User;
import cht.com.cht.utils.Constants;
import cht.com.cht.utils.LogUtil;

/**
 * Created by Administrator on 2016/11/19.
 */
public class MyAppDB {

    private static MyAppDB singleton;
    private SQLiteDatabase db;

    /**
     * 私有构造方法不让外部初始化，提供一个全局单例
     */
    private MyAppDB(Context context) {
        MyAppOpenHelper helper = new MyAppOpenHelper(context, Constants.ChtDB.DB_NAME, null, Constants.ChtDB.DB_VERSION);
        db = helper.getWritableDatabase();
    }


    public static MyAppDB getSingleton(Context context) {
        if (singleton == null) {
            synchronized (MyAppDB.class) {
                if (singleton == null) {
                    singleton = new MyAppDB(context);
                }
            }
        }
        return singleton;
    }


    public SQLiteDatabase getMyAppWritableDb() {
        return db;
    }


    /**
     * 保存登陆用户
     *
     * @param user
     */
    public void saveUser(User user) {
        if (user != null) {
            ContentValues values = new ContentValues();
            values.put("stu_id", user.getId());
            values.put("sch_id", user.getSch_id());
            values.put("username", user.getUsername());
            values.put("password", user.getPassword());
            values.put("gender", user.getGender());
            values.put("phone", user.getPhone());
            values.put("nickname", user.getNickname());
            values.put("head_img", user.getHead_img());
            values.put("status", user.getStatus());
            values.put("type", user.getType());
            db.insert(Constants.ChtDB.TABLENAME_USER, null, values);
        }
    }

    /**
     * 读取用户通过用户名？？
     * query 第二的参数会返回你指定的列 null会返回所有的
     */


    public User readUser(String phone) {
        List<User> userInfo = new ArrayList<>();

        Cursor cursor = db.query(Constants.ChtDB.TABLENAME_USER, null, "phone=?", new String[]{phone}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                // LogUtil.e(Constants.TAG,"Constants.ChtDB.COLUMNNAME_STU_ID "+cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_STU_ID)+" "+cursor.getColumnName(0));
                user.setId(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_STU_ID)));
                user.setSch_id(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_SCHOOL_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_PASSWORD)));
                user.setGender(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_GENDER)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_PHONE)));
                user.setNickname(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_NICKNAME)));
                user.setHead_img(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_HEAD_IMG)));
                user.setStatus(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_STATUS)));
                user.setType(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_TYPE)));
                userInfo.add(user);
            } while (cursor.moveToNext());
        }
        return userInfo.get(0);
    }

    /**
     * 读最后一条
     *
     * @param
     * @return
     */

    public List<User> readUser() {
        List<User> userInfo = new ArrayList<>();

        Cursor cursor = db.query(Constants.ChtDB.TABLENAME_USER, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_STU_ID)));
                user.setSch_id(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_SCHOOL_ID)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_USERNAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_PASSWORD)));
                user.setGender(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_GENDER)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_PHONE)));
                user.setNickname(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_NICKNAME)));
                user.setHead_img(cursor.getString(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_HEAD_IMG)));
                user.setStatus(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_STATUS)));
                user.setType(cursor.getInt(cursor.getColumnIndex(Constants.ChtDB.COLUMNNAME_TYPE)));
                userInfo.add(user);
            } while (cursor.moveToNext());
        }
        return userInfo;
    }

    public boolean updateUser(User user) {
        if (checkUser(user)) {
            ContentValues values = new ContentValues();
            values.put("stu_id", user.getId());
            values.put("sch_id", user.getSch_id());
            values.put("username", user.getUsername());
            values.put("password", user.getPassword());
            values.put("gender", user.getGender());
            values.put("nickname", user.getNickname());
            values.put("head_img", user.getHead_img());
            values.put("status", user.getStatus());
            values.put("type", user.getType());
            int num = db.update(Constants.ChtDB.TABLENAME_USER, values, "phone = ?", new String[]{user.getPhone()});
            if (num > 0) {
                return true;
            }
            return false;
        } else {
            saveUser(user);
            return true;
        }
    }

    public boolean updateUserNickname(User user){
        ContentValues values = new ContentValues();
        values.put("stu_id", user.getId());
        values.put("sch_id", user.getSch_id());
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("gender", user.getGender());
        values.put("nickname", user.getNickname());
        values.put("head_img", user.getHead_img());
        values.put("status", user.getStatus());
        values.put("type", user.getType());
        int num = db.update(Constants.ChtDB.TABLENAME_USER, values, "phone = ?", new String[]{user.getPhone()});
        if (num > 0) {
            return true;
        }
        return false;
    }


    /**
     * 检查用户是否存在
     */
    public boolean checkUser(User user) {
        Cursor query = db.query(Constants.ChtDB.TABLENAME_USER, new String[]{Constants.ChtDB.COLUMNNAME_PHONE}, "phone = ?", new String[]{user.getPhone()}, null, null, null);
        if (query.moveToFirst()) {
            return true;
        }
        return false;

    }

    /**
     * 插入collection
     *
     * @param topic_id
     * @param user_id
     */
    public void insertIntoCollection(int topic_id, int user_id) {
        ContentValues values = new ContentValues();
        values.put("topic_id", topic_id);
        values.put("user_id", user_id);
        db.insert(Constants.ChtDB.TABLE_COLLECTION, null, values);
    }

    /**
     * 是否已经收藏
     *
     * @param topic_id
     * @param user_id
     * @return
     */
    public boolean isCollection(int topic_id, int user_id) {
        boolean isFlag = false;
        Cursor query = db.query(Constants.ChtDB.TABLE_COLLECTION, null, "topic_id = ? and user_id = ?", new String[]{topic_id + "", user_id + ""}, null, null, null);
        isFlag = query.moveToFirst();
        return isFlag;
    }

    /**
     * 删除收藏
     *
     * @param topic_id
     * @param user_id
     * @return
     */
    public boolean deleteCollection(int topic_id, int user_id) {
        int delete = db.delete(Constants.ChtDB.TABLE_COLLECTION, "topic_id = ? and user_id = ?", new String[]{topic_id + "", user_id + ""});
        if (delete > 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 插入关注
     */
    public void insertFavorite(int user_id, String favorite_item) {
        ContentValues values = new ContentValues();
        values.put("user_id", user_id);
        values.put("favorite_item", favorite_item);
        long insert = db.insert(Constants.ChtDB.TABLE_FAVORITE, null, values);

    }

    /**
     * 是否已经插入了关注
     */
    public boolean isFavorite(int user_id, String favorite_item) {
        Cursor query = db.query(Constants.ChtDB.TABLE_FAVORITE, null, "user_id = ? and favorite_item = ?", new String[]{user_id + "", favorite_item + ""}, null, null, null);
        boolean b = query.moveToFirst();
        return b;
    }

    /**
     * 取消喜欢
     */
    public boolean deleteFavorite(int user_id, String favorite_item) {
        int delete = db.delete(Constants.ChtDB.TABLE_FAVORITE, "user_id = ? and favorite_item = ?", new String[]{user_id + "", favorite_item});
        if (delete > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除所有喜欢
     */
    public boolean deleteAllUserFavorite(int user_id) {
        int delete = db.delete(Constants.ChtDB.TABLE_FAVORITE, "user_id = ?", new String[]{user_id + ""});
        if (delete > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到所有的喜欢
     */
    public List<String> getAllUserFavorite(int user_id) {
        List<String> tags = new ArrayList<>();
        Cursor query = db.query(Constants.ChtDB.TABLE_FAVORITE, null, "user_id = ?", new String[]{user_id + ""}, null, null, null);
        if (query.moveToFirst()) {
            LogUtil.e(Constants.TAG,"getAllUserFavorite:"+user_id);
            do {
                String tag = query.getString(query.getColumnIndex("favorite_item"));
                tags.add(tag);
            } while (query.moveToNext());
        }
        return tags;
    }


    /**
     * 清除所有表信息
     */
    public void clearAll(){
        db.delete(Constants.ChtDB.TABLE_FAVORITE,null,null);
        db.delete(Constants.ChtDB.TABLENAME_USER,null,null);
        db.delete(Constants.ChtDB.TABLE_COLLECTION,null,null);
    }
}
