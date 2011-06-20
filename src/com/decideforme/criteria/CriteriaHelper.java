package com.decideforme.criteria;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;

import com.db.CursorUtils;
import com.db.decideforme.criteria.CriteriaDatabaseAdapter;
import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.decisionrating.DecisionRatingsHelper;

public class CriteriaHelper {

    public static long createCriterion(Activity thisActivity, String description, long decisionRowId, long ratingSystemID) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
		return criteriaDBAdapter.createCriterion(description, decisionRowId, ratingSystemID);
	}
    
    public static long createCriterion(Activity thisActivity, long decisionRowId) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
		long mNextCriterionRowId = criteriaDBAdapter.getNextCriterionSequenceID();
    	String criterionName = "CR" + mNextCriterionRowId;
    	
    	long criterionRowId = criteriaDBAdapter.createCriterion(criterionName, decisionRowId, new Long(0));
    	
    	criteriaDBAdapter.close();
    	
    	return criterionRowId;
	}
    
    public static boolean updateCriterion(Activity thisActivity, long criterionRowId, String description, long ratingSystemId) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
    	boolean result = criteriaDBAdapter.updateCriterion(criterionRowId, description, ratingSystemId);
    	criteriaDBAdapter.close();
    	return result;
    }
    
    public static List<Criterion> getCriterion(Activity thisActivity, long criterionRowId) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
		Cursor cCriteria = criteriaDBAdapter.fetchCriterion(criterionRowId);
		List<Criterion> criteriaList = CursorUtils.getCriteriaForCursor(cCriteria);
		
		criteriaDBAdapter.close();
		
		return criteriaList;
    }
    
    public static List<Criterion> getCriterionByName(Activity thisActivity, long decisionRowId, String criterionName) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
		Cursor cCriteria = criteriaDBAdapter.fetchCriterion(decisionRowId, criterionName);
		List<Criterion> criteriaList = CursorUtils.getCriteriaForCursor(cCriteria);
		criteriaDBAdapter.close();
		return criteriaList;
    }
    
    public static List<Criterion> getAllCriteriaForDecision(Activity thisActivity, long decisionRowId) {
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
    	Cursor allCriteriaForDecision = criteriaDBAdapter.fetchAllCriteriaForDecision(decisionRowId);
    	List<Criterion> criteriaForDecision = CursorUtils.getCriteriaForCursor(allCriteriaForDecision);
    	criteriaDBAdapter.close();
    	return criteriaForDecision;
    }
    
    /**
     * Delete all DecisionRatings associated with the criterion at the same time.
     * @param thisActivity
     * @param criterionRowId
     * @return
     */
    public static boolean deleteCriterion(Activity thisActivity, long criterionRowId) {
    	
    	CriteriaDatabaseAdapter criteriaDBAdapter = getDatabaseAdapter(thisActivity);
    	
    	boolean ratingsDeleted = DecisionRatingsHelper.deleteRatingsForCriterion(thisActivity, criterionRowId);
    	boolean criterionDeleted = criteriaDBAdapter.deleteCriterion(criterionRowId);
    	
    	criteriaDBAdapter.close();
    	
    	return criterionDeleted && ratingsDeleted;
    	
    }

	private static CriteriaDatabaseAdapter getDatabaseAdapter(Activity thisActivity) {
		CriteriaDatabaseAdapter mCriteriaDBAdapter = new CriteriaDatabaseAdapter(thisActivity);
		mCriteriaDBAdapter.open();
		return mCriteriaDBAdapter;
	}
    
}
