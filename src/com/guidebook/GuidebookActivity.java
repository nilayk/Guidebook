package com.guidebook;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class GuidebookActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this,
				GuidebookLocationsTabActivity.class);
		spec = tabHost
				.newTabSpec("locations")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_tab_locations))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this,
				GuidebookLocationPacksTabActivity.class);
		spec = tabHost
				.newTabSpec("locationpacks")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_tab_locationpacks))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
}
