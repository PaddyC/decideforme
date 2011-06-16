package com.decideforme.criteria;

import java.math.BigDecimal;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;
import com.decideforme.utils.SubjectConstants;
import com.decideforme.utils.TableLayoutHelperImpl;
import com.decideforme.utils.ViewHelper;
import com.decideforme.utils.ViewHelperImpl;

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


	public void fillData() {
		mDynamicCriteriaTable = (TableLayout) findViewById(R.id.criteriaList);
    	// Table Start! Initialise the table layout:
		if(mDynamicCriteriaTable.getChildCount() > 0) {
			mDynamicCriteriaTable.removeAllViews();
		}
		TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
		
    	ViewHelper viewHelper = new ViewHelperImpl(mDecisionRowId, this, SubjectConstants.CRITERION);
    	
        Cursor competitorsCursor = CriteriaHelper.getAllCriteriaForDecision(this, mDecisionRowId);
        if (competitorsCursor.getCount() == 0) {
        	TableRow thisRow = tableLayoutHelper.getNewRow(this, true);
        	thisRow.addView(viewHelper.getTextView(0, getResources().getString(R.string.no_criteria), R.style.HomeButton, true, false));
        	thisRow.addView(viewHelper.getNewButton(1));
        	
        	mDynamicCriteriaTable.addView(thisRow);
        } else {
            competitorsCursor.moveToFirst();
        	
        	while(competitorsCursor.isAfterLast() == false) {
        		TableRow thisRow = tableLayoutHelper.getNewRow(this, true);
            	
            	BigDecimal id = new BigDecimal(competitorsCursor.getPosition()).multiply(new BigDecimal(100));
            	Integer viewId = id.intValue();

            	String criterionDesc = competitorsCursor.getString(Criterion.COLUMN_INDEX_DECRIPTION);
        		thisRow.addView(viewHelper.getTextView(viewId++, criterionDesc, R.style.HomeButton, true, true));
        		
        		thisRow.addView(viewHelper.getNewButton(viewId++));
        		thisRow.addView(viewHelper.getEditButton(viewId++));
        		thisRow.addView(viewHelper.getDeleteButton(viewId++));
        		
        		mDynamicCriteriaTable.addView(thisRow);
            	   		
            	competitorsCursor.moveToNext();
        	}
        }

		Log.d(TAG, " << fillData()");
		
	}

}
