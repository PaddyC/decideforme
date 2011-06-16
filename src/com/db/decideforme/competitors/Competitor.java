package com.db.decideforme.competitors;

import com.db.decideforme.DatabaseObject;

import android.provider.BaseColumns;

public final class Competitor extends DatabaseObject {
	
	public static final String TABLE_NAME = "competitor";

	public static final Integer COLUMN_INDEX_DECISION_ID = 1;
	public static final Integer COLUMN_INDEX_DESCRIPTION = 2;
	
	// This class cannot be instantiated.
	private Competitor() {
	};

	public static final class CompetitorColumns implements BaseColumns {
		
		public static final String DECISIONID = "decisionid";
		
		public static final String DESCRIPTION = "description";
		
	}
}
