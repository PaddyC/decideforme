package com.decideforme.criteria;

import android.app.Activity;
import android.database.Cursor;

import com.db.decideforme.criteria.CriteriaDatabaseAdapter;

public class CriteriaHelper {

    public static long createCriterion(Activity thisActivity, String description, long decisionRowId, long ratingSystemID) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
		return criteriaDBAdapter.createCriterion(description, decisionRowId, ratingSystemID);
	}
    
    public static long createCriterion(Activity thisActivity, long decisionRowId) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
		long mNextCriterionRowId = criteriaDBAdapter.getNextCriterionSequenceID();
    	String criterionName = "CR" + mNextCriterionRowId;
    	
    	return criteriaDBAdapter.createCriterion(criterionName, decisionRowId, new Long(0));
	}
    
    public static boolean updateCriterion(Activity thisActivity, long criterionRowId, String description, long ratingSystemId) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
    	return criteriaDBAdapter.updateCriterion(criterionRowId, description, ratingSystemId);
    }
    
    public static Cursor getCriterion(Activity thisActivity, long criterionRowId) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
		Cursor cCriteria = criteriaDBAdapter.fetchCriterion(criterionRowId);
		return cCriteria;
    }
    
    public static Cursor getCriterionByName(Activity thisActivity, long decisionRowId, String criterionName) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
		Cursor cCriteria = criteriaDBAdapter.fetchCriterion(decisionRowId, criterionName);
		return cCriteria;
    }
    
    public static Cursor getAllCriteriaForDecision(Activity thisActivity, long decisionRowId) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
    	Cursor competitorsCursor = criteriaDBAdapter.fetchAllCriteriaForDecision(decisionRowId);
    	return competitorsCursor;
    }
    
    public static boolean deleteCriterion(Activity thisActivity, long criterionRowId) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
    	return criteriaDBAdapter.deleteCriterion(criterionRowId);
    }

	private static CriteriaDatabaseAdapter getDatabaseAdapter(Activity thisActivity) {
		CriteriaDatabaseAdapter mCriteriaDBAdapter = new CriteriaDatabaseAdapter(thisActivity);
		mCriteriaDBAdapter.open();
		return mCriteriaDBAdapter;
	}
    
}
