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
    	db.execSQL("insert into ratinginstance values(2, 2, 1, 'Amber')");
    	db.execSQL("insert into ratinginstance values(3, 3, 1, 'Red')");
    	
    	db.execSQL("insert into ratingsystem values(2, 'Grades')");
    	db.execSQL("insert into ratinginstance values(4, 1, 2, 'A')");
    	db.execSQL("insert into ratinginstance values(5, 2, 2, 'B')");
    	db.execSQL("insert into ratinginstance values(6, 3, 2, 'C')");
    	db.execSQL("insert into ratinginstance values(7, 4, 2, 'D')");
    	db.execSQL("insert into ratinginstance values(8, 5, 2, 'E')");
    	db.execSQL("insert into ratinginstance values(9, 6, 2, 'F')");
    	    	
    	db.execSQL("insert into ratingsystem values(3, 'Temperature')");
    	db.execSQL("insert into ratinginstance values(10, 1, 3, 'Hottest')");
    	db.execSQL("insert into ratinginstance values(11, 2, 3, 'Hot')");
    	db.execSQL("insert into ratinginstance values(12, 3, 3, 'Warm')");
    	db.execSQL("insert into ratinginstance values(13, 4, 3, 'Lukewarm')");
    	db.execSQL("insert into ratinginstance values(14, 5, 3, 'Cold')");
    	db.execSQL("insert into ratinginstance values(15, 6, 3, 'Coldest')");
    	
    	db.execSQL("insert into ratingsystem values(4, 'How Good?')");
    	db.execSQL("insert into ratinginstance values(16, 1, 4, 'The Best')");
    	db.execSQL("insert into ratinginstance values(17, 2, 4, 'Very Good')");
    	db.execSQL("insert into ratinginstance values(18, 3, 4, 'Good')");
    	db.execSQL("insert into ratinginstance values(19, 4, 4, 'Bad')");
    	db.execSQL("insert into ratinginstance values(20, 5, 4, 'Very Bad')");
    	db.execSQL("insert into ratinginstance values(21, 6, 4, 'The Worst')");
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
}
