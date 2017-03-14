package project.nhom13.newsfeed.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import project.nhom13.newsfeed.R;
import project.nhom13.newsfeed.model.FeedDBHelper;

/**
 * Created by WILL on 11/20/2016.
 */

public class EditDialog extends Activity {
    private FeedDBHelper helper;
    private String url;
    private String old_site;
    private int old_index;

    public static final String FEED_URL = "url";
    public static final String FEED_SITE = "name";
    public static final String FEED_TOPIC_INDEX = "topic";

    private EditText site;
    private Spinner topic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_feed_popup);

        Intent intent = getIntent();
        url = intent.getStringExtra(FEED_URL);
        old_site = intent.getStringExtra(FEED_SITE);
        old_index = intent.getIntExtra(FEED_TOPIC_INDEX,0);

        this.setTitle(url);

        site = (EditText)findViewById(R.id.edit_rss_name);
        topic = (Spinner)findViewById(R.id.edit_rss_topic);
        site.setText(old_site);
        topic.setSelection(old_index);
    }

    public void save(View view){
        String new_site = site.getText().toString();
        String new_topic = getResources().
                getStringArray(R.array.topic_list_sqlite)[topic.getSelectedItemPosition()];

        helper = new FeedDBHelper(this,null,FeedDBHelper.DB_VERSION);
        helper.edit_rss(url,new_site,new_topic);
        helper.close();
        this.setResult(Activity.RESULT_OK);
        finish();
    }

    public void cancel(View view){
        this.setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
