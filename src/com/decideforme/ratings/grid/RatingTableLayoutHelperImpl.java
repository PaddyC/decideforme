package com.decideforme.ratings.grid;

import java.math.BigDecimal;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.decideforme.utils.StringUtils;
import com.decideforme.utils.TableLayoutHelperImpl;

public class RatingTableLayoutHelperImpl extends TableLayoutHelperImpl implements RatingTableLayoutHelper {
	private static final String EMPTY_STRING = "";

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

	public TextView getCompetitorNameTextView(Activity activity, String competitorName) {
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
	
	

}
