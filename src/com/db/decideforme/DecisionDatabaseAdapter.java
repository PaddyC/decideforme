package com.db.decideforme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.decideforme.utils.StringUtils;

public class DecisionDatabaseAdapter {
	private static final String TAG = "DecisionDatabaseAdapter";
	
    public static final String TABLE_NAME_DECISION = "decision";
	
    private final Context mContext;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
	
    
    public static class DatabaseHelper extends SQLiteOpenHelper {
    	
		private static final String TAG = DatabaseHelper.class.getName();

        public DatabaseHelper(Context context) {
            super(context, DatabaseScriptConstants.DB_NAME, null, DatabaseScriptConstants.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	Log.d(TAG, ">> about to run SQL: " + DatabaseScriptConstants.DATABASE_CREATE);
            db.execSQL(DatabaseScriptConstants.DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL(DatabaseScriptConstants.DATABASE_DROP);
            onCreate(db);
        }
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
        initialValues.put(DatabaseAttributeConstants.FIELD_NAME_DECISION_NAME, decisionName);
        initialValues.put(DatabaseAttributeConstants.FIELD_NAME_DECISION_DESCRIPTION, decisionDescription);

        long insertResult = mDb.insert(DatabaseAttributeConstants.TABLE_NAME_DECISION, null, initialValues);
        
        Log.d(TAG, " << createDecision(), returned " + StringUtils.objectAsString(insertResult));
		return insertResult;
    }
    
    public boolean deleteDecision(long rowId) {
    	Log.d(TAG, " >> deleteDecision(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "')");
    	
        boolean deleteResult = mDb.delete(
        		DatabaseAttributeConstants.TABLE_NAME_DECISION, 
        		DatabaseAttributeConstants.FIELD_NAME_DECISION_KEY_ROWID + "=" + rowId, null) > 0;
        
        Log.d(TAG, " << deleteDecision(), returned '" + StringUtils.objectAsString(deleteResult) + "'");
		return deleteResult;
    }
    
    public Cursor fetchAllDecisions() {
    	Log.d(TAG, " >> fetchAllDecisions()");
        Cursor resultOfFetchQuery = mDb.query(DatabaseAttributeConstants.TABLE_NAME_DECISION, 
        		new String[] {DatabaseAttributeConstants.FIELD_NAME_DECISION_KEY_ROWID, 
        		DatabaseAttributeConstants.FIELD_NAME_DECISION_NAME, 
        		DatabaseAttributeConstants.FIELD_NAME_DECISION_DESCRIPTION}, 
        		null, null, null, null, null);
        
        Log.d(TAG, " << fetchAllDecisions(), returned " + StringUtils.objectAsString(resultOfFetchQuery));
		return resultOfFetchQuery;
    }
    
    public Cursor fetchDecision(long rowId) throws SQLException {
    	Log.d(TAG, " >> fetchDecision(" +
    			"rowId '" + StringUtils.objectAsString(rowId) + "')");
    	
        Cursor mCursor = mDb.query(
            		true, 
            		DatabaseAttributeConstants.TABLE_NAME_DECISION, 
            		new String[] {DatabaseAttributeConstants.FIELD_NAME_DECISION_KEY_ROWID,
            					  DatabaseAttributeConstants.FIELD_NAME_DECISION_NAME, 
            					  DatabaseAttributeConstants.FIELD_NAME_DECISION_DESCRIPTION},
            		DatabaseAttributeConstants.FIELD_NAME_DECISION_KEY_ROWID + "=" + rowId, 
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
        args.put(DatabaseAttributeConstants.FIELD_NAME_DECISION_NAME, decisionName);
        args.put(DatabaseAttributeConstants.FIELD_NAME_DECISION_DESCRIPTION, decisionDescrption);

        boolean resultOfUpdate = mDb.update(
        		DatabaseAttributeConstants.TABLE_NAME_DECISION, 
        		args, 
        		DatabaseAttributeConstants.FIELD_NAME_DECISION_KEY_ROWID + "=" + rowId, null) > 0;

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

