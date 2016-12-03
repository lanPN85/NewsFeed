package project.nhom13.newsfeed;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.nhom13.newsfeed.util.ImageGetter;
import project.nhom13.newsfeed.util.RSSParser;

public class Main extends AppCompatActivity {
    public static final int ARTICLE_CACHE_SIZE = 10;

    private ProgressBar loading;
    private TextView topic;
    private ListView listView;
    private NavigationView nav_view;

    private String current_topic;
    private List<NewsHeader> model = null;
    private ArrayAdapter<NewsHeader> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loading = (ProgressBar) findViewById(R.id.list_progress);

        topic = (TextView) findViewById(R.id.topic_view);
        listView = (ListView) findViewById(R.id.article_list);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            nav_view = (NavigationView) findViewById(R.id.nav_view);
            nav_view.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            return onOptionsItemSelected(menuItem);
                        }
                    });
        }

        if(savedInstanceState==null){
            current_topic = "latest";
        }else{
            current_topic = savedInstanceState.getString("current_topic");
            if(current_topic.equals("world")) topic.setText(getResources().getString(R.string.world_label));
            else if(current_topic.equals("vn")) topic.setText(getResources().getString(R.string.vn_label));
            else if(current_topic.equals("sports")) topic.setText(getResources().getString(R.string.sports_label));
            else if(current_topic.equals("entertain")) topic.setText(getResources().getString(R.string.entertain_label));
            else if(current_topic.equals("tech")) topic.setText(getResources().getString(R.string.tech_label));
            else if(current_topic.equals("other")) topic.setText(getResources().getString(R.string.other_label));
            else if(current_topic.equals("favorite")) topic.setText(getResources().getString(R.string.favorite_label));
            else if(current_topic.equals("downloaded")) topic.setText(getResources().getString(R.string.downloaded_label));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                ArrayList<String> manifest = new ArrayList<String>(ARTICLE_CACHE_SIZE);
                for(int i=0;i<ARTICLE_CACHE_SIZE;i++){
                    String key = "k" + i;
                    NewsHeader h = model.get((position+i)%model.size());
                    bundle.putSerializable(key,h);
                    manifest.add(key);
                }
                bundle.putStringArrayList("manifest",manifest);

                Intent intent = new Intent(Main.this, ArticleViewActivity.class);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                final NewsHeader header = model.get(position);
                String to_download = (header.isDownloaded())?getString(R.string.action_remove):getString(R.string.action_download);

                final AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());
                AlertDialog dialog;
                builder.setTitle(header.getTitle())
                        .setItems(new CharSequence[]{to_download,"Quay lại"},
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        if(!header.isDownloaded()){
                                            Intent intent = new Intent(getApplicationContext(), DownloadService.class);
                                            intent.putExtra(DownloadService.HEADER_EXTRA,header);
                                            startService(intent);
                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    getString(R.string.notify_download_start),Toast.LENGTH_SHORT);
                                            toast.show();
                                            getHeaders();
                                        }else{
                                            FeedDBHelper helper = new FeedDBHelper(getApplicationContext(),null,FeedDBHelper.DB_VERSION);
                                            helper.delete_article(header.getUrl());
                                            helper.close();

                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                    getString(R.string.notify_delete),Toast.LENGTH_SHORT);
                                            toast.show();
                                            getHeaders();
                                        }
                                        dialog.dismiss();
                                        break;
                                    case 1:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        });
                dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        getHeaders();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString("current_topic",current_topic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(Main.this,Preferences.class);
                startActivity(intent);
                return true;
            case R.id.action_add:
                Intent intent2 = new Intent(Main.this,AddDialog.class);
                startActivity(intent2);
                return true;
            case R.id.action_refresh:
                getHeaders();
                return true;

            case R.id.item_latest:
                current_topic = "latest";
                getHeaders();
                topic.setText(getResources().getString(R.string.latest_label));
                return true;
            case R.id.item_world:
                current_topic = "world";
                getHeaders();
                topic.setText(getResources().getString(R.string.world_label));
                return true;
            case R.id.item_vn:
                current_topic = "vn";
                getHeaders();
                topic.setText(getResources().getString(R.string.vn_label));
                return true;
            case R.id.item_sport:
                current_topic = "sports";
                getHeaders();
                topic.setText(getResources().getString(R.string.sports_label));
                return true;
            case R.id.item_entertain:
                current_topic = "entertain";
                getHeaders();
                topic.setText(getResources().getString(R.string.entertain_label));
                return true;
            case R.id.item_tech:
                current_topic = "tech";
                getHeaders();
                topic.setText(getResources().getString(R.string.tech_label));
                return true;
            case R.id.item_other:
                current_topic = "other";
                getHeaders();
                topic.setText(getResources().getString(R.string.other_label));
                return true;
            case R.id.item_favorite:
                current_topic = "favorite";
                getHeaders();
                topic.setText(getResources().getString(R.string.favorite_label));
                return true;
            case R.id.item_downloaded:
                current_topic = "downloaded";
                getHeaders();
                topic.setText(getResources().getString(R.string.downloaded_label));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getHeaders(){
        model = new ArrayList<NewsHeader>(50);
        List<String> downloaded_articles = new ArrayList<String>(30);
        FeedDBHelper helper = new FeedDBHelper(this,null,FeedDBHelper.DB_VERSION);
        Cursor cursor;

        cursor = helper.select_article_all();
        while (cursor.moveToNext()){
            downloaded_articles.add(helper.getURl(cursor));
        }

        if(current_topic.equals("downloaded")){
            //TODO Add downloaded query
            RSSParser.threads_left = 0;
            listView.setAdapter(null);
            if(!cursor.moveToFirst()) {
                helper.close();
                return;
            }
            loading.setVisibility(View.VISIBLE);
            do{
                NewsHeader header = new NewsHeader();
                header.setUrl(helper.getArticleUrl(cursor));
                header.setTitle(helper.getArticleTitle(cursor));
                header.setPubDate(helper.getArticleDate(cursor));
                header.setSite(helper.getArticleSite(cursor));
                header.setPreview(helper.getArticlePreview(cursor));
                header.setDownloaded(true);
                model.add(header);
            }while(cursor.moveToNext());
            loading.setVisibility(View.GONE);

            adapter = new HeaderAdapter();
            listView.setAdapter(adapter);
            return;
        }else{
            if(!isNetworkAvailable()){
                Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.notify_no_network),Toast.LENGTH_LONG);
                toast.show();
                helper.close();
                return;
            }

            if(current_topic.equals("favorite")){
                cursor = helper.select_fav();
            }else{
                cursor = helper.select_topic(current_topic);
            }
            int feed_count = cursor.getCount();
            RSSParser.threads_left = feed_count;
            while (cursor.moveToNext()){
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                int total_limit,feed_limit;
                try{
                    total_limit = Integer.parseInt(prefs.getString("load_amount","30"));
                    feed_limit = (int)Math.ceil((double)total_limit/(double)feed_count);
                }catch (NumberFormatException e){
                    feed_limit = Integer.MAX_VALUE;
                }

                RSSParser parser = new RSSParser(model,downloaded_articles,feed_limit);
                parser.execute(helper.getURl(cursor),helper.getSite(cursor));
            }

            helper.close();
        }

        adapter = new HeaderAdapter();
        Runnable wait = new Runnable() {
            @Override
            public void run() {
                waitForHeaders();
            }
        };
        Thread thread = new Thread(wait);
        thread.start();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null);
    }

    private void waitForHeaders(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listView.setAdapter(null);
                loading.setVisibility(View.VISIBLE);
            }
        });

        while (RSSParser.threads_left > 0){
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Collections.sort(model);
                loading.setVisibility(View.GONE);
                listView.setAdapter(adapter);
            }
        });
    }

    private class HeaderAdapter extends ArrayAdapter<NewsHeader>{
        HeaderAdapter(){
            super(Main.this,R.layout.article_item,model);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View row = convertView;
            HeaderHolder holder;

            if (row==null) {
                LayoutInflater inflater = getLayoutInflater();

                row = inflater.inflate(R.layout.article_item, parent, false);
                holder = new HeaderHolder(row);
                row.setTag(holder);
            }else{
                holder = (HeaderHolder)row.getTag();
            }

            holder.populateFrom(model.get(position));
            return row;
        }
    }

    private static class HeaderHolder{
        private TextView source;
        private TextView date;
        private TextView title;
        private TextView preview2;
        private ImageView image;
        private ImageView download;

        HeaderHolder(View row){
            source = (TextView)row.findViewById(R.id.article_source);
            date = (TextView)row.findViewById(R.id.article_date);
            title = (TextView)row.findViewById(R.id.article_title);
            preview2 = (TextView)row.findViewById(R.id.article_preview2);
            image = (ImageView)row.findViewById(R.id.article_image);
            download = (ImageView)row.findViewById(R.id.download_note);
        }

        void populateFrom(NewsHeader header){
            source.setText(header.getSite());
            date.setText(header.getPubDateAsString());
            title.setText(header.getTitle());
            image.setImageDrawable(null);
            if(header.getThumbnail()!=null){
                image.setImageBitmap(header.getThumbnail());
            }
            preview2.setText(Html.fromHtml(header.getPreview(), new ImageGetter(image), null));
            if(header.isDownloaded()) download.setImageResource(R.drawable.downloadedicon2);
        }
    }
}
