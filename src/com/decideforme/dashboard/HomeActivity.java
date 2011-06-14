package com.decideforme.dashboard;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.db.decideforme.DatabaseScripts;
import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.decideforme.R;

public class HomeActivity extends DashboardActivity	{
	public static final String TAG = HomeActivity.class.getName();

	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_home);
	    
//	    recreateDatabaseFromScratch();
	}
	   
	/**
	 * For when the database needs re-creatin'
	 * @author PaddyC
	 */
	private void recreateDatabaseFromScratch() {
		Log.d(TAG, " >> recreateDatabaseFromScratch()");
        DecisionDatabaseAdapter decisionDBAdapter = new DecisionDatabaseAdapter(this);
        decisionDBAdapter.open();
		SQLiteDatabase db = decisionDBAdapter.getmDbHelper().getWritableDatabase();
        DatabaseScripts.dropAllTables(db);
        DatabaseScripts.createAllTables(db);
        Log.d(TAG, " << recreateDatabaseFromScratch()");
	}
	
	
	protected void onDestroy () {
	   super.onDestroy ();
	}

	protected void onPause () {
	   super.onPause ();
	}

	protected void onRestart () {
	   super.onRestart ();
	}

	protected void onResume () {
	   super.onResume ();
	}

	protected void onStart () {
	   super.onStart ();
	}

	protected void onStop () {
	   super.onStop ();
	}

}