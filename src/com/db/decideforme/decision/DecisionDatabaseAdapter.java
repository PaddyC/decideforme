package com.db.decideforme.decision;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.db.decideforme.DatabaseScripts;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.utils.StringUtils;
/**
 * 
 * @author PaddyC
 *
 */
public class DecisionDatabaseAdapter {
	private static final String TAG = "DecisionDatabaseAdapter";
	
    private final Context mContext;
    private DatabaseHelper mDbHelper;
    protected SQLiteDatabase mDb;
	
    public DecisionDatabaseAdapter(Context ctx) {
    	this.mContext = ctx;
    }
    
    public static class DatabaseHelper extends SQLiteOpenHelper {
    	
		private static final String TAG = DatabaseHelper.class.getName();

        public DatabaseHelper(Context context) {
            super(context, DatabaseScripts.DB_NAME, null, DatabaseScripts.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	DatabaseScripts.createAllTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            
            DatabaseScripts.dropAllTables(db);
            
            onCreate(db);
        }
    }
    
    public boolean recreateTheDatabase() {
    	Log.d(TAG, " >> recreateTheDatabase()");
    	boolean databaseRecreated = false;
        // Should only be rarely needed: re-create the database from scratch.
        SQLiteDatabase mdb = getmDbHelper().getWritableDatabase();
        DatabaseScripts.dropAllTables(mdb);
        DatabaseScripts.createAllTables(mdb);
        
        databaseRecreated = true;
        
    	Log.d(TAG, " << recreateTheDatabase(), returned '" + StringUtils.objectAsString(databaseRecreated) + "'");
    	return databaseRecreated;
    }
    
    public Integer getNextDecisionSequenceID() {
    	Integer nextRowId = 0;
    	Cursor resultOfFetchQuery = mDb.query(Decision.TABLE_NAME, 
        		new String[] {DecisionColumns._ID, 
        		DecisionColumns.NAME, 
        		DecisionColumns.DESCRIPTION}, 
        		null, null, null, null, DecisionColumns._ID);

		if (resultOfFetchQuery.getCount() > 0) {
    		resultOfFetchQuery.moveToLast();
    		nextRowId = resultOfFetchQuery.getInt(0);	
    	}
		
    	nextRowId++;
    	
    	return nextRowId;
    }
    
	

    public DecisionDatabaseAdapter open() throws SQLException {
        setmDbHelper(new DatabaseHelper(mContext));
        mDb = getmDbHelper().getWritableDatabase();
        return this;
    }

    public void close() {
        getmDbHelper().close();
    }
    
    
    public long createDecision(String decisionName, String decisionDescription) {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(DecisionColumns.NAME, escapeString(decisionName));
        initialValues.put(DecisionColumns.DESCRIPTION, escapeString(decisionDescription));

        long insertResult = mDb.insert(Decision.TABLE_NAME, null, initialValues);
        
        Log.d(TAG, " << createDecision(), returned " + StringUtils.objectAsString(insertResult));
		return insertResult;
    }
    
    public boolean deleteDecision(long rowId) {
    	boolean deleteResult = mDb.delete(
        		Decision.TABLE_NAME, 
        		DecisionColumns._ID + "=" + rowId, null) > 0;
        
        Log.d(TAG, " << deleteDecision(), returned '" + StringUtils.objectAsString(deleteResult) + "'");
		return deleteResult;
    }
    
    public Cursor fetchAllDecisions() {
    	Cursor resultOfFetchQuery = mDb.query(Decision.TABLE_NAME, 
        		new String[] {DecisionColumns._ID, 
        		DecisionColumns.NAME, 
        		DecisionColumns.DESCRIPTION}, 
        		null, null, null, null, null);
        
    	return resultOfFetchQuery;
    }
    
    
    public Cursor fetchDecision(long rowId) throws SQLException {
    	Cursor mCursor = mDb.query(
            		true, 
            		Decision.TABLE_NAME, 
            		new String[] {DecisionColumns._ID, 
                    		DecisionColumns.NAME, 
                    		DecisionColumns.DESCRIPTION},
                    DecisionColumns._ID + "=" + rowId, 
            		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        Log.d(TAG, "<< fetchDecision(), returned '" + StringUtils.objectAsString(mCursor) + "'");
        return mCursor;
    }
    
    public String escapeString(String inputString) {
    	String returnString = inputString.replace("'", "''");
    	return returnString;
    }
    
    public Cursor fetchDecision(String name) throws SQLException {
    	Cursor mCursor = mDb.query(
            		true, 
            		Decision.TABLE_NAME, 
            		new String[] {DecisionColumns._ID, 
                    		DecisionColumns.NAME, 
                    		DecisionColumns.DESCRIPTION},
                    DecisionColumns.NAME + "='" + escapeString(name) + "'", 
            		null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        
        Log.d(TAG, "<< fetchDecision(), returned '" + StringUtils.objectAsString(mCursor) + "'");
        return mCursor;
    }
    
    public boolean updateDecision(long rowId, String decisionName, String decisionDescrption) {
    	ContentValues args = new ContentValues();
        args.put(DecisionColumns.NAME, escapeString(decisionName));
        args.put(DecisionColumns.DESCRIPTION, escapeString(decisionDescrption));

        boolean resultOfUpdate = mDb.update(
        		Decision.TABLE_NAME, 
        		args, 
        		DecisionColumns._ID + "=" + rowId, null) > 0;

        Log.d(TAG, "<< updateDecision(), returned '" + StringUtils.objectAsString(resultOfUpdate) + "'");
		return resultOfUpdate;
    }

	public void setmDbHelper(DatabaseHelper mDbHelper) {
		this.mDbHelper = mDbHelper;
	}

	public DatabaseHelper getmDbHelper() {
		return mDbHelper;
	}
}

