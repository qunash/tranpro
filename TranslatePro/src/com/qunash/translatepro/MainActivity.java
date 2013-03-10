package com.qunash.translatepro;

import translation.TransAsyncTask;
import translation.Translation;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends ParentActivity {

	private static String TAG = "TranslatePro";
	
	EditText et01 = null;
	static ScrollView sv01 = null;
	static TextView tv01 = null;
	static ImageButton ibTranslate = null;
	Spinner spSL;
	Spinner spTL;
	ImageButton ibSwapLanguages;
	ImageButton ibClear;
	ArrayAdapter<String> spSLadapter;
	ArrayAdapter<String> spTLadapter;
	
	String sl;
	String tl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		makeToast("Initialized = " + GlobalManager.initialized);
		
		if (!GlobalManager.initialized) {
			startActivity(new Intent(this, InitActivity.class));
		}

		setContentView(R.layout.main);
		initializeUI();

//		makeToast(TAG + " onCreate");
	}
	
	private void initializeUI(){
		
		ibClear = (ImageButton) findViewById(R.id.ibClear);
		ibClear.setEnabled(false); //TODO remove this hardcode (for some reason, isEnabled() of this imageButton = "true" even though isClickable set to "false")
		
		ibTranslate = (ImageButton) findViewById(R.id.ibTranslate);
		ibTranslate.setEnabled(false); //TODO remove this hardcode (for some reason this imageButton isEnabled() = "true" even though isClickable set to "false")
		
		et01 = (EditText) findViewById(R.id.et01);
		et01.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
					ibClear.setEnabled(s.length()>0 ? true : false);
					ibTranslate.setEnabled(s.length()>0 ? true : false);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		et01.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					// hide soft keyboard
					InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					et01.clearFocus();
				}
			}
		});
		
		sv01 = (ScrollView) findViewById(R.id.sv01);
		tv01 = (TextView) findViewById(R.id.tv01);
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "SegoeWP-Semilight.ttf");
		tv01.setTypeface(tf);
		
		// spinners
		spSL = (Spinner) findViewById(R.id.spSL);
		spTL = (Spinner) findViewById(R.id.spTL);
		ibSwapLanguages = (ImageButton) findViewById(R.id.ibSwapLanguages);
		
		spSLadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GlobalManager.mFROMLangsArray);
		spSLadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spTLadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GlobalManager.mTOLangsArray);
		spTLadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		sl = mPreferences.getString("sl", GlobalManager.mFROMLangsArray[0]);
		tl = mPreferences.getString("tl", GlobalManager.mTOLangsArray[0]);
		
		spSL.setAdapter(spSLadapter);
		spSL.setSelection(spSLadapter.getPosition(sl));
		spSL.setOnItemSelectedListener(new listener());
		
		spTL.setAdapter(spTLadapter);
		spTL.setSelection(spTLadapter.getPosition(tl));
		spTL.setOnItemSelectedListener(new listener());
		
		LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
		parentLayout.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				return false;
			}
		});
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		makeToast(TAG + " onDestroy");
	}

	@Override
	protected void onStart() {
		super.onStart();
//		makeToast(TAG + " onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
//		makeToast(TAG + " onStop");
	}

	@Override
	protected void onPause() {
		super.onPause();
//		makeToast(TAG + " onPause");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
//		makeToast(TAG + " onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
//		makeToast(TAG + " onResume");
	}

	public class listener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			
			switch (parent.getId()) {
			case R.id.spSL:
				sl = (String) parent.getItemAtPosition(position);	
				
				mPrefEditor.set("sl", sl);
				break;
			
			case R.id.spTL:
				tl = (String) parent.getItemAtPosition(position);	
				
				mPrefEditor.set("tl", tl);
				break;
			
			default:
				break;
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
		}

	}

	public void SwapLanguages(View view) {
		
		String buffer = sl;
		
		sl = tl;
		tl = buffer;
		
		buffer = null;
		
		spSL.setSelection(spSLadapter.getPosition(sl));
		spTL.setSelection(spTLadapter.getPosition(tl));

		mPrefEditor.set("sl", sl);
		mPrefEditor.set("tl", tl);
	
	}
	
	public void ClearText(View view) {
		et01.setText("");
	}
	
	public void Translate(View view) {
		
		// Check internet connection
		if (!hasConnection()) {
			makeToast("Network error.\n Check your internet connection!");
			return;
		}
		
		String strInput = et01.getText().toString();
		
		if (("").equals(strInput)) {
			return;
		}

		// make button inactive
		ibTranslate.setEnabled(false);
		
		String tl = GlobalManager.mFROMLangsHashMap.get(mPreferences.getString("tl", ""));
		String sl = GlobalManager.mTOLangsHashMap.get(mPreferences.getString("sl", ""));
		
		
		TransAsyncTask transAsyncTask = new TransAsyncTask();
		transAsyncTask.execute(strInput, sl, tl);

	}
	
	public static void showResult(Translation trans) {
		
		// reactivate button
		ibTranslate.setEnabled(true);
		
		String Output="";
		Output += trans.sl;
		Output += "\n" + trans.input;
		Output += "\n" + trans.sTranslit;
		Output += "\n" + trans.output;
		Output += "\n" + trans.tTranslit;
		if (trans.hasDictionary) {
			Output += "\n" + Html.fromHtml(trans.dictionary);
		}
		
		tv01.setText(Output);
		
//		tv01.setText(Html.fromHtml(output)); TODO
		
		sv01.fullScroll(ScrollView.FOCUS_UP);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mymenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
    	
    	return super.onOptionsItemSelected(item);
      }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //Ловить момент когда открыли или закрыли реальную клавиатуру НЕ ПАШЕТ
        if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
           makeToast("Keyboard opened");
        }
        else if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
        	makeToast("Keyboard hidden");
        }
    
	}
	
}
