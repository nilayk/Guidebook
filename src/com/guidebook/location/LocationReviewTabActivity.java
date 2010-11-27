package com.guidebook.location;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LocationReviewTabActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		TextView tv = new TextView(this);
		tv.setText("This is the location reviews tab");
		
		setContentView(tv);
	}

}
