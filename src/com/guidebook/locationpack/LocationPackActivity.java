package com.guidebook.locationpack;

import com.guidebook.R;
import com.guidebook.db.GuidebookDbAdapter;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

public class LocationPackActivity extends TabActivity {

	private String mLocationPackID;
	private TextView mLocationPackName;
	private TextView mLocationPackAddress;

	private GuidebookDbAdapter mDbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_pack_activity);

		mDbAdapter = new GuidebookDbAdapter(this);
		mDbAdapter.open();

		mLocationPackID = (savedInstanceState == null) ? null
				: (String) savedInstanceState.getSerializable("locationPackID");
		if (mLocationPackID == null) {
			Bundle extras = getIntent().getExtras();
			mLocationPackID = extras != null ? extras
					.getString("locationPackID") : null;
		}

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this,
				LocationPackLocationListActivity.class);
		intent.putExtra("locationPackID", mLocationPackID);
		spec = tabHost
				.newTabSpec("locations")
				.setIndicator(
						"",
						res.getDrawable(R.drawable.ic_tab_locationpack_locations))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this,
				LocationPackGuidedTourListActivity.class);
		intent.putExtra("locationPackID", mLocationPackID);
		spec = tabHost
				.newTabSpec("guidedtours")
				.setIndicator(
						"",
						res.getDrawable(R.drawable.ic_tab_locationpack_guidedtours))
				.setContent(intent);
		intent.putExtra("locationPackID", mLocationPackID);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

		Cursor lPackCursor = null;

		lPackCursor = mDbAdapter.fetchLocationPackDetails(Integer
				.parseInt(mLocationPackID));

		String lPackName = "";
		String lPackAddress = "";
		String lPackCity = "";

		if (lPackCursor.moveToFirst()) {
			int lPackNameColumn = lPackCursor
					.getColumnIndex("sLocationPackName");
			int lPackAddress1Column = lPackCursor
					.getColumnIndex("sLocationPackAddress1");
			int lPackCityColumn = lPackCursor
					.getColumnIndex("sLocationPackCity");

			lPackName = lPackCursor.getString(lPackNameColumn);
			lPackAddress = lPackCursor.getString(lPackAddress1Column);
			lPackCity = lPackCursor.getString(lPackCityColumn);

			mLocationPackName = (TextView) findViewById(R.id.PackName);
			mLocationPackName.setText(lPackName);

			mLocationPackAddress = (TextView) findViewById(R.id.PackAddress);
			mLocationPackAddress.setText(lPackAddress + ", " + lPackCity);

		}

		lPackCursor.close();
		mDbAdapter.close();
	}
}
