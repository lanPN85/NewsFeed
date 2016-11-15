package project.nhom13.newsfeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class Main extends AppCompatActivity {
    private TextView topic;
    private Button load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topic = (TextView)findViewById(R.id.topic_view);
        load = (Button)findViewById(R.id.load);
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
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_add:
                return true;
            case R.id.action_refresh:
                return true;
            
            case R.id.item_latest:
                topic.setText(getResources().getString(R.string.latest_label));
                return true;
            case R.id.item_world:
                topic.setText(getResources().getString(R.string.world_label));
                return true;
            case R.id.item_vn:
                topic.setText(getResources().getString(R.string.vn_label));
                return true;
            case R.id.item_sport:
                topic.setText(getResources().getString(R.string.sports_label));
                return true;
            case R.id.item_entertain:
                topic.setText(getResources().getString(R.string.entertain_label));
                return true;
            case R.id.item_tech:
                topic.setText(getResources().getString(R.string.tech_label));
                return true;
            case R.id.item_other:
                topic.setText(getResources().getString(R.string.other_label));
                return true;
            case R.id.item_favorite:
                topic.setText(getResources().getString(R.string.favorite_label));
                return true;
            case R.id.item_downloaded:
                topic.setText(getResources().getString(R.string.downloaded_label));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
