package com.decideforme.ratings.grid;

import android.app.Activity;
import android.widget.TableRow;
import android.widget.TextView;

public interface RatingGridHelper {
	
	public TableRow getNewRow(Activity activity);
	
	public TextView getCompetitorNameTextView(Activity activity, String competitorName);
	public TextView getCriterionNameTextView(Activity activity, String criterionName);
	
	public Integer getGridReference(Long competitorID, Long criterionID);

}
