package com.ivinny.tempcalc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ivnny.lib.MyForm;
import com.ivinny.json.CitiesT;
import com.ivinny.json.Json;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
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
	TextView dataTView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//adds the types from the resources 
		types = new String[3];
		types[0] = getString(R.string.f);
		types[1] = getString(R.string.c);
		types[2] = getString(R.string.k);
		
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		ll.setLayoutParams(lp);
		
		LinearLayout entryBox = MyForm.singleEntryWithButton(this, "EnterFarhenheit", "Convert");
		ll.addView(entryBox);
		
		//simple info view
		TextView tv = new TextView(this);
		tv.setText("Convert Fahrenheit to Celsius and Kelvin");
		
		ll.addView(tv);
		
		et = (EditText)entryBox.findViewById(1);
		et.setHint("Enter Farhenheit");
//		ll.addView(et);
		
		Button b = (Button)entryBox.findViewById(2);
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
		
		
		ll.addView(form);
		
		result = new TextView(this);
		ll.addView(result);
		
		Button getDataBtn = new Button(this);
		getDataBtn.setText("Download Cities");
		ll.addView(getDataBtn);
		
		dataTView = new TextView(this);
		ll.addView(dataTView);
		
		//button click for loading json data
		getDataBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JSONObject jsonObj = null;
				//using try catches to avoid app lockups
				try {
					jsonObj = new JSONObject(Json.getJsonString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					JSONArray cities = jsonObj.getJSONArray("Cities");
					String text = "";
				    for(int i=0;i<cities.length();i++){
				    	//getting json from string in jsonobject
				        JSONObject json_data = cities.getJSONObject(i);
				        String city =  json_data.getString("name");
				        //enum from string
				        CitiesT jType = CitiesT.fromLetter(city);
				        
				        String temp =  json_data.getString("temp");
				        // .. get all value here
				        Log.i("City: ", city);  
				        text = text+"\r\n"+city+": "+temp+" ("+jType.isCold(Integer.parseInt(temp))+")";
				    }
				    dataTView.setText(text);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		setContentView(ll);
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
		result.setText(text);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
