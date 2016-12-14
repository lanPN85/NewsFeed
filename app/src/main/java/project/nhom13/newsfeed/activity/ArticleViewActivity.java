package project.nhom13.newsfeed.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import project.nhom13.newsfeed.service.DownloadService;
import project.nhom13.newsfeed.R;
import project.nhom13.newsfeed.model.NewsHeader;
import project.nhom13.newsfeed.model.FeedDBHelper;

public class ArticleViewActivity extends AppCompatActivity {
    private ProgressBar loading;
    private WebView webView;
    private ArrayList<String> manifest;
    private ArrayList<NewsHeader> headers;
    private int article_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);

        loading = (ProgressBar)findViewById(R.id.article_progress);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        manifest = bundle.getStringArrayList("manifest");
        headers = new ArrayList<NewsHeader>(MainActivity.ARTICLE_CACHE_SIZE);
        for(String key : manifest){
            headers.add((NewsHeader)bundle.getSerializable(key));
        }

        if(savedInstanceState==null){
            article_index = 0;
        }else {
            article_index = savedInstanceState.getInt("index",0);
        }

        webView = (WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loading.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                loading.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        loadHtml();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("index",article_index);
    }

    private void loadHtml(){
        NewsHeader header = headers.get(article_index);
        webView.stopLoading();

        if(!isNetworkAvailable()){
            if(header.isDownloaded()){
                FeedDBHelper helper = new FeedDBHelper(this,null,FeedDBHelper.DB_VERSION);
                Cursor cursor = helper.select_article_html(header.getUrl());
                if(cursor.moveToFirst()){
                    String html = cursor.getString(0);
                    webView.loadData(html,"text/html; charset='utf-8'",null);
                    helper.close();
                    return;
                }
                helper.close();
            }

            Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.notify_no_network),Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        String url = header.getUrl();
        webView.loadUrl(url);
    }

    private void nextArticle(){
            article_index = (article_index+1)%headers.size();
            loadHtml();
    }

    private void prevArticle(){
            article_index = (article_index>0)?(article_index-1):(headers.size()-1);
            loadHtml();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article, menu);
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
            case R.id.action_settings2:
                Intent intent = new Intent(ArticleViewActivity.this,PreferencesActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_download:
                if(headers.get(article_index).isDownloaded()){
                    Toast.makeText(this,getString(R.string.notify_download_redundant),Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent2 = new Intent(getApplicationContext(), DownloadService.class);
                    intent2.putExtra(DownloadService.HEADER_EXTRA,headers.get(article_index));
                    startService(intent2);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            getString(R.string.notify_download_start),Toast.LENGTH_SHORT);
                    toast.show();
                }
                return true;
            case R.id.next:
                nextArticle();
                return true;
            case R.id.prev:
                prevArticle();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null);
    }

}
