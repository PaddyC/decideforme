package com.decideforme.criteria;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.criteria.Criterion.CriterionColumns;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.ratingsystems.RatingSystem;
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
	protected String criterionRatingSystem = "";

	
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
		Criterion thisCriterion = CriteriaHelper.getCriterion(this, criterionRowID).get(0);
		decisionRowId = thisCriterion.getDecisionId();
		
		Long ratingSystemId = thisCriterion.getRatingSystemId();
		if (ratingSystemId.intValue() > 0) {
			RatingSystem thisRatingSystem = RatingSystemHelper.getRatingSystem(this, ratingSystemId);
			criterionRatingSystem = thisRatingSystem.getName();
		}
		
	    Button saveButton = (Button) findViewById(R.id.saveButton);
	    saveButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.save_button));
	    saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		populateFields();
		
	}


	protected void populateFields() {
		
	    if (criterionRowID != null) {
	    	Criterion thisCriterion = CriteriaHelper.getCriterion(this, criterionRowID).get(0);
	        mCriterionName.setText(thisCriterion.getDescription());
	    }
	    
	    populateRatingSystemSpinner();
	    
	}

	private void populateRatingSystemSpinner() {
		
		List<RatingSystem> ratingSystemList = RatingSystemHelper.fetchAllRatingSystems(this);
		
		int selection = 0;
		
		ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int i = 0; i < ratingSystemList.size(); i++) {
			RatingSystem system = ratingSystemList.get(i);
			adapter.add(system.getName());
			if (criterionRatingSystem.equalsIgnoreCase(system.getName())) {
				selection = i;
			}
		}
		mRatingSystemSpinner.setAdapter(adapter);
		mRatingSystemSpinner.setSelection(selection);
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
		
		String ratingSystemName= (String) mRatingSystemSpinner.getSelectedItem();
		RatingSystem ratingSystem = RatingSystemHelper.getRatingSystemIdForName(this, ratingSystemName);
		Long ratingSystemID = ratingSystem.getRowId();
		
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
