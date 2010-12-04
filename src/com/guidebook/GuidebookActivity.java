package com.guidebook;

import com.guidebook.map.LocationMapViewActivity;
import com.guidebook.map.MyLocationListener;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class GuidebookActivity extends TabActivity {

	Button mapButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapButton = (Button) findViewById(R.id.buttonShowMap);

		mapButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(getApplicationContext(),
						LocationMapViewActivity.class);
				startActivity(i);

			}
		});

		Resources res = getResources();
		final TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this,
				GuidebookLocationsTabActivity.class);
		spec = tabHost.newTabSpec("locations")
				.setIndicator("", res.getDrawable(R.drawable.ic_tab_locations))
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

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).setBackgroundColor(0xFFb5b5b5); // unselected
		}
		tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
				.setBackgroundColor(0xFFE5E5E5); // selected

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
					tabHost.getTabWidget().getChildAt(i)
							.setBackgroundColor(0xFFB5B5B5); // unselected
				}
				tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
						.setBackgroundColor(0xFFE5E5E5); // selected

			}
		});

		tabHost.setCurrentTab(0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
