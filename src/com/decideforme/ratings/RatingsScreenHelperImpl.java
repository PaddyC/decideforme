package com.decideforme.ratings;

import com.db.decideforme.competitors.CompetitorsDatabaseAdapter;
import com.db.decideforme.criteria.CriteriaDatabaseAdapter;
import com.db.decideforme.ratinginstance.RatingInstanceDatabaseAdapter;
import com.db.decideforme.ratinginstance.RatingInstance.RatingInstanceColumns;
import com.decideforme.utils.StringUtils;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableRow.LayoutParams;

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
		Log.d(TAG, " >> fetchAllCompetitorsForDecision(" +
				"activity '" + StringUtils.objectAsString(activity) + "', " +
				"decisionRowID '" + StringUtils.objectAsString(decisionRowID) + "')");
		
		CompetitorsDatabaseAdapter competitorDatabaseAdapter = new CompetitorsDatabaseAdapter(activity);
		competitorDatabaseAdapter.open();
		Cursor allCompetitorsForDecision = competitorDatabaseAdapter.fetchAllCompetitorsForDecision(decisionRowID);
		
		Log.d(TAG, " << fetchAllCompetitorsForDecision(), " +
				"returned '" + StringUtils.objectAsString(allCompetitorsForDecision) + "'");
		return allCompetitorsForDecision;
	}


	public Cursor fetchAllRatingInstancesForSystem(Activity activity, Long ratingSystemID) {
		Log.d(TAG, " >> fetchAllRatingInstancesForSystem(" +
				"activity '" + StringUtils.objectAsString(activity) + "', " +
				"ratingSystemID '" + StringUtils.objectAsString(ratingSystemID) + "')");
		
		RatingInstanceDatabaseAdapter ratingInstanceDatabaseAdapter = new RatingInstanceDatabaseAdapter(activity);
		ratingInstanceDatabaseAdapter.open();
		Cursor allInstancesForSystemCursor = ratingInstanceDatabaseAdapter.fetchAllInstancesForRatingSystem(ratingSystemID);
		
		Log.d(TAG, " << fetchAllRatingInstancesForSystem()" +
				", returned '" + StringUtils.objectAsString(allInstancesForSystemCursor) + "'");
		return allInstancesForSystemCursor;
	}


	public Cursor fetchAllCriteriaForDecision(Activity activity, Long decisionRowID) {
		Log.d(TAG, " >> fetchAllCriteriaForDecision(" +
				"activity '" + StringUtils.objectAsString(activity) + "', " +
				"decisionRowID '" + StringUtils.objectAsString(decisionRowID) + "')");
		
		CriteriaDatabaseAdapter criteriaDatabaseAdapter = new CriteriaDatabaseAdapter(activity);
		criteriaDatabaseAdapter.open();
		Cursor allCriteriaForDecision = criteriaDatabaseAdapter.fetchAllCriteriaForDecision(decisionRowID);
		
		Log.d(TAG, " << fetchAllCompetitorsForDecision(), " +
				"returned '" + StringUtils.objectAsString(allCriteriaForDecision) + "'");
		return allCriteriaForDecision;
	}


	public Spinner generateRatingsSpinner(Activity activity, Long ratingSystemID, Integer spinnerID) {
		Log.d(TAG, " >> generateRatingsSpinner(" +
				"activity '" + StringUtils.objectAsString(activity) + "', " +
				"ratingSystemID '" + StringUtils.objectAsString(ratingSystemID) + "', " +
				"spinnerID '" + StringUtils.objectAsString(spinnerID) + "')");
		
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
		
		Log.d(TAG, " << generateRatingsSpinner()" +
				", returned '" + StringUtils.objectAsString(gridSpinner) + "'");
		
		return gridSpinner;
	}
	
	
}
