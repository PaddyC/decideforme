package com.db.decideforme;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.decideforme.Decision;
import com.decideforme.Decision.DecisionColumns;
import com.decideforme.utils.StringUtils;

public class DecisionDatabaseAdapter {
	private static final String TAG = "DecisionDatabaseAdapter";
	
    private final Context mContext;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
	
    
    public static class DatabaseHelper extends SQLiteOpenHelper {
    	
		private static final String TAG = DatabaseHelper.class.getName();

        public DatabaseHelper(Context context) {
            super(context, DatabaseScripts.DB_NAME, null, DatabaseScripts.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	Log.d(TAG, ">> onCreate: " + StringUtils.objectAsString(db));

        	DatabaseScripts.createAllTables(db);
            
            Log.d(TAG, " << onCreate()");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            
            DatabaseScripts.dropAllTables(db);
            
            onCreate(db);
        }
    }
    
    public static class CursorHelper extends Activity {
    	
    	public void getFirstRow() {
    		
    	}
    	
    }
    
    public boolean recreateTheDatabase() {
    	Log.d(TAG, " >> recreateTheDatabase()");
    	boolean databaseRecreated = false;
        // Should only be rarely needed: re-create the database from scratch.
        SQLiteDatabase mdb = getmDbHelper().getWritableDatabase();
        Log.d(TAG, ">> Dropping tables.");
        DatabaseScripts.dropAllTables(mdb);
        Log.d(TAG, ">> Creating tables.");
        DatabaseScripts.createAllTables(mdb);
        Log.d(TAG, ">> The database is back..");
    	
        databaseRecreated = true;
        
    	Log.d(TAG, " << recreateTheDatabase(), returned '" + StringUtils.objectAsString(databaseRecreated) + "'");
    	return databaseRecreated;
    }
    
    public Integer getNextDecisionSequenceID() {
    	Log.d(TAG, " >> getNextDecisionSequenceID()");
    	Integer nextRowId = 0;
    	Cursor resultOfFetchQuery = mDb.query(Decision.TABLE_NAME, 
        		new String[] {DecisionColumns._ID, 
        		DecisionColumns.NAME, 
        		DecisionColumns.DESCRIPTION}, 
        		null, null, null, null, DecisionColumns._ID);
    	Log.d(TAG, "Query run ok");
    	
    	resultOfFetchQuery.moveToLast();
    	nextRowId = resultOfFetchQuery.getInt(0);
    	resultOfFetchQuery.close();
    	Log.d(TAG, " << getNextDecisionSequenceID(), returned '" + StringUtils.objectAsString(nextRowId) + "'");
    	return nextRowId;
    }
    
    
	/**
	 * 
	 * @param Context ctx
	 * @author PaddyC
	 */
    public DecisionDatabaseAdapter(Context ctx) {
        this.mContext = ctx;
    }
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @author PaddyC
	 */
    public DecisionDatabaseAdapter open() throws SQLException {
        setmDbHelper(new DatabaseHelper(mContext));
        mDb = getmDbHelper().getWritableDatabase();
        return this;
    }

    public void close() {
        getmDbHelper().close();
    }
    
    
    public long createDecision(String decisionName, String decisionDescription) {
    	Log.d(TAG, " >> createDecision(" +
    			"decisionName " + StringUtils.objectAsString(decisionName) + "', " +
    			"decisionDescription " + StringUtils.objectAsString(decisionDescription) + "')");

    	ContentValues initialValues = new ContentValues();
        initialValues.put(DecisionColumns.NAME, decisionName);
        initialValues.put(DecisionColumns.DESCRIPTION, decisionDescription);

        long insertResult = mDb.insert(Decision.TABLE_NAME, null, initialValues);
        
        Log.d(TAG, " << createDecision(), returned " + StringUtils.objectAsString(insertResult));
		return insertResult;
    }
    
    public boolean deleteDecision(long rowId) {
    	Log.d(TAG, " >> deleteDecision(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "')");
    	
        boolean deleteResult = mDb.delete(
        		Decision.TABLE_NAME, 
        		DecisionColumns._ID + "=" + rowId, null) > 0;
        
        Log.d(TAG, " << deleteDecision(), returned '" + StringUtils.objectAsString(deleteResult) + "'");
		return deleteResult;
    }
    
    public Cursor fetchAllDecisions() {
    	Log.d(TAG, " >> fetchAllDecisions()");
        Cursor resultOfFetchQuery = mDb.query(Decision.TABLE_NAME, 
        		new String[] {DecisionColumns._ID, 
        		DecisionColumns.NAME, 
        		DecisionColumns.DESCRIPTION}, 
        		null, null, null, null, null);
        
        Log.d(TAG, " << fetchAllDecisions(), returned " + StringUtils.objectAsString(resultOfFetchQuery));
		return resultOfFetchQuery;
    }
    
    public Cursor fetchAllDecisionNames() {
    	Log.d(TAG, " >> fetchAllDecisionNames()");
        
    	Cursor resultOfFetchQuery = mDb.query(Decision.TABLE_NAME, 
        		new String[] {DecisionColumns.NAME}, 
        		null, null, null, null, DecisionColumns.NAME);
        
        Log.d(TAG, " << fetchAllDecisionNames(), returned " + StringUtils.objectAsString(resultOfFetchQuery));
		return resultOfFetchQuery;
    }
    
    
    public void deleteDecisionByName(String decisionName) {
    	Log.d(TAG, " >> deleteDecisionByName(" +
    			"decisionName '" + StringUtils.objectAsString(decisionName) + "')");
        
        Log.d(TAG, " << deleteDecisionByName()");
    }
    
    
    
    public Cursor fetchDecision(long rowId) throws SQLException {
    	Log.d(TAG, " >> fetchDecision(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "')");
    	
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
    
    public boolean updateDecision(long rowId, String decisionName, String decisionDescrption) {
    	Log.d(TAG, " >> updateDecision(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "', " +
    			"decisionName '" + StringUtils.objectAsString(decisionName) + "', " +
    			"decisionBody '" + StringUtils.objectAsString(decisionDescrption) + "')");
    	
        ContentValues args = new ContentValues();
        args.put(DecisionColumns.NAME, decisionName);
        args.put(DecisionColumns.DESCRIPTION, decisionDescrption);

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

