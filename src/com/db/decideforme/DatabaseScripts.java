package com.db.decideforme;

import android.database.sqlite.SQLiteDatabase;

public class DatabaseScripts {

    public static final String DB_NAME = "data";
    public static final int DB_VERSION = 2;
	
    public static final String TABLE_DECISION_CREATE = 
    	"create table decision (_id integer primary key autoincrement, " +
    	"decisionname text not null, decisiondescription text not null);";
    public static final String TABLE_DECISION_DROP = 
    	"drop table if exists decision";
    
    public static final String TABLE_CRITERION = "criterion";
    public static final String TABLE_CRITERION_CREATE = 
    	"create table criterion (_id integer primary key autoincrement, " +
    	"decisionid integer, description text not null, ratingsystem integer);";
    public static final String TABLE_CRITERION_DROP = 
    	"drop table if exists criterion";
    
    public static final String TABLE_COMPETITOR = "competitor";
    public static final String TABLE_COMPETITOR_CREATE = 
    	"create table competitor (_id integer primary key autoincrement, " +
    	"decisionid integer, description text not null);";
    public static final String TABLE_COMPETITOR_DROP = 
    	"drop table if exists competitor";
    
    public static final String TABLE_RECIPIENT = "recipient";
    public static final String TABLE_RECIPIENT_CREATE = 
    	"create table recipient (_id integer primary key autoincrement, " +
    	"decisionid integer, sentto text not null);";
    public static final String TABLE_RECIPIENT_DROP = 
    	"drop table if exists recipient";
    
    public static final String TABLE_DECISIONRATINGS = "decisionratings";
    public static final String TABLE_DECISIONRATINGS_CREATE = 
    	"create table decisionratings (_id integer primary key autoincrement, " +
    	"decisionid integer, competitorid integer, " +
    	"criterionid integer, ratingselectionid integer);";
    public static final String TABLE_DECISIONRATINGS_DROP = 
    	"drop table if exists decisionratings";
    
    public static final String TABLE_RATINGSYSTEM = "ratingsystem";
    public static final String TABLE_RATINGSYSTEM_CREATE = 
    	"create table ratingsystem (_id integer primary key autoincrement, " +
    	"name text not null);";

    public static final String TABLE_RATINGSYSTEM_DROP = 
    	"drop table if exists ratingsystem";
    
    public static final String TABLE_RATINGINSTANCE = "ratinginstance";
    public static final String TABLE_RATINGINSTANCE_CREATE = 
    	"create table ratinginstance (_id integer primary key autoincrement, " +
    	"systemid integer, rorder integer, name text not null);";
    public static final String TABLE_RATINGINSTANCE_DROP = 
    	"drop table if exists ratinginstance";
    
    
    public static void addRatingSystems(SQLiteDatabase db) {
    	db.execSQL("insert into ratingsystem values(1, 'Traffic Lights')");
    	db.execSQL("insert into ratinginstance values(1, 1, 1, 'Green')");
    	db.execSQL("insert into ratinginstance values(2, 1, 2, 'Amber')");
    	db.execSQL("insert into ratinginstance values(3, 1, 3, 'Red')");
    	
    	db.execSQL("insert into ratingsystem values(2, 'Grades')");
    	db.execSQL("insert into ratinginstance values(4, 2, 1, 'A')");
    	db.execSQL("insert into ratinginstance values(5, 2, 2, 'B')");
    	db.execSQL("insert into ratinginstance values(6, 2, 3, 'C')");
    	db.execSQL("insert into ratinginstance values(7, 2, 4, 'D')");
    	db.execSQL("insert into ratinginstance values(8, 2, 5, 'E')");
    	db.execSQL("insert into ratinginstance values(9, 2, 6, 'F')");
    	    	
    	db.execSQL("insert into ratingsystem values(3, 'Temperature')");
    	db.execSQL("insert into ratinginstance values(10, 3, 1, 'Hottest')");
    	db.execSQL("insert into ratinginstance values(11, 3, 2, 'Hot')");
    	db.execSQL("insert into ratinginstance values(12, 3, 3, 'Warm')");
    	db.execSQL("insert into ratinginstance values(13, 3, 4, 'Lukewarm')");
    	db.execSQL("insert into ratinginstance values(14, 3, 5, 'Cold')");
    	db.execSQL("insert into ratinginstance values(15, 3, 6, 'Coldest')");
    	
    	db.execSQL("insert into ratingsystem values(4, 'How Good?')");
    	db.execSQL("insert into ratinginstance values(16, 4, 1, 'The Best')");
    	db.execSQL("insert into ratinginstance values(17, 4, 2, 'Very Good')");
    	db.execSQL("insert into ratinginstance values(18, 4, 3, 'Good')");
    	db.execSQL("insert into ratinginstance values(19, 4, 4, 'Bad')");
    	db.execSQL("insert into ratinginstance values(20, 4, 5, 'Very Bad')");
    	db.execSQL("insert into ratinginstance values(21, 4, 6, 'The Worst')");
    }
    
