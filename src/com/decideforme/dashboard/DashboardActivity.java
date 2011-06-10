package com.decideforme.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.decideforme.DecisionEdit;
import com.decideforme.MyDecisions;
import com.decideforme.R;

public abstract class DashboardActivity extends Activity {

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
	           startActivity (new Intent(getApplicationContext(), DecisionEdit.class));
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
}
