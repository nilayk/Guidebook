package com.guidebook.map;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.guidebook.R;
import com.guidebook.db.GuidebookDbAdapter;

public class LocationMapViewActivity extends MapActivity {

	private GuidebookDbAdapter mDbAdapter;

	private static ArrayList<Integer> lID;
	private static ArrayList<String> lName;
	private static ArrayList<String> lNote;

	public static LocationManager myLocationManager;
	public static LocationListener myLocationListener;

	public static double latitude;
	public static double longitude;
	
	List<Overlay> mapOverlays;
	MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view_activity);

		getCurrentLocation();

	    mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();

		mDbAdapter = new GuidebookDbAdapter(this);
		mDbAdapter.open();

		Cursor locationsCursor = null;

		locationsCursor = mDbAdapter.fetchNearestLocations();
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

		mDbAdapter.close();

		
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getCurrentLocation();
	}

	public void getCurrentLocation() {
		try {
			myLocationListener = new MyLocationListener();
			myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			myLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
		} catch (Exception ex) {
			Log.w("GPS", "Unable to find GPS coordinates");
			return;
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class MyLocationListener implements LocationListener {

		// @Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if (location != null) {
				try {
					LocationMapViewActivity.latitude = location.getLatitude();
					LocationMapViewActivity.longitude = location.getLongitude();
					
					// My Location
					mapOverlays.clear();
					Drawable myLocationDrawable = LocationMapViewActivity.this.getResources().getDrawable(
							R.drawable.my_location_marker);
					MapItemizedOverlay myLocationItemizedOverlay = new MapItemizedOverlay(
							myLocationDrawable, LocationMapViewActivity.this);
					GeoPoint myLocationPoint = new GeoPoint((int) (LocationMapViewActivity.this.latitude * 1E6),
							(int) (LocationMapViewActivity.this.longitude * 1E6));
					OverlayItem myLocationOverlayItem = new OverlayItem(myLocationPoint,
							"Current Location", "My current location");
					myLocationItemizedOverlay.addOverlay(myLocationOverlayItem);
					mapOverlays.add(myLocationItemizedOverlay);

					MapController myMapController = mapView.getController();
					myMapController.animateTo(myLocationPoint);
					myMapController.setZoom(17);
					
					Log.i("LOCATION", "<lat, long> = <"
							+ LocationMapViewActivity.latitude + ", "
							+ LocationMapViewActivity.longitude + ">");
				} catch (NullPointerException e) {
					Log.i("LOCATION", e.toString());
				}
			}

		}

		// @Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		// @Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		// @Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}
	}
}
