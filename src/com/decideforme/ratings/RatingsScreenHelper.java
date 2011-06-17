package com.decideforme.ratings;

import android.app.Activity;
import android.database.Cursor;
import android.widget.Spinner;

public interface RatingsScreenHelper {
	
	public Spinner generateRatingsSpinner(Activity activity, Long ratingSystemID, Integer spinnerID);
	
	public Cursor fetchAllRatingInstancesForSystem(Activity activity, Long ratingSystemID);

}
