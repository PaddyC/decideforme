package com.decideforme.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.decisionrating.DecisionRatings;
import com.decideforme.R;
import com.decideforme.competitors.CompetitorHelper;
import com.decideforme.criteria.CriteriaHelper;
import com.decideforme.ratings.RatingEntitySorter;
import com.decideforme.ratings.RatingInstanceHelper;

public class ReportHelper {

	public static final String AND_WITH_SPACES = " and ";
	public static final String COMMA = ",";
	public static final String SINGLE_SPACE = " ";
	private static final String TAG = ReportHelper.class.getName();
	

	public static String getCompetitorListAsString(Activity thisActivity, List<Competitor> competitorList) {

		StringBuilder competitorsBuilder = new StringBuilder();
		if (competitorList.size() == 0) {
			competitorsBuilder.append(thisActivity.getResources().getString(R.string.report_no_competitors));
		} else if (competitorList.size() == 1) {
			Competitor competitor = competitorList.get(0);
			competitorsBuilder.append(thisActivity.getResources().getString(R.string.report_one_competitor) + SINGLE_SPACE + competitor.getDescription());
		} else if (competitorList.size() == 2) {
			competitorsBuilder.append(thisActivity.getResources().getString(R.string.report_competitors));
			competitorsBuilder.append(SINGLE_SPACE + competitorList.get(0).getDescription());
			competitorsBuilder.append(AND_WITH_SPACES + competitorList.get(1).getDescription());
		} else if (competitorList.size() > 2) {
			competitorsBuilder.append(thisActivity.getResources().getString(R.string.report_competitors));
			for (int i = 0; i < competitorList.size(); i++) {
				String competitorDesc = competitorList.get(i).getDescription();
				if (competitorList.size() == (i + 1)) {
					competitorsBuilder.append(AND_WITH_SPACES + competitorDesc);
				} else {
					if (i > 0) {
						competitorsBuilder.append(COMMA);
					}
					competitorsBuilder.append(SINGLE_SPACE + competitorDesc);
				}
			}
		}
		competitorsBuilder.append(".");
		return competitorsBuilder.toString();
	}
	
	public static String getCriteriaListAsString(Activity thisActivity, List<Criterion> criteriaList) {

		StringBuilder criteriaBuilder = new StringBuilder();
		if (criteriaList.size() == 0) {
			criteriaBuilder.append(thisActivity.getResources().getString(R.string.report_no_criteria));
		} else if (criteriaList.size() == 1) {
			Criterion criterion = criteriaList.get(0);
			criteriaBuilder.append(thisActivity.getResources().getString(R.string.report_one_criterion) + SINGLE_SPACE + criterion.getDescription());
		} else if (criteriaList.size() == 2) {
			criteriaBuilder.append(thisActivity.getResources().getString(R.string.report_criteria));
			criteriaBuilder.append(SINGLE_SPACE + criteriaList.get(0).getDescription());
			criteriaBuilder.append(AND_WITH_SPACES + criteriaList.get(1).getDescription());
		} else if (criteriaList.size() > 2) {
			criteriaBuilder.append(thisActivity.getResources().getString(R.string.report_criteria));
			for (int i = 0; i < criteriaList.size(); i++) {
				String criteriaDesc = criteriaList.get(i).getDescription();
				if (criteriaList.size() == (i + 1)) {
					criteriaBuilder.append(AND_WITH_SPACES + criteriaDesc);
				} else {
					if (i > 0) {
						criteriaBuilder.append(COMMA);
					}
					criteriaBuilder.append(SINGLE_SPACE + criteriaDesc);
				}
			}
		}
		criteriaBuilder.append(".");
		return criteriaBuilder.toString();
	}

	public static BigDecimal getScore(Activity thisActivity, List<DecisionRatings> ratingsForCompetitor) {
		
		BigDecimal score = new BigDecimal(0);
		for (DecisionRatings rating : ratingsForCompetitor) {
			Long ratingSelectionRowId = rating.getRatingSelectionId();
			Long rorder = RatingInstanceHelper.getScore(thisActivity, ratingSelectionRowId);
			score = score.add(new BigDecimal(rorder));
		}
		return score;
	}

