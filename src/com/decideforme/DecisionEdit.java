package com.decideforme;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;


public class DecisionEdit extends DashboardActivity {
	static final String TAG = DecisionEdit.class.getName();
	
	private static final int DONE = Menu.FIRST;
	
	protected DecisionDatabaseAdapter mDecisionDBAdapter;
	
	private EditText mDecisionName;
	private EditText mDecisionDescription;
	
	protected Long mDecisionRowId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, DONE, 0, R.string.done).setIcon(R.drawable.ic_menu_revert);
        return true;
    }
	
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case DONE:
	        	saveState();
	        	finish();
	        	return true;
        } 
        
        return super.onMenuItemSelected(featureId, item);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(" +
				"savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		mDecisionDBAdapter = new DecisionDatabaseAdapter(this);
		mDecisionDBAdapter.open();
		
		setContentView(R.layout.decision_edit);
		setTitleFromActivityLabel (R.id.title_text);
		
		mDecisionName = (EditText) findViewById(R.id.decision_name);
		mDecisionDescription = (EditText) findViewById(R.id.decision_description);
		
		BundleHelper bundleHelper = new BundleHelper(this,  savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
		
		populateFields();
		
		Log.d(TAG, " << onCreate()");
	}


	protected void populateFields() {
		Log.d(TAG, " >> populateFields()");
		
	    if (mDecisionRowId != null) {
	        Cursor decisionCursor = mDecisionDBAdapter.fetchDecision(mDecisionRowId);
	        mDecisionName.setText(decisionCursor.getString(decisionCursor.getColumnIndexOrThrow(DecisionColumns.NAME)));
	        mDecisionDescription.setText(decisionCursor.getString(decisionCursor.getColumnIndexOrThrow(DecisionColumns.DESCRIPTION)));
	    }
	    
	    Log.d(TAG, " << populateFields()");
	}



	@Override
	protected void onPause() {
		Log.d(TAG, " >> onPause()");
		
		super.onPause();
		saveState();
		
		Log.d(TAG, " << onPause()");
	}


	@Override
	protected void onResume() {
		Log.d(TAG, " >> onResume()");
		
		super.onResume();
		populateFields();
		
		Log.d(TAG, " << onResume()");
	}


	protected void saveState() {
		Log.d(TAG, " >> saveState()");
		
		String title = mDecisionName.getText().toString();
	    String body = mDecisionDescription.getText().toString();
	
	    if (mDecisionRowId == null) {
	        long id = mDecisionDBAdapter.createDecision(title, body);
	        if (id > 0) {
	        	mDecisionRowId = id;
	        }
	    } else {
	    	mDecisionDBAdapter.updateDecision(mDecisionRowId, title, body);
	    }	
	
	    Log.d(TAG, " << saveState()");
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, " >> onSaveInstanceState(" +
				"outState '" + StringUtils.objectAsString(outState) + "')");
		
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(DecisionColumns._ID, mDecisionRowId);
		
		Log.d(TAG, " << onSaveInstanceState()");
	}
    
}
