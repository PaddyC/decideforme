package com.decideforme.ratings;

import java.util.List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.decisionrating.DecisionRatings;
import com.db.decideforme.decisionrating.DecisionRatingsHelper;
import com.db.decideforme.ratinginstance.RatingInstance;
import com.decideforme.R;
import com.decideforme.competitors.CompetitorHelper;
import com.decideforme.criteria.CriteriaHelper;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.ratings.grid.RatingTableLayoutHelper;
import com.decideforme.ratings.grid.RatingTableLayoutHelperImpl;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.SubjectConstants;
import com.decideforme.utils.ViewHelper;
import com.decideforme.utils.ViewHelperImpl;
/**
 * This class is all about getting a Grid to display, which will present the user with the rating system..<p>
 * Competitors will be listed down Column A, Criteria will display across row 1.<p>
 * Row 1 is a header row, including the names of all of our previously selected Criteria for this decision.<p>
 * NOTE: Cell A1 in our grid is blank. This is intentional!<p>
 * While looking at the criteria, we'll use the rating system ids to get all the Rating Instances associated. These will 
 *  then be used to populate Spinners. These spinners will hold the actual ratings.<p>
 * After populating the first row, we then have a new row for each competitor associated with this decision.
 *  The first cell in the row holds the competitor name, and subsequent cells hold the rating system corresponding
 *  with the Criterion in the column header.
 * @author PaddyC
 *
 */
public class RatingsScreen extends DashboardActivity {
	public static final String TAG = RatingsScreen.class.getName();
	
	private Long mDecisionRowId;
	private Long mThisCompetitorRowId;
	private Long mThisCriterionRowId;
	private Long mThisRatingSystemId;
	
	private TableLayout mDynamicRatingTable;
	
	private RatingTableLayoutHelper mRatingTableLayoutHelper;
	private RatingsScreenHelper mRatingsScreenHelper;
	
