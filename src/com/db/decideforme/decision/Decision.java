package com.db.decideforme.decision;

import android.provider.BaseColumns;

import com.db.decideforme.DatabaseObject;

public final class Decision extends DatabaseObject {
	
	public static final String TABLE_NAME = "decision";
	
	public static final Integer COLUMN_INDEX_NAME = 1;
	public static final Integer COLUMN_INDEX_DESC = 2;
	
	// This class cannot be instantiated.
	private Decision() {
	};

	public static final class DecisionColumns implements BaseColumns {
		
		public static final String NAME = "decisionname";
		
		public static final String DESCRIPTION = "decisiondescription";
		
	}
}