    public static void dropAllTables(SQLiteDatabase db) {
        db.execSQL(TABLE_DECISION_DROP);
        db.execSQL(TABLE_COMPETITOR_DROP);
        db.execSQL(TABLE_CRITERION_DROP);
        db.execSQL(TABLE_DECISIONRATINGS_DROP);
        
        db.execSQL(TABLE_RATINGSYSTEM_DROP);
        db.execSQL(TABLE_RATINGINSTANCE_DROP);
        
        db.execSQL(TABLE_RECIPIENT_DROP);	
    }
    
    public static void createAllTables(SQLiteDatabase db) {
        db.execSQL(TABLE_DECISION_CREATE);
        db.execSQL(TABLE_COMPETITOR_CREATE);
        db.execSQL(TABLE_CRITERION_CREATE);
        db.execSQL(TABLE_DECISIONRATINGS_CREATE);
        
        db.execSQL(TABLE_RATINGSYSTEM_CREATE);
        db.execSQL(TABLE_RATINGINSTANCE_CREATE);
        addRatingSystems(db);
        
        db.execSQL(TABLE_RECIPIENT_CREATE);	
    }

	public static void createSampleDecision(SQLiteDatabase db) {
		
		db.execSQL("insert into decision values(1, 'Sample: Which is the best movie?', '(Just a sample decision, but a tough one!)')");
		
		db.execSQL("insert into competitor values(1, 1, 'Chinatown')");
		db.execSQL("insert into competitor values(2, 1, 'Airplane')");
		db.execSQL("insert into competitor values(3, 1, 'Fight Club')");
		db.execSQL("insert into competitor values(4, 1, 'The Princess Bride')");
		
		db.execSQL("insert into criterion values(1, 1, 'Acting', 2)");
		db.execSQL("insert into criterion values(2, 1, 'Romance', 3)");
		db.execSQL("insert into criterion values(3, 1, 'Script', 4)");
		db.execSQL("insert into criterion values(4, 1, 'Comedy', 4)");
		db.execSQL("insert into criterion values(5, 1, 'Drama', 4)");
		
		// Chinatown
		db.execSQL("insert into decisionratings values(1, 1, 1, 1, 4)");
		db.execSQL("insert into decisionratings values(2, 1, 1, 2, 10)");
		db.execSQL("insert into decisionratings values(3, 1, 1, 3, 16)");
		db.execSQL("insert into decisionratings values(4, 1, 1, 4, 19)");
		db.execSQL("insert into decisionratings values(5, 1, 1, 5, 17)");

		// Airplane
		db.execSQL("insert into decisionratings values(6, 1, 2, 1, 7)");
		db.execSQL("insert into decisionratings values(7, 1, 2, 2, 12)");
		db.execSQL("insert into decisionratings values(8, 1, 2, 3, 16)");
		db.execSQL("insert into decisionratings values(9, 1, 2, 4, 17)");
		db.execSQL("insert into decisionratings values(10, 1, 2, 5, 18)");
		
		// Fight Club
		db.execSQL("insert into decisionratings values(11, 1, 3, 1, 5)");
		db.execSQL("insert into decisionratings values(12, 1, 3, 2, 11)");
		db.execSQL("insert into decisionratings values(13, 1, 3, 3, 17)");
		db.execSQL("insert into decisionratings values(14, 1, 3, 4, 18)");
		db.execSQL("insert into decisionratings values(15, 1, 3, 5, 17)");
		
		// Princess Bride
		db.execSQL("insert into decisionratings values(16, 1, 4, 1, 6)");
		db.execSQL("insert into decisionratings values(17, 1, 4, 2, 11)");
		db.execSQL("insert into decisionratings values(18, 1, 4, 3, 17)");
		db.execSQL("insert into decisionratings values(19, 1, 4, 4, 17)");
		db.execSQL("insert into decisionratings values(20, 1, 4, 5, 18)");

	}
}
