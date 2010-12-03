package com.guidebook.location;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guidebook.R;
import com.guidebook.db.GuidebookDbAdapter;

public class LocationVoiceListActivity extends Activity {

	private GuidebookDbAdapter mDbAdapter;
	private String mLocationID;

	private static ArrayList<Integer> voiceID;
	private static ArrayList<String> voiceTitle;
	private static ArrayList<String> voiceURL;
	private static ArrayList<String> voiceUser;

	public class EfficientAdapter extends BaseAdapter {

		public LayoutInflater mInflater;

		public EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return voiceID.size();
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
						R.layout.location_voices_list_item, null);

				holder = new ViewHolder();

				holder.voiceID = (TextView) convertView
						.findViewById(R.id.LocationVoiceRowID);
				holder.voiceTitle = (TextView) convertView
						.findViewById(R.id.LocationVoiceTitle);
				holder.voiceUser = (TextView) convertView
						.findViewById(R.id.LocationVoiceUser);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.voiceID.setText(voiceID.get(position).toString());
			holder.voiceTitle.setText(voiceTitle.get(position));
			holder.voiceUser.setText(voiceUser.get(position));

			convertView.setClickable(true);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					try {

						Context mContext = mInflater.getContext();
						final Dialog dialog = new Dialog(mContext);
						final MediaPlayer mp = MediaPlayer.create(
								mInflater.getContext(), R.raw.audio);

						dialog.setContentView(R.layout.audio_player);
						dialog.setTitle("Voice Notes");
						dialog.setCancelable(true);

						Button buttonStopPlay = (Button) dialog
								.findViewById(R.id.buttonStopPlay);

						dialog.setOnCancelListener(new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								LocationVoiceListActivity.this
										.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												try {
													mp.stop();
												} catch (Exception ex) {

												}

											}
										});
							}
						});

						buttonStopPlay
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {

										dialog.cancel();
									}
								});

						final ProgressBar pb = (ProgressBar) dialog
								.findViewById(R.id.pbAudio);
						pb.setProgress(0);
						pb.setMax(mp.getDuration());

						dialog.show();

						new Thread(new Runnable() {
							public void run() {

								mp.start();

								while ((mp != null) && (mp.isPlaying())) {
									// Update the progress bar
									LocationVoiceListActivity.this
											.runOnUiThread(new Runnable() {

												@Override
												public void run() {
													if (dialog.isShowing()) {
														pb.setProgress(mp
																.getCurrentPosition());
													}

												}
											});
								}

								dialog.cancel();
								mp.release();
							}
						}).start();

					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception ex) {

					}
				}
			});

			return convertView;
		}

		class ViewHolder {
			TextView voiceID;
			TextView voiceTitle;
			TextView voiceUser;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_voices_list);

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

		ListView listView = (ListView) findViewById(R.id.LocationVoicesList);
		listView.setAdapter(new EfficientAdapter(this));

		mDbAdapter.close();

		// ListView listView = getListView();
		// listView.setTextFilterEnabled(true);
	}

	private void fillData() {
		// TODO Auto-generated method stub

		Cursor voiceCursor = null;

		voiceCursor = mDbAdapter.fetchLocationVoice(Integer
				.parseInt(mLocationID));
		startManagingCursor(voiceCursor);

		voiceID = new ArrayList<Integer>();
		voiceTitle = new ArrayList<String>();
		voiceURL = new ArrayList<String>();
		voiceUser = new ArrayList<String>();

		if (voiceCursor.moveToFirst()) {
			int voiceIDColumn = voiceCursor.getColumnIndex("iVoiceID");
			int voiceTitleColumn = voiceCursor.getColumnIndex("sTitle");
			int voiceURLColumn = voiceCursor.getColumnIndex("sVoiceURL");
			int voiceUserColumn = voiceCursor.getColumnIndex("sUsername");

			do {
				voiceID.add(voiceCursor.getInt(voiceIDColumn));
				voiceTitle.add(voiceCursor.getString(voiceTitleColumn));
				voiceURL.add(voiceCursor.getString(voiceURLColumn));
				voiceUser.add(voiceCursor.getString(voiceUserColumn));
			} while (voiceCursor.moveToNext());
		}

		voiceCursor.close();
	}

}
