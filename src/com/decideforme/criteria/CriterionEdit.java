package com.decideforme.criteria;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.db.decideforme.criteria.CriteriaDatabaseAdapter;
import com.db.decideforme.criteria.Criterion.CriterionColumns;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.ratingsystems.RatingSystem.RatingSystemColumns;
import com.db.decideforme.ratingsystems.RatingSystemDatabaseAdapter;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;


public class CriterionEdit extends DashboardActivity {
	static final String TAG = CriterionEdit.class.getName();
	protected CriteriaDatabaseAdapter mCriteriaDBAdapter;
	protected RatingSystemDatabaseAdapter mRatingSystemDBAdapter;
	
	protected EditText mCriterionName;
	protected Spinner mRatingSystemSpinner;
	private Button mDoneButton;
	
	protected Long decisionRowId;
	protected Long criterionRowID;

	private static final int DONE = Menu.FIRST;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(" +
				"savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		mCriteriaDBAdapter = new CriteriaDatabaseAdapter(this);
		mCriteriaDBAdapter.open();
		
		mRatingSystemDBAdapter = new RatingSystemDatabaseAdapter(this);
		mRatingSystemDBAdapter.open();
		
		setContentView(R.layout.criterion_edit);
		setTitleFromActivityLabel (R.id.title_text);
		
		mCriterionName = (EditText) findViewById(R.id.criterion_name);
		
		mRatingSystemSpinner = (Spinner) findViewById(R.id.rating_system_spinner);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		criterionRowID = bundleHelper.getBundledFieldLongValue(CriterionColumns._ID);
		Log.d(TAG, "Competitor Row ID Retrieved: " + criterionRowID);

		// Competitor must be associated with a decision.
		Cursor thisCriterionCursor = mCriteriaDBAdapter.fetchCriterion(criterionRowID);
		decisionRowId = thisCriterionCursor.getLong(thisCriterionCursor.getColumnIndexOrThrow(CriterionColumns.DECISIONID));
		Log.d(TAG, "Associated Decision: " + decisionRowId);
		
		populateFields();
		
		mDoneButton = (Button) findViewById(R.id.done_editing_criterion_button);
		mDoneButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				saveState();
				finish();				
			}
		});
		
		Log.d(TAG, " << onCreate()");
	}


	protected void populateFields() {
		Log.d(TAG, " >> populateFields()");
		
	    if (criterionRowID != null) {
	        Cursor criteriaCursor = mCriteriaDBAdapter.fetchCriterion(criterionRowID);
	        mCriterionName.setText(criteriaCursor.getString(criteriaCursor.getColumnIndexOrThrow(CriterionColumns.DESCRIPTION)));
	    }
	    
	    populateRatingSystemSpinner();
	    
	    Log.d(TAG, " << populateFields()");
	}

	private void populateRatingSystemSpinner() {
		Log.d(TAG, " >> populateSpinner()");
		
		Cursor allRatingSystemsCursor = mRatingSystemDBAdapter.fetchAllRatingSystems();
		
		String[] from = new String[]{RatingSystemColumns.NAME};
		int[] to = new int[]{android.R.id.text1};
		
		SimpleCursorAdapter adapter =
		  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allRatingSystemsCursor, from, to );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		
		mRatingSystemSpinner.setAdapter(adapter);
		
		Log.d(TAG, " << populateSpinner()");
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, DONE, 0, R.string.done);
        return true;
    }
	
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case DONE:
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


	@Override
	protected void onResume() {
		Log.d(TAG, " >> onResume()");
		
		super.onResume();
		populateFields();
		
		Log.d(TAG, " << onResume()");
	}


	protected void saveState() {
		Log.d(TAG, " >> saveState()");
		
		String criterionDescription = mCriterionName.getText().toString();
		
		Cursor ratingSystemCursor = (Cursor) mRatingSystemSpinner.getSelectedItem();
		Long ratingSystemID = ratingSystemCursor.getLong(0);
		String ratingSystemName = ratingSystemCursor.getString(1);
		Log.d(TAG, "Rating System Selected: '" + ratingSystemName + "'");
		
	    if (criterionRowID == null) {
	        long id = mCriteriaDBAdapter.createCriterion(criterionDescription, decisionRowId, ratingSystemID);
	        if (id > 0) {
	        	criterionRowID = id;
	        }
	    } else {
	    	mCriteriaDBAdapter.updateCriterion(criterionRowID, criterionDescription, ratingSystemID);
	    }	
	    Log.d(TAG, " << saveState()");
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, " >> onSaveInstanceState(" +
				"outState '" + StringUtils.objectAsString(outState) + "')");
		
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(DecisionColumns._ID, decisionRowId);
		
		Log.d(TAG, " << onSaveInstanceState()");
	}
    
}
