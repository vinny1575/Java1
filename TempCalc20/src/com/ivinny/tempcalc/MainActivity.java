package com.ivinny.tempcalc;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ivnny.lib.MyForm;
import com.ivinny.json.CitiesT;
import com.ivinny.json.Json;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("UseValueOf")
public class MainActivity extends Activity {

	private static final String TAG = "Network";
	LinearLayout ll;
	LinearLayout.LayoutParams lp;
	EditText et;
	TextView result;
	String[] types;
	TextView dataTView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTheme(R.style.myTheme);
		//adds the types from the resources 
		types = new String[3];
		types[0] = getString(R.string.f);
		types[1] = getString(R.string.c);
		types[2] = getString(R.string.k);
		
		ll = (LinearLayout) getLayoutInflater().inflate(R.layout.form, null);
		ll.setOrientation(LinearLayout.VERTICAL);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		ll.setLayoutParams(lp);
		
//		LinearLayout entryBox = MyForm.singleEntryWithButton(this, "EnterFarhenheit", "Convert");
//		ll.addView(entryBox);
		
		//simple info view
		TextView tv = (TextView)ll.findViewById(R.id.textView2);;
		tv.setText("Convert Fahrenheit to Celsius and Kelvin");
				
		et = (EditText)ll.findViewById(R.id.editText1);
		et.setHint("Enter Farhenheit");
//		ll.addView(et);
		
		Button b = (Button)ll.findViewById(R.id.button1);
		b.setText("Convert");
//		ll.addView(b);
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				updateTemps(et.getText().toString());
			}
		});
		
		
		result = (TextView)ll.findViewById(R.id.textView1);;
//		ll.addView(result);
		
		//button used to download json data (for now we aren just simulating this)
		Button getDataBtn = (Button)ll.findViewById(R.id.button2);;
		getDataBtn.setText("Download Cities");
		
		//results of json data after formatted
		dataTView = (TextView)ll.findViewById(R.id.textView3);;
		
		//button click for loading json data
		getDataBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo ni = cm.getActiveNetworkInfo();
				if(ni!=null){
					if(ni.isConnected()){
						Log.i("network","online");
						JSONArray ar = null;
						try {
							JSONObject citiesToGet = new JSONObject(Json.getJsonString());
							ar = citiesToGet.getJSONArray("Cities");
							 
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						JSONArray cities = new JSONArray();
						
						//adds results for fetch (json string) to an array of cities info
						for(int i = 0; i<ar.length(); i++){
							String jsonStr = null;
							try {
								jsonStr = getTempForCityFromWebSvc(ar.getJSONObject(i).getString("city"), ar.getJSONObject(i).getString("state"));
								Log.i("json", jsonStr);
							} catch (JSONException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							// TODO Auto-generated method stub
							JSONObject jsonObj = null;
							//using try catches to avoid app lockups
							try {
								Log.i("json", jsonStr);
								jsonObj = new JSONObject(jsonStr);
								cities.put(jsonObj);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						try {
							String text = "";
						    for(int i=0;i<cities.length();i++){
						    	//getting json from string in jsonobject
						        JSONObject json_data = cities.getJSONObject(i).getJSONObject("current_observation");;
						        String city =  json_data.getJSONObject("display_location").getString("city");
						        //enum from string
						        Log.i("city", city);
						        CitiesT jType = CitiesT.fromLetter(city);
						        
						        double temp =  json_data.getDouble("temp_f");
						        // .. get all value here
						        Log.i("City: ", city);  
						        text = text+"\r\n"+city+": "+temp+" ("+jType.isCold(temp)+")";
						    }
						    dataTView.setText(text);
						    
						    //writing a cache file
						    String content = text;
						    File file = new File(getCacheDir(), "appCache");
						    FileOutputStream fos = new FileOutputStream(file.getAbsolutePath(), true);
						    fos.write(content.getBytes());
						    fos.close();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}else{
						showAlert();
					}
				}else{
					showAlert();
				}

			}
		});
		
//		//like setting rootviewcontroller on ios
		setContentView(ll);
		
	}
	
	//alert for no internet
	public void showAlert(){
		try {
			File cacheFile = new File(getCacheDir(), "appCache");
			if(cacheFile.exists()){	//if cache exists use it
				FileInputStream fis = new FileInputStream(cacheFile);
				StringBuffer fileContent = new StringBuffer("");
	
				byte[] buffer = new byte[1024];
	
				while (fis.read(buffer) != -1) {
				    fileContent.append(new String(buffer));
				}
				dataTView.setText(fileContent.toString());
				fis.close();
				
			}else{		//else show alert
				AlertDialog.Builder alt = new AlertDialog.Builder(this);     
				alt.setMessage("Not Connected To The Internet.").setPositiveButton("OK", null);
				alt.show();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//get network data (specific weather info for city and state
	public String getTempForCityFromWebSvc(String city, String state){
		String result = "";

		String url = "http://api.wunderground.com/api/87a8ccdf84e0370d/conditions/q/"+state+"/"+city+".json";
		WeatherAsync resp = new WeatherAsync();
		String returnStr = null;
		try {
			returnStr = resp.execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnStr;
//	    HttpClient httpclient = new DefaultHttpClient();
//
//	    // Prepare a request object
//	    HttpGet httpget = new HttpGet(url); 
//
//	    // Execute the request
//	    HttpResponse response;
//	    try {
//	        response = httpclient.execute(httpget);
//	        // Examine the response status
//	        Log.i("Praeda",response.getStatusLine().toString());
//
//	        // Get hold of the response entity
//	        HttpEntity entity = response.getEntity();
//	        // If the response does not enclose an entity, there is no need
//	        // to worry about connection release
//
//	        if (entity != null) {
//
//	            // A Simple JSON Response Read
//	            InputStream instream = entity.getContent();
//	            result= convertStreamToString(instream);
//	            // now you have the string representation of the HTML request
//	            Log.i(TAG, result);
//	            instream.close();
//	        }
//
//
//	    } catch (Exception e) {
//	    	Log.i("Connection", e.toString());
//	    }
//			    
//		Log.i("json", result);
//		return result;
	}
	
    private static String convertStreamToString(InputStream is) {
	    /*
	     * To convert the InputStream to String we use the BufferedReader.readLine()
	     * method. We iterate until the BufferedReader return null which means
	     * there's no more data to read. Each line will appended to a StringBuilder
	     * and returned as String.
	     */
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	
	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            is.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return sb.toString();
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
