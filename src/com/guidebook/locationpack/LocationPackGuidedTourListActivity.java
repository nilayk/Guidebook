package com.guidebook.locationpack;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.guidebook.R;
import com.guidebook.db.GuidebookDbAdapter;

public class LocationPackGuidedTourListActivity extends Activity {

	private GuidebookDbAdapter mDbAdapter;

	private static ArrayList<Integer> gID;
	private static ArrayList<String> gName;
	private static ArrayList<String> gNote;

	private String mLocationPackID;

	public static class EfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return gName.size();
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
						R.layout.location_pack_guidedtours_list_item, null);
				holder = new ViewHolder();
				holder.textID = (TextView) convertView
						.findViewById(R.id.GuidedTourID);
				holder.textName = (TextView) convertView
						.findViewById(R.id.GuidedTourName);
				holder.textNote = (TextView) convertView
						.findViewById(R.id.GuidedTourInfo);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textID.setText(gID.get(position).toString());
			holder.textName.setText(gName.get(position));
			holder.textNote.setText(gNote.get(position));

			convertView.setClickable(true);

			// convertView.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent i = new Intent(mInflater.getContext(),
			// LocationActivity.class);
			// i.putExtra("locationID",
			// ((ViewHolder) v.getTag()).textID.getText());
			// mInflater.getContext().startActivity(i);
			// }
			// });

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
		setContentView(R.layout.location_pack_guidedtours_list);

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

		ListView listView = (ListView) findViewById(R.id.GuidedTourList);
		listView.setAdapter(new EfficientAdapter(this));

		mDbAdapter.close();
	}

	private void fillData() {
		// TODO Auto-generated method stub
		Cursor gCursor = null;

		gCursor = mDbAdapter.fetchLocationPackGuidedTours(Integer
				.parseInt(mLocationPackID));
		startManagingCursor(gCursor);

		gID = new ArrayList<Integer>();
		gName = new ArrayList<String>();
		gNote = new ArrayList<String>();

		if (gCursor.moveToFirst()) {
			int locationIDColumn = gCursor.getColumnIndex("iGuidedTourID");
			int locationNameColumn = gCursor.getColumnIndex("sLocationInfo");
			int locationNoteColumn = gCursor.getColumnIndex("sLocationTourAudioURL");

			do {
				gID.add(gCursor.getInt(locationIDColumn));
				gName.add(gCursor.getString(locationNameColumn));
				gNote.add(gCursor.getString(locationNoteColumn));
			} while (gCursor.moveToNext());
		}

		gCursor.close();
	}
}
