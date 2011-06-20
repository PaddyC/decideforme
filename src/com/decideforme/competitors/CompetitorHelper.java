package com.decideforme.competitors;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;

import com.db.CursorUtils;
import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.competitors.CompetitorsDatabaseAdapter;
import com.db.decideforme.decisionrating.DecisionRatingsHelper;

public class CompetitorHelper {
	
	private static CompetitorsDatabaseAdapter getDatabaseAdapter(Activity thisActivity) {
		CompetitorsDatabaseAdapter competitorsDBAdapter = new CompetitorsDatabaseAdapter(thisActivity);
		competitorsDBAdapter.open();
		return competitorsDBAdapter;
	}

    public static long createCompetitor(Activity thisActivity, long decisionRowId) {
		
    	CompetitorsDatabaseAdapter competitorsDBAdapter = getDatabaseAdapter(thisActivity);
    	long nextCompetitorRowID = competitorsDBAdapter.getNextCompetitorSequenceID();
		String competitorName = "CO" + nextCompetitorRowID;
		long competitorRowId = competitorsDBAdapter.createCompetitor(competitorName, decisionRowId);
		competitorsDBAdapter.close();
		return competitorRowId;
	}
    
    public static long createCompetitor(Activity thisActivity, long decisionRowId, String competitorName) {
		
    	CompetitorsDatabaseAdapter competitorsDBAdapter = getDatabaseAdapter(thisActivity);
		long competitorRowId = competitorsDBAdapter.createCompetitor(competitorName, decisionRowId);
		competitorsDBAdapter.close();
		return competitorRowId;
	}
    
    public static boolean updateCompetitor(Activity thisActivity, long competitorRowId, String competitorName) {
    	CompetitorsDatabaseAdapter competitorsDBAdapter = getDatabaseAdapter(thisActivity);
    	boolean result = competitorsDBAdapter.updateCompetitor(competitorRowId, competitorName);
    	competitorsDBAdapter.close();
    	return result;
	}
    
    public static List<Competitor> getAllCompetitorsForDecision(Activity thisActivity, long decisionRowId) {
    	CompetitorsDatabaseAdapter competitorDatabaseAdapter = getDatabaseAdapter(thisActivity);
		Cursor allCompetitorsForDecision = competitorDatabaseAdapter.fetchAllCompetitorsForDecision(decisionRowId);
		List<Competitor> competitorList = CursorUtils.getCompetitorForCursor(allCompetitorsForDecision);
		competitorDatabaseAdapter.close();
		return competitorList;
    }
    
    public static List<Competitor> getCompetitorByName(Activity thisActivity, long decisionRowId, String competitorName) {
    	
    	CompetitorsDatabaseAdapter competitorsDBAdapter = getDatabaseAdapter(thisActivity);
		Cursor cCompetitors = competitorsDBAdapter.fetchCompetitor(decisionRowId, competitorName);
		List<Competitor> competitorList = CursorUtils.getCompetitorForCursor(cCompetitors);
		competitorsDBAdapter.close();
		return competitorList;
    	
    }
    
    public static List<Competitor> getCompetitor(Activity thisActivity, long competitorRowId) {
    	
    	CompetitorsDatabaseAdapter competitorsDBAdapter = getDatabaseAdapter(thisActivity);
		Cursor cCompetitors = competitorsDBAdapter.fetchCompetitor(competitorRowId);
		List<Competitor> competitorList = CursorUtils.getCompetitorForCursor(cCompetitors);
		competitorsDBAdapter.close();
		return competitorList;
    	
    }
    
    /**
     * Delete all DecisionRatings associated with the competitor as well.
     * @param thisActivity
     * @param competitorRowId
     * @return
     */
    public static boolean deleteCompetitor(Activity thisActivity, long competitorRowId) {
    	
    	CompetitorsDatabaseAdapter competitorsDBAdapter = getDatabaseAdapter(thisActivity);
    	
    	boolean ratingsDeleted = DecisionRatingsHelper.deleteRatingsForCompetitor(thisActivity, competitorRowId);
    	
    	boolean competitorDeleted = competitorsDBAdapter.deleteCompetitor(competitorRowId);
    	
    	competitorsDBAdapter.close();
    	
    	return ratingsDeleted && competitorDeleted;
    }
	
}
