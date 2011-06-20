package com.decideforme.ratings;

import java.util.List;

import android.app.Activity;
import android.database.Cursor;

import com.db.CursorUtils;
import com.db.decideforme.ratinginstance.RatingInstance;
import com.db.decideforme.ratinginstance.RatingInstanceDatabaseAdapter;

public class RatingInstanceHelper {
	
	private static RatingInstanceDatabaseAdapter getDatabaseAdapter(Activity thisActivity) {
		RatingInstanceDatabaseAdapter ratingInstanceDBAdapter = new RatingInstanceDatabaseAdapter(thisActivity);
		ratingInstanceDBAdapter.open();
		return ratingInstanceDBAdapter;
	}
	
	public static Long getScore(Activity thisActivity, Long ratingSelection) {
		
		RatingInstanceDatabaseAdapter ratingInstanceDBAdapter = getDatabaseAdapter(thisActivity);
		
		Cursor ratingInstanceCursor = ratingInstanceDBAdapter.fetchRatingInstance(ratingSelection);
		List<RatingInstance> ratingInstanceList = CursorUtils.getRatingInstancesForCursor(ratingInstanceCursor);
		
		Long score = ratingInstanceList.get(0).getRorder();
		
		ratingInstanceDBAdapter.close();
		return score;		
	}
	

	public static List<RatingInstance> fetchAllRatingInstancesForSystem(Activity activity, Long ratingSystemID) {
		RatingInstanceDatabaseAdapter ratingInstanceDatabaseAdapter = new RatingInstanceDatabaseAdapter(activity);
		ratingInstanceDatabaseAdapter.open();
		
		Cursor allInstancesForSystemCursor = ratingInstanceDatabaseAdapter.fetchAllInstancesForRatingSystem(ratingSystemID);
		List<RatingInstance> ratingInstanceList = CursorUtils.getRatingInstancesForCursor(allInstancesForSystemCursor);
		
		ratingInstanceDatabaseAdapter.close();
		
		return ratingInstanceList;
	}
	
	public static RatingInstance getRatingInstance(Activity activity, String ratingInstanceName) {
		RatingInstanceDatabaseAdapter ratingInstanceDatabaseAdapter = new RatingInstanceDatabaseAdapter(activity);
		ratingInstanceDatabaseAdapter.open();
		
		Cursor ratingInstances = ratingInstanceDatabaseAdapter.fetchRatingInstance(ratingInstanceName);
		List<RatingInstance> ratingInstanceList = CursorUtils.getRatingInstancesForCursor(ratingInstances);
		
		ratingInstanceDatabaseAdapter.close();
		
		return ratingInstanceList.get(0);
	}

}
