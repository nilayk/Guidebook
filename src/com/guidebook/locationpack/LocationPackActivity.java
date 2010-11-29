package com.guidebook.locationpack;

import com.guidebook.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class LocationPackActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_pack_activity);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, LocationPackLocationListActivity.class);
		spec = tabHost
				.newTabSpec("locations")
				.setIndicator(
						"Locations",
						res.getDrawable(R.drawable.ic_tab_locationpack_locations))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, LocationPackGuidedTourListActivity.class);
		spec = tabHost
				.newTabSpec("guidedtours")
				.setIndicator(
						"Guided Tours",
						res.getDrawable(R.drawable.ic_tab_locationpack_guidedtours))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
}
