package com.guidebook.location;

import com.guidebook.R;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class LocationActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_activity);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, LocationInfoListActivity.class);
		spec = tabHost
				.newTabSpec("info")
				.setIndicator("Info",
						res.getDrawable(R.drawable.ic_tab_location_info))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, LocationReviewListActivity.class);
		spec = tabHost
				.newTabSpec("reviews")
				.setIndicator("Reviews",
						res.getDrawable(R.drawable.ic_tab_location_reviews))
				.setContent(intent);
		tabHost.addTab(spec);
		
		intent = new Intent().setClass(this, LocationVoiceListActivity.class);
		spec = tabHost
				.newTabSpec("voice")
				.setIndicator("Voice Notes",
						res.getDrawable(R.drawable.ic_tab_location_voice))
				.setContent(intent);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}

}
