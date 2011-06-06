package com.db.decideforme;

public class DatabaseScriptConstants {

    public static final String DB_NAME = "data";
    public static final int DB_VERSION = 2;
	
    public static final String TABLE_DECISION_CREATE = 
    	"create table decision (_id integer primary key autoincrement, " +
    	"decisionname text not null, decisiondescription text not null);";
    
    public static final String TABLE_CRITERION = "criterion";
    public static final String TABLE_CRITERION_CREATE = 
    	"create table criterion (_id integer primary key autoincrement, " +
    	"decisionid integer, description text not null);";
    
    public static final String TABLE_COMPETITOR = "competitor";
    public static final String TABLE_COMPETITOR_CREATE = 
    	"create table criterion (_id integer primary key autoincrement, " +
    	"decisionid integer, description text not null);";
    
    public static final String TABLE_RATINGSYSTEM = "ratingsystem";
    public static final String TABLE_RATINGSYSTEM_CREATE = 
    	"create table criterion (_id integer primary key autoincrement, " +
    	"name text not null);";
    
    public static final String TABLE_RATINGSELECTION = "ratingselection";
    public static final String TABLE_RATINGSELECTION_CREATE = 
    	"create table criterion (_id integer primary key autoincrement, " +
    	"systemid integer, order integer, name text not null);";
    
    public static final String TABLE_RECIPIENT = "recipient";
    public static final String TABLE_RECIPIENT_CREATE = 
    	"create table criterion (_id integer primary key autoincrement, " +
    	"decisionid integer, sentto text not null);";
    
    public static final String TABLE_DECISIONRATINGS = "decisionratings";
    public static final String TABLE_DECISIONRATINGS_CREATE = 
    	"create table criterion (_id integer primary key autoincrement, " +
    	"decisionid integer, competitorid integer, " +
    	"criterionid integer, ratingselectionid integer);";
    
    public static final String DATABASE_CREATE =
    	TABLE_DECISION_CREATE +
    	TABLE_CRITERION_CREATE +
    	TABLE_COMPETITOR_CREATE + 
    	TABLE_RATINGSYSTEM_CREATE + 
    	TABLE_RATINGSELECTION_CREATE + 
    	TABLE_RECIPIENT_CREATE + 
    	TABLE_DECISIONRATINGS_CREATE;
    
    public static final String DATABASE_DROP = "DROP TABLE IF EXISTS decision, criterion, competitor, ratingssystem, ratingselection, recipient, decisionratings";
}
