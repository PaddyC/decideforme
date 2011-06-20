package com.decideforme.ratings;

import android.app.Activity;
import android.widget.Spinner;

public interface RatingsScreenHelper {
	
	public Spinner generateRatingsSpinner(Activity activity, Long ratingSystemID, Integer spinnerID);
	
}
