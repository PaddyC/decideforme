package com.decideforme;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.db.decideforme.competitors.Competitor.CompetitorColumns;
import com.db.decideforme.competitors.CompetitorsDatabaseAdapter;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;

public class CompetitorEdit extends Activity {
	private static final String TAG = CompetitorEdit.class.getName();
	
	private CompetitorsDatabaseAdapter mDbAdapter;
	private Long competitorRowID;
	private Long decisionRowID;
	
	private EditText competitorName;
	
	private static final int OK = Menu.FIRST;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(" +
				"savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		mDbAdapter = new CompetitorsDatabaseAdapter(this);
		mDbAdapter.open();
		
		setContentView(R.layout.competitor_edit);
		setTitle(R.string.edit_competitor);
		
		competitorName = (EditText) findViewById(R.id.competitor_name);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		competitorRowID = bundleHelper.getBundledFieldLongValue(CompetitorColumns._ID);
		Log.d(TAG, "Competitor Row ID Retrieved: " + competitorRowID);

		// Competitor must be associated with a decision.
		Cursor thisCompetitorCursor = mDbAdapter.fetchCompetitor(competitorRowID);
		startManagingCursor(thisCompetitorCursor);
		decisionRowID = thisCompetitorCursor.getLong(thisCompetitorCursor.getColumnIndexOrThrow(CompetitorColumns.DECISIONID));
		Log.d(TAG, "Associated Decision: " + decisionRowID);
		
		populateFields();
		
		Log.d(TAG, " << onCreate()");
	}
	
	protected void populateFields() {
		Log.d(TAG, " >> populateFields()");
		
	    if (competitorRowID != null) {
	        Cursor competitorCursor = mDbAdapter.fetchCompetitor(competitorRowID);
	        startManagingCursor(competitorCursor);
	        competitorName.setText(competitorCursor.getString(competitorCursor.getColumnIndexOrThrow(CompetitorColumns.DESCRIPTION)));
	    }
	    
	    Log.d(TAG, " << populateFields()");
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, OK, 0, R.string.ok);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case OK:
	        	finish();
	        	return true;
        } 
        
        return super.onMenuItemSelected(featureId, item);
    }
    
	@Override
	protected void onPause() {
		Log.d(TAG, " >> onPause()");
		
		super.onPause();
		saveState();
		
		Log.d(TAG, " << onPause()");
	}

	protected void saveState() {
		Log.d(TAG, " >> saveState()");
		
		String name = competitorName.getText().toString();
	
	    if (competitorRowID == null) {
	        long id = mDbAdapter.createCompetitor(name, decisionRowID);
	        if (id > 0) {
	        	competitorRowID = id;
	        }
	    } else {
	    	mDbAdapter.updateCompetitor(competitorRowID, name);
	    }	
	
	    Log.d(TAG, " << saveState()");
	}

	@Override
	protected void onResume() {
		Log.d(TAG, " >> onResume()");
		
		super.onResume();
		populateFields();
		
		Log.d(TAG, " << onResume()");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, " >> onSaveInstanceState(" +
				"outState '" + StringUtils.objectAsString(outState) + "')");
		
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(CompetitorColumns._ID, competitorRowID);
		
		Log.d(TAG, " << onSaveInstanceState()");
	}
}
