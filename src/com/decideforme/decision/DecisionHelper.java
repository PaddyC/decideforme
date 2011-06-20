package com.decideforme.decision;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;

import com.db.CursorUtils;
import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.decision.Decision;
import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.decideforme.competitors.CompetitorHelper;
import com.decideforme.criteria.CriteriaHelper;

public class DecisionHelper {

	public static long createNewDecision(Activity thisActivity) {
		
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		Integer nextDecisionNumber = decisionDBAdapter.getNextDecisionSequenceID();
		String decisionName = "D" + nextDecisionNumber;
  
		long decisionID = decisionDBAdapter.createDecision(decisionName, "");
		
		decisionDBAdapter.close();
		
		return decisionID;
		
	}
	
	public static long createNewDecision(Activity thisActivity, String decisionName, String decisionDescription) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		long decisionID = decisionDBAdapter.createDecision(decisionName, decisionDescription);
		decisionDBAdapter.close();
		return decisionID;
	}

	public static List<Decision> fetchAllDecisions(Activity thisActivity) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		
		Cursor decisionCursor = decisionDBAdapter.fetchAllDecisions();
		List<Decision> decisionList = CursorUtils.getDecisionsForCursor(decisionCursor);
		decisionDBAdapter.close();
		return decisionList;
	}
	
	public static boolean updateDecision(Activity thisActivity, long decisionRowId, String decisionName, String description) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		boolean result = decisionDBAdapter.updateDecision(decisionRowId, decisionName, description);
		decisionDBAdapter.close();
		return result;
	}
	
	public static List<Decision> getDecision(Activity thisActivity, long decisionRowId) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		Cursor decisionCursor = decisionDBAdapter.fetchDecision(decisionRowId);
		List<Decision> decisionList = CursorUtils.getDecisionsForCursor(decisionCursor);
		decisionDBAdapter.close();
		return decisionList;
	}
	
	public static List<Decision> getDecision(Activity thisActivity, String decisionName) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		
		Cursor decisionCursor = decisionDBAdapter.fetchDecision(decisionName);
		
		List<Decision> decisionList = CursorUtils.getDecisionsForCursor(decisionCursor);
		decisionDBAdapter.close();
		return decisionList;
	}
	/**
	 * Delete all associated Competitors, Criteria and DecisionRatings before deleting the decision.
	 * @param thisActivity
	 * @param decisionRowId
	 * @return boolean
	 */
	public static boolean deleteDecision(Activity thisActivity, long decisionRowId) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		
		boolean criteriaDeleted = false;
		boolean competitorsDeleted = false;
		List<Criterion> criteriaList = CriteriaHelper.getAllCriteriaForDecision(thisActivity, decisionRowId);
		for(Criterion criterion : criteriaList) {
			criteriaDeleted = CriteriaHelper.deleteCriterion(thisActivity, criterion.getRowId());
		}
		
		List<Competitor> competitorList = CompetitorHelper.getAllCompetitorsForDecision(thisActivity, decisionRowId);
		for (Competitor competitor : competitorList) {
			competitorsDeleted = CompetitorHelper.deleteCompetitor(thisActivity, competitor.getRowId());
		}

		boolean decisionDeleted = decisionDBAdapter.deleteDecision(decisionRowId);
		decisionDBAdapter.close();
		
		return (criteriaDeleted && competitorsDeleted) && decisionDeleted;
	}
	
	
	private static DecisionDatabaseAdapter getDatabaseAdapter(Activity thisActivity) {
		DecisionDatabaseAdapter decisionDBAdapter = new DecisionDatabaseAdapter(thisActivity);
		decisionDBAdapter.open();
		return decisionDBAdapter;
	}
}
