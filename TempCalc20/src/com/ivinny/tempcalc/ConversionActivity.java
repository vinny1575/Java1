package com.ivinny.tempcalc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConversionActivity extends Activity {
	
	String[] types;
	TextView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setTheme(R.style.myTheme);
		
		types = new String[3];
		types[0] = getString(R.string.f);
		types[1] = getString(R.string.c);
		types[2] = getString(R.string.k);
		
		
		LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.conversion, null);
		setContentView(ll);
		//simple info view
		TextView tv = (TextView)ll.findViewById(R.id.textView1);;
		tv.setText("Convert Fahrenheit to Celsius and Kelvin");
				
		final EditText et = (EditText)ll.findViewById(R.id.editText1);
		et.setHint("Enter Farhenheit");
//				ll.addView(et);
		
		Button b = (Button)ll.findViewById(R.id.button1);
		b.setText("Convert");
//				ll.addView(b);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				updateTemps(et.getText().toString());
			}
		});
		

		
		result = (TextView)ll.findViewById(R.id.textView2);
		
		String url = "";
        Bundle bundleObj= getIntent().getExtras();
        try {
        		String temp = bundleObj.get("temp").toString();
        		String city = bundleObj.get("city").toString();
        		url = bundleObj.get("url").toString();
        		tv.setText("Convert Fahrenheit to Celsius and Kelvin for " + city);
                et.setText(temp);
                updateTemps(temp);
        }
        catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
        }
        
        final String finalURL = url;
		Button mInfoB = (Button)ll.findViewById(R.id.button2);
		mInfoB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalURL));
				startActivity(browserIntent);
			}
		});
	}
	
	//does the temp conversion
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
		Log.i("temps", text);
		result.setText(text);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
