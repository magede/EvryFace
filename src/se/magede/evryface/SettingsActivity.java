package se.magede.evryface;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.app.Activity;
import android.view.Menu;

public class SettingsActivity extends Activity {

	public final static String KEY_PREF_HOST = "pref_host";
	public final static String KEY_PREF_URL = "pref_url";
	public final static String KEY_PREF_USR = "pref_userName";
	public final static String KEY_PREF_PWD = "pref_password";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Display the fragment as the main content.
		getFragmentManager().beginTransaction().replace(
				android.R.id.content, 
				new PreferenceFragment () {
					@Override
				    public void onCreate(Bundle savedInstanceState) {
				        super.onCreate(savedInstanceState);
				        addPreferencesFromResource(R.xml.preferences);
				    }
				}).commit();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }
}
