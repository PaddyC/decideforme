package com.db.decideforme.decision;

import android.provider.BaseColumns;

public final class Decision {
	
	public static final String TABLE_NAME = "decision";
	
	// This class cannot be instantiated.
	private Decision() {
	};

	public static final class DecisionColumns implements BaseColumns {
		
		public static final String NAME = "decisionname";
		
		public static final String DESCRIPTION = "decisiondescription";
		
	}
}
