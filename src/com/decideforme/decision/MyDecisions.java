package com.decideforme.decision;

import java.math.BigDecimal;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.db.decideforme.decision.Decision;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.ButtonHelper;
import com.decideforme.utils.ButtonHelperImpl;
import com.decideforme.utils.SubjectConstants;
import com.decideforme.utils.TableLayoutHelperImpl;

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
    	// Table Start! Initialise the table layout:
		if(mDynamicDecisionTable.getChildCount() > 0) {
			mDynamicDecisionTable.removeAllViews();
		}
		
		Cursor cDecisions = DecisionHelper.fetchAllDecisions(this);
    	cDecisions.moveToFirst();
    	
    	while(cDecisions.isAfterLast() == false) {
    		TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
        	TableRow thisRow = tableLayoutHelper.getNewRow(this, true);
        	thisRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield_default));
        	
        	BigDecimal id = new BigDecimal(cDecisions.getPosition()).multiply(new BigDecimal(100));
        	Integer viewId = id.intValue();
        	
        	TextView decisionName = new TextView(this);
        	decisionName.setText(cDecisions.getString(Decision.COLUMN_INDEX_NAME));
    		decisionName.setId(viewId++);
    		decisionName.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
    		
    		decisionName.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield));
    		decisionName.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = new Intent().setClass(MyDecisions.this, DecisionHome.class);
					Cursor decision = getDecision(thisView, ACTIVITY_CREATE);
					intent.putExtra(DecisionColumns._ID, decision.getLong(0));
					startActivity(intent);
				}
			});
    		thisRow.addView(decisionName);
    		
    		long decisionRowID = cDecisions.getLong(Decision.COLUMN_INDEX_ROW_ID);
    		ButtonHelper buttonHelper = new ButtonHelperImpl(decisionRowID, this, SubjectConstants.DECISION);
    		thisRow.addView(buttonHelper.getNewButton(viewId++));
    		thisRow.addView(buttonHelper.getEditButton(viewId++));
    		thisRow.addView(buttonHelper.getDeleteButton(viewId++));
    		
        	mDynamicDecisionTable.addView(thisRow);
        	   		
        	cDecisions.moveToNext();
    	}
    }

	private Cursor getDecision(View thisView, Integer offset) {
		
		TextView thisDecision = (TextView) findViewById(thisView.getId() - offset);
		String thisDecisionName = (String) thisDecision.getText();
		Cursor cDecisions = DecisionHelper.getDecision(this, thisDecisionName);
		
		return cDecisions;
	}

}