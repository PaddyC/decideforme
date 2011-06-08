package com.decideforme.utils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BundleHelper  {
	private static final String TAG = BundleHelper.class.getName();
	
	private Activity activity;
	private Bundle savedInstanceState;
	
	public BundleHelper(Activity activity, Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;
		this.activity = activity;
	}
	
	public Long getBundledFieldLongValue(String fieldName) {
		Log.d(TAG, " >> getBundledFieldLongValue()");
		
		Long bundledFieldValue = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(fieldName);
		if (bundledFieldValue == null) {
            Bundle extras = activity.getIntent().getExtras();
            bundledFieldValue = extras != null ? extras.getLong(fieldName) : null;
        }
		
		Log.d(TAG, " << getBundledFieldLongValue(), " +
				"returned: '" + StringUtils.objectAsString(bundledFieldValue) + "'");
		return bundledFieldValue;
	}

}
