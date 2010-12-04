package com.guidebook.map;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener {

	// @Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {
			try {
				LocationMapViewActivity.latitude = location.getLatitude();
				LocationMapViewActivity.longitude = location.getLongitude();
				//LocationMapViewActivity.myL
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
