package com.ivinny.tempcalc;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConversionFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	String[] types;
	TextView result;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		types = new String[3];
		types[0] = getString(R.string.f);
		types[1] = getString(R.string.c);
		types[2] = getString(R.string.k);
		
		
		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.conversion, container, false);

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
        Bundle bundleObj= getActivity().getIntent().getExtras();
        try {
        		String temp = bundleObj.get("temp").toString();
        		String city = bundleObj.get("city").toString();
        		url = bundleObj.get("url").toString();
        		tv.setText("Convert Fahrenheit to Celsius and Kelvin for " + city);
                et.setText(temp);
                updateTemps(temp);
        }
        catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
		
		return ll;
	}
	
	//does the temp conversion
	public void updateTemps(String temp){
		String text = "";
		for(int i = 0; i<types.length; i++){
			String type = types[i];
			Float finalTemp = Float.valueOf(0);
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
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
}
