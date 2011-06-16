package com.decideforme.decision;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.competitors.CompetitorsScreen;
import com.decideforme.criteria.CriteriaScreen;
import com.decideforme.dashboard.DashboardTabActivity;
import com.decideforme.ratings.RatingsScreen;
import com.decideforme.utils.BundleHelper;

public class DecisionHome extends DashboardTabActivity {
	
	private Long mDecisionRowId;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decision_home);
        setTitleFromActivityLabel (R.id.title_text);
        
        BundleHelper bundleHelper = new BundleHelper(this,  savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
        
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
        
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, DecisionEdit.class);
        intent.putExtra(DecisionColumns._ID, mDecisionRowId);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("info").setIndicator("Info",
                          res.getDrawable(R.drawable.ic_tab_info))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, CompetitorsScreen.class);
        intent.putExtra(DecisionColumns._ID, mDecisionRowId);
        spec = tabHost.newTabSpec("choices").setIndicator("Choices",
                          res.getDrawable(R.drawable.ic_tab_competitors))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, CriteriaScreen.class);
        intent.putExtra(DecisionColumns._ID, mDecisionRowId);
        spec = tabHost.newTabSpec("criteria").setIndicator("Criteria",
                          res.getDrawable(R.drawable.ic_tab_criteria))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, RatingsScreen.class);
        intent.putExtra(DecisionColumns._ID, mDecisionRowId);
        spec = tabHost.newTabSpec("rate").setIndicator("Rate",
                          res.getDrawable(R.drawable.ic_tab_rate))
                      .setContent(intent);
        tabHost.addTab(spec);
        

        tabHost.setCurrentTab(0);
    }
	
}
