package com.guidebook.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GuidebookDbAdapter {

	private static final String TAG = "NotesDbAdapter";

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 2;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			// db.execSQL(DATABASE_CREATE);
			createTables(db);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}

		private void createTables(SQLiteDatabase db) {

			String createUser = "CREATE TABLE tblUser "
					+ "(iUserID INT NOT NULL AUTO_INCREMENT, "
					+ "sUsername VARCHAR(50) NOT NULL, "
					+ "sName VARCHAR(50), " + "PRIMARY KEY (iUserID))";

			String createLocation = "CREATE TABLE tblLocation "
					+ "(biLocationID BIGINT NOT NULL AUTO_INCREMENT, "
					+ "dLatitude DOUBLE NOT NULL, "
					+ "dLongitude DOUBLE NOT NULL, "
					+ "iUserID INT, "
					+ "sLocationName VARCHAR(50), "
					+ "sLocationType VARCHAR(50), "
					+ "sLocationNote TEXT, "
					+ "PRIMARY KEY (biLocationID), "
					+ "FOREIGN KEY (iUserID) REFERENCES tblUser(iUserID) ON DELETE CASCADE)";

			String createLocationAddress = "CREATE TABLE tblLocationAddress (biLocationID BIGINT NOT NULL, "
					+ "sAddressLine1 VARCHAR(50), "
					+ "sAddressLine2 VARCHAR(50), "
					+ "sAddressLine3 VARCHAR(50), "
					+ "sCity VARCHAR(50), "
					+ "sState VARCHAR(50), "
					+ "iPostalCode INT, "
					+ "sCountry VARCHAR(50), "
					+ "PRIMARY KEY (biLocationID), "
					+ "FOREIGN KEY (biLocationID) REFERENCES tblLocation(biLocationID) ON DELETE CASCADE)";

			String createLocationVoiceNote = "CREATE TABLE tblLocationVoiceNote "
					+ "(iVoiceID INT NOT NULL AUTO_INCREMENT, "
					+ "biLocationID BIGINT NOT NULL, "
					+ "iUserID INT, "
					+ "sVoiceURL TEXT NOT NULL, "
					+ "iVoiceRating INT, "
					+ "iVoiceSpamCounter INT DEFAULT 0, "
					+ "PRIMARY KEY (iVoiceID), "
					+ "FOREIGN KEY (biLocationID) REFERENCES tblLocation(biLocationID) ON DELETE CASCADE, "
					+ "FOREIGN KEY (iUserID) REFERENCES tblUser(iUserID) ON DELETE CASCADE)";

			String createLocationReview = "CREATE TABLE tblLocationReview "
					+ "(iReviewID INT NOT NULL AUTO_INCREMENT,"
					+ "biLocationID BIGINT NOT NULL,"
					+ "iUserID INT,"
					+ "sReview TEXT NOT NULL,"
					+ "iReviewRating INT,"
					+ "iReviewSpamCounter INT DEFAULT 0,"
					+ "PRIMARY KEY (iReviewID),"
					+ "FOREIGN KEY (biLocationID) REFERENCES tblLocation(biLocationID) ON DELETE CASCADE,"
					+ "FOREIGN KEY (iUserID) REFERENCES tblUser(iUserID) ON DELETE CASCADE)";

			String createLocationPack = "CREATE TABLE tblLocationPack "
					+ "(iLocationPackID INT NOT NULL AUTO_INCREMENT, "
					+ "biLocationID BIGINT NOT NULL, "
					+ "PRIMARY KEY (iLocationPackID, biLocationID), "
					+ "FOREIGN KEY (biLocationID) REFERENCES tblLocation(biLocationID) ON DELETE CASCADE)";

			String createLocationPackInfo = "CREATE TABLE tblLocationPackInfo "
					+ "(iLocationPackID INT NOT NULL, "
					+ "sLocationPackName VARCHAR(50), "
					+ "sLocationPackType VARCHAR(50), "
					+ "sLocationPackNote TEXT, "
					+ "sLocationPackAddress1 VARCHAR(50), "
					+ "sLocationPackAddress2 VARCHAR(50), "
					+ "sLocationPackCity VARCHAR(50), "
					+ "sLocationPackState VARCHAR(50), "
					+ "sLocationPackCountry VARCHAR(50), "
					+ "PRIMARY KEY (iLocationPackID), "
					+ "FOREIGN KEY (iLocationPackID) REFERENCES tblLocationPack(iLocationPackID) ON DELETE CASCADE)";

			String createGuidedTour = "CREATE TABLE tblGuidedTour "
					+ "(iGuidedTourID INT NOT NULL AUTO_INCREMENT,"
					+ "iLocationPackID INT NOT NULL,"
					+ "PRIMARY KEY (iGuidedTourID, iLocationPackID),"
					+ "FOREIGN KEY (iLocationPackID) REFERENCES tblLocationPack(iLocationPackID) ON DELETE CASCADE)";

			String createGuidedTourLocationInfo = "CREATE TABLE tblGuidedTourLocationInfo "
					+ "(iGuidedTourID INT NOT NULL, "
					+ "biLocationID BIGINT NOT NULL, "
					+ "sLocationTourAudioURL TEXT, "
					+ "sLocationInfo TEXT,"
					+ "PRIMARY KEY (iGuidedTourID),"
					+ "FOREIGN KEY	(iGuidedTourID) REFERENCES tblGuidedTour(iGuidedTourID) ON DELETE CASCADE,"
					+ "FOREIGN KEY (biLocationID) REFERENCES tblLocation(biLocationID) ON DELETE CASCADE)";

			db.execSQL(createUser);
			db.execSQL(createLocation);
			db.execSQL(createLocationAddress);
			db.execSQL(createLocationVoiceNote);
			db.execSQL(createLocationReview);
			db.execSQL(createLocationPack);
			db.execSQL(createLocationPackInfo);
			db.execSQL(createGuidedTour);
			db.execSQL(createGuidedTourLocationInfo);
		}
	}

	public GuidebookDbAdapter(Context mCtx) {
		this.mCtx = mCtx;
	}

}
