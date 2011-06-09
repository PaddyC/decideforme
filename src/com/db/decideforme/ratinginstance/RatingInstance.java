package com.db.decideforme.ratinginstance;

import android.provider.BaseColumns;

public class RatingInstance {
	
	private RatingInstance() {
	}

	public static final String TABLE_NAME = "ratinginstance";
	
	public static final class RatingInstanceColumns implements BaseColumns {
		
		public static final String SYSTEM_ID = "systemid";
		
		public static final String RORDER = "rorder";
		
		public static final String NAME = "name";
		
	}
}