	private List<Criterion> allCriteriaForDecision;
	private List<Competitor> allCompetitorsForDecision;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_screen);
		setTitleFromActivityLabel (R.id.title_text);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);

		// Grid Start! Create the table layout:
		mDynamicRatingTable = (TableLayout) findViewById(R.id.ratingsTable);
		mDynamicRatingTable.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield_default));
		
		populateFields();
	}
	
	private void retrieveDecisionData() {
		
		allCriteriaForDecision = CriteriaHelper.getAllCriteriaForDecision(this, mDecisionRowId);
		allCompetitorsForDecision = CompetitorHelper.getAllCompetitorsForDecision(this, mDecisionRowId);
		
	}
	

	protected void saveState() {
		
		retrieveDecisionData();
		
		for (Competitor thisCompetitor : allCompetitorsForDecision) {
			mThisCompetitorRowId = thisCompetitor.getRowId();
			
			for (Criterion thisCriterion : allCriteriaForDecision) {
				mThisCriterionRowId = thisCriterion.getRowId();
				
				Integer gridReference = getmRatingTableLayoutHelper().getGridReference(mThisCompetitorRowId, mThisCriterionRowId);
				Spinner thisRatingSpinner = (Spinner) findViewById(gridReference);
				String ratingName = (String) thisRatingSpinner.getSelectedItem();
				RatingInstance thisRatingInstance = RatingInstanceHelper.getRatingInstance(this, ratingName);
				Long ratingInstanceID = thisRatingInstance.getRowId();
				
				// If decisionrating existence check is true
				if (DecisionRatingsHelper.ratingExists(this, mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId)) {
					// update
					DecisionRatingsHelper.updateDecisionRating(this, mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId, ratingInstanceID);
				} else {
					// otherwise, add a new one
					DecisionRatingsHelper.createDecisionRating(this, mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId, ratingInstanceID);
				} 
			}
		}
	}


	private void populateFields() {
		
		if (mDynamicRatingTable.getChildCount() > 0) {
			mDynamicRatingTable.removeAllViews();
		}
		
		retrieveDecisionData();
		
		// Row 1: Criterion Names.
		TableRow firstRow = populateCriteriaHeaderRow();
		mDynamicRatingTable.addView(firstRow, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		
		// Row 2+ Competitor ratings
		for (Competitor thisCompetitor : allCompetitorsForDecision) {
			mThisCompetitorRowId = thisCompetitor.getRowId();
			TableRow nextRow = populateCompetitorRatingRow(thisCompetitor.getDescription());
			mDynamicRatingTable.addView(nextRow, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		}
		TableRow saveButtonRow = getSaveButtonRow();
		mDynamicRatingTable.addView(saveButtonRow);
	}


	private TableRow getSaveButtonRow() {
		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		tableRow.setPadding(1, 1, 1, 1);
		
	    Button saveButton = new Button(this);
	    saveButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.save_button));
	    saveButton.setGravity(Gravity.CENTER);
	    saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	    tableRow.addView(saveButton);
	    
	    return tableRow;
	}
	
	
	private TableRow populateCompetitorRatingRow(String competitorName) {
		ViewHelper viewHelper = new ViewHelperImpl(mDecisionRowId, this, SubjectConstants.DECISION);
		TableRow nextRow = viewHelper.getNewRow(this, true);
		
		// Add the cell with the competitor name.
		TextView competitorNameView = new TextView(this);
		competitorNameView.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
		competitorNameView.setText(competitorName);
		nextRow.addView(competitorNameView);
		
		// Generate a spinner for each rating.
		for (Criterion thisCriterion : allCriteriaForDecision) {
			mThisCriterionRowId = thisCriterion.getRowId();
			mThisRatingSystemId = thisCriterion.getRatingSystemId();
			
			Integer spinnerID = getmRatingTableLayoutHelper().getGridReference(mThisCompetitorRowId, mThisCriterionRowId);
			
			Spinner aSpinner = getmRatingsScreenHelper().generateRatingsSpinner(this, mThisRatingSystemId, spinnerID);
			
			if (DecisionRatingsHelper.ratingExists(this, mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId)) {
			 	// Retrieve and set to the spinner.
				DecisionRatings rating = DecisionRatingsHelper.getRating(this, mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId);
				Long ratingScore = RatingInstanceHelper.getScore(this, rating.getRatingSelectionId());
				Integer spinnerPosition = ratingScore.intValue() - 1;
				aSpinner.setSelection(spinnerPosition);
			}

			nextRow.addView(aSpinner);
		}
		return nextRow;
	}


	private TableRow populateCriteriaHeaderRow() {
		ViewHelper viewHelper = new ViewHelperImpl(mDecisionRowId, this, SubjectConstants.DECISION);
		TableRow firstRow = viewHelper.getNewRow(this, false);
		
		// Cell A1 is blank... this is intentional.
		TextView blankText = new TextView(this);
		blankText.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
		firstRow.addView(blankText);
		
		int viewId = 0;
		for (Criterion thisCriterion : allCriteriaForDecision) {
        	// Add a TextView with the Criterion Name
			TextView thisCriterionView = viewHelper.getTextView(viewId++, thisCriterion.getDescription(), R.style.HomeButton, 
					Typeface.SANS_SERIF, true, false);
        	
			firstRow.addView(thisCriterionView);
        }
        
		return firstRow;
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
		Log.d(TAG, " << onResume()");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(DecisionColumns._ID, mDecisionRowId);
	}


	public void setmGridHelper(RatingTableLayoutHelper ratingTableLayoutHelper) {
		this.mRatingTableLayoutHelper = ratingTableLayoutHelper;
	}


	public synchronized RatingTableLayoutHelperImpl getmRatingTableLayoutHelper() {
		if (mRatingTableLayoutHelper == null) {
			mRatingTableLayoutHelper = RatingTableLayoutHelperImpl.getInstance();
		}
		return (RatingTableLayoutHelperImpl) mRatingTableLayoutHelper;
	}


	public void setmRatingsScreenHelper(RatingsScreenHelper mRatingsScreenHelper) {
		this.mRatingsScreenHelper = mRatingsScreenHelper;
	}


	public synchronized RatingsScreenHelper getmRatingsScreenHelper() {
		if (mRatingsScreenHelper == null) {
			mRatingsScreenHelper = RatingsScreenHelperImpl.getInstance();
		}
		return mRatingsScreenHelper;
	}
	
	

}
