package com.decideforme.criteria;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.db.decideforme.criteria.Criterion.CriterionColumns;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.ratingsystems.RatingSystem;
import com.db.decideforme.ratingsystems.RatingSystem.RatingSystemColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.ratings.RatingSystemHelper;
import com.decideforme.utils.BundleHelper;


public class CriterionEdit extends DashboardActivity {
	static final String TAG = CriterionEdit.class.getName();

	protected EditText mCriterionName;
	protected Spinner mRatingSystemSpinner;
	
	protected Long decisionRowId;
	protected Long criterionRowID;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.criterion_edit);
		setTitleFromActivityLabel (R.id.title_text);
		
		mCriterionName = (EditText) findViewById(R.id.criterion_name);
		
		mRatingSystemSpinner = (Spinner) findViewById(R.id.rating_system_spinner);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		criterionRowID = bundleHelper.getBundledFieldLongValue(CriterionColumns._ID);


		// Competitor must be associated with a decision.
		Cursor thisCriterionCursor = CriteriaHelper.getCriterion(this, criterionRowID);
		decisionRowId = thisCriterionCursor.getLong(thisCriterionCursor.getColumnIndexOrThrow(CriterionColumns.DECISIONID));
		
		populateFields();
		
	}


	protected void populateFields() {
		
	    if (criterionRowID != null) {
	        Cursor criteriaCursor = CriteriaHelper.getCriterion(this, criterionRowID);
	        mCriterionName.setText(criteriaCursor.getString(criteriaCursor.getColumnIndexOrThrow(CriterionColumns.DESCRIPTION)));
	    }
	    
	    populateRatingSystemSpinner();
	    
	}

	private void populateRatingSystemSpinner() {
		
		Cursor allRatingSystemsCursor = RatingSystemHelper.fetchAllRatingSystems(this);
		
		String[] from = new String[]{RatingSystemColumns.NAME};
		int[] to = new int[]{android.R.id.text1};
		
		SimpleCursorAdapter adapter =
		  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allRatingSystemsCursor, from, to );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		
		mRatingSystemSpinner.setAdapter(adapter);
		
	}


	@Override
	protected void onPause() {
		
		super.onPause();
		saveState();
		
	}


	@Override
	protected void onResume() {
		
		super.onResume();
		populateFields();
		
	}


	protected void saveState() {
		
		String criterionDescription = mCriterionName.getText().toString();
		
		Cursor ratingSystemCursor = (Cursor) mRatingSystemSpinner.getSelectedItem();
		Long ratingSystemID = ratingSystemCursor.getLong(RatingSystem.COLUMN_INDEX_ROW_ID);
		
	    if (criterionRowID == null) {
	        long id = CriteriaHelper.createCriterion(this, criterionDescription, decisionRowId, ratingSystemID);
	        if (id > 0) {
	        	criterionRowID = id;
	        }
	    } else {
	    	CriteriaHelper.updateCriterion(this, criterionRowID, criterionDescription, ratingSystemID);
	    }	

	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(DecisionColumns._ID, decisionRowId);
		
	}
    
}
