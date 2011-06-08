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

import com.db.decideforme.competitors.CompetitorsDatabaseAdapter;
import com.db.decideforme.competitors.Competitor.CompetitorColumns;
import com.decideforme.R;
import com.decideforme.Decision.DecisionColumns;
import com.decideforme.utils.BundleHelper;
import com.decideforme.utils.StringUtils;

public class CompetitorsScreen extends ListActivity {
	private static final String TAG = CompetitorsScreen.class.getName();

	private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int BACK_ID = Menu.FIRST + 2;
	
	private CompetitorsDatabaseAdapter mDbAdapter;
	
	protected Long decisionRowId;
	
	private Integer nextCompetitorRowId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, " >> onCreate(savedInstanceState '" + StringUtils.objectAsString(savedInstanceState) + "')");
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.competitors_list);
		mDbAdapter = new CompetitorsDatabaseAdapter(this);
		mDbAdapter.open();
		
		BundleHelper bundleHelper = new BundleHelper(this, savedInstanceState);
		decisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
		
		nextCompetitorRowId = mDbAdapter.getNextCompetitorSequenceID();
		
		fillData();
		registerForContextMenu(getListView());
		
		Log.d(TAG, " << onCreate()");
	}


	private void fillData() {
		Log.d(TAG, " >> fillData()");
		
        // Get all of the rows from the database and create the item list
        Cursor competitorsCursor = mDbAdapter.fetchAllCompetitorsForDecision(decisionRowId);
        
        startManagingCursor(competitorsCursor);
        
        // Create an array to specify the fields we want to display in the list (only COMPETITORS.DESCRIPTION)
        String[] from = new String[]{CompetitorColumns.DESCRIPTION};
        
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.text1};
        
        // Now create a simple cursor adapter and set it to display
    	SimpleCursorAdapter competitors =
    		new SimpleCursorAdapter(this, R.layout.competitor_row, competitorsCursor, from, to);

        setListAdapter(competitors);
		
		Log.d(TAG, " << fillData()");
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.add_competitor);
        menu.add(1, DELETE_ID, 1, R.string.delete_competitor);
        menu.add(2, BACK_ID, 2, R.string.done);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
	        case INSERT_ID:
	            createCompetitor();
	            return true;
	        case DELETE_ID:
	        	deleteCompetitor();
	        	return true;
	        case BACK_ID:
	        	finish();
	        	return true;
        } 
        return super.onMenuItemSelected(featureId, item);
    }
    

    private void createCompetitor() {
		Log.d(TAG, " >> createCompetitor()");
		
		String competitorName = "New Competitor " + nextCompetitorRowId;
		long id = mDbAdapter.createCompetitor(competitorName, decisionRowId);
		
		Intent i = new Intent(this, CompetitorEdit.class);
        i.putExtra(CompetitorColumns._ID, id);
        i.putExtra(CompetitorColumns.DECISIONID, decisionRowId);
        startActivityForResult(i, DecideForMe.ACTIVITY_EDIT);
		
		Log.d(TAG, " << createCompetitor()");
	}

    private void deleteCompetitor() {
		Log.d(TAG, " >> deleteCompetitor()");
		
		Intent i = new Intent(this, CompetitorDelete.class);
		i.putExtra(DecisionColumns._ID, decisionRowId);
        startActivityForResult(i, DecideForMe.ACTIVITY_DELETE);
		
		Log.d(TAG, " << deleteCompetitor()");
	}
    

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(0, DELETE_ID, 0, R.string.menu_delete_competitor);
	}

    @Override
	public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            mDbAdapter.deleteCompetitor(info.id);
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
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        Intent i = new Intent(this, CompetitorEdit.class);
        i.putExtra(CompetitorColumns._ID, id);
        startActivityForResult(i, DecideForMe.ACTIVITY_EDIT);
    }

}
