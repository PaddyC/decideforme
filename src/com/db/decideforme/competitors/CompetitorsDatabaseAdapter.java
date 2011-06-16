package com.db.decideforme.competitors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.db.decideforme.competitors.Competitor.CompetitorColumns;
import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.decideforme.utils.StringUtils;
import com.decideforme.utils.SubjectConstants;


public class CompetitorsDatabaseAdapter extends DecisionDatabaseAdapter {
	private static final String TAG = CompetitorsDatabaseAdapter.class.getName();

	public CompetitorsDatabaseAdapter(Context ctx) {
		super(ctx);
		setSubject(SubjectConstants.COMPETITOR);
	}
	
    public Integer getNextCompetitorSequenceID() {
    	Log.d(TAG, " >> getNextCompetitorSequenceID()");
    	Integer nextRowId = 0;
    	
    	Cursor resultOfFetchQuery = mDb.query(Competitor.TABLE_NAME, 
        		new String[] {CompetitorColumns._ID, 
        		CompetitorColumns.DECISIONID, 
        		CompetitorColumns.DESCRIPTION}, 
        		null, null, null, null, CompetitorColumns._ID);

		if (resultOfFetchQuery!= null && resultOfFetchQuery.getCount() > 0) {
    		resultOfFetchQuery.moveToLast();
    		nextRowId = resultOfFetchQuery.getInt(0);	
    	}
    	nextRowId++;
  
    	Log.d(TAG, " << getNextCompetitorSequenceID(), returned '" + StringUtils.objectAsString(nextRowId) + "'");
    	return nextRowId;
    }
    
    public Cursor fetchAllCompetitorsForDecision(Long decisionRowId) {
    	Log.d(TAG, " >> fetchAllCompetitorsForDecision(" +
    			"decisionRowId '" + StringUtils.objectAsString(decisionRowId) + "')");
    	
    	String decisionRowIdStr = decisionRowId.toString();

    	Cursor resultOfFetchQuery = mDb.query(Competitor.TABLE_NAME, 
        		new String[] {CompetitorColumns._ID, 
        		CompetitorColumns.DECISIONID, 
        		CompetitorColumns.DESCRIPTION}, 
        		CompetitorColumns.DECISIONID + " = " + decisionRowIdStr, null, null, null, null);
    	
    	Log.d(TAG, "There are " + resultOfFetchQuery.getCount() + " competitors associated with this decision.");
        
        Log.d(TAG, " << fetchAllCompetitorsForDecision(), returned " + StringUtils.objectAsString(resultOfFetchQuery));
		return resultOfFetchQuery;
    }
    
    public long createCompetitor(String competitorName, Long decisionRowid) {
    	Log.d(TAG, " >> createCompetitor(" +
    			"competitorName " + StringUtils.objectAsString(competitorName) + "', " +
    			"decisionRowid " + StringUtils.objectAsString(decisionRowid) + "')");

    	ContentValues initialValues = new ContentValues();
        initialValues.put(CompetitorColumns.DESCRIPTION, competitorName);
        initialValues.put(CompetitorColumns.DECISIONID, decisionRowid);

        long insertResult = mDb.insert(Competitor.TABLE_NAME, null, initialValues);
        
        Log.d(TAG, " << createCompetitor(), returned " + StringUtils.objectAsString(insertResult));
		return insertResult;
    }
    
    
    public boolean deleteCompetitor(long rowId) {
    	Log.d(TAG, " >> deleteCompetitor(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "')");
    	
        boolean deleteResult = mDb.delete(
        		Competitor.TABLE_NAME, 
        		CompetitorColumns._ID + "=" + rowId, null) > 0;
        
        Log.d(TAG, " << deleteCompetitor(), returned '" + StringUtils.objectAsString(deleteResult) + "'");
		return deleteResult;
    }
    
    public Cursor fetchCompetitor(long rowId) throws SQLException {
    	Log.d(TAG, " >> fetchCompetitor(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "')");
    	
        Cursor mCursor = mDb.query(
            		true, 
            		Competitor.TABLE_NAME, 
            		new String[] {CompetitorColumns._ID, 
            				CompetitorColumns.DECISIONID, 
            				CompetitorColumns.DESCRIPTION},
            				CompetitorColumns._ID + "=" + rowId, 
            		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        Log.d(TAG, "<< fetchCompetitor(), returned '" + StringUtils.objectAsString(mCursor) + "'");
        return mCursor;
    }
    
    public Cursor fetchCompetitor(long decisionRowId, String competitorName) throws SQLException {
    	
    	StringBuilder whereClause = new StringBuilder(CompetitorColumns.DECISIONID + "=" + decisionRowId);
    	whereClause.append(" AND ");
    	if (competitorName == null || "".equalsIgnoreCase(competitorName)) {
    		whereClause.append(CompetitorColumns.DESCRIPTION + " IS NULL");
    	} else {
    		whereClause.append(CompetitorColumns.DESCRIPTION + "= '" + competitorName + "'");	
    	}
    	Log.d(TAG, "Where Clause: " + whereClause);
    	
    	Cursor mCursor = mDb.query(
            		true, 
            		Competitor.TABLE_NAME, 
            		new String[] {CompetitorColumns._ID, 
            				CompetitorColumns.DECISIONID, 
            				CompetitorColumns.DESCRIPTION},
            				whereClause.toString(), 
            		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        Log.d(TAG, "<< fetchCompetitor(), returned '" + StringUtils.objectAsString(mCursor) + "'");
        return mCursor;
    }
    
    public boolean updateCompetitor(long rowId, String competitorName) {
    	Log.d(TAG, " >> updateCompetitor(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "', " +
    			"competitorName '" + StringUtils.objectAsString(competitorName) + "')");
    	
        ContentValues args = new ContentValues();
        args.put(CompetitorColumns.DESCRIPTION, competitorName);
        
        boolean resultOfUpdate = mDb.update(
        		Competitor.TABLE_NAME, 
        		args, 
        		CompetitorColumns._ID + "=" + rowId, null) > 0;

        Log.d(TAG, "<< updateCompetitor(), returned '" + StringUtils.objectAsString(resultOfUpdate) + "'");
		return resultOfUpdate;
    }
	
}
