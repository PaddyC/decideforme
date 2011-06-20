package com.db.decideforme.competitors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.db.decideforme.competitors.Competitor.CompetitorColumns;
import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.decideforme.utils.SubjectConstants;


public class CompetitorsDatabaseAdapter extends DecisionDatabaseAdapter {
	private static final String TAG = CompetitorsDatabaseAdapter.class.getName();

	public CompetitorsDatabaseAdapter(Context ctx) {
		super(ctx);
		setSubject(SubjectConstants.COMPETITOR);
	}
	
    public Integer getNextCompetitorSequenceID() {
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
  
    	return nextRowId;
    }
    
    public Cursor fetchAllCompetitorsForDecision(Long decisionRowId) {
    	String decisionRowIdStr = decisionRowId.toString();

    	Cursor resultOfFetchQuery = mDb.query(Competitor.TABLE_NAME, 
        		new String[] {CompetitorColumns._ID, 
        		CompetitorColumns.DECISIONID, 
        		CompetitorColumns.DESCRIPTION}, 
        		CompetitorColumns.DECISIONID + " = " + decisionRowIdStr, null, null, null, null);
    	
    	return resultOfFetchQuery;
    }
    
    public long createCompetitor(String competitorName, Long decisionRowid) {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(CompetitorColumns.DESCRIPTION, competitorName);
        initialValues.put(CompetitorColumns.DECISIONID, decisionRowid);

        long insertResult = mDb.insert(Competitor.TABLE_NAME, null, initialValues);
        
        return insertResult;
    }
    
    
    public boolean deleteCompetitor(long rowId) {
    	boolean deleteResult = mDb.delete(
        		Competitor.TABLE_NAME, 
        		CompetitorColumns._ID + "=" + rowId, null) > 0;
        
        return deleteResult;
    }
    
    public Cursor fetchCompetitor(long rowId) throws SQLException {
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
        
        return mCursor;
    }
    
    public boolean updateCompetitor(long rowId, String competitorName) {
    	ContentValues args = new ContentValues();
        args.put(CompetitorColumns.DESCRIPTION, competitorName);
        
        boolean resultOfUpdate = mDb.update(
        		Competitor.TABLE_NAME, 
        		args, 
        		CompetitorColumns._ID + "=" + rowId, null) > 0;

        return resultOfUpdate;
    }

}