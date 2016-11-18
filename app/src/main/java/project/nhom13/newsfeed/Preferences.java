package project.nhom13.newsfeed;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.util.List;

/**
 * Created by WILL on 11/16/2016.
 */

public class Preferences extends AppCompatPreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildHeaders(List<Header> target){
        loadHeadersFromResource(R.xml.pref_headers,target);
    }

    @Override
    public boolean isValidFragment(String fragmentName){
        return GeneralPrefs.class.getName().equals(fragmentName);
    }

    public static class GeneralPrefs extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            PreferenceManager.setDefaultValues(getActivity(), R.xml.general_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.general_preferences);
        }

        @Override
        public void onStop(){
            CharSequence text = getResources().getString(R.string.notify_changes_saved);
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),text,Toast.LENGTH_SHORT);
            toast.show();
            super.onStop();
        }
    }
}
