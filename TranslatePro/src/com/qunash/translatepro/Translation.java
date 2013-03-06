package com.qunash.translatepro;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

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

	/**
	 * Converts from JSONObject to Translate object
	 * 
	 * @param jsonObj
	 *            JSONObject to parse
	 */
	public Translation getTranslation(JSONObject jsonObj) {
		return getTranslation(jsonObj, false, true);
	}

	/**
	 * Converts from JSONObject to Translate object
	 * 
	 * @param jsonObj
	 *            JSONObject to parse
	 * @param auto_detection
	 *            translation used auto detection of source language
	 * @param includeRevTrans
	 *            include reverse translations
	 * 
	 */
	public Translation getTranslation(JSONObject jsonObj, Boolean auto_detection, boolean includeRevTrans) {

		if (jsonObj == null) {
			return null;
		}

		Translation trans = new Translation();

		trans.auto_detect = auto_detect;
		trans.sl = (String) jsonObj.opt("src");

		try {

			// Translation
			JSONArray sentences = jsonObj.optJSONArray("sentences");

			if (sentences != null) {

				for (int i = 0; i < sentences.length(); i++) {
					JSONObject entry = sentences.getJSONObject(i);

					// now get the data from each entry
					trans.input = entry.get("orig").toString();
					trans.output = entry.get("trans").toString();
					trans.sTranslit = entry.get("src_translit").toString();
					trans.tTranslit = entry.get("translit").toString();
					
				}
			}
		} catch (Exception e) {
			Log.e(TAG, "error on \"sentences\" part of JSON");
		}

		try {

			// Dictionary
			JSONArray dictionary = jsonObj.optJSONArray("dict");

			if (dictionary != null) {

				trans.hasDictionary = true;

				for (int i = 0; i < dictionary.length(); i++) {
					JSONObject entry = dictionary.getJSONObject(i);

					if (i > 0) {
						trans.dictionary += "<br /> <br />";
					}

					// Part of speech
					String pos = entry.get("pos").toString();
					trans.dictionary += "<b>" + pos + "</b>";

					// Terms
					if (!includeRevTrans) {
						// without reverse translation
						JSONArray terms = entry.optJSONArray("terms");
						for (int j = 0; j < terms.length(); j++) {
							String term = terms.getString(j);
							trans.dictionary += j+1 + ". " + term + "<br />";
						}

					} else {

						// with reverse translation
						JSONArray entries = entry.optJSONArray("entry");
						for (int j = 0; j < entries.length(); j++) {
							
							JSONObject entry1 = entries.optJSONObject(j);
							
							if (null == entry1) {
								break;
							}

							trans.dictionary += "<br /> ";

							String word = entry1.getString("word");

							trans.dictionary += j+1 + ". " + word;

							JSONArray reverseTranslations = entry1.optJSONArray("reverse_translation");
							
							if (null != reverseTranslations) {

								for (int k = 0; k < reverseTranslations.length(); k++) {

									if (k == 0) {
										trans.dictionary += " ";
									} else {
										trans.dictionary += ", ";
									}

									String rt = reverseTranslations.getString(k);
									trans.dictionary += "<font color=\"#C0C0C0\">" + rt + "</font>";
								}

							}
						}

					}
				}
			}

		} catch (Exception e) {
			Log.e(TAG, "error on \"dict\" part of JSON");
		}

		return trans;
	}
}
