package com.db.decideforme.ratinginstance;

import android.provider.BaseColumns;

import com.db.decideforme.DatabaseObject;

public class RatingInstance extends DatabaseObject {
	
	private RatingInstance() {
	}
	public static final String TABLE_NAME = "ratinginstance";

	public static final Integer COLUMN_INDEX_SYSTEMID = 1;
	public static final Integer COLUMN_INDEX_RORDER = 2;
	public static final Integer COLUMN_INDEX_NAME = 1;
	

	public static final class RatingInstanceColumns implements BaseColumns {
		
		public static final String SYSTEM_ID = "systemid";
		
		public static final String RORDER = "rorder";
		
		public static final String NAME = "name";
		
	}
}
