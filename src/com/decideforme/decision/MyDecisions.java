package com.decideforme.decision;

import java.math.BigDecimal;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.db.decideforme.decision.Decision;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.SubjectConstants;
import com.decideforme.utils.ViewHelper;
import com.decideforme.utils.ViewHelperImpl;

public class MyDecisions extends DashboardActivity {    
    public static final int ACTIVITY_CREATE=0;
    public static final int ACTIVITY_EDIT=1;
    public static final int ACTIVITY_DELETE=2;
    
    private TableLayout mDynamicDecisionTable;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.my_decisions);
        setTitleFromActivityLabel (R.id.title_text);
        
        fillData();
    }

	
    @Override
	protected void onResume() {
		super.onResume();
		fillData();
	}

    
    public void fillData() {

		mDynamicDecisionTable = (TableLayout) findViewById(R.id.myDecisionsLayout);
		mDynamicDecisionTable.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield));
		
    	// Table Start! Initialise the table layout:
		if(mDynamicDecisionTable.getChildCount() > 0) {
			mDynamicDecisionTable.removeAllViews();
		}
		long decisionRowID = 0;
    	ViewHelper viewHelper;
    	
		Cursor cDecisions = DecisionHelper.fetchAllDecisions(this);
    	cDecisions.moveToFirst();
    	if (cDecisions.getCount() == 0) {
    		viewHelper = new ViewHelperImpl(decisionRowID, this, SubjectConstants.DECISION);
    		TableRow thisRow = viewHelper.getNewRow(this, true);
    		thisRow.addView(viewHelper.getTextView(
    				0, getResources().getString(R.string.no_decisions), R.style.HomeButton, Typeface.SANS_SERIF, true, true));
    		
    		thisRow.addView(viewHelper.getNewButton(1));
    		
    		mDynamicDecisionTable.addView(thisRow);
    	} else {
    		decisionRowID = cDecisions.getLong(Decision.COLUMN_INDEX_ROW_ID);
        	viewHelper = new ViewHelperImpl(decisionRowID, this, SubjectConstants.DECISION);
        	
        	TableRow headerRow = viewHelper.getNewRow(this, false);
        	headerRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.tableheader));
        	
        	TextView decisionName = getTableHeaderTextView("Decision Name ");
        	headerRow.addView(decisionName);
        	TextView add = getTableHeaderTextView("Add");
        	headerRow.addView(add);
        	TextView edit = getTableHeaderTextView("Edit");
        	headerRow.addView(edit);
        	TextView delete = getTableHeaderTextView("Del");
        	headerRow.addView(delete);
        	TextView report = getTableHeaderTextView("Rpt");
        	headerRow.addView(report);
        	mDynamicDecisionTable.addView(headerRow);
        	
        	while(cDecisions.isAfterLast() == false) {
        		TableRow thisRow = viewHelper.getNewRow(this, true);
        		
            	BigDecimal id = new BigDecimal(cDecisions.getPosition()).multiply(new BigDecimal(100));
            	Integer viewId = id.intValue();
            	
            	String decisionNameText = cDecisions.getString(Decision.COLUMN_INDEX_NAME);
            	thisRow.addView(viewHelper.getTextView(
            			viewId++, decisionNameText, R.style.HomeButton, Typeface.SANS_SERIF, true, true));
        		
        		thisRow.addView(viewHelper.getNewButton(viewId++));
        		thisRow.addView(viewHelper.getEditButton(viewId++));
        		thisRow.addView(viewHelper.getDeleteButton(viewId++));
        		
        		thisRow.addView(viewHelper.getReportButton(viewId++));
        		
            	mDynamicDecisionTable.addView(thisRow);
            	   		
            	cDecisions.moveToNext();
        	}
    	}
    }


	private TextView getTableHeaderTextView(String text) {
		
		TextView thisTextView = new TextView(this);
		
		Typeface envyCode = Typeface.createFromAsset(getAssets(), "fonts/Envy Code R.ttf");
		
		thisTextView.setText(text);
		thisTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.tableheader));
		thisTextView.setTypeface(envyCode, R.style.TableHeaderText);
		thisTextView.setGravity(Gravity.CENTER);
		
		return thisTextView;
	}
}