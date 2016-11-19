package project.nhom13.newsfeed;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
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
import java.util.GregorianCalendar;
import java.util.List;

public class Main extends AppCompatActivity {
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

        getHeaders();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsHeader header = model.get(position);
                String url = header.getUrl();

                Intent intent = new Intent(Main.this, ArticleViewActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
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
        if(!isNetworkAvailable()){
            Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.notify_no_network),Toast.LENGTH_LONG);
            toast.show();
        }

        model = new ArrayList<NewsHeader>(30);
        // Test item
        NewsHeader item = new NewsHeader();
        item.setPreview("ABCDEWS");
        item.setTitle("Fake Item");
        item.setPubDate(new GregorianCalendar());
        item.setSite("FakeNews");
        item.setImageUrl("https://scontent-hkg3-1.xx.fbcdn.net/t31.0-8/13502876_1041588202596877_4589638001439672089_o.jpg");
        item.setUrl("http://www.cracked.com/article_24424_6-extremely-minor-movie-scenes-that-cost-fortune-to-film.html");
        NewsHeader item2 = new NewsHeader();
        item2.setPreview("GKE SMDKDKDf EEF KKSdKS FKDFED Kfkdlk dfnallaskdmlkd ldkklkndan akdlsa");
        item2.setTitle("Fake Item 2");
        item2.setPubDate(new GregorianCalendar());
        item2.setSite("FakeNews");
        item2.setImageUrl("https://scontent-hkg3-1.xx.fbcdn.net/t31.0-8/13502876_1041588202596877_4589638001439672089_o.jpg");
        item2.setUrl("http://www.cracked.com/personal-experiences-2409-my-entire-town-was-fire-evacuating-epic-disaster.html");

        model.add(item);

        adapter = new HeaderAdapter();
        listView.setAdapter(adapter);

        model.add(item2);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null);
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
        private TextView preview;
        private ImageView image;

        HeaderHolder(View row){
            source = (TextView)row.findViewById(R.id.article_source);
            date = (TextView)row.findViewById(R.id.article_date);
            title = (TextView)row.findViewById(R.id.article_title);
            preview = (TextView)row.findViewById(R.id.article_preview);
            image = (ImageView)row.findViewById(R.id.article_image);
        }

        void populateFrom(NewsHeader header){
            source.setText(header.getSite());
            date.setText(header.getPubDateAsString());
            title.setText(header.getTitle());
            preview.setText(header.getPreview());
            header.getImage(image);
        }
    }
}
