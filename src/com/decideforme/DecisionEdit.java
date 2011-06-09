package com.decideforme;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.decideforme.competitors.CompetitorsScreen;
import com.decideforme.criteria.CriteriaScreen;
import com.decideforme.ratings.RatingsScreen;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;


public class DecisionEdit extends Activity {
	static final String TAG = DecisionEdit.class.getName();
	
	protected DecisionDatabaseAdapter mDecisionDBAdapter;
	
	private EditText mDecisionName;
	private EditText mDecisionDescription;
	
	protected Long mDecisionRowId;

	private static final int COMPETITORS_SCREEN = Menu.FIRST;
	private static final int CRITERIA_SCREEN = Menu.FIRST + 1;
	private static final int RATE = Menu.FIRST + 2;
    private static final int DONE = Menu.FIRST + 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(" +
				"savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		mDecisionDBAdapter = new DecisionDatabaseAdapter(this);
		mDecisionDBAdapter.open();
		
		setContentView(R.layout.decision_edit);
		setTitle(R.string.edit_decision);
		
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, COMPETITORS_SCREEN, 0, R.string.competitors_screen);
        menu.add(1, CRITERIA_SCREEN, 1, R.string.criteria_screen);
        menu.add(2, RATE, 2, R.string.rating_screen);
        menu.add(3, DONE, 3, R.string.done);
        return true;
    }
	
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case COMPETITORS_SCREEN:
	            addCompetitor();
	            return true;
	        case CRITERIA_SCREEN:
	            addCriterion();
	            return true;
	        case RATE:
	            rating();
	            return true;
	        case DONE:
	        	finish();
	        	return true;
        } 
        
        return super.onMenuItemSelected(featureId, item);
    }
    

	private void rating() {
		Log.d(TAG, " >> rating()");
		
		new PresentRatingScreen().execute();
		
		Log.d(TAG, " << rating()");
	}
	
	private class PresentRatingScreen extends AsyncTask<Void, Void, Void> {
		private ProgressDialog mProgressDialog;
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			mProgressDialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			mProgressDialog = ProgressDialog.show(DecisionEdit.this,    
		              "Just a sec...", "Crunching some numbers here...", true);
			
		}
		

		@Override
		protected Void doInBackground(Void... arg0) {
			Intent i = new Intent(DecisionEdit.this, RatingsScreen.class);
			i.putExtra(DecisionColumns._ID, mDecisionRowId);
			startActivityForResult(i, DecideForMe.ACTIVITY_EDIT);
			return null;
		}
		
	}


	private void addCompetitor() {
		Log.d(TAG, " >> addCompetitor()");
		
		Intent i = new Intent(this, CompetitorsScreen.class);
        i.putExtra(DecisionColumns._ID, mDecisionRowId);
        startActivityForResult(i, DecideForMe.ACTIVITY_EDIT);
		
		Log.d(TAG, " << addCompetitor()");	
	}
	
	private void addCriterion() {
		Log.d(TAG, " >> addCriterion()");
		
		Intent i = new Intent(this, CriteriaScreen.class);
        i.putExtra(DecisionColumns._ID, mDecisionRowId);
        startActivityForResult(i, DecideForMe.ACTIVITY_EDIT);
		
		Log.d(TAG, " << addCriterion()");	
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
