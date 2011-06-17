package com.db.decideforme.criteria;

import com.db.decideforme.DatabaseObject;

import android.provider.BaseColumns;

public class Criterion extends DatabaseObject {
	
	public static final String TABLE_NAME = "criterion";
	
	public static final Integer COLUMN_INDEX_DECISION_ID = 1;
	public static final Integer COLUMN_INDEX_DECRIPTION = 2;
	public static final Integer COLUMN_INDEX_RATING_SYSTEM = 3;
	
	private Long decisionId;
	private String description;
	private Long ratingSystemId;
	
	public Criterion(Long rowId, Long decisionId, String description, Long ratingSystemId) {
		this.rowId = rowId;
		this.decisionId = decisionId;
		this.description = description;
		this.ratingSystemId = ratingSystemId;
	}

	public Long getDecisionId() {
		return decisionId;
	}

	public String getDescription() {
		return description;
	}

	public Long getRatingSystemId() {
		return ratingSystemId;
	}

	public static final class CriterionColumns implements BaseColumns {
		
		public static final String DECISIONID = "decisionid";
		
		public static final String DESCRIPTION = "description";
		
		public static final String RATINGSYSTEM = "ratingsystem";
		
	}
}
