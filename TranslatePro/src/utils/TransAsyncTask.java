package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qunash.translatepro.Main;

import android.os.AsyncTask;
import android.util.Log;

//***************Background connection class***************//
	public class TransAsyncTask extends AsyncTask<String, Void, JSONObject> {

		private static final String LOG_TAG = "AsyncTask";
		
		private JSONObject mJSONObject = null;
		private String output = "";
		
		
		@Override
		protected JSONObject doInBackground(String...strings) {

			String URLString = strings[0];
			
			//GET is probably faster than POST, but it has restriction of 2048 (tested: works only for length<2031) of URL length; reducing to 1900 for safeness
			if (URLString.length() < 1900) {
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
			
	        return mJSONObject;
	    }

		@Override
	    protected void onProgressUpdate(Void... progress) {
	    }

		@Override
	    protected void onPostExecute(JSONObject result) {
			Main.showResult(output);
	    }
	    
	    
	    private void executeHttpPOST(String URLString) throws Exception {

	    	String mURLString = URLString;
			InputStream is = null;

			// HTTP
			try {
				HttpClient client = new DefaultHttpClient();
//				client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.97 Safari/537.22"); //!кодировка стала работать только после танцев с этой строкой, "android" не работал
				
				HttpPost request = new HttpPost(mURLString);
				
				request.setHeader("Content-Type", "text/plain; charset=utf-8");
				HttpResponse response = client.execute(request);

				HttpEntity entity = response.getEntity();
				is = entity.getContent();

				mJSONObject = getJSONObject(is);
				
				JSONToString();


			} finally {
			}
		}
		
		private void executeHttpGET(String URLString) throws Exception {
			
			String mURLString = URLString;
			InputStream is = null;
			
			// HTTP
			try {
				HttpClient client = new DefaultHttpClient();
//				client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)"); //!кодировка стала работать только после танцев с этой строкой, "android" не работал

				HttpGet request = new HttpGet(mURLString);

				request.setHeader("Content-Type", "text/plain; charset=utf-8");
				HttpResponse response = client.execute(request);

				HttpEntity entity = response.getEntity();
				is = entity.getContent();

				mJSONObject = getJSONObject(is);
				
				JSONToString();


			} finally {
			}
		}
		
		private JSONObject getJSONObject(InputStream is) {
			
			String result = "";
			
			// Read response to string
			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();
			} catch (Exception e) {
				return null;
			}

			// Convert string to object
			try {
				return new JSONObject(result);
			} catch (JSONException e) {
				return null;
			}

		}
		
		private void JSONToString() {
			
			if (mJSONObject == null) {
				return;
			}
			
			try {
				
				//Translation
				JSONArray sentences = mJSONObject.optJSONArray("sentences");
				
				if (sentences != null) {

					for (int i = 0; i < sentences.length(); i++) { 
						JSONObject entry = sentences.getJSONObject(i);
						
						// now get the data from each entry
						String trans = entry.get("trans").toString();
						output += "<h3><b>" + trans + "</b></h3>";
					
					}
				}
			} catch (Exception e) {
				Log.e(LOG_TAG, "error on \"sentences\" part of JSON");
			}
			
			
			try {

				//Dictionary
				JSONArray dictionary = mJSONObject.optJSONArray("dict");
				
				if (dictionary != null) {

					for (int i = 0; i < dictionary.length(); i++) { 
						JSONObject entry = dictionary.getJSONObject(i);
						
						if (i>0) {
							output += "<br /> <br />";	
						}
						
						
						// часть речи
						String pos = entry.get("pos").toString();
						output += "<b>" + pos + "</b>";
						
						// Переводы
//						// метод 1, без обратного перевода
//						JSONArray terms = entry.optJSONArray("terms");
//						for (int j = 0; j < terms.length(); j++) {
//							String term = terms.getString(j);
//							Output += term  + "<br />";
//						}
						
						// метод 2, с обратным переводом
						JSONArray terms = entry.optJSONArray("entry");
						for (int j = 0; j < terms.length(); j++) {
							JSONObject term = terms.optJSONObject(j);
							if (term == null) {
								break;
							}
							
							output += "<br /> ";
							
							String trans = term.getString("word");
							
							output += trans;
							
							JSONArray reverseTranslations = term.optJSONArray("reverse_translation");
							if (reverseTranslations != null) {
								
								for (int k = 0; k < reverseTranslations.length(); k++) {
									
									if (k == 0) {
										output += " ";
									} else {
										output += ", ";
									}
									
									String rt = reverseTranslations.getString(k);
									output += "<font color=\"#C0C0C0\">" + rt + "</font>";
								}
								
							}
						}
					
					}
				}
				
			} catch (Exception e) {
				Log.e(LOG_TAG, "error on \"dict\" part of JSON");
			}
			
		}
	    
		
	}