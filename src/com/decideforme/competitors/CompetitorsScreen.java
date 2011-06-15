package com.decideforme.competitors;

import java.math.BigDecimal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.db.decideforme.competitors.Competitor.CompetitorColumns;
import com.db.decideforme.competitors.CompetitorsDatabaseAdapter;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;
import com.decideforme.utils.TableLayoutHelperImpl;

public class CompetitorsScreen extends DashboardActivity {
	private static final String TAG = CompetitorsScreen.class.getName();

    private static final int BACK_ID = Menu.FIRST;
    
    public static final int ACTIVITY_EDIT = 1;
    public static final int ACTIVITY_DELETE = 2;
	
	private CompetitorsDatabaseAdapter mCompetitorsDBAdapter;
	
	protected Long mDecisionRowId;
	private TableLayout mDynamicCompetitorsTable;
	
	private Integer mNextCompetitorRowId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.competitors_list);
		mCompetitorsDBAdapter = new CompetitorsDatabaseAdapter(this);
		mCompetitorsDBAdapter.open();
		mNextCompetitorRowId = mCompetitorsDBAdapter.getNextCompetitorSequenceID();
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
		
		setTitleFromActivityLabel (R.id.title_text);
		
		fillData();
		
		Log.d(TAG, " << onCreate()");
	}


	private void fillData() {
    	Log.d(TAG, " >> fillData()");
    	mDynamicCompetitorsTable = (TableLayout) findViewById(R.id.competitorsList);
    	// Table Start! Initialise the table layout:
		if(mDynamicCompetitorsTable.getChildCount() > 0) {
			mDynamicCompetitorsTable.removeAllViews();
		}
		
		Log.d(TAG, "Number of rows in the table: " + mDynamicCompetitorsTable.getChildCount());
		
    	Cursor cCompetitors = mCompetitorsDBAdapter.fetchAllCompetitorsForDecision(mDecisionRowId);
    	
    	Log.d(TAG, "Number of Competitors For Decision: " + StringUtils.objectAsString(cCompetitors.getCount()));
    	cCompetitors.moveToFirst();
    	
    	while(cCompetitors.isAfterLast() == false) {
    		TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
        	TableRow thisRow = tableLayoutHelper.getNewRow(this, true);
        	thisRow.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield_default));
        	
        	BigDecimal id = new BigDecimal(cCompetitors.getPosition()).multiply(new BigDecimal(100));
        	Integer viewId = id.intValue();
        	
        	TextView competitorName = new TextView(this);
        	Log.d(TAG, "Competitor Name: " + StringUtils.objectAsString(cCompetitors.getString(2)));
        	competitorName.setText(cCompetitors.getString(2));
    		competitorName.setId(viewId++);
    		competitorName.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
    		
    		competitorName.setBackgroundDrawable(getResources().getDrawable(R.drawable.textfield));
    		competitorName.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = new Intent().setClass(CompetitorsScreen.this, CompetitorEdit.class);
					Cursor decision = getCompetitor(thisView, 0);
					intent.putExtra(DecisionColumns._ID, decision.getLong(0));
					intent.putExtra(CompetitorColumns.DECISIONID, mDecisionRowId);
					startActivity(intent);
				}
			});
    		
    		
    		thisRow.addView(competitorName);
    		
    		Button openButton = new Button(this);
    		openButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.edit_button));
    		openButton.setId(viewId++);
    		openButton.setGravity(Gravity.CLIP_VERTICAL);
    		openButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = new Intent().setClass(CompetitorsScreen.this, CompetitorEdit.class);
					Cursor decision = getCompetitor(thisView, ACTIVITY_EDIT);
					intent.putExtra(CompetitorColumns._ID, decision.getLong(0));
					intent.putExtra(CompetitorColumns.DECISIONID, mDecisionRowId);
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
					Cursor competitor = getCompetitor(thisView, ACTIVITY_DELETE);
					displayAreYouSure(competitor);
				}
			});
    		thisRow.addView(deleteButton);
    		
        	mDynamicCompetitorsTable.addView(thisRow);
        	   		
        	cCompetitors.moveToNext();
    	}
    	
    	fillAddButtonRow();
		
	}
	
    private void fillAddButtonRow() {
    	
        TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
    	TableRow thisRow = tableLayoutHelper.getNewRow(this, false);
   	
    	TextView addCompetitor = new TextView(this);
    	
    	addCompetitor.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
		thisRow.addView(addCompetitor);
		
		Button newCompetitorButton = new Button(this);
		newCompetitorButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_button));
		newCompetitorButton.setGravity(Gravity.CLIP_VERTICAL);
		newCompetitorButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View thisView) {
		    	long competitorRowID = createCompetitor();
			    	 
			  	Intent i = new Intent(CompetitorsScreen.this, CompetitorEdit.class);
			    i.putExtra(CompetitorColumns._ID, competitorRowID);
			    i.putExtra(CompetitorColumns.DECISIONID, mDecisionRowId);
			    startActivityForResult(i, ACTIVITY_EDIT);
			}
		});
		
		thisRow.addView(newCompetitorButton);
		
		mDynamicCompetitorsTable.addView(thisRow);

	}

	public void displayAreYouSure(final Cursor competitor) {
        // prepare the alert box
    	AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
    	
    	String competitorName = competitor.getString(2);
    	String wantToDelete = getString(R.string.want_to_delete) + competitorName + "'?";
		alertbox.setMessage(wantToDelete);

    	alertbox.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int whichButton) {

	 	    	Integer rowID = competitor.getInt(0);
	 	    	
	 	    	mCompetitorsDBAdapter.deleteCompetitor(rowID);
		    	
		    	Context context = getApplicationContext();
		    	String competitorName = competitor.getString(2);
		    	String deleted = getString(R.string.deleted_competitor) + competitorName + "'";
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

	public Cursor getCompetitor(View thisView, Integer offset) {
		
		TextView thisCompetitor = (TextView) findViewById(thisView.getId() - offset);
		String thisCompetitorName = (String) thisCompetitor.getText();
		Cursor cDecisions = mCompetitorsDBAdapter.fetchCompetitor(mDecisionRowId, thisCompetitorName);
		
		return cDecisions;
	}
	
    @Override
	protected void onResume() {
		super.onResume();
		fillData();
		Log.d(TAG, "Number of rows in dynamic table: " + mDynamicCompetitorsTable.getChildCount());
		Log.d(TAG, " << onResume()");
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(1, BACK_ID, 1, R.string.done).setIcon(R.drawable.ic_menu_revert);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case BACK_ID:
	        	finish();
	        	return true;
        } 
        return super.onMenuItemSelected(featureId, item);
    }
    

    private long createCompetitor() {
		Log.d(TAG, " >> createCompetitor()");
		
		String competitorName = "C " + mNextCompetitorRowId;
		long id = mCompetitorsDBAdapter.createCompetitor(competitorName, mDecisionRowId);
		
		Log.d(TAG, " << createCompetitor()");
		return id;
	}
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }


}
