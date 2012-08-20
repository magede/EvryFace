package se.magede.evryface;

import java.io.InputStream;

import se.magede.evryface.intranet.HtmlToXmlParser;
import se.magede.evryface.intranet.IntranetConnector;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

public class MainActivity extends Activity {
	public final static String EXTRA_MESSAGE = "se.magede.evryface.MESSAGE";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_settings:
                settings();
                return true;
            case R.id.menu_about:
                //showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void settings() {
    	Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
		startActivity(intent);
	}

	public void sendMessage(View view) {
    	
    	ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar1);
    	progressBar.setVisibility(ProgressBar.VISIBLE);
    	
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	String searchText = editText.getText().toString();

    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	String url = 
    			sharedPref.getString(SettingsActivity.KEY_PREF_HOST, "") +
    			sharedPref.getString(SettingsActivity.KEY_PREF_URL, "") +
    			searchText;

        String username = sharedPref.getString(SettingsActivity.KEY_PREF_USR, "");
        String password = sharedPref.getString(SettingsActivity.KEY_PREF_PWD, "");
    	
        new DataRetrieverTask().execute(url, username, password);
    }
    
    
    private class DataRetrieverTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... connectProps) {
        	
			try {
				String url = connectProps[0];
				String username = connectProps[1];
				String password = connectProps[2];
				
				String html = new String(IntranetConnector.retrievePageHtml(url, username, password));
				//System.out.println("HTML:" + html);
				
				// Transform the HTML to a XML contact card
				InputStream xslStream = getResources().openRawResource(R.raw.transform);
				String xmlContactCard = HtmlToXmlParser.parse(html, xslStream);
				//System.out.println("XML: " + xmlContactCard);
				return xmlContactCard;
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
        }

        protected void onPostExecute(String result) {

        	ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        	progressBar.setVisibility(ProgressBar.INVISIBLE);
        	
			System.out.println("Got result:");
			System.out.println(result);

        	
        	if (result != null && result.indexOf("<person>") > 0) {
            	Intent intent = new Intent(MainActivity.this, DisplayPersonActivity.class);
            	intent.putExtra(EXTRA_MESSAGE, result);

        		startActivity(intent);
        	} else {
        		
        		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        		builder.setMessage("Find you did not!");
        		AlertDialog alert = builder.create();
        		alert.show();
        	}
        	
        }
    }
}
