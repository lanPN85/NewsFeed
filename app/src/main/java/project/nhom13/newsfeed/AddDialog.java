package project.nhom13.newsfeed;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.mcsoxford.rss.RSSReader;

/**
 * Created by WILL on 11/19/2016.
 */

public class AddDialog extends Activity {
    private FeedDBHelper helper;
    private Toast failureToast1;
    private Toast failureToast2;
    private Toast successToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.add_feed_popup, null);
        setContentView(contentView);

        failureToast1 = Toast.makeText(this, R.string.notify_add_failure,Toast.LENGTH_LONG);
        failureToast2 = Toast.makeText(this, R.string.notify_add_failure2,Toast.LENGTH_LONG);
        successToast = Toast.makeText(this, R.string.notify_add_success,Toast.LENGTH_LONG);

    }

    public void cancel(View view) {
        this.finish();
    }

    public void add(View view) {
        EditText urlView = (EditText) findViewById(R.id.add_rss_url);
        EditText nameView = (EditText) findViewById(R.id.add_rss_name);
        Spinner topicView = (Spinner) findViewById(R.id.add_rss_topic);
        Switch isFavView = (Switch) findViewById(R.id.favorite_switch);
        ProgressBar loading = (ProgressBar) findViewById(R.id.adding);

        loading.setVisibility(View.VISIBLE);
        String url = urlView.getText().toString();
        if(!validateURL(url)){
            failureToast1.show();
            return;
        }
        helper = new FeedDBHelper(this, null, FeedDBHelper.DB_VERSION);
        Cursor cursor = helper.select_url(url);
        if(cursor.moveToFirst()){
            failureToast2.show();
            helper.close();
            return;
        }
        String name = nameView.getText().toString();
        int topic_index = topicView.getSelectedItemPosition();
        String topic = getResources().getStringArray(R.array.topic_list_sqlite)[topic_index];
        boolean isFav = isFavView.isChecked();

        boolean b = helper.add_rss(url,name,topic,isFav);
        if(b) successToast.show();
        helper.close();
        loading.setVisibility(View.GONE);
        return;
    }

    private boolean validateURL(String url_str) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            RSSReader reader = new RSSReader();
            reader.load(url_str);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
