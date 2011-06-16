package com.decideforme.competitors;

import java.math.BigDecimal;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.competitors.Competitor.CompetitorColumns;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.ButtonHelper;
import com.decideforme.utils.ButtonHelperImpl;
import com.decideforme.utils.StringUtils;
import com.decideforme.utils.SubjectConstants;
import com.decideforme.utils.TableLayoutHelperImpl;

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
		
		Cursor cCompetitors =CompetitorHelper.getAllCompetitorsForDecision(this, mDecisionRowId);
    	Log.d(TAG, "Number of Competitors For Decision: " + StringUtils.objectAsString(cCompetitors.getCount()));
    	cCompetitors.moveToFirst();
    	
    	while(cCompetitors.isAfterLast() == false) {
    		TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
        	TableRow thisRow = tableLayoutHelper.getNewRow(this, true);
        	thisRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield_default));
        	
        	BigDecimal id = new BigDecimal(cCompetitors.getPosition()).multiply(new BigDecimal(100));
        	Integer viewId = id.intValue();
        	
        	TextView competitorName = new TextView(this);
        	Log.d(TAG, "Competitor Name: " + StringUtils.objectAsString(cCompetitors.getString(Competitor.COLUMN_INDEX_DESCRIPTION)));
        	competitorName.setText(cCompetitors.getString(Competitor.COLUMN_INDEX_DESCRIPTION));
    		competitorName.setId(viewId++);
    		Log.d(TAG, "Competitor Name View ID: " + competitorName.getId());
    		competitorName.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
    		
    		competitorName.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield));
    		competitorName.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = new Intent().setClass(CompetitorsScreen.this, CompetitorEdit.class);
					Cursor competitorCursor = getCompetitor(thisView, 0);
					intent.putExtra(DecisionColumns._ID, competitorCursor.getLong(Competitor.COLUMN_INDEX_ROW_ID));
					intent.putExtra(CompetitorColumns.DECISIONID, mDecisionRowId);
					startActivity(intent);
				}
			});
    		thisRow.addView(competitorName);
    		
    		ButtonHelper buttonHelper = new ButtonHelperImpl(mDecisionRowId, this, SubjectConstants.COMPETITOR);
			thisRow.addView(buttonHelper.getNewButton(viewId++));
			thisRow.addView(buttonHelper.getEditButton(viewId++));
			thisRow.addView(buttonHelper.getDeleteButton(viewId++));
        	mDynamicCompetitorsTable.addView(thisRow);
        	   		
        	cCompetitors.moveToNext();
    	}
	
	}

	public Cursor getCompetitor(View thisView, Integer offset) {
		
		TextView thisCompetitor = (TextView) findViewById(thisView.getId() - offset);
		String thisCompetitorName = (String) thisCompetitor.getText();
		Cursor cCompetitors = CompetitorHelper.getCompetitorByName(this, mDecisionRowId, thisCompetitorName);
		
		return cCompetitors;
	}
	
	
	
    @Override
	protected void onResume() {
		super.onResume();
		fillData();
		Log.d(TAG, " << onResume()");
	}

}
