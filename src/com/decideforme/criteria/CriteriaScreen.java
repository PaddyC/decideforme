package com.decideforme.criteria;

import java.math.BigDecimal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.db.decideforme.criteria.CriteriaDatabaseAdapter;
import com.db.decideforme.criteria.Criterion.CriterionColumns;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.MyDecisions;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;
import com.decideforme.utils.TableLayoutHelperImpl;

public class CriteriaScreen extends DashboardActivity {
	private static final String TAG = CriteriaScreen.class.getName();

	private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int BACK_ID = Menu.FIRST + 2;
    
    public static final int ACTIVITY_EDIT=1;
    public static final int ACTIVITY_DELETE=2;
	
	private CriteriaDatabaseAdapter mCriteriaDBAdapter;
	private TableLayout mDynamicCriteriaTable;
	
	protected Long mDecisionRowId;
	
	private Integer mNextCriterionRowId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.criteria_list);
		mCriteriaDBAdapter = new CriteriaDatabaseAdapter(this);
		mCriteriaDBAdapter.open();
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
		
		mNextCriterionRowId = mCriteriaDBAdapter.getNextCriterionSequenceID();
		
		setTitleFromActivityLabel (R.id.title_text);
		
		fillData();
		
		Log.d(TAG, " << onCreate()");
	}
	
    @Override
	protected void onResume() {
		super.onResume();
		fillData();
		Log.d(TAG, "Number of rows in criteria table: " + mDynamicCriteriaTable.getChildCount());
		Log.d(TAG, " << onResume()");
	}


	private void fillData() {
		Log.d(TAG, " >> fillData()");
		
		mDynamicCriteriaTable = (TableLayout) findViewById(R.id.criteriaList);
    	// Table Start! Initialise the table layout:
		if(mDynamicCriteriaTable.getChildCount() > 0) {
			mDynamicCriteriaTable.removeAllViews();
		}
		
		Log.d(TAG, "Number of rows in the table: " + mDynamicCriteriaTable.getChildCount());
		
		
        // Get all of the rows from the database and create the item list
        Cursor competitorsCursor = mCriteriaDBAdapter.fetchAllCriteriaForDecision(mDecisionRowId);
        
        Log.d(TAG, "Number of Criteria in DB: " + StringUtils.objectAsString(competitorsCursor.getCount()));
        competitorsCursor.moveToFirst();
    	
    	while(competitorsCursor.isAfterLast() == false) {
    		TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
        	TableRow thisRow = tableLayoutHelper.getNewRow(this);
        	thisRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield_default));
        	
        	BigDecimal id = new BigDecimal(competitorsCursor.getPosition()).multiply(new BigDecimal(100));
        	Integer viewId = id.intValue();
        	
        	TextView criterionName = new TextView(this);
        	String criterionDesc = competitorsCursor.getString(2);
			Log.d(TAG, "Criterion Name: " + StringUtils.objectAsString(criterionDesc));
        	criterionName.setText(criterionDesc);
    		criterionName.setId(viewId++);
    		criterionName.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
    		
    		criterionName.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield));
    		criterionName.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = new Intent().setClass(CriteriaScreen.this, CriterionEdit.class);
					Cursor criterion = getCriterion(thisView, 0);
			        intent.putExtra(CriterionColumns._ID, criterion.getLong(0));
			        intent.putExtra(CriterionColumns.DECISIONID, mDecisionRowId);
					startActivity(intent);
				}
			});
    		
    		
    		thisRow.addView(criterionName);
    		
    		Button openButton = new Button(this);
    		openButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_button));
    		openButton.setId(viewId++);
    		openButton.setGravity(Gravity.CLIP_VERTICAL);
    		openButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = new Intent().setClass(CriteriaScreen.this, CriterionEdit.class);
					Cursor criterion = getCriterion(thisView, ACTIVITY_EDIT);
					intent.putExtra(CriterionColumns._ID, criterion.getLong(0));
					intent.putExtra(CriterionColumns.DECISIONID, mDecisionRowId);
					startActivity(intent);
				}
			});
    		thisRow.addView(openButton);
    		
    		Button deleteButton = new Button(this);
    		deleteButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.delete_button));
    		deleteButton.setId(viewId++);
    		deleteButton.setGravity(Gravity.CLIP_VERTICAL);
    		deleteButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Cursor criterion = getCriterion(thisView, ACTIVITY_DELETE);
					displayAreYouSure(criterion);
				}
			});
    		thisRow.addView(deleteButton);
    		
        	mDynamicCriteriaTable.addView(thisRow);
        	   		
        	competitorsCursor.moveToNext();
    	}
    	
    	fillAddButtonRow();
		
		Log.d(TAG, " << fillData()");
		
	}
	
	public Cursor getCriterion(View thisView, Integer offset) {
		
		TextView thisCriterion = (TextView) findViewById(thisView.getId() - offset);
		String thisCriterionName = (String) thisCriterion.getText();
		Cursor cCriteria = mCriteriaDBAdapter.fetchCriterion(mDecisionRowId, thisCriterionName);
		
		return cCriteria;
	}
	
    private void fillAddButtonRow() {
    	
        TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
    	TableRow thisRow = tableLayoutHelper.getNewRow(this);
   	
    	TextView addDecision = new TextView(this);
    	
    	addDecision.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
		thisRow.addView(addDecision);
		
		Button newDecisionButton = new Button(this);
		newDecisionButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_button));
		newDecisionButton.setGravity(Gravity.CLIP_VERTICAL);
		newDecisionButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View thisView) {
		    	  long criterionID = createCriterion();
			    	 
		    	  Intent i = new Intent(getApplicationContext(), CriterionEdit.class);
		    	  i.putExtra(CriterionColumns._ID, criterionID);
		    	  startActivity (i);
			}
		});
		
		thisRow.addView(newDecisionButton);
		
		mDynamicCriteriaTable.addView(thisRow);

	}

	public void displayAreYouSure(final Cursor criterion) {
        // prepare the alert box
    	AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
    	
    	String criterionName = criterion.getString(2);
    	String wantToDelete = getString(R.string.want_to_delete) + criterionName + "'?";
		alertbox.setMessage(wantToDelete);

    	alertbox.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int whichButton) {

	 	    	Integer rowID = criterion.getInt(0);
	 	    	
	 	    	mCriteriaDBAdapter.deleteCriterion(rowID);
		    	
		    	Context context = getApplicationContext();
		    	String criterionName = criterion.getString(2);
		    	String deleted = getString(R.string.deleted_criterion) + criterionName + "'";
		    	int duration = Toast.LENGTH_SHORT;
		    	Toast toast = Toast.makeText(context, deleted, duration);
		    	toast.show();
		    	
		    	setResult(RESULT_OK);
		    	fillData();
	         }
    	});

		alertbox.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
    	 	public void onClick(DialogInterface arg0, int arg1) {
	        	Toast.makeText(getApplicationContext(), getString(R.string.nothing_deleted), Toast.LENGTH_SHORT).show();
    	 	}
		});

	     // display box
	     alertbox.show();
	 }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.add_criterion).setIcon(R.drawable.ic_menu_compose);
        menu.add(1, DELETE_ID, 1, R.string.delete_criterion).setIcon(R.drawable.ic_menu_delete);
        menu.add(2, BACK_ID, 2, R.string.done).setIcon(R.drawable.ic_menu_revert);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case INSERT_ID:
	            createCriterion();
	            return true;
	        case DELETE_ID:
	        	deleteCriterion();
	        	return true;
	        case BACK_ID:
	        	finish();
	        	return true;
        } 
        return super.onMenuItemSelected(featureId, item);
    }
    

    private long createCriterion() {
		Log.d(TAG, " >> createCriterion()");
		
		String competitorName = "C" + mNextCriterionRowId;
		long id = mCriteriaDBAdapter.createCriterion(competitorName, mDecisionRowId, new Long(0));
		
		Intent i = new Intent(this, CriterionEdit.class);
        i.putExtra(CriterionColumns._ID, id);
        i.putExtra(CriterionColumns.DECISIONID, mDecisionRowId);
        startActivityForResult(i, MyDecisions.ACTIVITY_EDIT);
		
		Log.d(TAG, " << createCriterion()");
		return id;
	}

    private void deleteCriterion() {
		Log.d(TAG, " >> deleteCriterion()");
		
		Intent i = new Intent(this, CriterionDelete.class);
		i.putExtra(CriterionColumns._ID, mDecisionRowId);
        startActivityForResult(i, MyDecisions.ACTIVITY_DELETE);
		
		Log.d(TAG, " << deleteCriterion()");
	}
    

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(0, DELETE_ID, 0, R.string.menu_delete_criterion);
	}

    @Override
	public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            mCriteriaDBAdapter.deleteCriterion(info.id);
            fillData();
            return true;
        }
    	return super.onContextItemSelected(item);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }

}
