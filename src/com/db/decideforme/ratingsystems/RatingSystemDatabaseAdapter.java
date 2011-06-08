package com.db.decideforme.ratingsystems;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.db.decideforme.DecisionDatabaseAdapter;
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

}
