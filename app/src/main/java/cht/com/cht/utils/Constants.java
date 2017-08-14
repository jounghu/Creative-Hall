package cht.com.cht.utils;

/**
 * Created by Administrator on 2016/11/18.
 */
public class Constants {

    public static final String TAG = "CHT";

    public static final String BASE_URL = "http://192.168.1.101:8080";


    public static final String ZAN_BRODCAST_ACTION = "com.cht.zan";
    public static final String COLLECTION_UPDATE_BRODCAST_ACTION = "com.cht.update.collection";
    public static final String FAVORITE_UPFATE_BRODCAST_ACTION = "com.cht.update.favorite";





    public class SharePerferenceConfig{
        public static final  String FIRST_LOGIN = "first";
        public static final  String USER_TOKEN = "user_token";

        public static final boolean NOT_LOGIN = false;
        public static final boolean IS_LOGIN = true;
    }

    public class Registe {
        public static final String REGISTE_RULE = "http://192.168.1.100:8080/test/issu.html";
    }

    public class Jpush{
        public static final String JPUSH_title = "title";
        public static final String JPUSH_content = "content";


        //比赛  跳转比赛查看页面 bisai
        //
    }


    public class Mob {
        public static final String APP_KEY = "192a3d7602212";
        public static final String APP_SECRET = "3ebd7d36d2e61d8658a5a194453cd5d7";

        /**注册flag
         *
         */
        public static final long REGISTE_FLAG = 12322133423L;
        /**忘记密码flag
         *
         *
         */
        public static final long FORGET_FLAG = 12321332423L;
    }

    public class ChtDB {
        /**
         * 数据库表名
         */
        public static final String DB_NAME = "cht.db";

        /**
         * 数据库版本
         */
        public static final int DB_VERSION = 1;

        /**
         * user表名
         */
        public static final String TABLENAME_USER = "user";
        /**
         * 创建user表
         */

        public static final String SQL_CREATE_USER_TABLE = "CREATE TABLE user\n" +
                "(\n" +
                "   id                    integer  NOT NULL  primary key autoincrement,\n" +
                "   stu_id                   integer,\n  "+
                "   sch_id                integer ,\n" +
                "   username            text NOT NULL,\n" +
                "   password            text NOT NULL,\n" +
                "   gender                integer  NOT NULL,\n" +
                "   phone                text NOT NULL,\n" +
                "   nickname            text,\n" +
                "   head_img           text,\n" +
                "   status                integer  NOT NULL,\n" +
                "   type                  integer  NOT NULL\n" +
                ")";

        /**
         * user表列名
         */
        public static final String COLUMNNAME_ID = "id";
        public static final String COLUMNNAME_STU_ID = "stu_id";
        public static final String COLUMNNAME_SCHOOL_ID= "sch_id";
        public static final String COLUMNNAME_USERNAME = "username";
        public static final String COLUMNNAME_PASSWORD = "password";
        public static final String COLUMNNAME_GENDER = "gender";
        public static final String COLUMNNAME_PHONE = "phone";
        public static final String COLUMNNAME_NICKNAME = "nickname";
        public static final String COLUMNNAME_HEAD_IMG = "head_img";
        public static final String COLUMNNAME_STATUS = "status";
        public static final String COLUMNNAME_TYPE = "type";


        /**
         * collection表
         * topic_id
         * user_id
         *
         */

        public static  final  String TABLE_COLLECTION = "collection";

        /**
         * 建表语句
         */
        public static final String SQL_CAREATE_COLLECTION  = "CREATE TABLE collection(" +
                "id integer not null primary key autoincrement," +
                "topic_id integer not null," +
                "user_id integer not null" +
                ");";

        /**
         * favorite表
         * user_id
         * favorite_item
         */
        public static  final  String TABLE_FAVORITE = "favorite";

        /**
         * 建表语句
         */
        public static final String SQL_CAREATE_FAVORITE = "CREATE TABLE favorite(" +
                "id integer not null primary key autoincrement," +
                "user_id integer not null," +
                "favorite_item text not null" +
                ");";
    }
}
