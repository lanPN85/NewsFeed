package project.nhom13.newsfeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FeedDBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 4;
    private static final String DB_NAME = "feeds.db";

    public static final String RSS_TABLE_NAME = "rss";

    public static final String COLUMN_URL = "_id";
    public static final String COLUMN_SITE = "site";
    public static final String COLUMN_TOPIC = "topic";
    public static final String COLUMN_ACTIVE = "active";
    public static final String COLUMN_FAVORITE = "fav";

    public static final String ARTICLES_TABLE_NAME = "articles";
    public static final String COLUMN_ARITCLE_URL = "_id";
    public static final String COLUMN_ARTICLE_SITE= "site";
    public static final String COLUMN_ARTICLE_DATE="date";
    public static final String COLUMN_ARITCLE_TITLE = "title";
    public static final String COLUMN_ARTICLE_PREVIEW = "preview";
    public static final String COLUMN_ARTICLE_HTML = "html";

    private final Context context;

    public FeedDBHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RSS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                RSS_TABLE_NAME + "("
                +  COLUMN_URL
                + " TEXT PRIMARY KEY," + COLUMN_SITE + " TEXT," + COLUMN_TOPIC+" TEXT,"+COLUMN_ACTIVE+" Boolean,"+
                COLUMN_FAVORITE+" Boolean"+")";
        db.execSQL(CREATE_RSS_TABLE);

        String CREATE_ARTICLES_TABLE = "CREATE TABLE IF NOT EXISTS " +
                ARTICLES_TABLE_NAME + "("
                +  COLUMN_ARITCLE_URL
                + " TEXT PRIMARY KEY," + COLUMN_ARTICLE_SITE +" TEXT, "+COLUMN_ARTICLE_DATE+" TEXT,"+COLUMN_ARITCLE_TITLE +
                " TEXT,"+ COLUMN_ARTICLE_PREVIEW+" TEXT,"+COLUMN_ARTICLE_HTML+" TEXT"+")";
        db.execSQL(CREATE_ARTICLES_TABLE);

        insertDefault(db);
    }

    private void insertDefault(SQLiteDatabase db){
        String query = "Select * FROM " + RSS_TABLE_NAME ;
        Cursor cursor = db.rawQuery(query,null);

        if(!cursor.moveToFirst()){
            String [] feeds = context.getResources().getStringArray(R.array.latest);
            String [] feed_names = context.getResources().getStringArray(R.array.latest_name);
            for(int i=0;i<feeds.length;i++){
                ContentValues values = new ContentValues(5);
                values.put(COLUMN_URL,feeds[i]);
                values.put(COLUMN_SITE,feed_names[i]);
                values.put(COLUMN_TOPIC,"latest");
                values.put(COLUMN_ACTIVE,1);
                values.put(COLUMN_FAVORITE,0);
                db.insert(RSS_TABLE_NAME,null,values);
            }

            feeds = context.getResources().getStringArray(R.array.world);
            feed_names = context.getResources().getStringArray(R.array.world_name);
            for(int i=0;i<feeds.length;i++){
                ContentValues values = new ContentValues(5);
                values.put(COLUMN_URL,feeds[i]);
                values.put(COLUMN_SITE,feed_names[i]);
                values.put(COLUMN_TOPIC,"world");
                values.put(COLUMN_ACTIVE,1);
                values.put(COLUMN_FAVORITE,0);
                db.insert(RSS_TABLE_NAME,null,values);
            }

            feeds = context.getResources().getStringArray(R.array.vn);
            feed_names = context.getResources().getStringArray(R.array.vn_name);
            for(int i=0;i<feeds.length;i++){
                ContentValues values = new ContentValues(5);
                values.put(COLUMN_URL,feeds[i]);
                values.put(COLUMN_SITE,feed_names[i]);
                values.put(COLUMN_TOPIC,"vn");
                values.put(COLUMN_ACTIVE,1);
                values.put(COLUMN_FAVORITE,0);
                db.insert(RSS_TABLE_NAME,null,values);
            }

            feeds = context.getResources().getStringArray(R.array.sports);
            feed_names = context.getResources().getStringArray(R.array.sports_name);
            for(int i=0;i<feeds.length;i++){
                ContentValues values = new ContentValues(5);
                values.put(COLUMN_URL,feeds[i]);
                values.put(COLUMN_SITE,feed_names[i]);
                values.put(COLUMN_TOPIC,"sports");
                values.put(COLUMN_ACTIVE,1);
                values.put(COLUMN_FAVORITE,0);
                db.insert(RSS_TABLE_NAME,null,values);
            }

            feeds = context.getResources().getStringArray(R.array.entertain);
            feed_names = context.getResources().getStringArray(R.array.entertain_name);
            for(int i=0;i<feeds.length;i++){
                ContentValues values = new ContentValues(5);
                values.put(COLUMN_URL,feeds[i]);
                values.put(COLUMN_SITE,feed_names[i]);
                values.put(COLUMN_TOPIC,"entertain");
                values.put(COLUMN_ACTIVE,1);
                values.put(COLUMN_FAVORITE,0);
                db.insert(RSS_TABLE_NAME,null,values);
            }

            feeds = context.getResources().getStringArray(R.array.tech);
            feed_names = context.getResources().getStringArray(R.array.tech_name);
            for(int i=0;i<feeds.length;i++){
                ContentValues values = new ContentValues(5);
                values.put(COLUMN_URL,feeds[i]);
                values.put(COLUMN_SITE,feed_names[i]);
                values.put(COLUMN_TOPIC,"tech");
                values.put(COLUMN_ACTIVE,1);
                values.put(COLUMN_FAVORITE,0);
                db.insert(RSS_TABLE_NAME,null,values);
            }

            feeds = context.getResources().getStringArray(R.array.other);
            feed_names = context.getResources().getStringArray(R.array.other_name);
            for(int i=0;i<feeds.length;i++){
                ContentValues values = new ContentValues(5);
                values.put(COLUMN_URL,feeds[i]);
                values.put(COLUMN_SITE,feed_names[i]);
                values.put(COLUMN_TOPIC,"other");
                values.put(COLUMN_ACTIVE,1);
                values.put(COLUMN_FAVORITE,0);
                db.insert(RSS_TABLE_NAME,null,values);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + RSS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLES_TABLE_NAME);
        onCreate(db);
    }

    public void add_rss(String url, String site, String topic , Boolean fav) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_URL, url);
        values.put(COLUMN_SITE, site);
        values.put(COLUMN_TOPIC, topic);
        values.put(COLUMN_ACTIVE, true);
        values.put(COLUMN_FAVORITE, fav);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(RSS_TABLE_NAME, null, values);
        db.close();
    }
    public void add_article(String url, String site, String date, String title, String preview) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_URL, url);
        values.put(COLUMN_SITE, site);
        values.put(COLUMN_TOPIC, date);
        values.put(COLUMN_SITE, title);
        values.put(COLUMN_TOPIC, preview);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(RSS_TABLE_NAME, null, values);
        db.close();
    }


    public boolean delete_article (String url) {
        boolean result = false;
        String query = "Select * FROM " +ARTICLES_TABLE_NAME + " WHERE " +
                COLUMN_ARITCLE_URL + " = \"" + url + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            db.delete(ARTICLES_TABLE_NAME, COLUMN_ARITCLE_URL + " = \""+ url + "\"", null );

            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public boolean set_active(String url, boolean bl){
        int active = (bl)?1:0;
        String query = "Select * FROM " + RSS_TABLE_NAME + " WHERE " +
                COLUMN_URL + " = \"" + url  + "\"" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        ContentValues contentValues = new ContentValues(1);
        contentValues.put(COLUMN_ACTIVE, active);
        if(cursor.moveToFirst()){

            db.update(RSS_TABLE_NAME,contentValues, COLUMN_URL +" = \""+url + "\"",null);
            return  true;
        }

        return false;
    }


    public Cursor select_topic (String topic){
        String query = "Select * FROM " + RSS_TABLE_NAME + " WHERE " +
                COLUMN_TOPIC + " = \"" + topic  + "\"" +" and "+
                COLUMN_ACTIVE +" = 1" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor select_fav (){
        String query = "Select * FROM " + RSS_TABLE_NAME + " WHERE " +
                COLUMN_FAVORITE+ " = 1" + " and "+
                COLUMN_ACTIVE +" = 1" ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor select_article_all (){
        String query = "Select " + COLUMN_ARITCLE_URL + COLUMN_ARTICLE_SITE+
                COLUMN_ARTICLE_DATE+
                COLUMN_ARITCLE_TITLE+
                COLUMN_ARTICLE_PREVIEW+
                " FROM " + ARTICLES_TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }
    public Cursor select_article_html (String url){
        String query = "Select " + COLUMN_ARTICLE_HTML+
                " FROM " + ARTICLES_TABLE_NAME + " WHERE "
                + COLUMN_ARITCLE_URL +" = \" "+ url + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public String getURl(Cursor c){
        return c.getString(0);
    }
    public int getActive(Cursor c){
        return c.getInt(3);
    }

}
