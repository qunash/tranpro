package translation;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import utils.HTTPConnector;
import utils.JSON;

import com.qunash.translatepro.MainActivity;

import android.os.AsyncTask;
import android.util.Log;

public class TransAsyncTask extends AsyncTask<String, Void, Translation> {

	private static final String TAG = "AsyncTask";

	private JSONObject mJSONObject = null;
	private Translation trans;

	@Override
	protected Translation doInBackground(String... strings) {

		InputStream is = null;
		
		String strInput = strings[0];
		String sl = strings[1];
		String tl = strings[2];

		String URLString = "";
		try {
			URLString = TransManager.prepareTranslateUrl(strInput, sl, tl);
		} catch (UnsupportedEncodingException e) {
			Log.d(TAG, e.toString(), e);
		}

		//GET is probably faster than POST, but it has restriction of 2048 (tested: works only for length<2031) of URL length; reduced to 1900 for safety
		if (1900 > URLString.length()) {
			try {
				is = HTTPConnector.executeHTTPRequest(URLString, HTTPConnector.HTTP_METHOD_GET);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				is = HTTPConnector.executeHTTPRequest(URLString, HTTPConnector.HTTP_METHOD_POST);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		mJSONObject = JSON.StringToJSONObject(HTTPConnector.InputStreamToString(is));
		
		try {
			trans = new Translation();
			trans.JSONToTranslation(mJSONObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
        return trans;
    }

	@Override
    protected void onProgressUpdate(Void... progress) {
    }

	@Override
    protected void onPostExecute(Translation trans) {
		MainActivity.showResult(trans);
    }
	
}