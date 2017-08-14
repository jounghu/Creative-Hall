package cht.com.cht.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cht.com.cht.utils.Constants;

/**
 * Created by Administrator on 2016/11/19.
 */
public class MyAppOpenHelper extends SQLiteOpenHelper {


    public MyAppOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.ChtDB.SQL_CREATE_USER_TABLE);//创建用户表
        db.execSQL(Constants.ChtDB.SQL_CAREATE_COLLECTION);//收藏用户表
        db.execSQL(Constants.ChtDB.SQL_CAREATE_FAVORITE);//关注表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
