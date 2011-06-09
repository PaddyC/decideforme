package com.db.decideforme.decisionrating;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.db.decideforme.decisionrating.DecisionRatings.DecisionRatingsColumns;
import com.decideforme.utils.StringUtils;

public class DecisionRatingsDatabaseAdapter extends DecisionDatabaseAdapter {
	private static final String TAG = DecisionRatingsDatabaseAdapter.class.getName();

	public DecisionRatingsDatabaseAdapter(Context ctx) {
		super(ctx);
	}

    public long createDecisionRating(Long decisionID, Long competitorID, Long criterionID, Long ratingSelectionID) {
    	Log.d(TAG, " >> createDecisionRating(" +
    			"decisionID '"+ StringUtils.objectAsString(decisionID) + "', " +
    			"competitorID '"+ StringUtils.objectAsString(competitorID) + "', " +
    			"criterionID '"+ StringUtils.objectAsString(criterionID) + "', " +
    			"ratingSelectionID '"+ StringUtils.objectAsString(ratingSelectionID) + "')");

    	ContentValues initialValues = new ContentValues();
        initialValues.put(DecisionRatingsColumns.DECISIONID, decisionID);
        initialValues.put(DecisionRatingsColumns.COMPETITORID, competitorID);
        initialValues.put(DecisionRatingsColumns.CRITERIONID, criterionID);
        initialValues.put(DecisionRatingsColumns.RATINGSELECTIONID, ratingSelectionID);

        long insertResult = mDb.insert(DecisionRatings.TABLE_NAME, null, initialValues);
        
        Log.d(TAG, " << createDecisionRating(), returned " + StringUtils.objectAsString(insertResult));
		return insertResult;
    }
    
    public boolean updateDecisionRating(Long decisionID, Long competitorID, Long criterionID, Long ratingSelectionID) {
    	Log.d(TAG, " >> updateDecisionRating(" +
    			"decisionID '"+ StringUtils.objectAsString(decisionID) + "', " +
    			"competitorID '"+ StringUtils.objectAsString(competitorID) + "', " +
    			"criterionID '"+ StringUtils.objectAsString(criterionID) + "', " +
    			"ratingSelectionID '"+ StringUtils.objectAsString(ratingSelectionID) + "')");

    	ContentValues args = new ContentValues();
        args.put(DecisionRatingsColumns.DECISIONID, decisionID);
        args.put(DecisionRatingsColumns.COMPETITORID, competitorID);
        args.put(DecisionRatingsColumns.CRITERIONID, criterionID);
        args.put(DecisionRatingsColumns.RATINGSELECTIONID, ratingSelectionID);

        StringBuilder whereClause = new StringBuilder(DecisionRatingsColumns.DECISIONID + " = " + decisionID + " AND ");
        whereClause.append(DecisionRatingsColumns.COMPETITORID + " = " + competitorID + " AND ");
        whereClause.append(DecisionRatingsColumns.CRITERIONID + " = " + criterionID);
        
        boolean insertResult = mDb.update(DecisionRatings.TABLE_NAME, args, whereClause.toString(), null) > 0;
        
        Log.d(TAG, " << createDecisionRating(), returned " + StringUtils.objectAsString(insertResult));
		return insertResult;
    }
}
