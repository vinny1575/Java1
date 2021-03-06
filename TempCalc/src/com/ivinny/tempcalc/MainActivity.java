package com.ivinny.tempcalc;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("UseValueOf")
public class MainActivity extends Activity {

	LinearLayout ll;
	LinearLayout.LayoutParams lp;
	EditText et;
	TextView result;
	String[] types;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		types = new String[3];
		types[0] = getString(R.string.f);
		types[1] = getString(R.string.c);
		types[2] = getString(R.string.k);
		
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		ll.setLayoutParams(lp);
		
		TextView tv = new TextView(this);
		tv.setText("Convert Fahrenheit to Celsius and Kelvin");
		
		ll.addView(tv);
		
		et = new EditText(this);
		et.setHint("Enter Farhenheit");
//		ll.addView(et);
		
		Button b = new Button(this);
		b.setText("Convert");
//		ll.addView(b);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				updateTemps(et.getText().toString());
			}
		});
		
		
		LinearLayout form = new LinearLayout(this);
		form.setOrientation(LinearLayout.HORIZONTAL);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		form.setLayoutParams(lp);
		
		form.addView(et);
		form.addView(b);
		
		ll.addView(form);
		
		result = new TextView(this);
		ll.addView(result);
		
		setContentView(ll);
	}

	public void updateTemps(String temp){
		String text = "";
		for(int i = 0; i<types.length; i++){
			String type = types[i];
			Float finalTemp = new Float(0);
			Boolean isF = type == getString(R.string.f);
			Boolean isC = type == getString(R.string.c);
			if(isF){
				finalTemp = Float.parseFloat(temp);
			}else if(isC){
				finalTemp = (float) ((Float.parseFloat(temp)-32)*.55);
			}else{
				finalTemp = (float) ((Float.parseFloat(temp)-32)*.55 + 273.15);
			}
			text = text+"\r\n"+finalTemp+" "+type;
		}
		result.setText(text);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
