package com.qunash.translatepro;

import android.os.Bundle;
import android.view.View;
import org.holoeverywhere.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import org.holoeverywhere.widget.Spinner;
import org.holoeverywhere.widget.AdapterView.OnItemSelectedListener;

public class LanguagesActivity extends ParentActivity {

	Spinner spSL;
	Spinner spTL;
	ImageButton imgbtnSwitchLanguages;
	ArrayAdapter<CharSequence> adapter;
	
	String sl;
	String tl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.languages_activity);
		
		spSL = (Spinner) findViewById(R.id.spSL);
		spTL = (Spinner) findViewById(R.id.spTL);
		imgbtnSwitchLanguages = (ImageButton) findViewById(R.id.imgbtnSwitchLanguages);
		
		adapter = ArrayAdapter.createFromResource(this, R.array.languages_list, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		sl = mPreferences.getString("sl", adapter.getPosition(mCurrentLanguage)>-1 ? mCurrentLanguage : "en");
		tl = mPreferences.getString("tl", "en");
		
		spSL.setAdapter(adapter);
		spSL.setSelection(adapter.getPosition(sl));
		spSL.setOnItemSelectedListener(new listener());
		
		spTL.setAdapter(adapter);
		spTL.setSelection(adapter.getPosition(tl));
		spTL.setOnItemSelectedListener(new listener());
	}
	
	public void SwitchLanguages(View view) {
		
		String buffer = sl;
		
		sl = tl;
		tl = buffer;
		
		buffer = null;
		
		spSL.setSelection(adapter.getPosition(sl));
		spTL.setSelection(adapter.getPosition(tl));

		mPrefEditor.set("sl", sl);
		mPrefEditor.set("tl", tl);
	
	}
	
	public class listener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			
			switch (parent.getId()) {
			case R.id.spSL:
				String sl = parent.getItemAtPosition(position).toString();	
				
				mPrefEditor.set("sl", sl);
				break;
			
			case R.id.spTL:
				String tl = parent.getItemAtPosition(position).toString();	
				
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

}
