package com.guidebook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.guidebook.db.GuidebookDbAdapter;
import com.guidebook.location.LocationActivity;

public class GuidebookLocationsTabActivity extends Activity {

	private GuidebookDbAdapter mDbAdapter;

	private static ArrayList<Integer> lID;
	private static ArrayList<String> lName;
	private static ArrayList<String> lNote;

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
				convertView = mInflater.inflate(R.layout.location_list_item,
						null);
				holder = new ViewHolder();
				holder.textID = (TextView) convertView.findViewById(R.id.RowID);
				holder.textName = (TextView) convertView
						.findViewById(R.id.TextView01);
				holder.textNote = (TextView) convertView
						.findViewById(R.id.TextView02);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.textID.setText(lID.get(position).toString());
			holder.textName.setText(lName.get(position));
			holder.textNote.setText(lNote.get(position));

			convertView.setClickable(true);
			// convertView.setOnTouchListener(new OnTouchListener() {
			//
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// // TODO Auto-generated method stub
			//
			// v.setBackgroundColor(0xFFFFFFFF);
			//
			// return false;
			// }
			// });

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

			// convertView.setOnKeyListener(new OnKeyListener() {
			//
			// @Override
			// public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
			// // TODO Auto-generated method stub
			// Toast.makeText(mInflater.getContext(), "test",
			// Toast.LENGTH_SHORT).show();
			// return true;
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

	private TextWatcher filterTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			// adapter.getFilter().filter(s);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_list);

		mDbAdapter = new GuidebookDbAdapter(this);
		mDbAdapter.open();

		// filterText = (EditText) findViewById(R.id.search_box);
		// filterText.addTextChangedListener(filterTextWatcher);

		fillData();

		ListView listView = (ListView) findViewById(R.id.ListView01);
		listView.setAdapter(new EfficientAdapter(this));
		/*
		 * listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		 * {
		 * 
		 * @Override public void onItemClick(AdapterView parentView, View
		 * childView, int position, long id) { // TODO Auto-generated method
		 * stub Toast.makeText(Guidebook.this, "test", Toast.LENGTH_SHORT)
		 * .show(); } });
		 */

		mDbAdapter.close();

	}

	private void fillData() {

		Cursor locationsCursor = null;

		locationsCursor = mDbAdapter.fetchNearestLocations();
		startManagingCursor(locationsCursor);

		lID = new ArrayList<Integer>();
		lName = new ArrayList<String>();
		lNote = new ArrayList<String>();

		if (locationsCursor.moveToFirst()) {
			int locationIDColumn = locationsCursor
					.getColumnIndex("iLocationID");
			int locationNameColumn = locationsCursor.getColumnIndex("sLocationName");
			int locationNoteColumn = locationsCursor.getColumnIndex("sLocationNote");

			do {
				lID.add(locationsCursor.getInt(locationIDColumn));
				lName.add(locationsCursor.getString(locationNameColumn));
				lNote.add(locationsCursor.getString(locationNoteColumn));
			} while (locationsCursor.moveToNext());
		}

		// adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, lData);

		// setListAdapter(adapter);

		// adapter.getFilter().filter(s);
		locationsCursor.close();

	}

}
