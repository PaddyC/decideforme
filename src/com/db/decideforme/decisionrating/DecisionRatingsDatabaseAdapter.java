package com.db.decideforme.decisionrating;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.db.decideforme.decisionrating.DecisionRatings.DecisionRatingsColumns;
import com.db.decideforme.ratinginstance.RatingInstance;
import com.db.decideforme.ratinginstance.RatingInstanceDatabaseAdapter;

public class DecisionRatingsDatabaseAdapter extends DecisionDatabaseAdapter {
	public static final String TAG = DecisionRatingsDatabaseAdapter.class.getName();
	
    private String[] decisionRatingColumns = new String[] {
			DecisionRatingsColumns._ID, 
			DecisionRatingsColumns.DECISIONID, 
			DecisionRatingsColumns.COMPETITORID, 
			DecisionRatingsColumns.CRITERIONID, 
			DecisionRatingsColumns.RATINGSELECTIONID
		};

	public DecisionRatingsDatabaseAdapter(Context ctx) {
		super(ctx);
	}
	
	public long createDecisionRating(Long decisionID, Long competitorID, Long criterionID, Long ratingSelectionID) {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(DecisionRatingsColumns.DECISIONID, decisionID);
        initialValues.put(DecisionRatingsColumns.COMPETITORID, competitorID);
        initialValues.put(DecisionRatingsColumns.CRITERIONID, criterionID);
        initialValues.put(DecisionRatingsColumns.RATINGSELECTIONID, ratingSelectionID);

        long insertResult = mDb.insert(DecisionRatings.TABLE_NAME, null, initialValues);
        
        return insertResult;
    }
    
    public boolean updateDecisionRating(Long decisionID, Long competitorID, Long criterionID, Long ratingSelectionID) {
    	ContentValues args = new ContentValues();
        args.put(DecisionRatingsColumns.DECISIONID, decisionID);
        args.put(DecisionRatingsColumns.COMPETITORID, competitorID);
        args.put(DecisionRatingsColumns.CRITERIONID, criterionID);
        args.put(DecisionRatingsColumns.RATINGSELECTIONID, ratingSelectionID);

        StringBuilder whereClause = new StringBuilder(DecisionRatingsColumns.DECISIONID + " = " + decisionID + " AND ");
        whereClause.append(DecisionRatingsColumns.COMPETITORID + " = " + competitorID + " AND ");
        whereClause.append(DecisionRatingsColumns.CRITERIONID + " = " + criterionID);
        
        boolean insertResult = mDb.update(DecisionRatings.TABLE_NAME, args, whereClause.toString(), null) > 0;
        
        return insertResult;
    }
    
    public Cursor fetchDecisionRating(Long decisionID, Long competitorID, Long criterionID) throws SQLException {
    	StringBuilder whereClause = new StringBuilder(DecisionRatingsColumns.DECISIONID + " = " + decisionID + " AND ");
        whereClause.append(DecisionRatingsColumns.COMPETITORID + " = " + competitorID + " AND ");
        whereClause.append(DecisionRatingsColumns.CRITERIONID + " = " + criterionID);
    	
		Cursor mCursor = mDb.query(true, DecisionRatings.TABLE_NAME, decisionRatingColumns, whereClause.toString(), 
            		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        return mCursor;
    }
    
    public Cursor fetchAllRatingsForDecision(Long decisionID) throws SQLException {
    	StringBuilder whereClause = new StringBuilder(DecisionRatingsColumns.DECISIONID + " = " + decisionID);
        
        Cursor mCursor = mDb.query(
            		true, DecisionRatings.TABLE_NAME, decisionRatingColumns,whereClause.toString(), 
            		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        return mCursor;
    }
    
    public Long fetchDecisionRatingSelectionRorder(Activity activity, Long decisionID, Long competitorID, Long criterionID) {
    	Long ratingInstanceRorder = new Long(0);
    	
    	Cursor cDecisionRating = fetchDecisionRating(decisionID, competitorID, criterionID);
    	Long ratingSelection = cDecisionRating.getLong(DecisionRatings.COLUMN_INDEX_RATINGSELECTIONID);
		
    	RatingInstanceDatabaseAdapter ratingInstanceDBAdapter = new RatingInstanceDatabaseAdapter(activity);
		ratingInstanceDBAdapter.open();
		Cursor cRatingInstance = ratingInstanceDBAdapter.fetchRatingInstance(ratingSelection);
		ratingInstanceRorder = cRatingInstance.getLong(RatingInstance.COLUMN_INDEX_RORDER);
    	
		return ratingInstanceRorder;
    }
    
    
    public boolean existenceCheckDecisionRating(Long decisionID, Long competitorID, Long criterionID) {
    	boolean decisionRatingExists = false;
    	
    	Cursor thisDecisionRating = fetchDecisionRating(decisionID, competitorID, criterionID);
    	if (thisDecisionRating.getCount() > 0) {
    		decisionRatingExists = true;
    	}
    	
		return decisionRatingExists;
    }

	public Cursor fetchAllRatingsForDecisionCompetitor(long decisionRowId, long competitorRowId) {
    	
		StringBuilder whereClause = new StringBuilder(DecisionRatingsColumns.DECISIONID + " = " + decisionRowId + " AND ");
        whereClause.append(DecisionRatingsColumns.COMPETITORID + " = " + competitorRowId);
    	
        Cursor mCursor = mDb.query(true, DecisionRatings.TABLE_NAME, decisionRatingColumns, 
        						   whereClause.toString(), null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
	}
	
	public Cursor fetchAllRatingsForDecisionCriterion(long decisionRowId, long criterionRowId) {
    	
		StringBuilder whereClause = new StringBuilder(DecisionRatingsColumns.DECISIONID + " = " + decisionRowId + " AND ");
        whereClause.append(DecisionRatingsColumns.CRITERIONID + " = " + criterionRowId);
    	
        Cursor mCursor = mDb.query(true, DecisionRatings.TABLE_NAME, decisionRatingColumns, 
        						   whereClause.toString(), null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
	}
}
