package com.ivinny.tempcalc;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ConversionActivity extends Activity {
	
	String[] types;
	TextView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.conversion_fragment);
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
