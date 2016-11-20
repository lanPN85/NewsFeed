package project.nhom13.newsfeed;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import project.nhom13.newsfeed.util.ListViewUtil;

public class RSSPrefs extends AppCompatActivity {
    public static final int FEED_COUNT = 7;

    private FeedDBHelper helper;

    private ListView [] listViews;
    private Cursor [] cursors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssprefs);

        cursors = new Cursor[FEED_COUNT];
        listViews = new ListView[FEED_COUNT];

        listViews[0] = (ListView)findViewById(R.id.latest_feeds);
        listViews[1] = (ListView)findViewById(R.id.world_feeds);
        listViews[2] = (ListView)findViewById(R.id.vn_feeds);
        listViews[3] = (ListView)findViewById(R.id.sports_feeds);
        listViews[4] = (ListView)findViewById(R.id.entertain_feeds);
        listViews[5] = (ListView)findViewById(R.id.tech_feeds);
        listViews[6] = (ListView)findViewById(R.id.othet_feeds);
    }

    @Override
    protected void onStart(){
        super.onStart();
        helper = new FeedDBHelper(this,null,FeedDBHelper.DB_VERSION);

        cursors[0] = helper.select_all_topic("latest");
        cursors[1] = helper.select_all_topic("world");
        cursors[2] = helper.select_all_topic("vn");
        cursors[3] = helper.select_all_topic("sports");
        cursors[4] = helper.select_all_topic("entertain");
        cursors[5] = helper.select_all_topic("tech");
        cursors[6] = helper.select_all_topic("other");

        for(int i=0;i<FEED_COUNT;i++){
            listViews[i].setAdapter(new FeedAdapter(cursors[i]));
            ListViewUtil.setListViewHeightBasedOnItems(listViews[i]);
        }
    }

    private class FeedAdapter extends CursorAdapter{
        FeedAdapter(Cursor cursor){
            super(RSSPrefs.this,cursor,CursorAdapter.NO_SELECTION);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.feed_item,parent,false);

            FeedHolder holder = new FeedHolder(row);
            row.setTag(holder);

            return row;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            FeedHolder holder = (FeedHolder)view.getTag();
            holder.populateFrom(cursor,helper);
        }
    }

    private class FeedHolder{
        private TextView name;
        private ToggleButton active;

        FeedHolder(View row){
            name = (TextView)row.findViewById(R.id.feed_name);
            active = (ToggleButton)row.findViewById(R.id.is_active);
            active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    boolean b = helper.set_active(name.getText().toString(),isChecked);
                    if(!b){
                        Toast toast = Toast.makeText(getBaseContext(),
                                getResources().getString(R.string.notify_general_error),Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }

        void populateFrom(Cursor cursor, FeedDBHelper helper){
            name.setText(helper.getURl(cursor));
            int isActive = helper.getActive(cursor);
            if(isActive!=0){
                active.setChecked(true);
            } else{
                active.setChecked(false);
            }
        }
    }

    @Override
    protected void onStop(){
        CharSequence text = getResources().getString(R.string.notify_changes_saved);
        Toast toast = Toast.makeText(this.getApplicationContext(),text,Toast.LENGTH_SHORT);
        toast.show();

        helper.close();
        super.onStop();
    }
}
