package com.db.decideforme.competitors;

import android.provider.BaseColumns;

public final class Competitor {
	
	public static final String TABLE_NAME = "competitor";
	
	// This class cannot be instantiated.
	private Competitor() {
	};

	public static final class CompetitorColumns implements BaseColumns {
		
		public static final String DECISIONID = "decisionid";
		
		public static final String DESCRIPTION = "description";
		
	}
}
