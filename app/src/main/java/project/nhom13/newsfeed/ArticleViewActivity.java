package project.nhom13.newsfeed;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

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
        headers = new ArrayList<NewsHeader>(Main.ARTICLE_CACHE_SIZE);
        for(String key : manifest){
            headers.add((NewsHeader)bundle.getSerializable(key));
        }

        article_index = 0;

        webView = (WebView)findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        loadHtml();
    }

    private void loadHtml(){
        webView.stopLoading();
        if(!isNetworkAvailable()){
            Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.notify_no_network),Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        String url = headers.get(article_index).getUrl();
        webView.loadUrl(url);
    }

    private void nextArticle(){
        if(article_index<headers.size()-1){
            article_index++;
            loadHtml();
        }
    }

    private void prevArticle(){
        if(article_index>0){
            article_index--;
            loadHtml();
        }
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
                Intent intent = new Intent(ArticleViewActivity.this,Preferences.class);
                startActivity(intent);
                return true;
            case R.id.action_download:
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
