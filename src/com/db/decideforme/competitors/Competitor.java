package com.db.decideforme.competitors;

import com.db.decideforme.DatabaseObject;

import android.provider.BaseColumns;

public final class Competitor extends DatabaseObject {
	
	public static final String TABLE_NAME = "competitor";

	public static final Integer COLUMN_INDEX_DECISION_ID = 1;
	public static final Integer COLUMN_INDEX_DESCRIPTION = 2;
	
	private Long decisionId;
	private String description;
	public Competitor(Long rowId, Long decisionId, String description) {
		this.rowId = rowId;
		this.decisionId = decisionId;
		this.description = description;
	};

	public Long getDecisionId() {
		return decisionId;
	}

	public String getDescription() {
		return description;
	}

	public static final class CompetitorColumns implements BaseColumns {
		
		public static final String DECISIONID = "decisionid";
		
		public static final String DESCRIPTION = "description";
		
	}
}
