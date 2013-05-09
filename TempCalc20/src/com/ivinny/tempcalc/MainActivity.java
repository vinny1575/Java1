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
import android.content.Intent;
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
		
		final Context context = this;
		setTheme(R.style.myTheme);
		//adds the types from the resources 

		ll = (LinearLayout) getLayoutInflater().inflate(R.layout.form, null);
		ll.setOrientation(LinearLayout.VERTICAL);
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		ll.setLayoutParams(lp);
		
		
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
						        final JSONObject json_data = cities.getJSONObject(i).getJSONObject("current_observation");;
						        final String city =  json_data.getJSONObject("display_location").getString("city");
						        final String url = json_data.getString("forecast_url");
						        //enum from string
						        CitiesT jType = CitiesT.fromLetter(city);
						        
						        final double temp =  json_data.getDouble("temp_f");
						        // .. get all value here
						        Log.i("City: ", city);  
						        Button cityB = new Button(context);
						        cityB.setText(city+": "+temp+" ("+jType.isCold(temp)+")");
						        cityB.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(context, ConversionActivity.class);
						                Bundle bundleObj = new Bundle();
						                //Just pass a username to other screen
						                bundleObj.putString("city", city);
						                bundleObj.putString("url", url);
						                bundleObj.putString("temp", Double.toString(temp));
						                intent.putExtras(bundleObj);
										startActivity(intent);
									}
								});
						        ll.addView(cityB);
//						        text = text+"\r\n"+city+": "+temp+" ("+jType.isCold(temp)+")";
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
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
