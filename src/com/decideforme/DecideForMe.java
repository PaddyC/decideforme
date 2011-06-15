package com.decideforme;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.db.decideforme.decision.Decision.DecisionColumns;
import com.db.decideforme.decision.DecisionDatabaseAdapter;
import com.decideforme.utils.StringUtils;

public class DecideForMe extends ListActivity {
	private static final String TAG = DecideForMe.class.getName();
	
	private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int DONE = Menu.FIRST + 2;
    
    public static final int ACTIVITY_CREATE=0;
    public static final int ACTIVITY_EDIT=1;
    public static final int ACTIVITY_DELETE=2;
    
    private Integer nextDecisionNumber;
	
	private DecisionDatabaseAdapter mDbAdapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.d(TAG, " >> onCreate(" +
    			"savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.decisions_list);
        mDbAdapter = new DecisionDatabaseAdapter(this);
        mDbAdapter.open();
        
        nextDecisionNumber = mDbAdapter.getNextDecisionSequenceID();
        
        fillData();
        registerForContextMenu(getListView());
        
        Log.d(TAG, " << onCreate()");
    }


    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.add_decision);
        menu.add(1, DELETE_ID, 1, R.string.delete_decision);
        menu.add(2, DONE, 2, R.string.done);
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
	        	Toast.makeText(getApplicationContext(), "Catch you later! Thanks for using DecideForMe.", Toast.LENGTH_SHORT).show();
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
    	
		String decisionName = "New Decision " + nextDecisionNumber;
		
		long id = mDbAdapter.createDecision(decisionName, "");
		Intent i = new Intent(this, DecisionEdit.class);
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, DecisionEdit.class);
        i.putExtra(DecisionColumns._ID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
    
    
    private void fillData() {
    	Log.d(TAG, " >> fillData()");
        
    	Cursor decisionsCursor = mDbAdapter.fetchAllDecisions();
        String[] from = new String[]{DecisionColumns.NAME};
        int[] to = new int[]{R.id.text1};
    	SimpleCursorAdapter decisions =
    		new SimpleCursorAdapter(this, R.layout.decision_row, decisionsCursor, from, to);

        setListAdapter(decisions);
        
        Log.d(TAG, " << fillData()");
    }
}