package com.guidebook.db;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.guidebook.R;

public class GuidebookDbAdapter {

	private static final String TAG = "GuidebookDbAdapter";

	private static final String DATABASE_NAME = "data";
	private static final int DATABASE_VERSION = 2;

	private static InputStream myInputFile;
	private static Context mCtx;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

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
					+ "(iUserID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "sUsername VARCHAR(50) NOT NULL, "
					+ "sName VARCHAR(50));";

			String createLocation = "CREATE TABLE tblLocation "
					+ "(iLocationID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "dLatitude DOUBLE NOT NULL, "
					+ "dLongitude DOUBLE NOT NULL, "
					+ "iUserID INTEGER, "
					+ "sLocationName VARCHAR(50), "
					+ "sLocationType VARCHAR(50), "
					+ "sLocationNote TEXT, "
					+ "sLocationImageURL TEXT, "
					+ "sLocationInfo TEXT, "
					+ "FOREIGN KEY (iUserID) REFERENCES tblUser(iUserID) ON DELETE CASCADE);";

			String createLocationAddress = "CREATE TABLE tblLocationAddress (iLocationID INTEGER NOT NULL, "
					+ "sAddressLine1 VARCHAR(50), "
					+ "sAddressLine2 VARCHAR(50), "
					+ "sAddressLine3 VARCHAR(50), "
					+ "sCity VARCHAR(50), "
					+ "sState VARCHAR(50), "
					+ "iPostalCode INTEGER, "
					+ "sCountry VARCHAR(50), "
					+ "PRIMARY KEY (iLocationID), "
					+ "FOREIGN KEY (iLocationID) REFERENCES tblLocation(iLocationID) ON DELETE CASCADE);";

			String createLocationVoiceNote = "CREATE TABLE tblLocationVoiceNote "
					+ "(iVoiceID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "iLocationID INTEGER NOT NULL, "
					+ "iUserID INTEGER, "
					+ "sTitle VARCHAR(50), "
					+ "sVoiceURL TEXT NOT NULL, "
					+ "iVoiceRating INTEGER, "
					+ "iVoiceSpamCounter INTEGER DEFAULT 0, "
					+ "FOREIGN KEY (iLocationID) REFERENCES tblLocation(iLocationID) ON DELETE CASCADE, "
					+ "FOREIGN KEY (iUserID) REFERENCES tblUser(iUserID) ON DELETE CASCADE);";

			String createLocationReview = "CREATE TABLE tblLocationReview "
					+ "(iReviewID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "iLocationID INTEGER NOT NULL,"
					+ "iUserID INTEGER,"
					+ "sReview TEXT NOT NULL,"
					+ "iReviewRating INTEGER,"
					+ "iReviewSpamCounter INTEGER DEFAULT 0,"
					+ "FOREIGN KEY (iLocationID) REFERENCES tblLocation(iLocationID) ON DELETE CASCADE,"
					+ "FOREIGN KEY (iUserID) REFERENCES tblUser(iUserID) ON DELETE CASCADE)";

			String createLocationPack = "CREATE TABLE tblLocationPack "
					+ "(iLocationPackID INTEGER NOT NULL , "
					+ "iLocationID INTEGER NOT NULL, "
					+ "PRIMARY KEY (iLocationPackID, iLocationID), "
					+ "FOREIGN KEY (iLocationID) REFERENCES tblLocation(iLocationID) ON DELETE CASCADE)";

			String createLocationPackInfo = "CREATE TABLE tblLocationPackInfo "
					+ "(iLocationPackID INTEGER NOT NULL, "
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
					+ "(iGuidedTourID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "iLocationPackID INTEGER NOT NULL,"
					+ "FOREIGN KEY (iLocationPackID) REFERENCES tblLocationPack(iLocationPackID) ON DELETE CASCADE)";

			String createGuidedTourLocationInfo = "CREATE TABLE tblGuidedTourLocationInfo "
					+ "(iGuidedTourID INTEGER NOT NULL, "
					+ "iLocationID INTEGER NOT NULL, "
					+ "sLocationTourAudioURL TEXT, "
					+ "sLocationInfo TEXT,"
					+ "PRIMARY KEY (iGuidedTourID, iLocationID),"
					+ "FOREIGN KEY	(iGuidedTourID) REFERENCES tblGuidedTour(iGuidedTourID) ON DELETE CASCADE,"
					+ "FOREIGN KEY (iLocationID) REFERENCES tblLocation(iLocationID) ON DELETE CASCADE)";

