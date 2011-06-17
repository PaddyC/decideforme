package com.decideforme.report;

import java.math.BigDecimal;

public class CompetitorOverallRating {
	
	private String competitorName;
	private BigDecimal rating;
	
	public CompetitorOverallRating(String competitorName, BigDecimal rating) {
		this.setCompetitorName(competitorName);
		this.setRating(rating);
	}

	public void setCompetitorName(String competitorName) {
		this.competitorName = competitorName;
	}

	public String getCompetitorName() {
		return competitorName;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public BigDecimal getRating() {
		return rating;
	}

}
