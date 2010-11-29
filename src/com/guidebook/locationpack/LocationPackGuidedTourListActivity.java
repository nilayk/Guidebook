package com.guidebook.locationpack;

import com.guidebook.R;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationPackGuidedTourListActivity extends ListActivity {
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_pack_guidedtours_list);

		fillData();

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
	}

	private void fillData() {
		// TODO Auto-generated method stub

		String[] location = getResources().getStringArray(
				R.array.locations_array);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, location);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Toast.makeText(getApplicationContext(), ((TextView) v).getText(),
				Toast.LENGTH_LONG).show();
	}
}
