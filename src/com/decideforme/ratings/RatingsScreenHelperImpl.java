package com.decideforme.ratings;

import android.app.Activity;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableRow.LayoutParams;

import com.db.decideforme.competitors.CompetitorsDatabaseAdapter;
import com.db.decideforme.criteria.CriteriaDatabaseAdapter;
import com.db.decideforme.ratinginstance.RatingInstance.RatingInstanceColumns;
import com.db.decideforme.ratinginstance.RatingInstanceDatabaseAdapter;

/**
 * 
 * @author PaddyC
 *
 */
public class RatingsScreenHelperImpl implements RatingsScreenHelper {
	public static final String TAG = RatingsScreenHelperImpl.class.getName();
	
	private RatingsScreenHelperImpl() {
	}

	private static RatingsScreenHelperImpl instance;
	
	public static RatingsScreenHelperImpl getInstance() {
		if (instance == null) {
			instance = new RatingsScreenHelperImpl();
		}
		return instance;
	}
	

	public Cursor fetchAllCompetitorsForDecision(Activity activity, Long decisionRowID) {
		CompetitorsDatabaseAdapter competitorDatabaseAdapter = new CompetitorsDatabaseAdapter(activity);
		competitorDatabaseAdapter.open();
		Cursor allCompetitorsForDecision = competitorDatabaseAdapter.fetchAllCompetitorsForDecision(decisionRowID);
		
		return allCompetitorsForDecision;
	}


	public Cursor fetchAllRatingInstancesForSystem(Activity activity, Long ratingSystemID) {
		RatingInstanceDatabaseAdapter ratingInstanceDatabaseAdapter = new RatingInstanceDatabaseAdapter(activity);
		ratingInstanceDatabaseAdapter.open();
		Cursor allInstancesForSystemCursor = ratingInstanceDatabaseAdapter.fetchAllInstancesForRatingSystem(ratingSystemID);
		
		return allInstancesForSystemCursor;
	}


	public Cursor fetchAllCriteriaForDecision(Activity activity, Long decisionRowID) {
		CriteriaDatabaseAdapter criteriaDatabaseAdapter = new CriteriaDatabaseAdapter(activity);
		criteriaDatabaseAdapter.open();
		Cursor allCriteriaForDecision = criteriaDatabaseAdapter.fetchAllCriteriaForDecision(decisionRowID);
		
		return allCriteriaForDecision;
	}


	public Spinner generateRatingsSpinner(Activity activity, Long ratingSystemID, Integer spinnerID) {
		String[] from = new String[]{RatingInstanceColumns.NAME};
		int[] to = new int[]{android.R.id.text1};
		
		// For each rating system ID populate a spinner with the rating instances, and add cell to the row:
		Cursor allRatingInstances = fetchAllRatingInstancesForSystem(activity, ratingSystemID);
		
		SimpleCursorAdapter adapter =
		  new SimpleCursorAdapter(activity, android.R.layout.simple_spinner_item, allRatingInstances, from, to );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		// get reference to our spinner
		Spinner gridSpinner = new Spinner(activity);
		gridSpinner.setAdapter(adapter);
		
		gridSpinner.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		gridSpinner.setId(spinnerID);
		
		return gridSpinner;
	}
	
	
}
