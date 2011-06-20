package com.db.decideforme.criteria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.db.decideforme.criteria.Criterion.CriterionColumns;
import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.decideforme.utils.SubjectConstants;

public class CriteriaDatabaseAdapter extends DecisionDatabaseAdapter {
	
	public CriteriaDatabaseAdapter(Context ctx) {
		super(ctx);
		setSubject(SubjectConstants.CRITERION);
	}
	
	private String[] criterionTableColumns = new String[] {
			CriterionColumns._ID, 
			CriterionColumns.DECISIONID, 
			CriterionColumns.DESCRIPTION,
			CriterionColumns.RATINGSYSTEM
		};

	
    public Integer getNextCriterionSequenceID() {
    	Integer nextRowId = 0;
    	

		Cursor resultOfFetchQuery = mDb.query(
    			Criterion.TABLE_NAME, 
        		criterionTableColumns, 
        		null, null, null, null, CriterionColumns._ID);

		if (resultOfFetchQuery!= null && resultOfFetchQuery.getCount() > 0) {
    		resultOfFetchQuery.moveToLast();
    		nextRowId = resultOfFetchQuery.getInt(0);	
    	}
    	nextRowId++;
    	
    	return nextRowId;
    }
    
    public Cursor fetchAllCriteriaForDecision(Long decisionRowId) {
    	String decisionRowIdStr = decisionRowId.toString();
    	
    	Cursor resultOfFetchQuery = mDb.query(
    			Criterion.TABLE_NAME, 
    			criterionTableColumns, 
        		CriterionColumns.DECISIONID + " = " + decisionRowIdStr, null, null, null, null);
        
        return resultOfFetchQuery;
    }
    
    public long createCriterion(String criterionName, Long decisionRowid, Long ratingSystemRowID) {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(CriterionColumns.DESCRIPTION, criterionName);
        initialValues.put(CriterionColumns.DECISIONID, decisionRowid);
        initialValues.put(CriterionColumns.RATINGSYSTEM, ratingSystemRowID);

        long insertResult = mDb.insert(Criterion.TABLE_NAME, null, initialValues);
        
        return insertResult;
    }
	
    public boolean deleteCriterion(long rowId) {
    	boolean deleteResult = mDb.delete(
        		Criterion.TABLE_NAME, 
        		CriterionColumns._ID + "=" + rowId, null) > 0;
        
        return deleteResult;
    }
    
    public Cursor fetchCriterion(long rowId) throws SQLException {
    	Cursor mCursor = mDb.query(
            		true, 
            		Criterion.TABLE_NAME, 
            		criterionTableColumns,
            		CriterionColumns._ID + "=" + rowId, 
            		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        return mCursor;
    }
    
    public Cursor fetchCriterion(long decisionId, String criterionName) throws SQLException {
    	
    	StringBuilder whereClause = new StringBuilder(CriterionColumns.DECISIONID + "=" + decisionId);
    	whereClause.append(" AND ");
    	whereClause.append(CriterionColumns.DESCRIPTION + "='" + escapeString(criterionName) + "'");
    	
    	Cursor mCursor = mDb.query(
            		false, 
            		Criterion.TABLE_NAME, 
            		criterionTableColumns,
            		whereClause.toString(), 
            		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        return mCursor;
    }
    
    public boolean updateCriterion(long rowId, String criterionName, Long ratingSystemID) {
    	ContentValues args = new ContentValues();
        args.put(CriterionColumns.DESCRIPTION, criterionName);
        args.put(CriterionColumns.RATINGSYSTEM, ratingSystemID);
        
        boolean resultOfUpdate = mDb.update(
        		Criterion.TABLE_NAME, 
        		args, 
        		CriterionColumns._ID + "=" + rowId, null) > 0;

        return resultOfUpdate;
    }
}
