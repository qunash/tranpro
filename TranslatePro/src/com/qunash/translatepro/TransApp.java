package com.qunash.translatepro;

import java.util.Locale;

import utils.PreferencesEditor;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TransApp extends Application {

	public SharedPreferences mPreferences;
	public PreferencesEditor mPrefEditor;
	public final static String mCurrentLanguage = Locale.getDefault().getLanguage();
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		mPrefEditor = new PreferencesEditor(mPreferences);
		
	}
	
	

}
