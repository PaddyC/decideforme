package com.db.decideforme.criteria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.db.decideforme.DecisionDatabaseAdapter;
import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.competitors.Competitor.CompetitorColumns;
import com.db.decideforme.criteria.Criterion.CriterionColumns;
import com.decideforme.utils.StringUtils;

public class CriteriaDatabaseAdapter extends DecisionDatabaseAdapter {
	private static final String TAG = CriteriaDatabaseAdapter.class.getName();

	public CriteriaDatabaseAdapter(Context ctx) {
		super(ctx);
	}

    public Integer getNextCriterionSequenceID() {
    	Log.d(TAG, " >> getNextCriterionSequenceID()");
    	Integer nextRowId = 0;
    	
    	Cursor resultOfFetchQuery = mDb.query(
    			Criterion.TABLE_NAME, 
        		new String[] {
	    			CriterionColumns._ID, 
	    			CriterionColumns.DECISIONID, 
	    			CriterionColumns.DESCRIPTION,
	    			CriterionColumns.RATINGSYSTEM
    			}, 
        		null, null, null, null, CriterionColumns._ID);

		if (resultOfFetchQuery!= null && resultOfFetchQuery.getCount() > 0) {
    		resultOfFetchQuery.moveToLast();
    		nextRowId = resultOfFetchQuery.getInt(0);	
    	}
    	nextRowId++;
    	Log.d(TAG, "nextRowId: '" + nextRowId + "'");
    	
    	resultOfFetchQuery.close();
    	Log.d(TAG, " << getNextCriterionSequenceID(), returned '" + StringUtils.objectAsString(nextRowId) + "'");
    	return nextRowId;
    }
    
    public Cursor fetchAllCriteriaForDecision(Long decisionRowId) {
    	Log.d(TAG, " >> fetchAllCriteriaForDecision(" +
    			"decisionRowId '" + StringUtils.objectAsString(decisionRowId) + "')");
    	
    	String decisionRowIdStr = decisionRowId.toString();
    	
    	Cursor resultOfFetchQuery = mDb.query(
    			Criterion.TABLE_NAME, 
        		new String[] {
    	    			CriterionColumns._ID, 
    	    			CriterionColumns.DECISIONID, 
    	    			CriterionColumns.DESCRIPTION,
    	    			CriterionColumns.RATINGSYSTEM
        			}, 
        		CriterionColumns.DECISIONID + " = " + decisionRowIdStr, null, null, null, null);
        
        Log.d(TAG, " << fetchAllCriteriaForDecision(), returned " + StringUtils.objectAsString(resultOfFetchQuery));
		return resultOfFetchQuery;
    }
    
    public long createCriterion(String criterionName, Long decisionRowid) {
    	Log.d(TAG, " >> createCriterion(" +
    			"criterionName " + StringUtils.objectAsString(criterionName) + "', " +
    			"decisionRowid " + StringUtils.objectAsString(decisionRowid) + "')");

    	ContentValues initialValues = new ContentValues();
        initialValues.put(CriterionColumns.DESCRIPTION, criterionName);
        initialValues.put(CriterionColumns.DECISIONID, decisionRowid);

        long insertResult = mDb.insert(Criterion.TABLE_NAME, null, initialValues);
        
        Log.d(TAG, " << createCriterion(), returned " + StringUtils.objectAsString(insertResult));
		return insertResult;
    }
	
    public boolean deleteCriterion(long rowId) {
    	Log.d(TAG, " >> deleteCriterion(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "')");
    	
        boolean deleteResult = mDb.delete(
        		Criterion.TABLE_NAME, 
        		CriterionColumns._ID + "=" + rowId, null) > 0;
        
        Log.d(TAG, " << deleteCriterion(), returned '" + StringUtils.objectAsString(deleteResult) + "'");
		return deleteResult;
    }
    
    public Cursor fetchCriterion(long rowId) throws SQLException {
    	Log.d(TAG, " >> fetchCriterion(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "')");
    	
        Cursor mCursor = mDb.query(
            		true, 
            		Criterion.TABLE_NAME, 
            		new String[] {
        	    			CriterionColumns._ID, 
        	    			CriterionColumns.DECISIONID, 
        	    			CriterionColumns.DESCRIPTION,
        	    			CriterionColumns.RATINGSYSTEM
            			},
            		CriterionColumns._ID + "=" + rowId, 
            		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        Log.d(TAG, "<< fetchCriterion(), returned '" + StringUtils.objectAsString(mCursor) + "'");
        return mCursor;
    }
    
    public boolean updateCriterion(long rowId, String criterionName) {
    	Log.d(TAG, " >> updateCriterion(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "', " +
    			"criterionName '" + StringUtils.objectAsString(criterionName) + "')");
    	
        ContentValues args = new ContentValues();
        args.put(CriterionColumns.DESCRIPTION, criterionName);
        
        boolean resultOfUpdate = mDb.update(
        		Criterion.TABLE_NAME, 
        		args, 
        		CriterionColumns._ID + "=" + rowId, null) > 0;

        Log.d(TAG, "<< updateCriterion(), returned '" + StringUtils.objectAsString(resultOfUpdate) + "'");
		return resultOfUpdate;
    }
}
