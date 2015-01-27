package com.testdatatiaan.loyal3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class DisplayActivity extends Activity {
	
	
	private int scanCount = 0;
	private int maxScans = 0;
	private String shopName = "";
	private int shopID = 0;
	private int redeemable = 0;
	private int needed = 0;
	private ViewFlipper viewFlipper;			
	private float lastX;
	

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);
		
		//Retrieves extra info passed from previous activity (MainActivity)
		Bundle extraInfo = getIntent().getExtras();
		scanCount = extraInfo.getInt("scanCount");
		maxScans = extraInfo.getInt("maxScans");
		shopName = extraInfo.getString("shopName");
		shopID = extraInfo.getInt("shopID");
		
		redeemable = scanCount / maxScans;				//Checks how many redeemable vouchers you have.
		needed = maxScans - (scanCount % maxScans);		//Checks how many scans you need for next item
		

		//Initialize all components
		TextView amountDisplayed = (TextView)findViewById(R.id.amountDisplayed);
		Button buttonRedeemItem = (Button)findViewById(R.id.buttonRedeemItem);
		viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper_one);
		
		if(redeemable > 1)	{
			buttonRedeemItem.setText("Click here to claim your " + redeemable + " free items!");
		}	else if(redeemable == 1)	{
			buttonRedeemItem.setText("Click here to claim your " + redeemable + " free item!");
		}	else {
			buttonRedeemItem.setText("You need " + needed + " more 3wards!");
		}
		
		
		
		// Makes the advert viewFlipper auto flip after 10 seconds	
		viewFlipper.setFlipInterval(10000);
		viewFlipper.startFlipping();		
		viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);			
		viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);
		
		//RedeemButton OnClickListener - Opens RedeemActivity
		buttonRedeemItem.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(redeemable > 0)	{
					scanCount -= maxScans;
					Intent intent = new Intent (DisplayActivity.this, RedeemActivity.class);
					startActivity(intent);
					
					//need to update database (already redeemed & countAfterScan)
				}
				
			}
		});
		
		
		//Sets resources according to info received
		amountDisplayed.setText("You have recieved a " + shopName + " 3ward!");
		
		setStarsImage();
		setAdvertImages();
		
	}
	
	
	
	//Sets the starsImage based on countAfterScan
	private void setStarsImage()	{
		ImageView stars =(ImageView) findViewById(R.id.star0);

		if((scanCount % maxScans) == 1)	{
			stars.setImageResource(R.drawable.stars_one_filled);
		}	else if((scanCount % maxScans)==2)	{
			stars.setImageResource(R.drawable.stars_two_filled);
		}	else if((scanCount % maxScans)==3)	{
			stars.setImageResource(R.drawable.stars_three_filled);
		}	else if((scanCount % maxScans)==4)	{
			stars.setImageResource(R.drawable.stars_four_filled);
		}	else if((scanCount % maxScans)==5)	{
			stars.setImageResource(R.drawable.stars_five_filled);
		}	else if((scanCount % maxScans)==6)	{
			stars.setImageResource(R.drawable.stars_six_filled);
		}	else if((scanCount % maxScans)==7)	{
			stars.setImageResource(R.drawable.stars_seven_filled);
		}	else if((scanCount % maxScans)==8)	{
			stars.setImageResource(R.drawable.stars_eight_filled);
		}	else if((scanCount % maxScans)==9)	{
			stars.setImageResource(R.drawable.stars_nine_filled);
		}	else if((scanCount % maxScans)==0)	{     //Insert code to provide for maxScans >10;
			stars.setImageResource(R.drawable.stars_ten_filled_gold);
		}
	}
	
	//Sets advertisement + shopName images dynamically based on shopName (3 adverts + shopNameLogo = shopName0,shopName1,shopName2,shopName3)
	private void setAdvertImages ()	{
		ImageView image1 = (ImageView) findViewById(R.id.imageView1);
		ImageView image2 = (ImageView) findViewById(R.id.imageView2);
		ImageView image3 = (ImageView) findViewById(R.id.imageView3);
		ImageView companyNameImage = (ImageView)findViewById(R.id.companyNameImage);
		
		String shopNameToLowerCase = shopName.toLowerCase();
		
		int id0 = getResources().getIdentifier("com.testdatatiaan.loyal3:drawable/" + shopNameToLowerCase + "0", null, null);
		int id1 = getResources().getIdentifier("com.testdatatiaan.loyal3:drawable/" + shopNameToLowerCase + "1", null, null);
		int id2 = getResources().getIdentifier("com.testdatatiaan.loyal3:drawable/" + shopNameToLowerCase + "2", null, null);
		int id3 = getResources().getIdentifier("com.testdatatiaan.loyal3:drawable/" + shopNameToLowerCase + "3", null, null);
		
		
		companyNameImage.setImageResource(id0);
		image1.setImageResource(id1);
		image2.setImageResource(id2);
		image3.setImageResource(id3);
		
	}

	
	//Sets motion of Image Flipper (advertisements , 3 images)
	public boolean onTouchEvent(MotionEvent touchevent) {						
		
		switch (touchevent.getAction()) {				
						
		case MotionEvent.ACTION_DOWN: 				
						
			lastX = touchevent.getX();			
			break;			
						
		case MotionEvent.ACTION_UP: 				
			float currentX = touchevent.getX();			
						
			// Handling left to right screen swap.			
			if (lastX < currentX) {			
				// If there aren't any other children, just break.		
				if (viewFlipper.getDisplayedChild() == 0)		
					break;	
				// Next screen comes in from left.		
				viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);		
						
				// Current screen goes out from right. 		
				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);		
						
				// Display next screen.		
				viewFlipper.showNext();		
			}			
			// Handling right to left screen swap.			
			if (lastX > currentX) {			
				// If there is a child (to the left), just break.		
				if (viewFlipper.getDisplayedChild() == 1)		
					break;	
						
				// Next screen comes in from right.		
				viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);		
						
				// Current screen goes out from left. 		
				viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);		
						
				// Display previous screen.		
				viewFlipper.showPrevious();		
			}			
			break;			
		}				
		return false;				
	}					

}
