package com.decideforme.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.decision.Decision;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.decisionrating.DecisionRatings;
import com.db.decideforme.decisionrating.DecisionRatingsHelper;
import com.decideforme.R;
import com.decideforme.competitors.CompetitorHelper;
import com.decideforme.criteria.CriteriaHelper;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.decision.DecisionHelper;
import com.decideforme.ratings.CompetitorSort;
import com.decideforme.utils.BundleHelper;

public class ReportActivity extends DashboardActivity {
	public static final String TAG = ReportActivity.class.getName();
	
	private LinearLayout mReportLayout;
	private long mDecisionRowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		setTitleFromActivityLabel (R.id.title_text);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);

		fillReport();
	}
	
	private void fillReport() {
		
		mReportLayout = (LinearLayout) findViewById(R.id.report);
		mReportLayout.setBackgroundDrawable(
				getResources().getDrawable(R.drawable.textfield_default));
		
		Typeface envyCode = Typeface.createFromAsset(getAssets(), "fonts/Envy Code R.ttf");
		
		// get decision name : header row
		Cursor thisDecision = DecisionHelper.getDecision(this, mDecisionRowId);
		
		String decisionName = thisDecision.getString(Decision.COLUMN_INDEX_NAME);
		TextView reportHeader = (TextView) findViewById(R.id.reportHeader);
		reportHeader.setTypeface(envyCode);
		reportHeader.setText(decisionName);
		
		// decision description : sub-heading
		String decisionDesc = thisDecision.getString(Decision.COLUMN_INDEX_DESC);
		TextView reportSubHeader = (TextView) findViewById(R.id.reportSubHeader);
		reportSubHeader.setTypeface(envyCode);
		reportSubHeader.setText(decisionDesc);
		
		// criteria
		List<Criterion> allCriteriaForDecision = CriteriaHelper.getAllCriteriaForDecision(this, mDecisionRowId);
		
		String allCriteria = ReportHelper.getCriteriaListAsString(this, allCriteriaForDecision);
		TextView summaryCriteria = (TextView) findViewById(R.id.decisionSummaryCriteria);
		summaryCriteria.setText(allCriteria);
		
		// for each criterion
		StringBuilder criteriaEvaluation = new StringBuilder();
		for (Criterion currentCriterion : allCriteriaForDecision) {
			String criterionName = currentCriterion.getDescription();
			
			List<DecisionRatings> ratingsForCriterion = DecisionRatingsHelper.getAllRatingsForDecisionCriterion(
					this, mDecisionRowId, currentCriterion.getRowId());
			
			criteriaEvaluation.append(ReportHelper.evaluateCriterion(this, criterionName, ratingsForCriterion));
			criteriaEvaluation.append(". ");
		}
		TextView criteriaReport = (TextView) findViewById(R.id.criterionReport);
		criteriaReport.setText(criteriaEvaluation);

		
		// competitors
		List<Competitor> allCompetitorsForDecision = CompetitorHelper.getAllCompetitorsForDecision(this, mDecisionRowId);
		String allCompetitors = ReportHelper.getCompetitorListAsString(this, allCompetitorsForDecision);
		TextView summaryCompetitors = (TextView) findViewById(R.id.decisionSummaryCompetitors);
		summaryCompetitors.setText(allCompetitors);
		
		// for each competitor
		List<CompetitorOverallRating> overallRatings = new ArrayList<CompetitorOverallRating>();
		
		StringBuilder competitorEvaluation = new StringBuilder();
			
		for (Competitor currentCompetitor : allCompetitorsForDecision) {
			
			String competitorName = currentCompetitor.getDescription();
			
			List<DecisionRatings> ratingsForCompetitor = DecisionRatingsHelper.getAllRatingsForDecisionCompetitor(
					this, mDecisionRowId, currentCompetitor.getRowId());
			
			competitorEvaluation.append(ReportHelper.evaluateCompetitor(this, competitorName, ratingsForCompetitor));
			competitorEvaluation.append(". ");
			
			BigDecimal competitorOverallScore = ReportHelper.getScore(this, ratingsForCompetitor);
			CompetitorOverallRating thisCompetitorRating = new CompetitorOverallRating(competitorName, competitorOverallScore);
			overallRatings.add(thisCompetitorRating);
		}
		
		TextView competitorReport = (TextView) findViewById(R.id.competitorReport);
		competitorReport.setText(competitorEvaluation);
		
		// winning competitor
		Collections.sort(overallRatings, new CompetitorSort());
		String preferredOption = ReportHelper.getPreferredOption(this, overallRatings);
		TextView preference = (TextView) findViewById(R.id.decisionSummaryWinner);
		preference.setText(preferredOption);
		
		
		TableLayout rankingTable = (TableLayout) findViewById(R.id.rankings);
		if (rankingTable.getChildCount() > 0) {
			rankingTable.removeAllViews();
		}
		
		TableRow header = new TableRow(this);
		header.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		TextView headerText = new TextView(this);
		headerText.setText(R.string.ranking_table);
		headerText.setTypeface(envyCode, android.R.style.TextAppearance_Small_Inverse);
		header.addView(headerText);
		rankingTable.addView(header);
		
		Integer i = 1;
		for (CompetitorOverallRating rating : overallRatings) {
			TableRow rankingRow = new TableRow(this);
			rankingRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			
			TextView rank = new TextView(this);
			rank.setGravity(Gravity.CENTER);
			rank.setText(i.toString());
			rankingRow.addView(rank);
			
			TextView competitor = new TextView(this);
			competitor.setText(rating.getCompetitorName());
			rankingRow.addView(competitor);
			
			rankingTable.addView(rankingRow);
			i++;
		}
	}
	
    @Override
	protected void onResume() {
		super.onResume();
		fillReport();
		Log.d(TAG, " << onResume()");
	}

}
