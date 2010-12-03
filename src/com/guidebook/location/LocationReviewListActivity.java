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
import android.widget.RatingBar;
import android.widget.TextView;

import com.guidebook.R;
import com.guidebook.db.GuidebookDbAdapter;

public class LocationReviewListActivity extends Activity {

	private GuidebookDbAdapter mDbAdapter;
	private String mLocationID;

	private static ArrayList<Integer> reviewID;
	private static ArrayList<String> reviewText;
	private static ArrayList<String> reviewUser;
	private static ArrayList<Integer> reviewRating;

	public static class EfficientAdapter extends BaseAdapter {

		public LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return reviewID.size();
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

			ViewHolder holder;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.location_review_list_item, null);

				holder = new ViewHolder();

				holder.reviewID = (TextView) convertView
						.findViewById(R.id.LocationReviewRowID);
				holder.reviewText = (TextView) convertView
						.findViewById(R.id.LocationReviewText);
				holder.reviewUsername = (TextView) convertView
						.findViewById(R.id.LocationReviewUser);
				holder.reviewRating = (RatingBar) convertView
						.findViewById(R.id.LocationReviewRating);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.reviewID.setText(reviewID.get(position).toString());
			holder.reviewText.setText(reviewText.get(position));
			holder.reviewUsername.setText(reviewUser.get(position));
			holder.reviewRating.setRating(reviewRating.get(position));

			convertView.setClickable(true);

			return convertView;
		}

		static class ViewHolder {
			TextView reviewID;
			TextView reviewText;
			TextView reviewUsername;
			RatingBar reviewRating;
		}

	}

	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_review_list);

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

		ListView listView = (ListView) findViewById(R.id.LocationReviewList);
		listView.setAdapter(new EfficientAdapter(this));

		mDbAdapter.close();

	}

	private void fillData() {
		// TODO Auto-generated method stub

		Cursor reviewCursor = null;

		reviewCursor = mDbAdapter.fetchLocationReviews(Integer
				.parseInt(mLocationID));
		startManagingCursor(reviewCursor);

		reviewID = new ArrayList<Integer>();
		reviewText = new ArrayList<String>();
		reviewUser = new ArrayList<String>();
		reviewRating = new ArrayList<Integer>();

		if (reviewCursor.moveToFirst()) {
			int reviewIDColumn = reviewCursor.getColumnIndex("iReviewID");
			int reviewTextColumn = reviewCursor.getColumnIndex("sReview");
			int reviewUserColumn = reviewCursor.getColumnIndex("sUsername");
			int reviewRatingColumn = reviewCursor
					.getColumnIndex("iReviewRating");

			do {
				reviewID.add(reviewCursor.getInt(reviewIDColumn));
				reviewText.add(reviewCursor.getString(reviewTextColumn));
				reviewUser.add(reviewCursor.getString(reviewUserColumn));
				reviewRating.add(reviewCursor.getInt(reviewRatingColumn));
			} while (reviewCursor.moveToNext());
		}

		reviewCursor.close();
	}

}
