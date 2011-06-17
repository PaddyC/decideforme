package com.decideforme.competitors;

import android.os.Bundle;
import android.widget.EditText;

import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.competitors.Competitor.CompetitorColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;

public class CompetitorEdit extends DashboardActivity {
	
	private Long competitorRowID;
	private Long decisionRowID;
	
	private EditText competitorName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.competitor_edit);
		setTitleFromActivityLabel (R.id.title_text);
		
		competitorName = (EditText) findViewById(R.id.competitor_name);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		competitorRowID = bundleHelper.getBundledFieldLongValue(CompetitorColumns._ID);
		
		Competitor thisCompetitor = CompetitorHelper.getCompetitor(this, competitorRowID).get(0);
		decisionRowID = thisCompetitor.getDecisionId();
		
		populateFields();
		
	}
	
	protected void populateFields() {
		
	    if (competitorRowID != null) {
	    	Competitor thisCompetitor = CompetitorHelper.getCompetitor(this, competitorRowID).get(0);
	        competitorName.setText(thisCompetitor.getDescription());
	    }

	}

    @Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	protected void saveState() {
		String name = competitorName.getText().toString();
	
	    if (competitorRowID == null) {
	    	long id = CompetitorHelper.createCompetitor(this, decisionRowID, name);
	        if (id > 0) {
	        	competitorRowID = id;
	        }
	    } else {
	    	CompetitorHelper.updateCompetitor(this, competitorRowID, name);
	    }	
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();

	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(CompetitorColumns._ID, competitorRowID);
	}
}
