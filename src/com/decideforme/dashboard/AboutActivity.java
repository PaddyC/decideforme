package com.decideforme.dashboard;

import com.decideforme.R;

import android.os.Bundle;

public class AboutActivity extends DashboardActivity {

	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    setContentView (R.layout.activity_about);
	    setTitleFromActivityLabel (R.id.title_text);
	}
	    
} 
