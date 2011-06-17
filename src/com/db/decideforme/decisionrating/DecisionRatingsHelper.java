package com.db.decideforme.decisionrating;

import java.util.List;

import com.db.CursorUtils;

import android.app.Activity;
import android.database.Cursor;

public class DecisionRatingsHelper {
	
	public static List<DecisionRatings> getAllRatingsForDecision(Activity thisActivity, long decisionRowId) {
		DecisionRatingsDatabaseAdapter dbAdapter = getDatabaseAdapter(thisActivity);
		
		Cursor decisionRatingsCursor = dbAdapter.fetchAllRatingsForDecision(decisionRowId);
		List<DecisionRatings> allRatingsForDecision = CursorUtils.getDecisionRatingsListForCursor(decisionRatingsCursor);
		dbAdapter.close();
		
		return allRatingsForDecision;
	}
	
	public static List<DecisionRatings> getAllRatingsForDecisionCompetitor(Activity thisActivity, long decisionRowId, long competitorRowId) {
		DecisionRatingsDatabaseAdapter dbAdapter = getDatabaseAdapter(thisActivity);
		
		Cursor decisionRatingsCursor = dbAdapter.fetchAllRatingsForDecisionCompetitor(decisionRowId, competitorRowId);
		List<DecisionRatings> allRatingsForDecision = CursorUtils.getDecisionRatingsListForCursor(decisionRatingsCursor);
		dbAdapter.close();
		
		return allRatingsForDecision;
	}
	
	
	public static List<DecisionRatings> getAllRatingsForDecisionCriterion(Activity thisActivity, long decisionRowId, long criterionRowId) {
		DecisionRatingsDatabaseAdapter dbAdapter = getDatabaseAdapter(thisActivity);
		
		Cursor decisionRatingsCursor = dbAdapter.fetchAllRatingsForDecisionCriterion(decisionRowId, criterionRowId);
		List<DecisionRatings> allRatingsForDecisionCriterion = CursorUtils.getDecisionRatingsListForCursor(decisionRatingsCursor);
		dbAdapter.close();
		
		return allRatingsForDecisionCriterion;
	}
	
	
	private static DecisionRatingsDatabaseAdapter getDatabaseAdapter(Activity thisActivity) {
		DecisionRatingsDatabaseAdapter dbAdapter = new DecisionRatingsDatabaseAdapter(thisActivity);
		dbAdapter.open();
		return dbAdapter;
	}

}
