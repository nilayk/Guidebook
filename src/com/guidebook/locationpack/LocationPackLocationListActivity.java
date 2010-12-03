package com.guidebook.locationpack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.guidebook.R;
import com.guidebook.db.GuidebookDbAdapter;
import com.guidebook.location.LocationActivity;

public class LocationPackLocationListActivity extends Activity {

	private GuidebookDbAdapter mDbAdapter;

	private static ArrayList<Integer> lID;
	private static ArrayList<String> lName;
	private static ArrayList<String> lNote;

	private String mLocationPackID;

	public static class EfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lName.size();
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
						R.layout.location_pack_locations_list_item, null);
				holder = new ViewHolder();
				holder.textID = (TextView) convertView
						.findViewById(R.id.PackLocationID);
				holder.textName = (TextView) convertView
						.findViewById(R.id.PackLocationName);
				holder.textNote = (TextView) convertView
						.findViewById(R.id.PackLocationNote);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textID.setText(lID.get(position).toString());
			holder.textName.setText(lName.get(position));
			holder.textNote.setText(lNote.get(position));

			convertView.setClickable(true);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(mInflater.getContext(),
							LocationActivity.class);
					i.putExtra("locationID",
							((ViewHolder) v.getTag()).textID.getText());
					mInflater.getContext().startActivity(i);
				}
			});

			return convertView;
		}

		static class ViewHolder {
			TextView textID;
			TextView textName;
			TextView textNote;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_pack_locations_list);

		mLocationPackID = (savedInstanceState == null) ? null
				: (String) savedInstanceState.getSerializable("locationPackID");
		if (mLocationPackID == null) {
			Bundle extras = getIntent().getExtras();
			mLocationPackID = extras != null ? extras
					.getString("locationPackID") : null;
		}

		mDbAdapter = new GuidebookDbAdapter(this);
		mDbAdapter.open();

		fillData();

		ListView listView = (ListView) findViewById(R.id.LocationPackLocationsList);
		listView.setAdapter(new EfficientAdapter(this));

		mDbAdapter.close();
	}

	private void fillData() {
		// TODO Auto-generated method stub
		Cursor locationsCursor = null;

		locationsCursor = mDbAdapter.fetchLocationPackLocations(Integer
				.parseInt(mLocationPackID));
		startManagingCursor(locationsCursor);

		lID = new ArrayList<Integer>();
		lName = new ArrayList<String>();
		lNote = new ArrayList<String>();

		if (locationsCursor.moveToFirst()) {
			int locationIDColumn = locationsCursor
					.getColumnIndex("iLocationID");
			int locationNameColumn = locationsCursor
					.getColumnIndex("sLocationName");
			int locationNoteColumn = locationsCursor
					.getColumnIndex("sLocationNote");

			do {
				lID.add(locationsCursor.getInt(locationIDColumn));
				lName.add(locationsCursor.getString(locationNameColumn));
				lNote.add(locationsCursor.getString(locationNoteColumn));
			} while (locationsCursor.moveToNext());
		}

		locationsCursor.close();
	}
}
