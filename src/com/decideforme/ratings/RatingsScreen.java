package com.decideforme.ratings;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.decisionrating.DecisionRatingsDatabaseAdapter;
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
	
	private List<Long> mRatingSystemIDs;
	private List<Long> mCriteriaRowIDs;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_screen);
		setTitleFromActivityLabel (R.id.title_text);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);

		// Grid Start! Create the table layout:
		mDynamicRatingTable = (TableLayout) findViewById(R.id.ratingsTable);
		
		populateFields();
	}
	

	protected void saveState() {
		List<Competitor> competitorList = CompetitorHelper.getAllCompetitorsForDecision(this, mDecisionRowId);
		for (Competitor thisCompetitor : competitorList) {
			mThisCompetitorRowId = thisCompetitor.getRowId();
			
			for (int criterionCount = 0; criterionCount < mRatingSystemIDs.size(); criterionCount++) {
				mThisCriterionRowId = mCriteriaRowIDs.get(criterionCount);
				
				Integer gridReference = getmRatingTableLayoutHelper().getGridReference(mThisCompetitorRowId, mThisCriterionRowId);
				
				Spinner thisCompetitorCriterion = (Spinner) findViewById(gridReference);
				Cursor selectedRatingInstance = (Cursor) thisCompetitorCriterion.getSelectedItem();
				Long ratingInstanceID = selectedRatingInstance.getLong(0);
				
				// If decisionrating existence check is true
				DecisionRatingsDatabaseAdapter decisionRatingDBAdapter = new DecisionRatingsDatabaseAdapter(this);
				decisionRatingDBAdapter.open();
				if (decisionRatingDBAdapter.existenceCheckDecisionRating(mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId)) {
					// update
					decisionRatingDBAdapter.updateDecisionRating(mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId, ratingInstanceID);
				} else {
					// otherwise, add a new one
					decisionRatingDBAdapter.createDecisionRating(mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId, ratingInstanceID);
				} 
			}
		}
	}


	private void populateFields() {
		// Row 1: Criterion Names.
		TableRow firstRow = populateCriteriaHeaderRow();
		mDynamicRatingTable.addView(firstRow, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		
		List<Competitor> competitorList = CompetitorHelper.getAllCompetitorsForDecision(this, mDecisionRowId);
		for (Competitor thisCompetitor : competitorList) {
			mThisCompetitorRowId = thisCompetitor.getRowId();
			// Row 2+: Competitor Names with a spinner for each criterion rating system..
			TableRow nextRow = populateCompetitorRatingRow(thisCompetitor.getDescription());
			mDynamicRatingTable.addView(nextRow, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		}
	}
	
	private void repopulateRatingsOnResume() {
		
		List<Competitor> competitorList = CompetitorHelper.getAllCompetitorsForDecision(this, mDecisionRowId);
		for (int i = 0; i < competitorList.size(); i++) {
		
			for (int criterionCount = 0; criterionCount < mRatingSystemIDs.size(); criterionCount++) {
				mThisCriterionRowId = mCriteriaRowIDs.get(criterionCount);
				mThisRatingSystemId = mRatingSystemIDs.get(criterionCount);
				
				Integer spinnerID = getmRatingTableLayoutHelper().getGridReference(mThisCompetitorRowId, mThisCriterionRowId);
				
				Spinner thisSpinner = (Spinner) findViewById(spinnerID);
				
				// If decisionrating exists
				DecisionRatingsDatabaseAdapter decisionRatingDBAdapter = new DecisionRatingsDatabaseAdapter(this);
				decisionRatingDBAdapter.open();
				if (decisionRatingDBAdapter.existenceCheckDecisionRating(
						mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId)) {
				 	// Retrieve and set to the spinner.
					Long thisRating = decisionRatingDBAdapter.fetchDecisionRatingSelectionRorder(
							this, mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId);
					Integer spinnerPosition = thisRating.intValue() - 1;
					thisSpinner.setSelection(spinnerPosition);
				}
			}
		}
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
		for (int criterionCount = 0; criterionCount < mRatingSystemIDs.size(); criterionCount++) {
			mThisCriterionRowId = mCriteriaRowIDs.get(criterionCount);
			mThisRatingSystemId = mRatingSystemIDs.get(criterionCount);
			
			Integer spinnerID = getmRatingTableLayoutHelper().getGridReference(mThisCompetitorRowId, mThisCriterionRowId);
			
			Spinner aSpinner = getmRatingsScreenHelper().generateRatingsSpinner(this, mThisRatingSystemId, spinnerID);
			
			// If decisionrating exists
			DecisionRatingsDatabaseAdapter decisionRatingDBAdapter = new DecisionRatingsDatabaseAdapter(this);
			decisionRatingDBAdapter.open();
			if (decisionRatingDBAdapter.existenceCheckDecisionRating(
					mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId)) {
			 	// Retrieve and set to the spinner.
				Long thisRating = decisionRatingDBAdapter.fetchDecisionRatingSelectionRorder(
						this, mDecisionRowId, mThisCompetitorRowId, mThisCriterionRowId);
				Integer spinnerPosition = thisRating.intValue() - 1;
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
		
		mCriteriaRowIDs = new ArrayList<Long>();
		mRatingSystemIDs = new ArrayList<Long>();
		
		List<Criterion> allCriteriaForDecision = CriteriaHelper.getAllCriteriaForDecision(this, mDecisionRowId);
		for (Criterion thisCriterion : allCriteriaForDecision) {
        	// Add a TextView with the Criterion Name
        	TextView thisCriterionView = new TextView(this);
       		thisCriterionView.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
       		thisCriterionView.setText(thisCriterion.getDescription());
       		thisCriterionView.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield_default));
    		
        	firstRow.addView(thisCriterionView);

        	// Store the Criterion ID for later
        	mCriteriaRowIDs.add(thisCriterion.getRowId());
        	// Store the Rating System ID for later
        	mRatingSystemIDs.add(thisCriterion.getRatingSystemId());
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
		repopulateRatingsOnResume();
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
