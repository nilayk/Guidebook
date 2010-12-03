package com.guidebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (waited < 5000) {
						sleep(100);
						waited += 100;
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					finish();
					Intent i = new Intent();
					i.setClassName("com.guidebook",
							"com.guidebook.GuidebookActivity");
							//"com.guidebook.location.LocationActivity");
							//"com.guidebook.locationpack.LocationPackActivity");
					startActivity(i);
				}
			}
		};
		splashThread.start();
	}

}
