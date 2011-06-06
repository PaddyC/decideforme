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

import com.db.decideforme.DatabaseAttributeConstants;
import com.db.decideforme.DecisionDatabaseAdapter;
import com.decideforme.utils.StringUtils;

public class DecideForMe extends ListActivity {
	private static final String TAG = DecideForMe.class.getName();
	
	private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    
    private int decisionNumber = 1;
	
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
        fillData();
        registerForContextMenu(getListView());
        
        Log.d(TAG, " << onCreate()");
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID,0, R.string.menu_insert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            createDecision();
            return true;
        }
        
        return super.onMenuItemSelected(featureId, item);
    }
    
    private void createDecision() {
    	Log.d(TAG, " >> createDecision()");
    	
		String decisionName = "Decision" + decisionNumber++;
		mDbAdapter.createDecision(decisionName, "");
		fillData();
		
		Log.d(TAG, " << createDecision()");
    }

    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
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
        i.putExtra(DatabaseAttributeConstants.FIELD_NAME_DECISION_KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
    
    
    private void fillData() {
    	Log.d(TAG, " >> fillData()");
        // Get all of the rows from the database and create the item list
        Cursor decisionsCursor = mDbAdapter.fetchAllDecisions();
        
        startManagingCursor(decisionsCursor);
        
        // Create an array to specify the fields we want to display in the list (only DECISION.NAME)
        String[] from = new String[]{DatabaseAttributeConstants.FIELD_NAME_DECISION_NAME};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};
        
        // Now create a simple cursor adapter and set it to display
    	SimpleCursorAdapter decisions =
    		new SimpleCursorAdapter(this, R.layout.decision_row, decisionsCursor, from, to);

        setListAdapter(decisions);
        
        Log.d(TAG, " << fillData()");
    }
}