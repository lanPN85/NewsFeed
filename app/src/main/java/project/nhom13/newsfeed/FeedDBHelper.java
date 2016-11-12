package project.nhom13.newsfeed;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WILL on 11/12/2016.
 */

public class FeedDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "feeds.db";

    public static final String RSS_TABLE_NAME = "rss";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_SITE = "site";
    public static final String COLUMN_TOPIC = "topic";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_FAVORITE = "fav";

    public static final String ARTICLES_TABLE_NAME = "articles";
    public static final String COLUMN_RSS_ID = "id";
    public static final String COLUMN_ARITCLE_URL = "url";
    public static final String COLUMN_HTML = "html";

    public FeedDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
