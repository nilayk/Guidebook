package com.guidebook.location;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.guidebook.R;
import com.guidebook.db.GuidebookDbAdapter;

public class LocationActivity extends TabActivity {

	private String mLocationID;
	private TextView mLocationName;
	private TextView mLocationAddress;

	private GuidebookDbAdapter mDbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_activity);

		mDbAdapter = new GuidebookDbAdapter(this);
		mDbAdapter.open();

		mLocationID = (savedInstanceState == null) ? null
				: (String) savedInstanceState.getSerializable("locationID");
		if (mLocationID == null) {
			Bundle extras = getIntent().getExtras();
			mLocationID = extras != null ? extras.getString("locationID")
					: null;
		}

		Resources res = getResources();
		final TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, LocationInfoListActivity.class);
		intent.putExtra("locationID", mLocationID);
		spec = tabHost
				.newTabSpec("info")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_tab_location_info))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, LocationReviewListActivity.class);
		intent.putExtra("locationID", mLocationID);
		spec = tabHost
				.newTabSpec("reviews")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_tab_location_reviews))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, LocationVoiceListActivity.class);
		intent.putExtra("locationID", mLocationID);
		spec = tabHost
				.newTabSpec("voice")
				.setIndicator("",
						res.getDrawable(R.drawable.ic_tab_location_voice))
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

		Cursor lCursor = null;

		lCursor = mDbAdapter
				.fetchLocationDetails(Integer.parseInt(mLocationID));

		String locationName = "";
		String locationAddress1 = "";

		if (lCursor.moveToFirst()) {
			int locationNameColumn = lCursor.getColumnIndex("sLocationName");
			int locationAddress1Column = lCursor
					.getColumnIndex("sAddressLine1");

			locationName = lCursor.getString(locationNameColumn);
			locationAddress1 = lCursor.getString(locationAddress1Column);

			mLocationName = (TextView) findViewById(R.id.firstLine);
			mLocationName.setText(locationName);

			mLocationAddress = (TextView) findViewById(R.id.secondLine);
			mLocationAddress.setText(locationAddress1);

		}
		lCursor.close();
		mDbAdapter.close();
	}

	public static void setTabColor(TabHost tabhost) {

	}

}