	/**
	 * Get all the competitors with the top score. (Could be one or more..!)
	 * @param thisActivity
	 * @param overallRatings
	 * @return String
	 */
	public static String getPreferredOption(Activity thisActivity, List<CompetitorOverallRating> overallRatings) {
		
		List<CompetitorOverallRating> topChoices = new ArrayList<CompetitorOverallRating>();
		
		Integer topScore = overallRatings.get(0).getRating().intValue();
		for (CompetitorOverallRating rating : overallRatings) {
			if (rating.getRating().intValue() == topScore) {
				topChoices.add(rating);
			} else {
				break;
			}
		}
		
		StringBuilder preferredOption = new StringBuilder(thisActivity.getResources().getString(R.string.report_preference_preamble));
		if (topChoices.size() == 0) {
			preferredOption.append(thisActivity.getResources().getString(R.string.report_no_preference));
		} else if (topChoices.size() == 1) {
			CompetitorOverallRating rating = topChoices.get(0);
			preferredOption.append(SINGLE_SPACE + rating.getCompetitorName() + 
					SINGLE_SPACE + thisActivity.getResources().getString(R.string.report_one_preference));
		} else if (topChoices.size() == 2) {
			preferredOption.append(SINGLE_SPACE + topChoices.get(0).getCompetitorName());
			preferredOption.append(AND_WITH_SPACES + topChoices.get(1).getCompetitorName() + SINGLE_SPACE);
			preferredOption.append(thisActivity.getResources().getString(R.string.report_preferences));
		} else if (topChoices.size() > 2) {
			for (int i = 0; i < topChoices.size(); i++) {
				String competitorDesc = topChoices.get(i).getCompetitorName();
				if (topChoices.size() == (i + 1)) {
					preferredOption.append(AND_WITH_SPACES + competitorDesc);
				} else {
					if (i > 0) {
						preferredOption.append(COMMA);
					}
					preferredOption.append(SINGLE_SPACE + competitorDesc);
				}
				preferredOption.append(thisActivity.getResources().getString(R.string.report_preferences));
			}
		}
		
		return preferredOption.toString();
	}

	
	public static List<RatingEntity> getRatingEntities(Activity reportActivity, List<DecisionRatings> ratingsForCompetitor) {
		
		List<RatingEntity> ratingList = new ArrayList<RatingEntity>();
		for (DecisionRatings thisRating : ratingsForCompetitor) {
			
			Long competitorId = thisRating.getCompetitorId();
			Competitor competitor = CompetitorHelper.getCompetitor(reportActivity, competitorId).get(0);
			String competitorName = competitor.getDescription();
			
			Long criterionId = thisRating.getCriterionId();
			Log.d(TAG, "Criterion ID: " + criterionId);
			Criterion criterion = CriteriaHelper.getCriterion(reportActivity, criterionId).get(0);
			String criterionName = criterion.getDescription();
			Log.d(TAG, "Criterion Name: " + criterionName);
			
			Long ratingId = thisRating.getRatingSelectionId();
			Log.d(TAG, "Rating ID: " + ratingId);
			Long rating = RatingInstanceHelper.getScore(reportActivity, ratingId);
			Log.d(TAG, "Rating: " + rating);
			
			RatingEntity competitorScore = new RatingEntity(
					competitorName, criterionName, rating);
			ratingList.add(competitorScore);
		}
		return ratingList;
		
	}

	/**
	 * Get the best scores for each competitor, as well as the worst.
	 * @param reportActivity
	 * @param inputRatings
	 * @return
	 */
	public static String evaluateCompetitor(Activity reportActivity, String competitorName, List<DecisionRatings> inputRatings) {
		
		StringBuilder evaluationBuilder = new StringBuilder(competitorName);
		evaluationBuilder.append(SINGLE_SPACE);
		
		List<RatingEntity> ratings = getRatingEntities(reportActivity, inputRatings);
		
		String competitorsGoodSide = getCompetitorEvaluation(
				reportActivity, R.string.report_competitor_strength_preamble, getTopScores(ratings));
		evaluationBuilder.append(competitorsGoodSide);
		
		String competitorsBadSide = getCompetitorEvaluation(
				reportActivity, R.string.report_competitor_weakness_preamble, getBottomScores(ratings));
		evaluationBuilder.append(competitorsBadSide);
		
		return evaluationBuilder.toString();
	}

	private static List<RatingEntity> getBottomScores(List<RatingEntity> ratings) {
		Collections.reverse(ratings);
		Integer bottomScore = ratings.get(0).getRating().intValue();
		
		List<RatingEntity> bottomScores = new ArrayList<RatingEntity>();
		for (RatingEntity rating : ratings) {
			if (rating.getRating().intValue() == bottomScore) {
				bottomScores.add(rating);
			} else {
				break;
			}
		}
		return bottomScores;
	}

