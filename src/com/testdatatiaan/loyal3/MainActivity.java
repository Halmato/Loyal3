// Set each company's advert images name to:  shopName1,shopName2,shopName3
// Set each company's name logo to         :  shopName0 

package com.testdatatiaan.loyal3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private int scanCount = 0;
	private int maxScans = 0;
	private String shopName = "";
	private int shopID = 0;
	

	//Checks if network is available. 
	private boolean isNetworkAvailable() {
		    ConnectivityManager connectivityManager 
		          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
		}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button buttonScanButton =(Button)findViewById(R.id.buttonScanButton);
		
		//if network is available, clicking the Scan button takes the user to the QR-Scanner.
		//If network  is not available, button text changes, and clicking takes user to network settings.
		if(isNetworkAvailable())	{
			
			buttonScanButton.setText("START SCAN");
			// QR-Scanner gets called when button is clicked
			buttonScanButton.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
					startActivityForResult(intent, 0);
				}
			});
		}	else	{
			buttonScanButton.setText("Go To Settings");
			buttonScanButton.setOnClickListener(new View.OnClickListener()	{

				@Override
				public void onClick(View v) {
					 startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
					
					 //insert method here to either restart the app (to see if connection has been made)
					 //or to use a changelistener of the network settings, that changes button back to scan mode
					 //I think restarting the app will be easier, since not a lot has been loaded. Maar hulle 
					 //se dit is bad practice. fuck "hulle" imo.
					 
				}
			});
		}
		
		
	}
	
	// On result of QR-Scanned
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//If scan is successful: 
		if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		         String contents = intent.getStringExtra("SCAN_RESULT");
		         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         
		         //Starts the GET request with the scan contents as the URL
		         DownloadWebPageTask task = new DownloadWebPageTask();
				 task.execute(new String[] { contents });

		 //If scan is not successful: 
		      }	else if (resultCode == RESULT_CANCELED) {
		    	  
		      }
		      
		}
	}
	
	
	private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
		    @Override
		    protected String doInBackground(String... urls) {
		      String response = "";
		      for (String url : urls) {
		        DefaultHttpClient client = new DefaultHttpClient();
		        HttpGet httpGet = new HttpGet(url);
		        try {
		          HttpResponse execute = client.execute(httpGet);
		          InputStream content = execute.getEntity().getContent();

		          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
		          String s = "";
		          while ((s = buffer.readLine()) != null) {
		            response += s;
		          }

		        } catch (Exception e) {
		          e.printStackTrace();
		        }
		      }
		      return response;
		    }

		    @Override
		    protected void onPostExecute(String result) {
		    	
		    	//FIND A WAY TO GET THESE THINGS  (Remove //s of code below)
		    	scanCount = 5;		//result.getScanCount() + 1;    
		    	maxScans = 10;		//result.getMaxScans();
		    	shopName = "Pnp";	//result.getShopName();
		    	shopID = 2001;		//result.getShopID();
		    	
		    	//UPDATE DB TO SAY THAT TIAAN HAS SCANNED
		         updateServer();  
	
		         //Opens DisplayActivity with extra info passed on
		         Intent displayActivityIntent = new Intent(MainActivity.this, DisplayActivity.class);
		         displayActivityIntent.putExtra("scanCount", scanCount);
		         displayActivityIntent.putExtra("maxScans", maxScans);
		         displayActivityIntent.putExtra("shopName", shopName);
		         displayActivityIntent.putExtra("shopID", shopID);
		         
		         startActivity(displayActivityIntent);
		    	
		    }
		  }
	
	
	// Updates server with new scan info
	private void updateServer()	{
		//Upload to server to save
			//shopName as KEY
			//countAfterScan as VALUE
		
	}
	
}
