package com.db.decideforme.ratingsystems;

import android.provider.BaseColumns;

import com.db.decideforme.DatabaseObject;

public class RatingSystem extends DatabaseObject {
	
	public static final String TABLE_NAME = "ratingsystem";
	
	private String name;
	public RatingSystem(Long rowId, String name) {
		this.rowId = rowId;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static final Integer COLUMN_INDEX_NAME = 1;
	
	public static final class RatingSystemColumns implements BaseColumns {
		
		public static final String NAME = "name";
		
	}
}
