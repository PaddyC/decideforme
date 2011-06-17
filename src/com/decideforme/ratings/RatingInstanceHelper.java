package com.decideforme.ratings;

import java.util.List;

import com.db.CursorUtils;
import com.db.decideforme.ratinginstance.RatingInstance;
import com.db.decideforme.ratinginstance.RatingInstanceDatabaseAdapter;

import android.app.Activity;
import android.database.Cursor;

public class RatingInstanceHelper {
	
	public static Long getRorderForRating(Activity thisActivity, Long ratingSelection) {
		
		RatingInstanceDatabaseAdapter ratingInstanceDBAdapter = getDatabaseAdapter(thisActivity);
		
		Cursor ratingInstanceCursor = ratingInstanceDBAdapter.fetchRatingInstance(ratingSelection);
		List<RatingInstance> ratingInstanceList = CursorUtils.getRatingInstancesForCursor(ratingInstanceCursor);
		
		Long rorder = ratingInstanceList.get(0).getRorder();
		
		ratingInstanceDBAdapter.close();
		return rorder;		
	}

	private static RatingInstanceDatabaseAdapter getDatabaseAdapter(Activity thisActivity) {
		RatingInstanceDatabaseAdapter ratingInstanceDBAdapter = new RatingInstanceDatabaseAdapter(thisActivity);
		ratingInstanceDBAdapter.open();
		return ratingInstanceDBAdapter;
	}
	
	

}
