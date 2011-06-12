package com.decideforme.ratings;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.decisionrating.DecisionRatingsDatabaseAdapter;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.ratings.grid.RatingTableLayoutHelper;
import com.decideforme.ratings.grid.RatingTableLayoutHelperImpl;
import com.decideforme.utils.BundleHelper;
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
 * Next we will 
 * @author PaddyC
 *
 */
public class RatingsScreen extends DashboardActivity {
	public static final String TAG = RatingsScreen.class.getName();
	
	private static final int DONE = Menu.FIRST;
	
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
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, DONE, 0, R.string.done);
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_screen);
		setTitleFromActivityLabel (R.id.title_text);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);

		// Grid Start! Create the table layout:
		mDynamicRatingTable = (TableLayout) findViewById(R.id.ratingTableLayout);
		
		populateFields();
	}
	

	protected void saveState() {
		Cursor allCompetitors = getmRatingsScreenHelper().fetchAllCompetitorsForDecision(this, mDecisionRowId);
		allCompetitors.moveToFirst();
		while(allCompetitors.isAfterLast() == false) {
			
			mThisCompetitorRowId = allCompetitors.getLong(0);
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
			
			allCompetitors.moveToNext();
		}
	}


	private void populateFields() {
		// Row 1: Criterion Names.
		TableRow firstRow = populateCriteriaHeaderRow();
		mDynamicRatingTable.addView(firstRow, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		
		Cursor allCompetitors = getmRatingsScreenHelper().fetchAllCompetitorsForDecision(this, mDecisionRowId);
		allCompetitors.moveToFirst();
		while(allCompetitors.isAfterLast() == false) {
			mThisCompetitorRowId = allCompetitors.getLong(0);
			// Row 2+: Competitor Names with a spinner for each criterion rating system..
			TableRow nextRow = populateCompetitorRatingRow(allCompetitors.getString(2));
			mDynamicRatingTable.addView(nextRow, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));

			allCompetitors.moveToNext();
		}
		allCompetitors.close();
	}
	
	private void repopulateRatingsOnResume() {
		Cursor allCompetitors = getmRatingsScreenHelper().fetchAllCompetitorsForDecision(this, mDecisionRowId);
		allCompetitors.moveToFirst();
		while(allCompetitors.isAfterLast() == false) {
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
			allCompetitors.moveToNext();
		}
		allCompetitors.close();	
	}
	
	private TableRow populateCompetitorRatingRow(String competitorName) {
		TableRow nextRow = getmRatingTableLayoutHelper().getNewRow(this);
		
		// Add the cell with the competitor name.
		TextView competitorNameView = getmRatingTableLayoutHelper().getCompetitorNameTextView(this, competitorName);
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
		TableRow firstRow = getmRatingTableLayoutHelper().getNewRow(this);
		
		// Cell A1 is blank... this is intentional.
		TextView blankText = getmRatingTableLayoutHelper().getCriterionNameTextView(this, "");
		firstRow.addView(blankText);
		
		mCriteriaRowIDs = new ArrayList<Long>();
		mRatingSystemIDs = new ArrayList<Long>();
		
		Cursor allCriteria = getmRatingsScreenHelper().fetchAllCriteriaForDecision(this, mDecisionRowId);
		
		allCriteria.moveToFirst();
        while (allCriteria.isAfterLast() == false) {
        	// Add a TextView with the Criterion Name
        	TextView thisCriterion = getmRatingTableLayoutHelper().getCriterionNameTextView(this, allCriteria.getString(2));
        	firstRow.addView(thisCriterion);

        	// Store the Criterion ID for later
        	mCriteriaRowIDs.add(allCriteria.getLong(0));
        	// Store the Rating System ID for later
        	mRatingSystemIDs.add(allCriteria.getLong(3));
        	
            allCriteria.moveToNext();
        }
        allCriteria.close();
		
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
