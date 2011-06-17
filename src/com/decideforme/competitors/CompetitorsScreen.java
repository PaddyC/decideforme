package com.decideforme.competitors;

import java.math.BigDecimal;
import java.util.List;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;
import com.decideforme.utils.SubjectConstants;
import com.decideforme.utils.ViewHelper;
import com.decideforme.utils.ViewHelperImpl;

public class CompetitorsScreen extends DashboardActivity {
	private static final String TAG = CompetitorsScreen.class.getName();
    
    public static final int ACTIVITY_EDIT = 1;
    public static final int ACTIVITY_DELETE = 2;
	
	protected Long mDecisionRowId;
	private TableLayout mDynamicCompetitorsTable;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.competitors_list);

		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
		
		setTitleFromActivityLabel (R.id.title_text);
		
		fillData();
		
		Log.d(TAG, " << onCreate()");
	}


	public void fillData() {
    	Log.d(TAG, " >> fillData()");
    	mDynamicCompetitorsTable = (TableLayout) findViewById(R.id.competitorsList);
    	// Table Start! Initialise the table layout:
		if(mDynamicCompetitorsTable.getChildCount() > 0) {
			mDynamicCompetitorsTable.removeAllViews();
		}
		
		ViewHelper viewHelper = new ViewHelperImpl(mDecisionRowId, this, SubjectConstants.COMPETITOR);
    	
		List<Competitor> competitorList = CompetitorHelper.getAllCompetitorsForDecision(this, mDecisionRowId);
    	if (competitorList.size() == 0) {
    		TableRow thisRow = viewHelper.getNewRow(this, true);
    		thisRow.addView(viewHelper.getTextView(
    				0, getResources().getString(R.string.no_competitors), R.style.HomeButton, Typeface.SANS_SERIF, true, false));
        	thisRow.addView(viewHelper.getNewButton(1));
        	
    		mDynamicCompetitorsTable.addView(thisRow);
    	} else {
    		for (int competitorCount = 0; competitorCount < competitorList.size(); competitorCount++) {
        		Competitor thisCompetitor = competitorList.get(competitorCount);
    			BigDecimal id = new BigDecimal(competitorCount).multiply(new BigDecimal(100));
            	Integer viewId = id.intValue();
            	
            	TableRow thisRow = viewHelper.getNewRow(this, true);
            	
            	String competitorDesc = thisCompetitor.getDescription();
        		thisRow.addView(viewHelper.getTextView(
            			viewId++, competitorDesc, R.style.HomeButton, Typeface.SANS_SERIF, true, true));
    			
        		thisRow.addView(viewHelper.getNewButton(viewId++));
    			thisRow.addView(viewHelper.getEditButton(viewId++));
    			thisRow.addView(viewHelper.getDeleteButton(viewId++));
    			
    			mDynamicCompetitorsTable.addView(thisRow);
        	}
    	}
	}

	
    @Override
	protected void onResume() {
    	super.onResume();
		fillData();
		Log.d(TAG, " << onResume()");
	}

}
