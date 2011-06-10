package com.decideforme.criteria;

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

import com.db.decideforme.criteria.CriteriaDatabaseAdapter;
import com.db.decideforme.criteria.Criterion.CriterionColumns;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.dashboard.DashboardActivity;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;

public class CriterionDelete extends DashboardActivity {
	private static final String TAG = CriterionDelete.class.getName();
	
	protected Spinner mCriteriaSpinner;
	protected Button mDeleteButton;

	protected CriteriaDatabaseAdapter mCriteriaDBAdapter;

	protected Long mCriterionRowID;
	protected Long mDecisionRowID;
	
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
		
		mCriteriaDBAdapter = new CriteriaDatabaseAdapter(this);
		mCriteriaDBAdapter.open();
		
		setContentView(R.layout.criterion_delete);
		setTitleFromActivityLabel (R.id.title_text);
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		mDecisionRowID = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
		
		mCriteriaSpinner = (Spinner) findViewById(R.id.criteria_spinner);
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
    	
    	Cursor selectedItem = (Cursor) mCriteriaSpinner.getSelectedItem();
    	String criterionDescription = selectedItem.getString(2);
    	
    	alertbox.setMessage(R.string.want_to_delete + criterionDescription + "'?");

    	// set a positive/yes button and create a listener
    	alertbox.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	         // do something when the button is clicked
	         public void onClick(DialogInterface dialog, int whichButton) {
	        	 
		    	Cursor selectedItem = (Cursor) mCriteriaSpinner.getSelectedItem();

		    	Integer rowID = selectedItem.getInt(0);
		    	String criterionDescription = selectedItem.getString(2);
		    	
		    	mCriteriaDBAdapter.deleteCriterion(rowID);
		    	
		    	Context context = getApplicationContext();
		    	CharSequence text = R.string.deleted_criterion + criterionDescription + "'";
		    	int duration = Toast.LENGTH_SHORT;
		    	Toast toast = Toast.makeText(context, text, duration);
		    	toast.show();
		    	
		    	setResult(RESULT_OK);
		    	
		    	finish();
	         }
    	});

     	// set a negative/no button and create a listener
		alertbox.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			// do something when the button is clicked
    	 	public void onClick(DialogInterface arg0, int arg1) {
	        	Toast.makeText(getApplicationContext(), R.string.nothing_deleted, Toast.LENGTH_SHORT).show();
    	 	}
		});

	     // display box
	     alertbox.show();
	 }
	
	
	private void populateSpinner() {
		Log.d(TAG, " >> populateSpinner()");
		
		Cursor allCriteriaCursor = mCriteriaDBAdapter.fetchAllCriteriaForDecision(mDecisionRowID);
		
		// create an array to specify which fields we want to display
		String[] from = new String[]{CriterionColumns.DESCRIPTION};
		// create an array of the display item we want to bind our data to
		int[] to = new int[]{android.R.id.text1};
		
		// create simple cursor adapter
		SimpleCursorAdapter adapter =
		  new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, allCriteriaCursor, from, to );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		
		// get reference to our spinner
		Spinner s = (Spinner) findViewById(R.id.criteria_spinner);
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
	
	    if (mCriterionRowID != null) {
	    	// Delete the decision
	    	mCriteriaDBAdapter.deleteCriterion(mCriterionRowID);
	    }	
	
	    Log.d(TAG, " << saveState()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, " >> onSaveInstanceState(" +
				"outState '" + StringUtils.objectAsString(outState) + "')");
		
		super.onSaveInstanceState(outState);
		saveState();
	    outState.putSerializable(CriterionColumns._ID, mCriterionRowID);
		
		Log.d(TAG, " << onSaveInstanceState()");
	}
}
