package com.decideforme.ratings.grid;

import java.math.BigDecimal;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.decideforme.utils.StringUtils;

public class RatingGridHelperImpl implements RatingGridHelper {
	private static final String EMPTY_STRING = "";

	public static final String TAG = RatingGridHelper.class.getName();

	private static RatingGridHelperImpl instance;
	
	private RatingGridHelperImpl() {
	}
	
	public static RatingGridHelperImpl getInstance() {
		if (instance == null) {
			instance = new RatingGridHelperImpl();
		}
		return instance;
	}
	
	public Integer getGridReference(Long competitorID, Long criterionID) {
		Log.d(TAG, " >> getGridReference(" +
				"competitorID '" + StringUtils.objectAsString(competitorID) + "', " +
				"criterionID '" + StringUtils.objectAsString(criterionID) + "')");
		
		Integer spinnerID = 0;
		
		BigDecimal competitor = new BigDecimal(competitorID);
		BigDecimal criterion = new BigDecimal(criterionID);
		BigDecimal id = (competitor.multiply(new BigDecimal(100))).add(criterion);
		
		spinnerID = id.intValue();
		
		Log.d(TAG, " << getGridReference(), " +
				"returned '" + StringUtils.objectAsString(spinnerID) + "'");
		
		return spinnerID;
	}

	public TextView getCompetitorNameTextView(Activity activity, String competitorName) {
		Log.d(TAG, " >> getCompetitorNameTextView(" +
				"activity '" + StringUtils.objectAsString(activity) + "', " +
				"competitorName '" + StringUtils.objectAsString(competitorName) + "')");
		
		TextView competitorNameView = new TextView(activity);
		competitorNameView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		competitorNameView.setBackgroundColor(Color.GRAY);
		competitorNameView.setTextColor(Color.BLACK);
		competitorNameView.setPadding(20, 0, 0, 0);
		competitorNameView.setText(competitorName);
		
		Log.d(TAG, " << getCompetitorNameTextView()" +
				", returned '" + StringUtils.objectAsString(competitorNameView) + "'");
		return competitorNameView;
	}
	
	public TextView getCriterionNameTextView(Activity activity, String criterionName) {
		Log.d(TAG, " >> getCriterionNameTextView(" +
				"activity '" + StringUtils.objectAsString(activity) + "', " +
				"criterionName '" + StringUtils.objectAsString(criterionName) + "')");
		
		TextView criterionNameView = new TextView(activity);
		criterionNameView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		criterionNameView.setText(criterionName);
		if (criterionName != null && ! EMPTY_STRING.equalsIgnoreCase(criterionName)) {
			criterionNameView.setBackgroundColor(Color.GRAY);
			criterionNameView.setTextColor(Color.BLACK);	
		}
		criterionNameView.setPadding(20, 0, 0, 0);
    	
		Log.d(TAG, " << getCriterionNameTextView()" +
				", returned '" + StringUtils.objectAsString(criterionNameView) + "'");
		return criterionNameView;
	}

	public TableRow getNewRow(Activity activity) {
		Log.d(TAG, " >> getNewRow(" +
				"activity '" + StringUtils.objectAsString(activity) + "')");
		
		TableRow tableRow = new TableRow(activity);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		tableRow.setPadding(0, 1, 0, 1);
		
		Log.d(TAG, " << getNewRow(), returned '" + StringUtils.objectAsString(tableRow) + "'");
		return tableRow;
	}
	
	

}
