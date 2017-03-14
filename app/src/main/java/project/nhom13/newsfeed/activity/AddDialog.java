package project.nhom13.newsfeed.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.mcsoxford.rss.RSSReader;

import project.nhom13.newsfeed.R;
import project.nhom13.newsfeed.model.FeedDBHelper;

/**
 * Created by WILL on 11/19/2016.
 */

public class AddDialog extends Activity {
    private FeedDBHelper helper;
    private Toast failureToast1;
    private Toast failureToast2;
    private Toast successToast;

    private String url;
    private String name;
    private int topic_index;
    private String topic;
    private boolean isFav;

    private EditText urlView;
    private EditText nameView;
    private Spinner topicView;
    private Switch isFavView;
    private ProgressBar loading;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        View contentView = inflater.inflate(R.layout.add_feed_popup, null);
        setContentView(contentView);

        urlView = (EditText) findViewById(R.id.add_rss_url);
        nameView = (EditText) findViewById(R.id.add_rss_name);
        topicView = (Spinner) findViewById(R.id.add_rss_topic);
        isFavView = (Switch) findViewById(R.id.favorite_switch);
        loading = (ProgressBar) findViewById(R.id.adding);
        confirm = (Button) findViewById(R.id.confirm_btn);

        failureToast1 = Toast.makeText(this, R.string.notify_add_failure, Toast.LENGTH_LONG);
        failureToast2 = Toast.makeText(this, R.string.notify_add_failure2, Toast.LENGTH_LONG);
        successToast = Toast.makeText(this, R.string.notify_add_success, Toast.LENGTH_LONG);

    }

    public void cancel(View view) {
        this.finish();
    }

    public void add(View view) {
        loading.setVisibility(View.VISIBLE);
        confirm.setEnabled(false);

        url = urlView.getText().toString();
        name = nameView.getText().toString();
        topic_index = topicView.getSelectedItemPosition();
        topic = getResources().getStringArray(R.array.topic_list_sqlite)[topic_index];
        isFav = isFavView.isChecked();

        RSSValidator validator = new RSSValidator();
        validator.execute();

        return;
    }

    private boolean validateURL(String url_str) {
        try {
            RSSReader reader = new RSSReader();
            reader.load(url_str);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private class RSSValidator extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            if (!validateURL(url)) {
                failureToast1.show();
            } else {
                helper = new FeedDBHelper(AddDialog.this, null, FeedDBHelper.DB_VERSION);
                Cursor cursor = helper.select_url(url);
                if (cursor.moveToFirst()) {
                    failureToast2.show();
                    helper.close();
                } else {
                    boolean b = helper.add_rss(url, name, topic, isFav);
                    if (b) successToast.show();
                    helper.close();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.setVisibility(View.GONE);
                    confirm.setEnabled(true);
                }
            });
        }
    }
}
