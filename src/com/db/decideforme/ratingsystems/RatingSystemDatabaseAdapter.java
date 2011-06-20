package com.db.decideforme.ratingsystems;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.db.decideforme.ratingsystems.RatingSystem.RatingSystemColumns;
import com.decideforme.utils.StringUtils;

public class RatingSystemDatabaseAdapter extends DecisionDatabaseAdapter {
	private static final String TAG = RatingSystemDatabaseAdapter.class.getName();

	public RatingSystemDatabaseAdapter(Context ctx) {
		super(ctx);
	}
	
    public Cursor fetchAllRatingSystems() {
    	Log.d(TAG, " >> fetchAllRatingSystems()");
        Cursor resultOfFetchQuery = mDb.query(RatingSystem.TABLE_NAME, 
        		new String[] {RatingSystemColumns._ID, 
        		RatingSystemColumns.NAME}, 
        		null, null, null, null, null);
        
        Log.d(TAG, " << fetchAllRatingSystems(), returned " + StringUtils.objectAsString(resultOfFetchQuery));
		return resultOfFetchQuery;
    }

	public Cursor getRatingSystem(String ratingSystemName) {
		Log.d(TAG, " >> getRatingSystem(ratingSystemName'" + StringUtils.objectAsString(ratingSystemName) + "')");
        
		Cursor resultOfFetchQuery = mDb.query(RatingSystem.TABLE_NAME, 
        		new String[] {
        			RatingSystemColumns._ID, 
        			RatingSystemColumns.NAME
        		}, 
        		RatingSystemColumns.NAME + "='" + ratingSystemName + "'", null, null, null, null);
        
        Log.d(TAG, " << getRatingSystem(), returned " + StringUtils.objectAsString(resultOfFetchQuery));
		return resultOfFetchQuery;
	}
	
	public Cursor getRatingSystem(long ratingSystemId) {
		Log.d(TAG, " >> getRatingSystem(ratingSystemId'" + StringUtils.objectAsString(ratingSystemId) + "')");
        
		Cursor resultOfFetchQuery = mDb.query(RatingSystem.TABLE_NAME, 
        		new String[] {
        			RatingSystemColumns._ID, 
        			RatingSystemColumns.NAME
        		}, 
        		RatingSystemColumns._ID+ "=" + ratingSystemId, null, null, null, null);
        
        Log.d(TAG, " << getRatingSystem(), returned " + StringUtils.objectAsString(resultOfFetchQuery));
		return resultOfFetchQuery;
	}

}
