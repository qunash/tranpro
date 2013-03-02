package com.qunash.translatepro;

import java.util.List;
import java.util.Locale;

import utils.PreferencesEditor;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import android.support.v4.view.ViewPager;

public class ParentActivity extends org.holoeverywhere.app.Activity {

	public SharedPreferences mPreferences;
	public PreferencesEditor mPrefEditor;
	public final static String mCurrentLanguage = Locale.getDefault().getLanguage();
	public ViewPager viewPager = null;
	public List<View> pages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		mPrefEditor = new PreferencesEditor(mPreferences);
		
	}
	
	/**
	 * Checks if the device has Internet connection.
	 * 
	 * @return <code>true</code> if the phone is connected to the Internet.
	 */
	public boolean hasConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}
	
	/**Shows toast message */
	public void makeToast(String text){
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	
}
