package com.guidebook;

import java.util.ArrayList;

import com.guidebook.db.GuidebookDbAdapter;
import com.guidebook.locationpack.LocationPackActivity;

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

public class GuidebookLocationPacksTabActivity extends Activity {

	private GuidebookDbAdapter mDbAdapter;

	private static ArrayList<Integer> lPackID;
	private static ArrayList<String> lPackName;
	private static ArrayList<String> lPackNote;

	public static class EfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lPackID.size();
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
						R.layout.location_pack_list_item, null);
				holder = new ViewHolder();
				holder.textID = (TextView) convertView
						.findViewById(R.id.LocationPackID);
				holder.textName = (TextView) convertView
						.findViewById(R.id.LocationPackName);
				holder.textNote = (TextView) convertView
						.findViewById(R.id.LocationPackNote);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textID.setText(lPackID.get(position).toString());
			holder.textName.setText(lPackName.get(position));
			holder.textNote.setText(lPackNote.get(position));

			convertView.setClickable(true);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(mInflater.getContext(),
							LocationPackActivity.class);
					i.putExtra("locationPackID",
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
		setContentView(R.layout.location_pack_list);

		mDbAdapter = new GuidebookDbAdapter(this);
		mDbAdapter.open();

		fillData();

		ListView listView = (ListView) findViewById(R.id.LocationPackList);
		listView.setAdapter(new EfficientAdapter(this));

		mDbAdapter.close();
	}

	private void fillData() {

		Cursor packCursor = null;

		packCursor = mDbAdapter.fetchNearestLocationPacks();
		startManagingCursor(packCursor);

		lPackID = new ArrayList<Integer>();
		lPackName = new ArrayList<String>();
		lPackNote = new ArrayList<String>();

		if (packCursor.moveToFirst()) {
			int locationPackIDColumn = packCursor
					.getColumnIndex("iLocationPackID");
			int locationPackNameColumn = packCursor
					.getColumnIndex("sLocationPackName");
			int locationPackNoteColumn = packCursor
					.getColumnIndex("sLocationPackNote");

			do {
				lPackID.add(packCursor.getInt(locationPackIDColumn));
				lPackName.add(packCursor.getString(locationPackNameColumn));
				lPackNote.add(packCursor.getString(locationPackNoteColumn));
			} while (packCursor.moveToNext());
		}

		packCursor.close();

	}

}
