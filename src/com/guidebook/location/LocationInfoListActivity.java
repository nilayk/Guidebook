package com.guidebook.location;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.guidebook.R;
import com.guidebook.db.GuidebookDbAdapter;

public class LocationInfoListActivity extends Activity {

	ArrayAdapter<String> adapter;

	private GuidebookDbAdapter mDbAdapter;

	private static ArrayList<String> lInfo;

	private String mLocationID;

	public static class EfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lInfo.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.location_info_list_item, null);
				holder = new ViewHolder();
				holder.textInfo = (TextView) convertView
						.findViewById(R.id.LocationInfoText);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textInfo.setText(lInfo.get(position));

			convertView.setClickable(true);

			return convertView;
		}

		static class ViewHolder {
			TextView textInfo;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_info_list);

		mLocationID = (savedInstanceState == null) ? null
				: (String) savedInstanceState.getSerializable("locationID");
		if (mLocationID == null) {
			Bundle extras = getIntent().getExtras();
			mLocationID = extras != null ? extras.getString("locationID")
					: null;
		}

		mDbAdapter = new GuidebookDbAdapter(this);
		mDbAdapter.open();

		fillData();

		ListView listView = (ListView) findViewById(R.id.LocationInfoList);
		listView.setAdapter(new EfficientAdapter(this));

		mDbAdapter.close();
	}

	private void fillData() {
		// TODO Auto-generated method stub
		Cursor locationsCursor = null;

		locationsCursor = mDbAdapter.fetchLocationInfo(Integer
				.parseInt(mLocationID));
		startManagingCursor(locationsCursor);

		lInfo = new ArrayList<String>();

		if (locationsCursor.moveToFirst()) {
			int locationInfoColumn = locationsCursor
					.getColumnIndex("sLocationInfo");

			do {
				lInfo.add(locationsCursor.getString(locationInfoColumn));
			} while (locationsCursor.moveToNext());
		}

		locationsCursor.close();

	}

}
