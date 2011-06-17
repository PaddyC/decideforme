package com.decideforme.ratings.grid;

import java.math.BigDecimal;

import android.util.Log;

import com.decideforme.utils.StringUtils;

public class RatingTableLayoutHelperImpl implements RatingTableLayoutHelper {
	public static final String TAG = RatingTableLayoutHelper.class.getName();

	private static RatingTableLayoutHelperImpl instance;
	
	private RatingTableLayoutHelperImpl() {
	}
	
	public static RatingTableLayoutHelperImpl getInstance() {
		if (instance == null) {
			instance = new RatingTableLayoutHelperImpl();
		}
		return instance;
	}
	
	public Integer getGridReference(Long competitorID, Long criterionID) {
		Integer spinnerID = 0;
		
		BigDecimal competitor = new BigDecimal(competitorID);
		BigDecimal criterion = new BigDecimal(criterionID);
		BigDecimal id = (competitor.multiply(new BigDecimal(100))).add(criterion);
		
		spinnerID = id.intValue();
		
		Log.d(TAG, " << getGridReference(), " +
				"returned '" + StringUtils.objectAsString(spinnerID) + "'");
		
		return spinnerID;
	}
}