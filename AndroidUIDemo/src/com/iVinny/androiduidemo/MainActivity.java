package com.iVinny.androiduidemo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	LinearLayout ll;
	LinearLayout.LayoutParams lp;
	EditText et;
	TextView result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		ll.setLayoutParams(lp);
		
		TextView tv = new TextView(this);
//		tv.setText(getString(R.integer.quarter)+","+getString(R.integer.dime)+","+getString(R.string.nickel)+","+getString(R.string.penny));
		tv.setText("Convert Dollars to Coins");
		
		ll.addView(tv);
		
		et = new EditText(this);
		et.setHint("Enter Dollars");
//		ll.addView(et);
		
		Button b = new Button(this);
		b.setText("Convert");
//		ll.addView(b);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int quarter = getResources().getInteger(R.integer.quarter);
				int dime = getResources().getInteger(R.integer.dime);
				int nickel = getResources().getInteger(R.integer.nickel);
				int penny = getResources().getInteger(R.integer.penny);
				
				int entry = Integer.parseInt(et.getText().toString());
				
				int numQ = (100/quarter)*entry;
				int numD = (100/dime)*entry;
				int numN = (100/nickel)*entry;
				int numP = (100/penny)*entry;
				
				result.setText("Quarter: "+numQ + "\r\n" + 
						"Dime: " + numD + "\r\n" +
						"Nickel: " + numN + "\r\n" +
						"Penny: " + numP + "\r\n"
						);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
