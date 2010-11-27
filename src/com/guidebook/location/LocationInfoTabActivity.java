package com.guidebook.location;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LocationInfoTabActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		TextView tv = new TextView(this);
		tv.setText("This is location info tab");
		
		setContentView(tv);
	}

}
