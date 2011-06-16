package com.decideforme.ratings;

import com.db.decideforme.ratingsystems.RatingSystemDatabaseAdapter;

import android.app.Activity;
import android.database.Cursor;

public class RatingSystemHelper {
	
	public static Cursor fetchAllRatingSystems(Activity thisActivity) {
		RatingSystemDatabaseAdapter mRatingSystemDBAdapter = new RatingSystemDatabaseAdapter(thisActivity);
		mRatingSystemDBAdapter.open();
		return mRatingSystemDBAdapter.fetchAllRatingSystems();
	}

}
