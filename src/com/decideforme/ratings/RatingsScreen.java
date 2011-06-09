package com.decideforme.ratings;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.ratings.grid.RatingGridHelper;
import com.decideforme.ratings.grid.RatingGridHelperImpl;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;
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
public class RatingsScreen extends Activity {
	private static final String TAG = RatingsScreen.class.getName();
	
	private static final int DONE = Menu.FIRST;
	
	private Long mDecisionRowId;
	private Long mThisCompetitorRowId;
	private Long mThisCriterionRowId;
	private Long mThisRatingSystemId;
	
	private RatingGridHelper mGridHelper;
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
	        	finish();
	        	return true;
        } 
        
        return super.onMenuItemSelected(featureId, item);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rating_screen);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);

		// Grid Start! Create the table layout:
		TableLayout dynamicRatingTable = (TableLayout) findViewById(R.id.ratingTableLayout);
		
		// Row 1: Criterion Names.
		TableRow firstRow = populateCriteriaHeaderRow();
		dynamicRatingTable.addView(firstRow, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		
		Cursor allCompetitors = getmRatingsScreenHelper().fetchAllCompetitorsForDecision(this, mDecisionRowId);
		allCompetitors.moveToFirst();
		while(allCompetitors.isAfterLast() == false) {
			mThisCompetitorRowId = allCompetitors.getLong(0);

			// Row 2+: Competitor Names with a spinner for each criterion rating system..
			TableRow nextRow = getmGridHelper().getNewRow(this);
			
			// Add the cell with the competitor name.
			TextView competitorNameView = getmGridHelper().getCompetitorNameTextView(this, allCompetitors.getString(2));
			nextRow.addView(competitorNameView);
			
			// Generate a spinner for each rating.
			for (int criterionCount = 0; criterionCount < mRatingSystemIDs.size(); criterionCount++) {
				mThisCriterionRowId = mCriteriaRowIDs.get(criterionCount);
				mThisRatingSystemId = mRatingSystemIDs.get(criterionCount);
				
				Integer spinnerID = getmGridHelper().getGridReference(mThisCompetitorRowId, mThisCriterionRowId);
				
				Spinner aSpinner = getmRatingsScreenHelper().generateRatingsSpinner(this, mThisRatingSystemId, spinnerID);
				nextRow.addView(aSpinner);
				Log.d(TAG, "Spinner added ok");
			}
			
			// Add the row
			dynamicRatingTable.addView(nextRow, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));

			allCompetitors.moveToNext();
		}
		
		Log.d(TAG, " << onCreate()");
	}


	private TableRow populateCriteriaHeaderRow() {
		Log.d(TAG, " >> populateCriteriaHeaderRow()");
		
		TableRow firstRow = getmGridHelper().getNewRow(this);
		
		// Cell A1 is blank... this is intentional.
		TextView blankText = getmGridHelper().getCriterionNameTextView(this, "");
		firstRow.addView(blankText);
		
		mCriteriaRowIDs = new ArrayList<Long>();
		mRatingSystemIDs = new ArrayList<Long>();
		
		Cursor allCriteria = getmRatingsScreenHelper().fetchAllCriteriaForDecision(this, mDecisionRowId);
		Log.d(TAG, "Number of Criteria: " + allCriteria.getCount());
		
		allCriteria.moveToFirst();
        while (allCriteria.isAfterLast() == false) {
        	Log.d(TAG, "Working with criterion " + StringUtils.objectAsString(allCriteria.getString(2)));
        	
        	// Add a TextView with the Criterion Name
        	TextView thisCriterion = getmGridHelper().getCriterionNameTextView(this, allCriteria.getString(2));
        	firstRow.addView(thisCriterion);

        	// Store the Criterion ID for later
        	mCriteriaRowIDs.add(allCriteria.getLong(0));
        	// Store the Rating System ID for later
        	mRatingSystemIDs.add(allCriteria.getLong(3));
        	
            allCriteria.moveToNext();
        }
		Log.d(TAG, " << populateCriteriaHeaderRow(), returned '" + StringUtils.objectAsString(firstRow) + "'");
		return firstRow;
	}


	public void setmGridHelper(RatingGridHelper mGridHelper) {
		this.mGridHelper = mGridHelper;
	}


	public synchronized RatingGridHelper getmGridHelper() {
		if (mGridHelper == null) {
			mGridHelper = RatingGridHelperImpl.getInstance();
		}
		return mGridHelper;
	}


	public void setmRatingsScreenHelper(RatingsScreenHelper mRatingsScreenHelper) {
		this.mRatingsScreenHelper = mRatingsScreenHelper;
	}


	public RatingsScreenHelper getmRatingsScreenHelper() {
		if (mRatingsScreenHelper == null) {
			mRatingsScreenHelper = RatingsScreenHelperImpl.getInstance();
		}
		return mRatingsScreenHelper;
	}
	
	

}
