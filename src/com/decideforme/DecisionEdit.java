package com.decideforme;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.db.decideforme.DatabaseAttributeConstants;
import com.db.decideforme.DecisionDatabaseAdapter;
import com.decideforme.R;
import com.decideforme.R.id;
import com.decideforme.R.layout;
import com.decideforme.R.string;
import com.decideforme.utils.StringUtils;

public class DecisionEdit extends Activity {
	private static final String TAG = DecisionEdit.class.getName();
	
	private DecisionDatabaseAdapter decisionDbAdapter;
	
	private EditText decisionName;
	private EditText decisionDescription;
	
	private Long decisionRowId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(" +
				"savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		decisionDbAdapter = new DecisionDatabaseAdapter(this);
		decisionDbAdapter.open();
		
		setContentView(R.layout.decision_edit);
		setTitle(R.string.edit_decision);
		
		decisionName = (EditText) findViewById(R.id.decision_name);
		decisionDescription = (EditText) findViewById(R.id.decision_description);
		
		Button nextButton = (Button) findViewById(R.id.next);
		
		decisionRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(DatabaseAttributeConstants.FIELD_NAME_DECISION_KEY_ROWID);
		if (decisionRowId == null) {
            Bundle extras = getIntent().getExtras();
            decisionRowId = extras != null ? extras.getLong(DatabaseAttributeConstants.FIELD_NAME_DECISION_KEY_ROWID) : null;
        }

		populateFields();
		
		
		nextButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View view) {
		    	setResult(RESULT_OK);
		    	finish();
		    }
		});
		
		Log.d(TAG, " << onCreate()");
	}

	private void populateFields() {
		Log.d(TAG, " >> populateFields()");
		
	    if (decisionRowId != null) {
	        Cursor note = decisionDbAdapter.fetchDecision(decisionRowId);
	        startManagingCursor(note);
	        decisionName.setText(note.getString(
	                note.getColumnIndexOrThrow(
	                  		DatabaseAttributeConstants.FIELD_NAME_DECISION_NAME)));
	        decisionDescription.setText(note.getString(
	                note.getColumnIndexOrThrow(
	                		DatabaseAttributeConstants.FIELD_NAME_DECISION_DESCRIPTION)));
	    }
	    
	    Log.d(TAG, " << populateFields()");
	}
	
	
	@Override
	protected void onPause() {
		Log.d(TAG, " >> onPause()");
		
		super.onPause();
		saveState();
		
		Log.d(TAG, " << onPause()");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, " >> onSaveInstanceState(" +
				"outState '" + StringUtils.objectAsString(outState) + "')");
		
		super.onSaveInstanceState(outState);
		saveState();
        outState.putSerializable(DatabaseAttributeConstants.FIELD_NAME_DECISION_KEY_ROWID, decisionRowId);
		
		Log.d(TAG, " << onSaveInstanceState()");
	}


	@Override
	protected void onResume() {
		Log.d(TAG, " >> onResume()");
		
		super.onResume();
		populateFields();
		
		Log.d(TAG, " << onResume()");
	}
	
    private void saveState() {
    	Log.d(TAG, " >> saveState()");
    	
        String title = decisionName.getText().toString();
        String body = decisionDescription.getText().toString();

        if (decisionRowId == null) {
            long id = decisionDbAdapter.createDecision(title, body);
            if (id > 0) {
            	decisionRowId = id;
            }
        } else {
        	decisionDbAdapter.updateDecision(decisionRowId, title, body);
        }
        
        Log.d(TAG, " << saveState()");
    }

}
