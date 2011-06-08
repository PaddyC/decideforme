package com.db.decideforme.ratingsystems;

import android.provider.BaseColumns;

public class RatingSystem {
	
	public static final String TABLE_NAME = "ratingsystem";
	
	// This class cannot be instantiated.
	private RatingSystem() {
	}

	public static final class RatingSystemColumns implements BaseColumns {
		
		public static final String NAME = "name";
		
	}
}
