package com.decideforme.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.decision.DecisionHelper;
import com.decideforme.decision.DecisionHome;
import com.decideforme.decision.MyDecisions;

public abstract class DashboardActivity extends Activity {

	private static final int OK = Menu.FIRST;

	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
	    
	protected void onDestroy ()	{
	   super.onDestroy ();
	}

	protected void onPause () {
	   super.onPause ();
	}

	protected void onRestart ()	{
	   super.onRestart ();
	}

	protected void onResume ()	{
	   super.onResume ();
	}

	protected void onStart ()	{
	   super.onStart ();
	}

	protected void onStop ()	{
	   super.onStop ();
	}

	public void onClickHome (View v) {
	    goHome (this);
	}

	public void onClickSearch (View v) {
	    startActivity (new Intent(getApplicationContext(), SearchActivity.class));
	}

	public void onClickAbout (View v) {
	    startActivity (new Intent(getApplicationContext(), AboutActivity.class));
	}

	public void onClickFeature (View v) {
	    int id = v.getId ();
	    switch (id) {
	      case R.id.home_btn_feature1 :
	          startActivity (new Intent(getApplicationContext(), MyDecisions.class));
	          break;
	      case R.id.home_btn_feature2 :
	    	  long decisionID = DecisionHelper.createNewDecision(this);
	    	 
	    	  Intent i = new Intent(getApplicationContext(), DecisionHome.class);
	    	  i.putExtra(DecisionColumns._ID, decisionID);
	    	  startActivity (i);
	    	  break;
	      default: 
	    	  break;
	    }
	}


	public void goHome(Context context)	{
	    final Intent intent = new Intent(context, HomeActivity.class);
	    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    context.startActivity (intent);
	}

	/**
	 * Use the activity label to set the text in the activity's title text view.
	 * The argument gives the name of the view.
	 *
	 * <p> This method is needed because we have a custom title bar rather than the default Android title bar.
	 * See the theme definitons in styles.xml.
	 * 
	 * @param textViewId int
	 * @return void
	 */

	public void setTitleFromActivityLabel (int textViewId) {
	    TextView tv = (TextView) findViewById (textViewId);
	    if (tv != null) tv.setText (getTitle ());
	} 

	/**
	 * Show a string on the screen via Toast.
	 * 
	 * @param msg String
	 * @return void
	 */

	public void toast (String msg) {
	    Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
	} 

	/**
	 * Send a message to the debug log and display it using Toast.
	 */
	public void trace (String msg) {
	    Log.d("Demo", msg);
	    toast (msg);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, OK, 0, R.string.done).setIcon(R.drawable.ic_menu_revert);
	    return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
	    
		boolean result = false;
		
		switch(item.getItemId()) {
	        case OK:
	        	finish();
	        	result = true;
	    } 
		
		return result;
	}
}
