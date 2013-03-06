package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import com.qunash.translatepro.Main;
import com.qunash.translatepro.Translation;

import android.os.AsyncTask;
import android.util.Log;

public class TransAsyncTask extends AsyncTask<String, Void, Translation> {

	private static final String TAG = "AsyncTask";

	private JSONObject mJSONObject = null;
	private Translation trans;

	@Override
	protected Translation doInBackground(String... strings) {

		String strInput = strings[0];
		String sl = strings[1];
		String tl = strings[2];
		
		String URLString;

		URLString = "http://translate.google.com/translate_a/t?client=p&text=";
		URLString += strInput;
		URLString += "&hl=" + tl;
		URLString += "&sl=" + sl;
		URLString += "&tl=" + tl;

		
		//GET is probably faster than POST, but it has restriction of 2048 (tested: works only for length<2031) of URL length; reduced to 1900 for safety
		if (1900 > URLString.length()) {
			try {
				executeHttpGET(URLString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				executeHttpPOST(URLString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
        return trans;
    }

	@Override
    protected void onProgressUpdate(Void... progress) {
    }

	@Override
    protected void onPostExecute(Translation trans) {
		Main.showResult(trans);
    }
    
    
    private void executeHttpPOST(String URLString) {

		InputStream is = null;

		// HTTP
		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.97 Safari/537.22"); //!кодировка стала работать только после танцев с этой строкой, "android" не работал
			
			HttpPost request = new HttpPost(URLString);
			
			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			HttpResponse response = client.execute(request);

			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			mJSONObject = IsToJSONObject(is);
			
			JSONToTranslation();


		} catch (ClientProtocolException e) {
			Log.d(TAG, e.toString());
		} catch (IllegalStateException e) {
			Log.d(TAG, e.toString());
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}
	}
	
	private void executeHttpGET(String URLString) {
		
		InputStream is = null;
		
		// HTTP
		try {
			HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"); //!кодировка стала работать только после танцев с этой строкой, "android" не работал

			HttpGet request = new HttpGet(URLString);

			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			HttpResponse response = client.execute(request);

			HttpEntity entity = response.getEntity();
			is = entity.getContent();

			mJSONObject = IsToJSONObject(is);
			
			JSONToTranslation();


		} catch (ClientProtocolException e) {
			Log.d(TAG, e.toString());
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}
	}
	
	private JSONObject IsToJSONObject(InputStream is) {
		
		String result = "";
		
		// Read response to string
		BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8);
		StringBuilder sb = new StringBuilder();
		String line = null;
		
		try {
			while (null != (line = reader.readLine())) {
				sb.append(line + "\n");
			}
			is.close();
		} catch (IOException e) {
			Log.d(TAG, e.toString());
		}
		
		result = sb.toString();

		
		// Convert string to JSONObject
		try {
			return new JSONObject(result);
		} catch (JSONException e) {
			Log.d(TAG, "Failed to create JSONObject from string "+result);
			return null;
		}

	}
	
	private void JSONToTranslation() {
		
		if (mJSONObject == null) {
			return;
		}
		
		trans = new Translation().getTranslation(mJSONObject);
		
	}
    
	
}