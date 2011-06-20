package com.decideforme.ratings;

import java.util.List;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableRow.LayoutParams;

import com.db.decideforme.ratinginstance.RatingInstance;

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


	public Spinner generateRatingsSpinner(Activity activity, Long ratingSystemID, Integer spinnerID) {

		// For each rating system ID populate a spinner with the rating instances, and add cell to the row:
		List<RatingInstance> ratingInstanceList = RatingInstanceHelper.fetchAllRatingInstancesForSystem(activity, ratingSystemID);
		
		ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (activity, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (RatingInstance rating : ratingInstanceList) {
			adapter.add(rating.getName());
		}
		
		// get reference to our spinner
		Spinner gridSpinner = new Spinner(activity);
		gridSpinner.setAdapter(adapter);
		gridSpinner.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		gridSpinner.setId(spinnerID);
		
		return gridSpinner;
	}
	
	
}