			db.execSQL(createUser);
			db.execSQL(createLocation);
			db.execSQL(createLocationAddress);
			db.execSQL(createLocationVoiceNote);
			db.execSQL(createLocationReview);
			db.execSQL(createLocationPack);
			db.execSQL(createLocationPackInfo);
			db.execSQL(createGuidedTour);
			db.execSQL(createGuidedTourLocationInfo);

			byte[] reader;
			String str = new String("");
			myInputFile = mCtx.getResources()
					.openRawResource(R.raw.create_user);
			try {
				reader = new byte[myInputFile.available()];
				while (myInputFile.read(reader) != -1) {
				}
				str = new String(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] s = str.split("\n");
			for (int i = 0; i < s.length; i++) {
				db.execSQL("insert into tblUser (sUsername, sName) values ("
						+ s[i] + ")");
			}

			myInputFile = mCtx.getResources().openRawResource(
					R.raw.create_location);
			try {
				reader = new byte[myInputFile.available()];
				while (myInputFile.read(reader) != -1) {
				}
				str = new String(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s = str.split("\n");
			for (int i = 0; i < s.length; i++) {
				db.execSQL("insert into tblLocation ("
						+ "dLatitude, dLongitude, iUserID, "
						+ "sLocationName, sLocationType, sLocationNote, "
						+ "sLocationImageURL, sLocationInfo) values (" + s[i]
						+ ")");
			}

			myInputFile = mCtx.getResources().openRawResource(
					R.raw.create_address);
			try {
				reader = new byte[myInputFile.available()];
				while (myInputFile.read(reader) != -1) {
				}
				str = new String(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s = str.split("\n");
			for (int i = 0; i < s.length; i++) {
				db.execSQL("insert into tblLocationAddress(iLocationId,sAddressLine1) values ("
						+ s[i] + ")");
			}

			myInputFile = mCtx.getResources().openRawResource(
					R.raw.create_voicenote);
			try {
				reader = new byte[myInputFile.available()];
				while (myInputFile.read(reader) != -1) {
				}
				str = new String(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s = str.split("\n");
			for (int i = 0; i < s.length; i++) {
				db.execSQL("insert into tblLocationVoiceNote (iLocationID,iUserID,sTitle,sVoiceURL,iVoiceRating,iVoiceSpamCounter) values ("
						+ s[i] + ")");
			}

			myInputFile = mCtx.getResources().openRawResource(
					R.raw.create_locationreview);
			try {
				reader = new byte[myInputFile.available()];
				while (myInputFile.read(reader) != -1) {
				}
				str = new String(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s = str.split("\n");
			for (int i = 0; i < s.length; i++) {
				db.execSQL("insert into tblLocationReview (iLocationID,iUserID,sReview,iReviewRating,iReviewSpamCounter) values ("
						+ s[i] + ")");
			}

			myInputFile = mCtx.getResources().openRawResource(
					R.raw.create_locationpack);
			try {
				reader = new byte[myInputFile.available()];
				while (myInputFile.read(reader) != -1) {
				}
				str = new String(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s = str.split("\n");
			for (int i = 0; i < s.length; i++) {
				db.execSQL("insert into tblLocationPack (iLocationPackID,iLocationID) values ("
						+ s[i] + ")");
			}

			myInputFile = mCtx.getResources().openRawResource(
					R.raw.create_locationpackinfo);
			try {
				reader = new byte[myInputFile.available()];
				while (myInputFile.read(reader) != -1) {
				}
				str = new String(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s = str.split("\n");
			for (int i = 0; i < s.length; i++) {
				db.execSQL("insert into tblLocationPackInfo (iLocationPackID,sLocationPackName,sLocationPackType,sLocationPackNote,sLocationPackAddress1,sLocationPackAddress2,sLocationPackCity,sLocationPackState,sLocationPackCountry) values ("
						+ s[i] + ")");
			}

			myInputFile = mCtx.getResources().openRawResource(
					R.raw.create_guidedtour);
			try {
				reader = new byte[myInputFile.available()];
				while (myInputFile.read(reader) != -1) {
				}
				str = new String(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s = str.split("\n");
			for (int i = 0; i < s.length; i++) {
				db.execSQL("insert into tblGuidedTour (iLocationPackID) values ("
						+ s[i] + ")");
			}

			myInputFile = mCtx.getResources().openRawResource(
					R.raw.create_guidedtourlocationinfo);
			try {
				reader = new byte[myInputFile.available()];
				while (myInputFile.read(reader) != -1) {
				}
				str = new String(reader);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s = str.split("\n");
			for (int i = 0; i < s.length; i++) {
				db.execSQL("insert into tblGuidedTourLocationInfo (iGuidedTourID,iLocationID,sLocationTourAudioURL,sLocationInfo) values ("
						+ s[i] + ")");
			}

		}
	}

	public GuidebookDbAdapter(Context mCtx) {
		GuidebookDbAdapter.mCtx = mCtx;
	}

	public GuidebookDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public Cursor fetchNearestLocations() {
		String LOCATION_ID = "iLocationID";
		String LATITUDE = "dLatitude";
		String LONGITUDE = "dLongitude";
		String LOCATION_NAME = "sLocationName";
		String LOCATION_NOTE = "sLocationNote";

		return mDb.query("tblLocation", new String[] { LOCATION_ID, LATITUDE,
				LONGITUDE, LOCATION_NAME, LOCATION_NOTE }, null, null, null,
				null, null);
	}

	public Cursor fetchNearestLocationPacks() {

		String LOCATION_PACK_ID = "iLocationPackID";
		String NAME = "sLocationPackName";
		String NOTE = "sLocationPackNote";

		return mDb.query("tblLocationPackInfo", new String[] {
				LOCATION_PACK_ID, NAME, NOTE }, null, null, null, null, null);
	}

	public Cursor fetchLocationDetails(int locationID) throws SQLException {

		String query = "SELECT t1.dLatitude, t1.dLongitude, "
				+ "t1.sLocationName, t1.sLocationNote, t2.sAddressLine1, "
				+ "t2.sCity, t2.sState, t2.iPostalCode FROM tblLocation AS t1, "
				+ "tblLocationAddress AS t2 WHERE t1.iLocationID = ? "
				+ "AND t2.iLocationID = ?";

		Cursor mCursor = mDb.rawQuery(
				query,
				new String[] { String.valueOf(locationID),
						String.valueOf(locationID) });

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchLocationPackGuidedTours(int locationPackID)
			throws SQLException {

		String query = "SELECT t2.iGuidedTourID, t2.iLocationID, t2.sLocationTourAudioURL, t2.sLocationInfo FROM tblGuidedTour AS t1, "
				+ "tblGuidedTourLocationInfo AS t2 WHERE t1.iLocationPackID = ? "
				+ "AND t2.iGuidedTourID = t1.iGuidedTourID";

		Cursor mCursor = mDb.rawQuery(query,
				new String[] { String.valueOf(locationPackID) });

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchLocationInfo(int iLocationID) {

		String query = "SELECT sLocationInfo " + "FROM tblLocation "
				+ "WHERE iLocationID = ?";

		Cursor mCursor = mDb.rawQuery(query,
				new String[] { String.valueOf(iLocationID) });

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchLocationReviews(int locationID) throws SQLException {

		String query = "SELECT t1.iReviewID, t1.iLocationID, t1.iUserID, "
				+ "t2.sUsername, t1.sReview, t1.iReviewRating, t1.iReviewSpamCounter "
				+ "FROM  tblLocationReview AS t1, tblUser AS "
				+ "t2 WHERE t1.iLocationID = ? AND t1.iUserID = t2.iUserID";

		Cursor mCursor = mDb.rawQuery(query,
				new String[] { String.valueOf(locationID) });

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchLocationVoice(int locationID) throws SQLException {

		String query = "SELECT t1.iVoiceID, t1.iUserID, "
				+ "t2.sUsername, t2.sName, t1.sTitle, t1.sVoiceURL, t1.iVoiceRating, "
				+ "t1.iVoiceSpamCounter FROM tblLocationVoiceNote "
				+ "AS t1 , tblUser AS t2 WHERE t1.iLocationID = ? "
				+ "AND t1.iUserID = t2.iUserID";

		Cursor mCursor = mDb.rawQuery(query,
				new String[] { String.valueOf(locationID) });

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchLocationPackDetails(int iLocationPackID) {

		String TABLE = "tblLocationPackInfo";
		String PACK_ID = "iLocationPackID";
		String PACK_NAME = "sLocationPackName";
		String PACK_ADDRESS1 = "sLocationPackAddress1";
		String PACK_ADDRESS2 = "sLocationPackCity";

		Cursor mCursor = mDb.query(true, TABLE, new String[] { PACK_NAME,
				PACK_ADDRESS1, PACK_ADDRESS2 },
				PACK_ID + "=" + iLocationPackID, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public Cursor fetchLocationPackLocations(int iLocationPackID)
			throws SQLException {

		String query = "SELECT t2.iLocationID, t2.sLocationName, "
				+ "t2.sLocationNote FROM tblLocationPack AS t1, "
				+ "tblLocation AS t2 WHERE t1.iLocationPackID = ? "
				+ "AND t1.iLocationID = t2.iLocationID";

		Cursor mCursor = mDb.rawQuery(query,
				new String[] { String.valueOf(iLocationPackID) });

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

}
