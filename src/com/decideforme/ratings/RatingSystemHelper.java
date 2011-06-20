package com.decideforme.ratings;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;

import com.db.CursorUtils;
import com.db.decideforme.ratingsystems.RatingSystem;
import com.db.decideforme.ratingsystems.RatingSystemDatabaseAdapter;

public class RatingSystemHelper {
	
	public static List<RatingSystem> fetchAllRatingSystems(Activity thisActivity) {
		RatingSystemDatabaseAdapter ratingSystemDBAdapter = getDBAdapter(thisActivity);
		
		Cursor ratingSystemCursor = ratingSystemDBAdapter.fetchAllRatingSystems();
		List<RatingSystem> ratingSystemList = CursorUtils.getRatingSystemsForCursor(ratingSystemCursor);

		ratingSystemDBAdapter.close();
		
		return ratingSystemList;
	}


	public static RatingSystem getRatingSystemIdForName(Activity thisActivity, String ratingSystemName) {
		RatingSystemDatabaseAdapter ratingSystemDBAdapter = getDBAdapter(thisActivity);
		
		Cursor ratingSystemCursor = ratingSystemDBAdapter.getRatingSystem(ratingSystemName);
		List<RatingSystem> ratingSystemList = CursorUtils.getRatingSystemsForCursor(ratingSystemCursor);
		
		ratingSystemDBAdapter.close();
		
		return ratingSystemList.get(0);
	}
	
	public static RatingSystem getRatingSystem(Activity thisActivity, long ratingSystemId) {
		RatingSystemDatabaseAdapter ratingSystemDBAdapter = getDBAdapter(thisActivity);
		
		Cursor ratingSystemCursor = ratingSystemDBAdapter.getRatingSystem(ratingSystemId);
		List<RatingSystem> ratingSystemList = CursorUtils.getRatingSystemsForCursor(ratingSystemCursor);
		
		ratingSystemDBAdapter.close();
		
		return ratingSystemList.get(0);
	}
	

	private static RatingSystemDatabaseAdapter getDBAdapter(Activity thisActivity) {
		RatingSystemDatabaseAdapter mRatingSystemDBAdapter = new RatingSystemDatabaseAdapter(thisActivity);
		mRatingSystemDBAdapter.open();
		return mRatingSystemDBAdapter;
	}
}
