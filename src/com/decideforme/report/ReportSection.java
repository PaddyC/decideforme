package com.decideforme.report;

import com.decideforme.R;

public enum ReportSection {
	
	PREFERRED_OPTION(2, R.string.report_preference_preamble, R.string.report_no_preference, R.string.report_one_preference, R.string.report_preferences),
	COMPETITOR_LIST(2, R.string.empty_string, R.string.report_no_competitors, R.string.report_one_competitor, R.string.report_competitors),
	CRITERIA_LIST(3, R.string.empty_string, R.string.report_no_criteria, R.string.report_one_criterion, R.string.report_criteria), 
	COMPETITOR_EVALUATION_POS(2, R.string.report_competitor_strength_preamble, R.string.report_no_competitor_ratings, R.string.empty_string, R.string.empty_string),
	COMPETITOR_EVALUATION_NEG(2, R.string.report_competitor_weakness_preamble, R.string.report_no_competitor_ratings, R.string.empty_string, R.string.empty_string);
	
	private Integer subjectType;
	private Integer preambleId;
	private Integer none;
	private Integer one;
	private Integer multi;
	private ReportSection(Integer subjectType, Integer preambleId, Integer none, Integer one, Integer multi) {
		this.subjectType = subjectType;
		this.preambleId = preambleId;
		this.none = none;
		this.one = one;
		this.multi = multi;
	}
	public Integer getPreambleId() {
		return preambleId;
	}
	public Integer getNone() {
		return none;
	}
	public Integer getOne() {
		return one;
	}
	public Integer getMulti() {
		return multi;
	}
	public Integer getSubjectType() {
		return subjectType;
	}
}
