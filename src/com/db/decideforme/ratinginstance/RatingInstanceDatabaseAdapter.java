package com.db.decideforme.ratinginstance;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.db.decideforme.ratinginstance.RatingInstance.RatingInstanceColumns;
import com.decideforme.utils.StringUtils;

public class RatingInstanceDatabaseAdapter extends DecisionDatabaseAdapter {
	public static final String TAG = RatingInstanceDatabaseAdapter.class.getName();

	public RatingInstanceDatabaseAdapter(Context ctx) {
		super(ctx);
	}
	
    public Cursor fetchAllInstancesForRatingSystem(Long systemID) {
    	Log.d(TAG, " >> fetchAllInstancesForRatingSystem(" +
    			"systemID '" + StringUtils.objectAsString(systemID) + "')");
    	
        Cursor resultOfFetchQuery = mDb.query(
        		RatingInstance.TABLE_NAME, 
        		new String[] {
        			RatingInstanceColumns._ID, 
        			RatingInstanceColumns.SYSTEM_ID,
        			RatingInstanceColumns.RORDER,
        			RatingInstanceColumns.NAME
        		}, 
        		RatingInstanceColumns.SYSTEM_ID + " = " + systemID, null, null, null, null);
        
        Log.d(TAG, " << fetchAllInstancesForRatingSystem()" +
        		", returned " + StringUtils.objectAsString(resultOfFetchQuery));
        
		return resultOfFetchQuery;
    }

}
