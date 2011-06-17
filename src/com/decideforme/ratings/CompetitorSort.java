package com.decideforme.ratings;

import java.util.Comparator;

import com.decideforme.report.CompetitorOverallRating;

/**
 * Lowest score is the best, so sort in ascending order.
 * @author PaddyC
 *
 */
public class CompetitorSort implements Comparator<CompetitorOverallRating> {

	public int compare(CompetitorOverallRating ratingOne, CompetitorOverallRating ratingTwo) {
		
		int result = 0;
		if (ratingOne.getRating().intValue() < ratingTwo.getRating().intValue()) {
			result = -1;
		} else if (ratingOne.getRating().intValue() > ratingTwo.getRating().intValue()) {
			result = 1;
		} 
		return result;
	}

}
