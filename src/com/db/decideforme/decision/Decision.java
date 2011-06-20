package com.db.decideforme.decision;

import android.provider.BaseColumns;

import com.db.decideforme.DatabaseObject;

public final class Decision extends DatabaseObject {
	
	public static final String TABLE_NAME = "decision";
	
	public static final Integer COLUMN_INDEX_NAME = 1;
	public static final Integer COLUMN_INDEX_DESC = 2;
	
	private String name;
	private String description;
	public Decision(Long rowId, String name, String description) {
		this.rowId = rowId;
		this.name = name;
		this.description = description;
	};
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}

	public static final class DecisionColumns implements BaseColumns {
		
		public static final String NAME = "decisionname";
		
		public static final String DESCRIPTION = "decisiondescription";
		
	}
}
