package com.db;

import java.util.ArrayList;
import java.util.List;

import com.db.decideforme.DatabaseObject;
import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.decision.Decision;
import com.db.decideforme.decisionrating.DecisionRatings;
import com.db.decideforme.ratinginstance.RatingInstance;
import com.db.decideforme.ratingsystems.RatingSystem;

import android.database.Cursor;

public class CursorUtils {

	public static List<RatingSystem> getRatingSystemsForCursor(Cursor ratingSystemCursor) {
		List<RatingSystem> ratingSystemList = new ArrayList<RatingSystem>();
		
		ratingSystemCursor.moveToFirst();
		while (ratingSystemCursor.isAfterLast() == false) {
			Long rowId = ratingSystemCursor.getLong(DatabaseObject.COLUMN_INDEX_ROW_ID);
			String name = ratingSystemCursor.getString(RatingSystem.COLUMN_INDEX_NAME);
			
			RatingSystem ratingSystem = new RatingSystem(rowId, name);
			ratingSystemList.add(ratingSystem);
			
			ratingSystemCursor.moveToNext();
		}
		ratingSystemCursor.close();
		
		return ratingSystemList;
	}
	
	
	public static List<RatingInstance> getRatingInstancesForCursor(Cursor ratingInstanceCursor) {
		List<RatingInstance> ratingInstanceList = new ArrayList<RatingInstance>();
		
		ratingInstanceCursor.moveToFirst();
		while (ratingInstanceCursor.isAfterLast() == false) {
			Long rowId = ratingInstanceCursor.getLong(DatabaseObject.COLUMN_INDEX_ROW_ID);
			Long systemId = ratingInstanceCursor.getLong(RatingInstance.COLUMN_INDEX_SYSTEMID);
			Long rorder = ratingInstanceCursor.getLong(RatingInstance.COLUMN_INDEX_RORDER);
			String name = ratingInstanceCursor.getString(RatingInstance.COLUMN_INDEX_NAME);
			
			RatingInstance instance = new RatingInstance(rowId, systemId, rorder, name);
			ratingInstanceList.add(instance);
			
			ratingInstanceCursor.moveToNext();
		}
		ratingInstanceCursor.close();
		
		return ratingInstanceList;
	}
	
	public static List<DecisionRatings> getDecisionRatingsListForCursor(Cursor decisionRatingsCursor) {
		
		List<DecisionRatings> decisionRatingList = new ArrayList<DecisionRatings>();
		
		decisionRatingsCursor.moveToFirst();
		while (decisionRatingsCursor.isAfterLast() == false) {
			
			Long rowId = decisionRatingsCursor.getLong(DatabaseObject.COLUMN_INDEX_ROW_ID);
			Long decisionId = decisionRatingsCursor.getLong(DecisionRatings.COLUMN_INDEX_DECISIONID);
			Long competitorId = decisionRatingsCursor.getLong(DecisionRatings.COLUMN_INDEX_COMPETITORID);
			Long criterionId = decisionRatingsCursor.getLong(DecisionRatings.COLUMN_INDEX_CRITERIONID);
			Long ratingSelectionId = decisionRatingsCursor.getLong(DecisionRatings.COLUMN_INDEX_RATINGSELECTIONID);
			
			DecisionRatings thisRating = new DecisionRatings(rowId, decisionId, competitorId, criterionId, ratingSelectionId);
			decisionRatingList.add(thisRating);
			
			decisionRatingsCursor.moveToNext();
		}
		decisionRatingsCursor.close();
		
		return decisionRatingList;
	}
	
	
	public static List<Criterion> getCriteriaForCursor(Cursor criteriaCursor) {
		
		List<Criterion> criteriaList = new ArrayList<Criterion>();
		
		criteriaCursor.moveToFirst();
		while (criteriaCursor.isAfterLast() == false) {
			
			Long rowId = criteriaCursor.getLong(DatabaseObject.COLUMN_INDEX_ROW_ID);
			Long decisionId = criteriaCursor.getLong(Criterion.COLUMN_INDEX_DECISION_ID);
			String description = criteriaCursor.getString(Criterion.COLUMN_INDEX_DECRIPTION);
			Long ratingSystemId = criteriaCursor.getLong(Criterion.COLUMN_INDEX_RATING_SYSTEM);
			
			Criterion thisCriterion = new Criterion(rowId, decisionId, description, ratingSystemId);
			criteriaList.add(thisCriterion);
			
			criteriaCursor.moveToNext();
		}
		criteriaCursor.close();
		
		return criteriaList;
		
	}
	
	public static List<Competitor> getCompetitorForCursor(Cursor competitorCursor) {
		
		List<Competitor> competitorList = new ArrayList<Competitor>();
		
		competitorCursor.moveToFirst();
		while (competitorCursor.isAfterLast() == false) {
			
			Long rowId = competitorCursor.getLong(Competitor.COLUMN_INDEX_ROW_ID);
			Long decisionId = competitorCursor.getLong(Competitor.COLUMN_INDEX_DECISION_ID);
			String description = competitorCursor.getString(Competitor.COLUMN_INDEX_DESCRIPTION);

			Competitor thisCompetitor = new Competitor(rowId, decisionId, description);
			competitorList.add(thisCompetitor);
			
			competitorCursor.moveToNext();
		}
		competitorCursor.close();
		
		return competitorList;

	}
	
	public static List<Decision> getDecisionsForCursor(Cursor decisionCursor) {
		
		List<Decision> decisionList = new ArrayList<Decision>();
		
		decisionCursor.moveToFirst();
		while (decisionCursor.isAfterLast() == false) {
			
			Long rowId = decisionCursor.getLong(Decision.COLUMN_INDEX_ROW_ID);
			String name = decisionCursor.getString(Decision.COLUMN_INDEX_NAME);
			String description = decisionCursor.getString(Decision.COLUMN_INDEX_DESC);

			Decision thisDecision = new Decision(rowId, name, description);
			decisionList.add(thisDecision);
			
			decisionCursor.moveToNext();
		}
		decisionCursor.close();
		
		return decisionList;

	}

}
