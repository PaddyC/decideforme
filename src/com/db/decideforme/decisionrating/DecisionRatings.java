package com.db.decideforme.decisionrating;

import android.provider.BaseColumns;

import com.db.decideforme.DatabaseObject;

public class DecisionRatings extends DatabaseObject {
	
	private Long decisionId;
	private Long criterionId;
	private Long competitorId;
	private Long ratingSelectionId;
	
	public DecisionRatings(Long rowId, Long decisionId, Long competitorId, Long criterionId, Long ratingSelectionId) {
		this.rowId = rowId;
		this.decisionId = decisionId;
		this.competitorId =competitorId;
		this.criterionId = criterionId;
		this.ratingSelectionId = ratingSelectionId;
	}
	
	public Long getDecisionId() {
		return decisionId;
	}

	public Long getCriterionId() {
		return criterionId;
	}

	public Long getCompetitorId() {
		return competitorId;
	}

	public Long getRatingSelectionId() {
		return ratingSelectionId;
	}

	public static final String TABLE_NAME = "decisionratings";
	
	public static final Integer COLUMN_INDEX_DECISIONID = 1;
	public static final Integer COLUMN_INDEX_COMPETITORID = 2;
	public static final Integer COLUMN_INDEX_CRITERIONID = 3;
	public static final Integer COLUMN_INDEX_RATINGSELECTIONID = 4;
	
	public static final class DecisionRatingsColumns implements BaseColumns {
		
		public static final String DECISIONID = "decisionid";
		public static final String COMPETITORID = "competitorid";
		public static final String CRITERIONID = "criterionid";
		public static final String RATINGSELECTIONID = "ratingselectionid";
		
	}
}
