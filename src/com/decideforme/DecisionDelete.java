package com.decideforme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.utils.StringUtils;

public class DecisionDelete extends Activity {
	private static final String TAG = DecisionDelete.class.getName();
	
	protected Spinner mDecisionSpinner;
	protected Button mDeleteButton;

	protected DecisionDatabaseAdapter mDecisionDbAdapter;

	protected Long mDecisionRowId;

	private static final int DONE = Menu.FIRST;
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, DONE, 0, R.string.done);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case DONE:
	        	finish();
	        	return true;
        } 
        
        return super.onMenuItemSelected(featureId, item);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(" +
				"savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		
		mDecisionDbAdapter = new DecisionDatabaseAdapter(this);
		mDecisionDbAdapter.open();
		
		setContentView(R.layout.decision_delete);
		setTitle(R.string.menu_delete_decision);
		
		mDecisionSpinner = (Spinner) findViewById(R.id.decision_spinner);
		populateSpinner();
		
		mDeleteButton = (Button) findViewById(R.id.delete);
		mDeleteButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View view) {
		    	// When the user selects one: are you sure you want to delete?
		    	displayAreYouSure();
		    }
		});
		
		Log.d(TAG, " << onCreate()");
	}
	
    public void displayAreYouSure() {
        // prepare the alert box
    	AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
    	
		Cursor selectedItem = (Cursor) mDecisionSpinner.getSelectedItem();
    	alertbox.setMessage(R.string.want_to_delete + selectedItem.getString(1) +  "'?");

    	alertbox.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int whichButton) {
	         	
	     		Cursor selectedItemCursor = (Cursor) mDecisionSpinner.getSelectedItem();

	 	    	Integer rowID = selectedItemCursor.getInt(0);
	 	    	String decisionName = selectedItemCursor.getString(1);
		    	mDecisionDbAdapter.deleteDecision(rowID);
		    	
		    	Context context = getApplicationContext();
		    	CharSequence text = R.string.deleted_decision + decisionName + "'";
		    	int duration = Toast.LENGTH_SHORT;
		    	Toast toast = Toast.makeText(context, text, duration);
		    	toast.show();
		    	
		    	setResult(RESULT_OK);	
		    	finish();
	         }
    	});

		alertbox.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
    	 	public void onClick(DialogInterface arg0, int arg1) {
	        	Toast.makeText(getApplicationContext(), R.string.nothing_deleted, Toast.LENGTH_SHORT).show();
    	 	}
		});

	     // display box
	     alertbox.show();
	 }
	

	private void populateSpinner() {
		Log.d(TAG, " >> populateSpinner()");
		
		Cursor allDecisionsCursor = mDecisionDbAdapter.fetchAllDecisions();

		String[] from = new String[]{DecisionColumns.NAME};
		
		int[] to = new int[]{android.R.id.text1};
		
		SimpleCursorAdapter adapter =
		  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allDecisionsCursor, from, to );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		
		// get reference to our spinner
		Spinner s = (Spinner) findViewById(R.id.decision_spinner);
		s.setAdapter(adapter);
		
		Log.d(TAG, " << populateSpinner()");
	}


	@Override
	protected void onPause() {
		Log.d(TAG, " >> onPause()");
		
		super.onPause();
		saveState();
		
		Log.d(TAG, " << onPause()");
	}

	@Override
	protected void onResume() {
		Log.d(TAG, " >> onResume()");
		
		super.onResume();
		populateSpinner();
		
		Log.d(TAG, " << onResume()");
	}

	protected void saveState() {
		Log.d(TAG, " >> saveState()");
	
	    if (mDecisionRowId != null) {
	    	// Delete the decision
	    	mDecisionDbAdapter.deleteDecision(mDecisionRowId);
	    }	
	
	    Log.d(TAG, " << saveState()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, " >> onSaveInstanceState(" +
				"outState '" + StringUtils.objectAsString(outState) + "')");
		
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(DecisionColumns._ID, mDecisionRowId);
		
		Log.d(TAG, " << onSaveInstanceState()");
	}
}
