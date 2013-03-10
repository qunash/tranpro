package translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/** Translation class (converted from JSONObject)*/
public class Translation {

	private final static String TAG = "Translation class";

	public boolean auto_detect;
	public String sl;
	public String tl;
	public String sTranslit;
	public String tTranslit;
	public String input;
	public String output;
	public boolean hasDictionary = false;
	public String dictionary;

	public Translation() {
		
	}
	
	/**
	 * Converts from JSONObject to Translate object
	 * 
	 * @param jsonObj
	 *            JSONObject to parse
	 * @throws JSONException 
	 */
	public void JSONToTranslation(JSONObject mJSONObject) throws JSONException {
		
		if (mJSONObject == null) {
			return;
		}
		
		this.getTranslation(mJSONObject, true);
		
	}
	
	/**
	 * creates Translate object from JSONObject 
	 * 
	 * @param jsonObj
	 *            JSONObject to parse
	 * @param auto_detection
	 *            translation used auto detection of source language
	 * @param includeRevTrans
	 *            include reverse translations
	 * @throws JSONException 
	 * 
	 */
	public void getTranslation(JSONObject jsonObj, boolean includeRevTrans) throws JSONException {

		if (jsonObj == null) {
			return;
		}

		// Source language		
		this.sl = (String) jsonObj.opt("src");

//		//гугл добавил JSONObject ld_result с автоопределенными языками, не знаю зачем, но может пригодиться
//		JSONObject langDetectionResult = jsonObj.optJSONObject("ld_result");
//
//		if (langDetectionResult != null) {
//			JSONArray detectedSourceLangs = jsonObj.optJSONArray("srclangs");
//
//			if (detectedSourceLangs != null && detectedSourceLangs.length() > 0) {
//				this.sl = detectedSourceLangs.getString(0);
//			}
//		}

		try {

			// Translation
			JSONArray sentences = jsonObj.optJSONArray("sentences");

			if (sentences != null) {

				for (int i = 0; i < sentences.length(); i++) {
					JSONObject entry = sentences.getJSONObject(i);

					// now get the data from each entry
					this.input = entry.get("orig").toString();
					this.output = entry.get("trans").toString();
					this.sTranslit = entry.get("src_translit").toString();
					this.tTranslit = entry.get("translit").toString();
					
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "error on \"sentences\" part of JSON");
		}

		try {

			// Dictionary
			JSONArray dictionary = jsonObj.optJSONArray("dict");

			if (dictionary != null) {

				this.hasDictionary = true;
				this.dictionary = "";

				for (int i = 0; i < dictionary.length(); i++) {
					JSONObject entry = dictionary.getJSONObject(i);

					if (i > 0) {
						this.dictionary += "<br /> <br />";
					}

					// Part of speech
					String pos = entry.get("pos").toString();
					this.dictionary += "<b>" + pos + "</b>";

					// Terms
					if (!includeRevTrans) {
						// without reverse translation
						JSONArray terms = entry.optJSONArray("terms");
						for (int j = 0; j < terms.length(); j++) {
							String term = terms.getString(j);
							this.dictionary += j+1 + ". " + term + "<br />";
						}

					} else {

						// with reverse translation
						JSONArray entries = entry.optJSONArray("entry");
						for (int j = 0; j < entries.length(); j++) {
							
							JSONObject entry1 = entries.optJSONObject(j);
							
							if (null == entry1) {
								break;
							}

							this.dictionary += "<br /> ";

							String word = entry1.getString("word");

							this.dictionary += j+1 + ". " + word;

							JSONArray reverseTranslations = entry1.optJSONArray("reverse_translation");
							
							if (null != reverseTranslations) {

								//for (int k = 0; k < reverseTranslations.length(); k++) {
								//
								//	if (k == 0) {
								//		this.dictionary += " ";
								//} else {
								//	this.dictionary += ", ";
								//}
								//
								//String rt = reverseTranslations.getString(k);
								//this.dictionary += "<font color=\"#C0C0C0\">" + rt + "</font>";
								//}
								
								//так намного быстрей
								this.dictionary += " <font color=\"#C0C0C0\">" + reverseTranslations.join(", ").replaceAll("\"", "") + "</font>";

							}
						}

					}
				}
			}

		} catch (Exception e) {
			Log.e(TAG, "error on \"dict\" part of JSON");
		}
	}
}