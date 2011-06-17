package com.decideforme.ratings;

import java.util.Comparator;

import com.decideforme.report.RatingEntity;

public class RatingEntitySorter implements Comparator<RatingEntity> {

	public int compare(RatingEntity ratingOne, RatingEntity ratingTwo) {
		int result = 0;
		if (ratingOne.getRating().intValue() < ratingTwo.getRating().intValue()) {
			result = -1;
		} else if (ratingOne.getRating().intValue() > ratingTwo.getRating().intValue()) {
			result = 1;
		} 
		return result;
	}

}
