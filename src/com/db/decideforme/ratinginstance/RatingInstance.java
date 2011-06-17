package com.db.decideforme.ratinginstance;

import android.provider.BaseColumns;

import com.db.decideforme.DatabaseObject;

public class RatingInstance extends DatabaseObject {
	
	private Long system;
	private Long rorder;
	private String name;
	public RatingInstance(Long rowId, Long system, Long rorder, String name) {
		this.rowId = rowId;
		this.system = system;
		this.rorder = rorder;
		this.name = name;
	}
	
	public Long getSystem() {
		return system;
	}

	public Long getRorder() {
		return rorder;
	}

	public String getName() {
		return name;
	}

	public static final String TABLE_NAME = "ratinginstance";

	public static final Integer COLUMN_INDEX_SYSTEMID = 1;
	public static final Integer COLUMN_INDEX_RORDER = 2;
	public static final Integer COLUMN_INDEX_NAME = 3;
	

	public static final class RatingInstanceColumns implements BaseColumns {
		
		public static final String SYSTEM_ID = "systemid";
		
		public static final String RORDER = "rorder";
		
		public static final String NAME = "name";
		
	}
}
