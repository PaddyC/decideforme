package com.decideforme.competitors;

import java.math.BigDecimal;

import android.database.Cursor;
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
import com.decideforme.utils.TableLayoutHelperImpl;
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
		
		TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
    	
    	ViewHelper viewHelper = new ViewHelperImpl(mDecisionRowId, this, SubjectConstants.COMPETITOR);
    	
		Cursor cCompetitors =CompetitorHelper.getAllCompetitorsForDecision(this, mDecisionRowId);
    	if (cCompetitors.getCount() == 0) {
    		TableRow thisRow = tableLayoutHelper.getNewRow(this, true);
    		thisRow.addView(viewHelper.getTextView(0, getResources().getString(R.string.no_competitors), R.style.HomeButton, true, false));
        	thisRow.addView(viewHelper.getNewButton(1));
        	
    		mDynamicCompetitorsTable.addView(thisRow);
    	} else {
    		cCompetitors.moveToFirst();
        	
        	while(cCompetitors.isAfterLast() == false) {
        		BigDecimal id = new BigDecimal(cCompetitors.getPosition()).multiply(new BigDecimal(100));
            	Integer viewId = id.intValue();
            	
            	TableRow thisRow = tableLayoutHelper.getNewRow(this, true);
            	
            	String competitorDesc = cCompetitors.getString(Competitor.COLUMN_INDEX_DESCRIPTION);
        		thisRow.addView(viewHelper.getTextView(
            			viewId++, competitorDesc, R.style.HomeButton, true, true));
    			
        		thisRow.addView(viewHelper.getNewButton(viewId++));
    			thisRow.addView(viewHelper.getEditButton(viewId++));
    			thisRow.addView(viewHelper.getDeleteButton(viewId++));
    			
    			mDynamicCompetitorsTable.addView(thisRow);
            	   		
            	cCompetitors.moveToNext();
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
