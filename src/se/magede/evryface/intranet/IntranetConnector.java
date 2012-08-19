package se.magede.evryface.intranet;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class IntranetConnector {

    public static byte[] retrievePageHtml(String theUrl, String authorization) {
        Log.d(IntranetConnector.class.getName(), "retrievePageHtml >>>");
    	
		try {
			
			URL url = new URL("https://" + theUrl);

			Log.d(IntranetConnector.class.getName(), "URL=" + url.toExternalForm());
			Log.d(IntranetConnector.class.getName(), "Authorization=" + authorization);
			
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty  ("Authorization", "Basic " + authorization);
            
            InputStream is = (InputStream)connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            
            Log.d(IntranetConnector.class.getName(), "EFTER");
        	
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = is.read(data, 0, data.length)) != -1) {
              buffer.write(data, 0, nRead);
            }

            buffer.flush();
            
            return buffer.toByteArray();
        } catch(Exception e) {
            e.printStackTrace();
            return new byte[]{};
        } finally {
        	System.out.println("IntranetConnector.retrievePageHtml <<<");
        }
    }	
}
