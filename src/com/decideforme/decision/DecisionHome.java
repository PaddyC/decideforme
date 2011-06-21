package com.decideforme.decision;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.competitors.CompetitorsScreen;
import com.decideforme.criteria.CriteriaScreen;
import com.decideforme.dashboard.DashboardTabActivity;
import com.decideforme.ratings.RatingsScreen;
import com.decideforme.utils.BundleHelper;

public class DecisionHome extends DashboardTabActivity {
	
	private Long mDecisionRowId;
	
	private TabHost mTabHost;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decision_home);
        setTitleFromActivityLabel (R.id.title_text);
        
        BundleHelper bundleHelper = new BundleHelper(this,  savedInstanceState);
		mDecisionRowId = bundleHelper.getBundledFieldLongValue(DecisionColumns._ID);
        
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
		
        // Create an Intent to launch an Activity for the tab (to be reused)
        Intent decisionEdit = new Intent().setClass(this, DecisionEdit.class);
        decisionEdit.putExtra(DecisionColumns._ID, mDecisionRowId);
        setUpTab("Info", decisionEdit);
        
        Intent competitors = new Intent().setClass(this, CompetitorsScreen.class);
        competitors.putExtra(DecisionColumns._ID, mDecisionRowId);
        setUpTab("Choices", competitors);

        Intent criteria = new Intent().setClass(this, CriteriaScreen.class);
        criteria.putExtra(DecisionColumns._ID, mDecisionRowId);
        setUpTab("Criteria", criteria);
        
        Intent rating = new Intent().setClass(this, RatingsScreen.class);
        rating.putExtra(DecisionColumns._ID, mDecisionRowId);
        setUpTab("Rate", rating);

        mTabHost.setCurrentTab(0);
    }
	
	private void setUpTab(final String tag, Intent intent) { 
		
		View tabView = createTabView(mTabHost.getContext(), tag);
		TabSpec tabSpec = mTabHost.newTabSpec(tag).setIndicator(tabView).setContent(intent);
		mTabHost.addTab(tabSpec);
	}
	
	private static View createTabView(final Context context, final String text) {
		
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		TextView tabTv = (TextView) view.findViewById(R.id.tabsText);
		tabTv.setText(text);
		return view;
		
	}
	
}
