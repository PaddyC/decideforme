package com.decideforme.competitors;

import android.app.Activity;
import android.database.Cursor;

import com.db.decideforme.competitors.CompetitorsDatabaseAdapter;

public class CompetitorHelper {
	
	private static CompetitorsDatabaseAdapter getDatabaseAdapter(Activity thisActivity) {
		CompetitorsDatabaseAdapter mCompetitorsDBAdapter = new CompetitorsDatabaseAdapter(thisActivity);
		mCompetitorsDBAdapter.open();
		return mCompetitorsDBAdapter;
	}

    public static long createCompetitor(Activity thisActivity, long decisionRowId) {
		
    	CompetitorsDatabaseAdapter competitorsDBAdapter = getDatabaseAdapter(thisActivity);
    	long nextCompetitorRowID = competitorsDBAdapter.getNextCompetitorSequenceID();
		String competitorName = "CO" + nextCompetitorRowID;
		long competitorRowId = competitorsDBAdapter.createCompetitor(competitorName, decisionRowId);
		
		return competitorRowId;
	}
    
    public static long createCompetitor(Activity thisActivity, long decisionRowId, String competitorName) {
		
    	CompetitorsDatabaseAdapter competitorsDBAdapter = getDatabaseAdapter(thisActivity);
		long competitorRowId = competitorsDBAdapter.createCompetitor(competitorName, decisionRowId);
		
		return competitorRowId;
	}
    
    public static boolean updateCompetitor(Activity thisActivity, long competitorRowId, String competitorName) {
		
    	CompetitorsDatabaseAdapter competitorsDBAdapter = getDatabaseAdapter(thisActivity);
		return competitorsDBAdapter.updateCompetitor(competitorRowId, competitorName);

	}
    
    public static Cursor getAllCompetitorsForDecision(Activity thisActivity, long decisionRowId) {
    	
    	CompetitorsDatabaseAdapter mCompetitorsDBAdapter = getDatabaseAdapter(thisActivity);
    	
    	Cursor cCompetitors = mCompetitorsDBAdapter.fetchAllCompetitorsForDecision(decisionRowId);
    	return cCompetitors;
    }
    
    public static Cursor getCompetitorByName(Activity thisActivity, long decisionRowId, String competitorName) {
    	
    	CompetitorsDatabaseAdapter mCompetitorsDBAdapter = getDatabaseAdapter(thisActivity);
		Cursor cCompetitors = mCompetitorsDBAdapter.fetchCompetitor(decisionRowId, competitorName);
		return cCompetitors;
    	
    }
    
    public static Cursor getCompetitor(Activity thisActivity, long competitorRowId) {
    	
    	CompetitorsDatabaseAdapter mCompetitorsDBAdapter = getDatabaseAdapter(thisActivity);
		Cursor cCompetitors = mCompetitorsDBAdapter.fetchCompetitor(competitorRowId);
		return cCompetitors;
    	
    }
    
    public static boolean deleteCompetitor(Activity thisActivity, long competitorRowId) {
    	
    	CompetitorsDatabaseAdapter mCompetitorsDBAdapter = getDatabaseAdapter(thisActivity);
    	
    	return mCompetitorsDBAdapter.deleteCompetitor(competitorRowId);
    	
    }
	
}
