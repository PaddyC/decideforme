package com.db.decideforme.decisionrating;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.db.CursorUtils;
import com.decideforme.utils.StringUtils;

public class DecisionRatingsHelper {
	
	private static final String TAG = DecisionRatingsHelper.class.getName();

	public static Long createDecisionRating(Activity thisActivity, long decisionRowId, long competitorRowId, long criterionRowId, long ratingSelectionId) {
		DecisionRatingsDatabaseAdapter dbAdapter = getDatabaseAdapter(thisActivity);
		Long result = dbAdapter.createDecisionRating(decisionRowId, competitorRowId, criterionRowId, ratingSelectionId);
		dbAdapter.close();
		return result;
	}
	
	public static boolean updateDecisionRating(Activity thisActivity, long decisionRowId, long competitorRowId, long criterionRowId, long ratingSelectionId) {
		DecisionRatingsDatabaseAdapter dbAdapter = getDatabaseAdapter(thisActivity);
		boolean result = dbAdapter.updateDecisionRating(decisionRowId, competitorRowId, criterionRowId, ratingSelectionId);
		dbAdapter.close();
		return result;
	}
	
	public static boolean ratingExists(Activity thisActivity, long decisionRowId, long competitorRowId, long criterionRowId) {
		DecisionRatingsDatabaseAdapter dbAdapter = getDatabaseAdapter(thisActivity);
		boolean ratingExists = dbAdapter.existenceCheckDecisionRating(
				decisionRowId, competitorRowId, criterionRowId);
		dbAdapter.close();
		return ratingExists;
	}
	
	public static DecisionRatings getRating(Activity thisActivity, long decisionRowId, long competitorRowId, long criterionRowId) {
		DecisionRatingsDatabaseAdapter dbAdapter = getDatabaseAdapter(thisActivity);
		Cursor ratingCursor = dbAdapter.fetchDecisionRating(decisionRowId, competitorRowId, criterionRowId);
		List<DecisionRatings> ratingList = CursorUtils.getDecisionRatingsListForCursor(ratingCursor);
		dbAdapter.close();
		return ratingList.get(0);
	}
	
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

	public static boolean deleteRatingsForCompetitor(Activity thisActivity, long competitorRowId) {
		Log.d(TAG, " >> deleteRatingsForCompetitor(criterionRowId '" + StringUtils.objectAsString(competitorRowId) + "'");
		
		DecisionRatingsDatabaseAdapter dbAdapter = getDatabaseAdapter(thisActivity);
		
		boolean deleteResult = dbAdapter.deleteRatingsForCompetitor(competitorRowId);
		dbAdapter.close();
		Log.d(TAG, " << deleteRatingsForCompetitor(), returned " + deleteResult);
		return deleteResult;
	}

	public static boolean deleteRatingsForCriterion(Activity thisActivity, long criterionRowId) {
		Log.d(TAG, " >> deleteRatingsForCriterion(criterionRowId '" + StringUtils.objectAsString(criterionRowId) + "'");
		
		DecisionRatingsDatabaseAdapter dbAdapter = getDatabaseAdapter(thisActivity);
		
		boolean deleteResult = dbAdapter.deleteRatingsForCriterion(criterionRowId);
		dbAdapter.close();
		Log.d(TAG, " << deleteRatingsForCriterion(), returned " + deleteResult);
		return deleteResult;
	}

}
