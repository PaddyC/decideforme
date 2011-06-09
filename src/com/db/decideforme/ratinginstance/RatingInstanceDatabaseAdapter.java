package com.db.decideforme.ratinginstance;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.db.decideforme.ratinginstance.RatingInstance.RatingInstanceColumns;

public class RatingInstanceDatabaseAdapter extends DecisionDatabaseAdapter {
	public static final String TAG = RatingInstanceDatabaseAdapter.class.getName();

	public RatingInstanceDatabaseAdapter(Context ctx) {
		super(ctx);
	}
	
    public Cursor fetchAllInstancesForRatingSystem(Long systemID) {
    	Cursor cRatingInstances = mDb.query(
        		RatingInstance.TABLE_NAME, 
        		new String[] {
        			RatingInstanceColumns._ID, 
        			RatingInstanceColumns.SYSTEM_ID,
        			RatingInstanceColumns.RORDER,
        			RatingInstanceColumns.NAME
        		}, 
        		RatingInstanceColumns.SYSTEM_ID + " = " + systemID, null, null, null, null);
        
        return cRatingInstances;
    }
    
    public Cursor fetchRatingInstance(long rowId) throws SQLException {
    	Cursor cRatingInstance = mDb.query(
            		true, 
            		RatingInstance.TABLE_NAME, 
            		new String[] {
                		RatingInstanceColumns._ID, 
                		RatingInstanceColumns.SYSTEM_ID,
                		RatingInstanceColumns.RORDER,
                		RatingInstanceColumns.NAME
                	},
                	RatingInstanceColumns._ID + "=" + rowId, 
            		null, null, null, null, null);
        
        if (cRatingInstance != null) {
            cRatingInstance.moveToFirst();
        }
        
        return cRatingInstance;
    }

}
