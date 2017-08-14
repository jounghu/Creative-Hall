package cht.com.cht.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import cht.com.cht.application.MyApplication;
import cht.com.cht.db.MyAppDB;
import cht.com.cht.model.Collection;

/**
 * Created by Administrator on 2016/12/6.
 */
public class CollectionSqliteUpdateBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
          List<Collection> collections = (List<Collection>) intent.getSerializableExtra("collections");

        MyAppDB db = MyAppDB.getSingleton(MyApplication.getContext());
        for (Collection collection:collections
                ) {
            db.insertIntoCollection(collection.getTopic_id(),collection.getUser_id());
        }
    }
}
