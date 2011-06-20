package com.decideforme.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

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
import com.decideforme.utils.StringUtils;

public class ReportActivity extends DashboardActivity {
	public static final String TAG = ReportActivity.class.getName();
	
	private RelativeLayout mReportLayout;
	private long mDecisionRowId;
	
	private String emailSubject;
	private String emailBody;
	
	private String validationMessage;
	
	private String decisionName;
	private String decisionDesc;
	private String allCriteria;
	private StringBuilder criteriaEvaluation;
	private String allCompetitors;
	private StringBuilder competitorEvaluation;
	private String preferredOption;
	private StringBuilder emailRanking;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		setTitleFromActivityLabel (R.id.title_text);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
		
	    Button saveButton = (Button) findViewById(R.id.saveButton);
	    saveButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.save_button));
	    saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
		Button emailButton = (Button) findViewById(R.id.emailButton);
		emailButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.email_button));
		emailButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View thisView) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{});
				i.putExtra(Intent.EXTRA_SUBJECT, getEmailSubject());
				i.putExtra(Intent.EXTRA_TEXT   , getEmailBody());
				try {
				    startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(ReportActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
				}
			}
		});
				
		fillReport();
	}
	
	private boolean isValid() {
		
		boolean isValid = false;
		
		// Check for Ratings
		List<DecisionRatings> allRatingsForDecision = DecisionRatingsHelper.getAllRatingsForDecision(this, mDecisionRowId);
		if (allRatingsForDecision.size() == 0) {
			validationMessage = "Not ready for a report yet, you need to rate your decision first";
		} else {
			isValid = true;
		}
		
		return isValid;
		
	}
	
	private void fillReport() {
		
		if (isValid()) {
			// produce report
			mReportLayout = (RelativeLayout) findViewById(R.id.report);
			mReportLayout.setBackgroundDrawable(
					getResources().getDrawable(R.drawable.textfield_default));
			
			Typeface envyCode = Typeface.createFromAsset(getAssets(), "fonts/Envy Code R.ttf");
			
			// get decision name : header row
			Decision thisDecision = DecisionHelper.getDecision(this, mDecisionRowId).get(0);
			
			decisionName = thisDecision.getName();
			TextView reportHeader = (TextView) findViewById(R.id.reportHeader);
			reportHeader.setTypeface(envyCode);
			reportHeader.setText(decisionName);
			
			// decision description : sub-heading
			decisionDesc = thisDecision.getDescription();
			TextView reportSubHeader = (TextView) findViewById(R.id.reportSubHeader);
			reportSubHeader.setTypeface(envyCode);
			reportSubHeader.setText(decisionDesc);
			
			setTheEmailSubject(decisionName, decisionDesc);
			
			// criteria
			List<Criterion> allCriteriaForDecision = CriteriaHelper.getAllCriteriaForDecision(this, mDecisionRowId);
			allCriteria = ReportHelper.getCriteriaListAsString(this, allCriteriaForDecision);
			TextView summaryCriteria = (TextView) findViewById(R.id.decisionSummaryCriteria);
			summaryCriteria.setText(allCriteria);
			
			// for each criterion
			populateCriteriaEvaluation(allCriteriaForDecision);

			// competitors
			List<Competitor> allCompetitorsForDecision = CompetitorHelper.getAllCompetitorsForDecision(this, mDecisionRowId);
			allCompetitors = ReportHelper.getCompetitorListAsString(this, allCompetitorsForDecision);
			Log.d(TAG, "All Competitors: " + StringUtils.objectAsString(allCompetitors));
			TextView summaryCompetitors = (TextView) findViewById(R.id.decisionSummaryCompetitors);
			summaryCompetitors.setText(allCompetitors);
			
			// for each competitor
			List<CompetitorOverallRating> overallRatings = new ArrayList<CompetitorOverallRating>();
			
			populateCompetitorEvaluation(allCompetitorsForDecision, overallRatings);
			TextView competitorReport = (TextView) findViewById(R.id.competitorReport);
			competitorReport.setText(competitorEvaluation);
			
			// winning competitor
			Collections.sort(overallRatings, new CompetitorSort());
			preferredOption = ReportHelper.getPreferredOption(this, overallRatings);
			TextView preference = (TextView) findViewById(R.id.decisionSummaryWinner);
			preference.setText(preferredOption);
			
			// Ranking table
			populateRankingTable(envyCode, overallRatings);
			
			populateEmailBody();
		} else {
			Toast.makeText(ReportActivity.this, validationMessage, Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void populateEmailBody() {
		StringBuilder emailBodyBuilder = new StringBuilder(getResources().getString(R.string.report_ident));
		emailBodyBuilder.append("\n\n" + getResources().getString(R.string.decision_name) + " " + decisionName);
		emailBodyBuilder.append("\n" + getResources().getString(R.string.decision_description) + " " + decisionDesc);
		emailBodyBuilder.append("\n\n" + getResources().getString(R.string.competitors));
		emailBodyBuilder.append("\n" + allCompetitors);
		emailBodyBuilder.append(preferredOption);
		emailBodyBuilder.append("\n\n" + emailRanking);
		emailBodyBuilder.append("\n\n" + competitorEvaluation);
		emailBodyBuilder.append("\n\n" + getResources().getString(R.string.criteria));
		emailBodyBuilder.append("\n" + allCriteria);
		emailBodyBuilder.append(criteriaEvaluation);
		setEmailBody(emailBodyBuilder.toString());
	}

	private void populateCompetitorEvaluation(List<Competitor> allCompetitorsForDecision, List<CompetitorOverallRating> overallRatings) {
		
		competitorEvaluation = new StringBuilder();
			
		for (Competitor currentCompetitor : allCompetitorsForDecision) {
			
			String competitorName = currentCompetitor.getDescription();
			Log.d(TAG, "Evaluating " + competitorName);
			
			List<DecisionRatings> ratingsForCompetitor = DecisionRatingsHelper.getAllRatingsForDecisionCompetitor(
					this, mDecisionRowId, currentCompetitor.getRowId());
			
			Log.d(TAG, "Number of Ratings " + ratingsForCompetitor.size());
			
			competitorEvaluation.append(ReportHelper.evaluateCompetitor(this, competitorName, ratingsForCompetitor));
			competitorEvaluation.append(". ");
			
			BigDecimal competitorOverallScore = ReportHelper.getScore(this, ratingsForCompetitor);
			CompetitorOverallRating thisCompetitorRating = new CompetitorOverallRating(competitorName, competitorOverallScore);
			overallRatings.add(thisCompetitorRating);
		}
	}

	private void populateCriteriaEvaluation(List<Criterion> allCriteriaForDecision) {
		
		criteriaEvaluation = new StringBuilder();
		for (Criterion currentCriterion : allCriteriaForDecision) {
			String criterionName = currentCriterion.getDescription();
			
			List<DecisionRatings> ratingsForCriterion = DecisionRatingsHelper.getAllRatingsForDecisionCriterion(
					this, mDecisionRowId, currentCriterion.getRowId());
			
			criteriaEvaluation.append(ReportHelper.evaluateCriterion(this, criterionName, ratingsForCriterion));
			criteriaEvaluation.append(". ");
		}
		TextView criteriaReport = (TextView) findViewById(R.id.criterionReport);
		criteriaReport.setText(criteriaEvaluation);
	}

	
	private void populateRankingTable(Typeface envyCode, List<CompetitorOverallRating> overallRatings) {
		
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
		emailRanking = new StringBuilder(getResources().getString(R.string.ranking_table));
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
			
			emailRanking.append("\n" + i.toString() + ". ");
			emailRanking.append(rating.getCompetitorName());
			
			rankingTable.addView(rankingRow);
			i++;
		}
	}

	private void setTheEmailSubject(String decisionName, String decisionDesc) {
		if (decisionName == null || "".equalsIgnoreCase(decisionName)) {
			if (decisionDesc == null || "".equalsIgnoreCase(decisionDesc)) {
				setEmailSubject(getResources().getString(R.string.app_name));
			} else {
				setEmailSubject(getResources().getString(R.string.email_subject) + " : " + decisionDesc);
			}
		} else {
			setEmailSubject(getResources().getString(R.string.email_subject) + " : " + decisionName);
		}
	}
	
    @Override
	protected void onResume() {
		super.onResume();
		fillReport();
		Log.d(TAG, " << onResume()");
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailBody() {
		return emailBody;
	}

}
