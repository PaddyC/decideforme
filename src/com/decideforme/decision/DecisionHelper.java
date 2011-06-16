package com.decideforme.decision;

import android.app.Activity;
import android.database.Cursor;

import com.db.decideforme.decision.DecisionDatabaseAdapter;

public class DecisionHelper {

	public static long createNewDecision(Activity thisActivity) {
		
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		Integer nextDecisionNumber = decisionDBAdapter.getNextDecisionSequenceID();
		String decisionName = "D" + nextDecisionNumber;
  
		long decisionID = decisionDBAdapter.createDecision(decisionName, "");
		return decisionID;
		
	}
	
	public static long createNewDecision(Activity thisActivity, String decisionName, String decisionDescription) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		long decisionID = decisionDBAdapter.createDecision(decisionName, decisionDescription);
		return decisionID;
	}

	public static Cursor fetchAllDecisions(Activity thisActivity) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		return decisionDBAdapter.fetchAllDecisions();
	}
	
	public static boolean updateDecision(Activity thisActivity, long decisionRowId, String decisionName, String description) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		return decisionDBAdapter.updateDecision(decisionRowId, decisionName, description);
	}
	
	public static Cursor getDecision(Activity thisActivity, long decisionRowId) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		return decisionDBAdapter.fetchDecision(decisionRowId);
	}
	
	public static Cursor getDecision(Activity thisActivity, String decisionName) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		return decisionDBAdapter.fetchDecision(decisionName);
	}
	
	public static boolean deleteDecision(Activity thisActivity, long decisionRowId) {
		DecisionDatabaseAdapter decisionDBAdapter = getDatabaseAdapter(thisActivity);
		return decisionDBAdapter.deleteDecision(decisionRowId);
	}
	
	
	private static DecisionDatabaseAdapter getDatabaseAdapter(Activity thisActivity) {
		DecisionDatabaseAdapter decisionDBAdapter = new DecisionDatabaseAdapter(thisActivity);
		decisionDBAdapter.open();
		return decisionDBAdapter;
	}
}
