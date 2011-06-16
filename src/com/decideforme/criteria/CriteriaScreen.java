package com.decideforme.criteria;

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

import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.criteria.Criterion.CriterionColumns;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.ButtonHelper;
import com.decideforme.utils.ButtonHelperImpl;
import com.decideforme.utils.StringUtils;
import com.decideforme.utils.SubjectConstants;
import com.decideforme.utils.TableLayoutHelperImpl;

public class CriteriaScreen extends DashboardActivity {
	private static final String TAG = CriteriaScreen.class.getName();
    
    public static final int ACTIVITY_CREATE=0;
    public static final int ACTIVITY_EDIT=1;
    public static final int ACTIVITY_DELETE=2;
	
	private TableLayout mDynamicCriteriaTable;
	
	protected Long mDecisionRowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.criteria_list);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
		
		setTitleFromActivityLabel (R.id.title_text);
		
		fillData();
		
		Log.d(TAG, " << onCreate()");
	}
	
    @Override
	protected void onResume() {
		super.onResume();
		fillData();
		Log.d(TAG, " << onResume()");
	}


	private void fillData() {
		mDynamicCriteriaTable = (TableLayout) findViewById(R.id.criteriaList);
    	// Table Start! Initialise the table layout:
		if(mDynamicCriteriaTable.getChildCount() > 0) {
			mDynamicCriteriaTable.removeAllViews();
		}
		
        Cursor competitorsCursor = CriteriaHelper.getAllCriteriaForDecision(this, mDecisionRowId);
        competitorsCursor.moveToFirst();
    	
    	while(competitorsCursor.isAfterLast() == false) {
    		TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
        	TableRow thisRow = tableLayoutHelper.getNewRow(this, true);
        	thisRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield_default));
        	
        	BigDecimal id = new BigDecimal(competitorsCursor.getPosition()).multiply(new BigDecimal(100));
        	Integer viewId = id.intValue();
        	
        	TextView criterionName = new TextView(this);
        	String criterionDesc = competitorsCursor.getString(Criterion.COLUMN_INDEX_DECRIPTION);
			criterionName.setText(criterionDesc);
    		criterionName.setId(viewId++);
    		criterionName.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
    		
    		criterionName.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield));
    		criterionName.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = new Intent().setClass(CriteriaScreen.this, CriterionEdit.class);
					Cursor criterion = getCriterion(thisView, ACTIVITY_CREATE);
			        intent.putExtra(CriterionColumns._ID, criterion.getLong(Criterion.COLUMN_INDEX_ROW_ID));
			        intent.putExtra(CriterionColumns.DECISIONID, mDecisionRowId);
					startActivity(intent);
				}
			});
    		thisRow.addView(criterionName);
    		
    		ButtonHelper buttonHelper = new ButtonHelperImpl(mDecisionRowId, this, SubjectConstants.CRITERION);
    		thisRow.addView(buttonHelper.getNewButton(viewId++));
    		thisRow.addView(buttonHelper.getEditButton(viewId++));
    		thisRow.addView(buttonHelper.getDeleteButton(viewId++));
    		
        	mDynamicCriteriaTable.addView(thisRow);
        	   		
        	competitorsCursor.moveToNext();
    	}
		
		Log.d(TAG, " << fillData()");
		
	}
	
	public Cursor getCriterion(View thisView, Integer offset) {
		
		TextView thisCriterion = (TextView) findViewById(thisView.getId() - offset);
		String thisCriterionName = (String) thisCriterion.getText();
		Cursor cCriteria = CriteriaHelper.getCriterionByName(this, mDecisionRowId, thisCriterionName);
		
		return cCriteria;
	}

}
