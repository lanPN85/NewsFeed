package project.nhom13.newsfeed.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import project.nhom13.newsfeed.R;
import project.nhom13.newsfeed.model.FeedDBHelper;
import project.nhom13.newsfeed.util.ListViewUtil;

public class RSSPrefsActivity extends AppCompatActivity {
    public static final int FEED_COUNT = 7;
    public static final int EDIT_REQUEST_CODE = 13;

    private FeedDBHelper helper;

    private ListView [] listViews;
    private Cursor [] cursors;
    private String [] topic_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rssprefs);

        cursors = new Cursor[FEED_COUNT];
        listViews = new ListView[FEED_COUNT];
        topic_list = getResources().getStringArray(R.array.topic_list_sqlite);

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

        for(int i=0;i<FEED_COUNT;i++){
            cursors[i] = helper.select_all_topic(topic_list[i]);
            listViews[i].setAdapter(new FeedAdapter(cursors[i]));
            ListViewUtil.setListViewHeightBasedOnItems(listViews[i]);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                refreshLists();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshLists(){
        for(int i=0;i<FEED_COUNT;i++){
            listViews[i].setAdapter(null);
            cursors[i] = helper.select_all_topic(topic_list[i]);
            listViews[i].setAdapter(new FeedAdapter(cursors[i]));
            ListViewUtil.setListViewHeightBasedOnItems(listViews[i]);
        }
    }

    private class FeedAdapter extends CursorAdapter{
        FeedAdapter(Cursor cursor){
            super(RSSPrefsActivity.this,cursor,CursorAdapter.NO_SELECTION);
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
        private ToggleButton fav;

        FeedHolder(View row){
            name = (TextView)row.findViewById(R.id.feed_name);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RSSPrefsActivity.this,EditDialog.class);
                    Cursor cursor = helper.select_url(name.getText().toString());
                    if(!cursor.moveToFirst())
                        return;

                    int index = 0;
                    for(int i=0;i<topic_list.length;i++){
                        if(helper.getTopic(cursor).equals(topic_list[i])){
                            index = i;
                            break;
                        }
                    }

                    intent.putExtra(EditDialog.FEED_URL,helper.getURl(cursor));
                    intent.putExtra(EditDialog.FEED_SITE,helper.getSite(cursor));
                    intent.putExtra(EditDialog.FEED_TOPIC_INDEX,index);

                    startActivityForResult(intent,EDIT_REQUEST_CODE);
                }
            });
            name.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RSSPrefsActivity.this,R.style.AppTheme_Dialog2);
                    AlertDialog dialog;
                    builder = builder
                            .setPositiveButton(R.string.action_remove, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    helper.delete_rss(name.getText().toString());
                                    refreshLists();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.request_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setMessage(R.string.delete_warning)
                            .setTitle(getString(R.string.request_delete_rss));
                    dialog = builder.create();
                    dialog.show();

                    return true;
                }
            });

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
            fav = (ToggleButton)row.findViewById(R.id.favorite_toggle);
            fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    boolean b = helper.set_favorite(name.getText().toString(),isChecked);
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

            int isFav = helper.getFavorite(cursor);
            if(isFav!=0){
                fav.setChecked(true);
            } else{
                fav.setChecked(false);
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
