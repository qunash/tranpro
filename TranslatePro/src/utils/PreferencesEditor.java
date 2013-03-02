package utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/** class to work with Preferences */
public class PreferencesEditor {

	private Editor mEditor;
	
	public PreferencesEditor(SharedPreferences mPreferences) {
		
		mEditor = mPreferences.edit();
	}
	
	public void set(String key, boolean value) {
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}
	
	public void set(String key, Float value) {
		mEditor.putFloat(key, value);
		mEditor.commit();
	}
	
	public void set(String key, int value) {
		mEditor.putInt(key, value);
		mEditor.commit();
	}
	
	public void set(String key, long value) {
		mEditor.putLong(key, value);
		mEditor.commit();
	}
	
	public void set(String key, String value) {
		mEditor.putString(key, value);
		mEditor.commit();
	}
	
//		//Call requires API level 11 (current min is 8)
//		public void set(String key, Set<String> values) {
//			mEditor.putStringSet(key, values);
//			mEditor.commit();
//		}
	
}
