package com.qunash.translatepro;

import java.net.URLEncoder;

import utils.TransAsyncTask;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import org.holoeverywhere.widget.Button;
import org.holoeverywhere.widget.EditText;
import org.holoeverywhere.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Main extends ParentActivity {

	private static String LOG_TAG = "TranslatePro";
	
	
	EditText et01 = null;
	static TextView tv01 = null;
	static Button btnTranslate = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		//****************//
//		//TEST
//		
//		LayoutInflater inflater = LayoutInflater.from(this);
//        pages = new ArrayList<View>();
//        
//        View page = inflater.inflate(R.layout.languages_activity, null);
//        pages.add(page);
//        
//        page = inflater.inflate(R.layout.main, null);
//        et01 = (EditText) page.findViewById(R.id.et01);
//		tv01 = (TextView) page.findViewById(R.id.tv01);
//		btnTranslate = (Button) page.findViewById(R.id.btn01);
//		
//		Typeface tf = Typeface.createFromAsset(getAssets(), "SegoeWP-Semilight.ttf");
//		tv01.setTypeface(tf);
//        pages.add(page);
//        
//        SamplePagerAdapter pagerAdapter = new SamplePagerAdapter(pages);
//        viewPager = new ViewPager(this);
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.setCurrentItem(1);     
//        
//        viewPager.setOnPageChangeListener(new ViewPagerListener());
//
//        setContentView(viewPager);
//		
//		//****************//
		
		setContentView(R.layout.main);
		
		et01 = (EditText) findViewById(R.id.et01);
		tv01 = (TextView) findViewById(R.id.tv01);
		btnTranslate = (Button) findViewById(R.id.btn01);
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "SegoeWP-Semilight.ttf");
		tv01.setTypeface(tf);
		
	}

	public void Translate(View view) {
		
		// hide soft keyboard
		InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		et01.clearFocus();
		
		// Check internet connection
		if (!hasConnection()) {
			makeToast("Network error. Check your internet connection!");
			return;
		}
		
		// make button inactive
		btnTranslate.setEnabled(false);
		
		String tl = mPreferences.getString("tl", "");
		String sl = mPreferences.getString("sl", "");
		
		String strInput = et01.getText().toString();
		
		if (strInput.equals("")) {
			return;
		}
		
		try {
			
			String URLstring;
			
			strInput = URLEncoder.encode(strInput, "UTF-8");
			
			URLstring = "http://translate.google.com/translate_a/t?client=p&text=";
			URLstring += strInput;
			URLstring += "&hl=" + tl;
			URLstring += "&sl=" + sl;
			URLstring += "&tl=" + tl;
			
			TransAsyncTask transAsyncTask = new TransAsyncTask();
			transAsyncTask.execute(URLstring);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void showResult(String output) {
		
		// reactivate button
		btnTranslate.setEnabled(true);
		
		tv01.setText(Html.fromHtml(output));
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.mymenu, menu);
		return super.onCreateOptionsMenu(menu); //по умолчанию возвращает true
	}

    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId()) {
		case R.id.menu_languages:
			Intent intent = new Intent(this, LanguagesActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
    	
    	return super.onOptionsItemSelected(item);
      }
	
    
    
	
	
}
