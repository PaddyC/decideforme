package com.decideforme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.db.decideforme.DecisionDatabaseAdapter;
import com.decideforme.Decision.DecisionColumns;
import com.decideforme.utils.StringUtils;

public class DecisionDelete extends Activity {
	private static final String TAG = DecisionDelete.class.getName();
	
	protected Spinner decisionSpinner;
	protected Button deleteButton;

	protected DecisionDatabaseAdapter decisionDbAdapter;

	protected Long decisionRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(" +
				"savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		
		decisionDbAdapter = new DecisionDatabaseAdapter(this);
		decisionDbAdapter.open();
		
		setContentView(R.layout.decision_delete);
		setTitle(R.string.menu_delete);
		
		decisionSpinner = (Spinner) findViewById(R.id.decision_spinner);
		populateSpinner();
		
		deleteButton = (Button) findViewById(R.id.delete);
		deleteButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View view) {
		    	// When the user selects one: are you sure you want to delete?
		    	Cursor selectedItem = (Cursor) decisionSpinner.getSelectedItem();

		    	Integer rowID = selectedItem.getInt(0);
		    	String decisionName = selectedItem.getString(1);
		    	
		    	decisionDbAdapter.deleteDecision(rowID);
		    	
		    	Context context = getApplicationContext();
		    	CharSequence text = "Decision named " + decisionName + " has been deleted";
		    	int duration = Toast.LENGTH_SHORT;
		    	Toast toast = Toast.makeText(context, text, duration);
		    	toast.show();
		    	
		    	setResult(RESULT_OK);
		    	finish();
		    }
		});
		
		Log.d(TAG, " << onCreate()");
	}

	private void populateSpinner() {
		Log.d(TAG, " >> populateSpinner()");
		
		Cursor allDecisionsCursor = decisionDbAdapter.fetchAllDecisions();
		startManagingCursor(allDecisionsCursor);
		// create an array to specify which fields we want to display
		String[] from = new String[]{DecisionColumns.NAME};
		// create an array of the display item we want to bind our data to
		int[] to = new int[]{android.R.id.text1};
		
		// create simple cursor adapter
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
	
	    if (decisionRowId != null) {
	    	// Delete the decision
	    	decisionDbAdapter.deleteDecision(decisionRowId);
	    }	
	
	    Log.d(TAG, " << saveState()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, " >> onSaveInstanceState(" +
				"outState '" + StringUtils.objectAsString(outState) + "')");
		
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(DecisionColumns._ID, decisionRowId);
		
		Log.d(TAG, " << onSaveInstanceState()");
	}
}
