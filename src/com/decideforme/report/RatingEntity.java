package com.decideforme.report;


public class RatingEntity {
	
	private String competitorName;
	private String criterionName;
	private Long rating;
	
	public RatingEntity(String competitorName, String criterionName, Long rating) {
		this.setCompetitorName(competitorName);
		this.setCriterionName(criterionName);
		this.setRating(rating);
	}

	public void setCompetitorName(String competitorName) {
		this.competitorName = competitorName;
	}

	public String getCompetitorName() {
		return competitorName;
	}

	public void setCriterionName(String criterionName) {
		this.criterionName = criterionName;
	}

	public String getCriterionName() {
		return criterionName;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public Long getRating() {
		return rating;
	}

}
