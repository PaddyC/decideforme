package com.db.decideforme.decisionrating;

import android.provider.BaseColumns;

public class DecisionRatings {
	
	private DecisionRatings() {
	}
	public static final String TABLE_NAME = "decisionratings";
	
	public static final Integer COLUMN_INDEX_ROWID = 0;
	public static final Integer COLUMN_INDEX_DECISIONID = 1;
	public static final Integer COLUMN_INDEX_CRITERIONID = 2;
	public static final Integer COLUMN_INDEX_COMPETITORID = 3;
	public static final Integer COLUMN_INDEX_RATINGSELECTIONID = 4;
	
	public static final class DecisionRatingsColumns implements BaseColumns {
		
		public static final String DECISIONID = "decisionid";
		public static final String CRITERIONID = "criterionid";
		public static final String COMPETITORID = "competitorid";
		public static final String RATINGSELECTIONID = "ratingselectionid";
		
	}
}
