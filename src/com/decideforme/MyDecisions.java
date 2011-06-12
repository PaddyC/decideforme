package com.decideforme;

import java.math.BigDecimal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.StringUtils;
import com.decideforme.utils.TableLayoutHelperImpl;

public class MyDecisions extends DashboardActivity {
	private static final String TAG = MyDecisions.class.getName();
	
	private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int DONE = Menu.FIRST + 2;
    
    public static final int ACTIVITY_CREATE=0;
    public static final int ACTIVITY_EDIT=1;
    public static final int ACTIVITY_DELETE=2;
    
    private Integer nextDecisionNumber;
    
    private TableLayout mDynamicDecisionTable;
	
	private DecisionDatabaseAdapter mDbAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, " >> onCreate(" +
    			"savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.my_decisions);
        setTitleFromActivityLabel (R.id.title_text);
        
        mDbAdapter = new DecisionDatabaseAdapter(this);
        mDbAdapter.open();
        
        fillData();
        
        Log.d(TAG, " << onCreate()");
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.add_decision).setIcon(R.drawable.ic_menu_compose);
        menu.add(1, DELETE_ID, 1, R.string.delete_decision).setIcon(R.drawable.ic_menu_delete);
        menu.add(2, DONE, 2, R.string.done).setIcon(R.drawable.ic_menu_revert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case INSERT_ID:
	            createDecision();
	            return true;
	        case DELETE_ID:
	        	deleteDecision();
	        	return true;
	        case DONE:
	        	finish();
	        	return true;
        } 
        
        return super.onMenuItemSelected(featureId, item);
    }
    
    private void deleteDecision() {
		Log.d(TAG, " >> deleteDecision()");
		
		Intent i = new Intent(this, DecisionDelete.class);
        startActivityForResult(i, ACTIVITY_DELETE);
		
		Log.d(TAG, " << deleteDecision()");
	}


	private void createDecision() {
    	Log.d(TAG, " >> createDecision()");
    	
    	nextDecisionNumber = mDbAdapter.getNextDecisionSequenceID();
		String decisionName = "New Decision " + nextDecisionNumber;
		
		long id = mDbAdapter.createDecision(decisionName, "");
		Intent i = new Intent(this, DecisionHome.class);
        i.putExtra(DecisionColumns._ID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
		
		fillData();
		
		Log.d(TAG, " << createDecision()");
    }

    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(0, DELETE_ID, 0, R.string.menu_delete_decision);
	}

    @Override
	public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            mDbAdapter.deleteDecision(info.id);
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
    
    
    private void fillData() {
    	Log.d(TAG, " >> fillData()");
        
    	// Table Start! Initialise the table layout:
		mDynamicDecisionTable = (TableLayout) findViewById(R.id.myDecisionsLayout);
		mDynamicDecisionTable.removeAllViews();
		Log.d(TAG, "Number of rows in the table: " + mDynamicDecisionTable.getChildCount());
		
    	Cursor cDecisions = mDbAdapter.fetchAllDecisions();
    	cDecisions.moveToFirst();
    	
    	while(cDecisions.isAfterLast() == false) {
    		TableLayoutHelperImpl tableLayoutHelper = new TableLayoutHelperImpl();
        	TableRow thisRow = tableLayoutHelper.getNewRow(this);
        	
        	BigDecimal id = new BigDecimal(cDecisions.getPosition()).multiply(new BigDecimal(100));
        	Integer viewId = id.intValue();
        	
        	TextView decisionName = new TextView(this);
        	decisionName.setText(cDecisions.getString(1));
//        	decisionName.setSelectAllOnFocus(true);
    		decisionName.setId(viewId++);
    		decisionName.setTypeface(Typeface.SANS_SERIF, R.style.HomeButton);
    		decisionName.setHighlightColor(Color.MAGENTA);
    		decisionName.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = new Intent().setClass(MyDecisions.this, DecisionHome.class);
					Cursor decision = getDecision(thisView, 0);
					intent.putExtra(DecisionColumns._ID, decision.getLong(0));
					startActivity(intent);
				}
			});
    		
    		
    		thisRow.addView(decisionName);
    		
    		Button openButton = new Button(this);
    		openButton.setBackgroundResource(R.drawable.ic_menu_edit);
    		
    		openButton.setId(viewId++);
    		openButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = new Intent().setClass(MyDecisions.this, DecisionHome.class);
					Cursor decision = getDecision(thisView, ACTIVITY_EDIT);
					intent.putExtra(DecisionColumns._ID, decision.getLong(0));
					startActivity(intent);
				}
			});
    		thisRow.addView(openButton);
    		
    		Button deleteButton = new Button(this);
    		deleteButton.setBackgroundResource(R.drawable.ic_menu_delete);
    		deleteButton.setId(viewId++);
    		deleteButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Cursor decision = getDecision(thisView, ACTIVITY_DELETE);
					displayAreYouSure(decision);
				}
			});
    		thisRow.addView(deleteButton);
    		
        	mDynamicDecisionTable.addView(thisRow);
    		
        	cDecisions.moveToNext();
    	}
    }
    
    public void displayAreYouSure(final Cursor decision) {
        // prepare the alert box
    	AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
    	
    	String decisionName = decision.getString(1);
    	String wantToDelete = getString(R.string.want_to_delete) + decisionName + "'?";
		alertbox.setMessage(wantToDelete);

    	alertbox.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int whichButton) {

	 	    	Integer rowID = decision.getInt(0);
	 	    	
	 	    	mDbAdapter.deleteDecision(rowID);
		    	
		    	Context context = getApplicationContext();
		    	String decisionName = decision.getString(1);
		    	String deleted = getString(R.string.deleted_decision) + decisionName + "'";
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

	public Cursor getDecision(View thisView, Integer offset) {
		
		TextView thisDecision = (TextView) findViewById(thisView.getId() - offset);
		String thisDecisionName = (String) thisDecision.getText();
		Cursor cDecisions = mDbAdapter.fetchDecision(thisDecisionName);
		
		return cDecisions;
	}

}