	private static List<RatingEntity> getTopScores(List<RatingEntity> ratings) {
		Collections.sort(ratings, new RatingEntitySorter());
		List<RatingEntity> topScores = new ArrayList<RatingEntity>();
		Integer topScore = ratings.get(0).getRating().intValue();
		for (RatingEntity rating : ratings) {
			if (rating.getRating().intValue() == topScore) {
				topScores.add(rating);
			} else {
				break;
			}
		}
		return topScores;
	}

	private static String getCompetitorEvaluation(Activity reportActivity, Integer preambleId, List<RatingEntity> ratings) {
		
		StringBuilder competitorEvaluation = new StringBuilder(reportActivity.getResources().getString(preambleId));
		if (ratings.size() == 0) {
			competitorEvaluation.append(reportActivity.getResources().getString(R.string.report_no_competitor_ratings));
		} else if (ratings.size() == 1) {
			RatingEntity rating = ratings.get(0);
			competitorEvaluation.append(SINGLE_SPACE + rating.getCriterionName());
		} else if (ratings.size() == 2) {
			competitorEvaluation.append(SINGLE_SPACE + reportActivity.getResources().getString(R.string.both));
			competitorEvaluation.append(SINGLE_SPACE + ratings.get(0).getCriterionName());
			competitorEvaluation.append(AND_WITH_SPACES + ratings.get(1).getCriterionName());
		} else if (ratings.size() > 2) {
			for (int i = 0; i < ratings.size(); i++) {
				String criterionName = ratings.get(i).getCriterionName();
				if (ratings.size() == (i + 1)) {
					competitorEvaluation.append(AND_WITH_SPACES + criterionName);
				} else {
					if (i > 0) {
						competitorEvaluation.append(COMMA);
					}
					competitorEvaluation.append(SINGLE_SPACE + criterionName);
				}
			}
		}
		return competitorEvaluation.toString();
	}
	
	private static String getCriterionEvaluation(Activity reportActivity, Integer preambleId, List<RatingEntity> ratings) {
		
		StringBuilder criterionEvaluation = new StringBuilder();
		if (ratings.size() == 0) {
			criterionEvaluation.append(reportActivity.getResources().getString(R.string.report_no_criteria));
		} else if (ratings.size() == 1) {
			criterionEvaluation.append(reportActivity.getResources().getString(preambleId));
			RatingEntity rating = ratings.get(0);
			criterionEvaluation.append(SINGLE_SPACE + rating.getCompetitorName());
		} else if (ratings.size() == 2) {
			criterionEvaluation.append(reportActivity.getResources().getString(preambleId));
			criterionEvaluation.append(SINGLE_SPACE + ratings.get(0).getCompetitorName());
			criterionEvaluation.append(AND_WITH_SPACES + ratings.get(1).getCompetitorName() + SINGLE_SPACE);
			criterionEvaluation.append(reportActivity.getResources().getString(R.string.jointly));
		} else if (ratings.size() > 2) {
			criterionEvaluation.append(reportActivity.getResources().getString(preambleId));
			for (int i = 0; i < ratings.size(); i++) {
				String competitorName = ratings.get(i).getCompetitorName();
				if (ratings.size() == (i + 1)) {
					criterionEvaluation.append(AND_WITH_SPACES + competitorName);
				} else {
					if (i > 0) {
						criterionEvaluation.append(COMMA);
					}
					criterionEvaluation.append(SINGLE_SPACE + competitorName);
				}
			}
		}
		return criterionEvaluation.toString();
	}

	public static String evaluateCriterion(ReportActivity reportActivity, String criterionName, List<DecisionRatings> ratingsForCriterion) {
		
		StringBuilder criterionReport = new StringBuilder(reportActivity.getResources().getString(R.string.report_criterion_preamble));
		criterionReport.append(SINGLE_SPACE + criterionName + SINGLE_SPACE);
		
		List<RatingEntity> ratings = getRatingEntities(reportActivity, ratingsForCriterion);
		
		String competitorsGoodSide = getCriterionEvaluation(
				reportActivity, R.string.report_criterion_strength_preamble, getTopScores(ratings));
		criterionReport.append(competitorsGoodSide);
		
		String competitorsBadSide = getCriterionEvaluation(
				reportActivity, R.string.report_criterion_weakness_preamble, getBottomScores(ratings));
		criterionReport.append(competitorsBadSide);
		
		return criterionReport.toString();
	}

}
