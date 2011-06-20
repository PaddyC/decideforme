package com.decideforme.decision;

import java.util.List;

import android.os.Bundle;
import android.widget.EditText;

import com.db.decideforme.decision.Decision;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;


public class DecisionEdit extends DashboardActivity {
	
	private EditText mDecisionName;
	private EditText mDecisionDescription;
	
	protected Long mDecisionRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.decision_edit);
		setTitleFromActivityLabel (R.id.title_text);
		
		mDecisionName = (EditText) findViewById(R.id.decision_name);
		mDecisionDescription = (EditText) findViewById(R.id.decision_description);
		
		BundleHelper bundleHelper = new BundleHelper(this,  savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
		
		populateFields();
		
	}


	protected void populateFields() {
		
	    if (mDecisionRowId != null) {
	        List<Decision> decisionList = DecisionHelper.getDecision(this, mDecisionRowId);
	        Decision thisDecision = decisionList.get(0);
	        mDecisionName.setText(thisDecision.getName());
	        mDecisionDescription.setText(thisDecision.getDescription());
	    }
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
		
		String title = mDecisionName.getText().toString();
	    String body = mDecisionDescription.getText().toString();
	
	    if (mDecisionRowId == null) {
	        long id = DecisionHelper.createNewDecision(this, title, body);
	        if (id > 0) {
	        	mDecisionRowId = id;
	        }
	    } else {
	    	DecisionHelper.updateDecision(this, mDecisionRowId, title, body);
	    }	
	
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(DecisionColumns._ID, mDecisionRowId);
		
	}
    
}
