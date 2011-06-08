package com.db.decideforme.criteria;

import android.provider.BaseColumns;

public class Criterion {
	
	public static final String TABLE_NAME = "criterion";
	
	// This class cannot be instantiated.
	private Criterion() {
	}

	public static final class CriterionColumns implements BaseColumns {
		
		public static final String DECISIONID = "decisionid";
		
		public static final String DESCRIPTION = "description";
		
		public static final String RATINGSYSTEM = "ratingsystem";
		
	}
}
