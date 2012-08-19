package se.magede.evryface;

import se.magede.evryface.intranet.IntranetConnector;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayPersonActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_person);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        
        String email = message.substring(message.indexOf("<email>")+7, message.indexOf("</email>"));
        String phone = message.indexOf("<phone>") != -1 ? message.substring(message.indexOf("<phone>")+7, message.indexOf("</phone>")) : "";
        String office = message.substring(message.indexOf("<office>")+8, message.indexOf("</office>"));
        String url = message.substring(message.indexOf("<image>")+7, message.indexOf("</image>"));
        
        ((TextView) findViewById(R.id.textPhone)).setText(phone);
        ((TextView) findViewById(R.id.textEmail)).setText(email);
        ((TextView) findViewById(R.id.textOffice)).setText(office);
        
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    	
    	url = 
    			sharedPref.getString(SettingsActivity.KEY_PREF_HOST, "") +
    			url;
    	String authString = 
        		sharedPref.getString(SettingsActivity.KEY_PREF_USR, "") + ":" +
        		sharedPref.getString(SettingsActivity.KEY_PREF_PWD, "");
        
    	new DataRetrieverTask().execute(url, authString);
    }


	
	private class DataRetrieverTask extends AsyncTask<String, Integer, byte[]> {
        protected byte[] doInBackground(String... connectProps) {
        	
        	String url = connectProps[0];
			String authString = connectProps[1];
			
			// Set authentication
			String encoded = Base64.encodeToString(authString.getBytes(), Base64.DEFAULT);
			
			try {
				byte[] imageBytes = IntranetConnector.retrievePageHtml(url, encoded);
				return imageBytes;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
        }

        protected void onPostExecute(byte[] imageBytes) {
        	try {
            	Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            	ImageView nameText = (ImageView) findViewById(R.id.profileImage);
            	nameText.setImageBitmap(bitmap);
            	
            } catch (Exception e) {
            	e.printStackTrace();
            }
        	
        }
    }

}
