package com.decideforme.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

public interface ViewHelper {
	
    public static final int ACTIVITY_CREATE=0;
    public static final int ACTIVITY_EDIT=1;
    public static final int ACTIVITY_DELETE=2;
    
    public TextView getTextView(int viewId, String text, int style, Typeface typeFace, boolean setBackground, boolean clickable);
	
	public Button getNewButton(int viewId);
	
	public Button getEditButton(int viewId);
	
	public Button getDeleteButton(int viewId);
	
	public Button getReportButton(int viewId);

	public TableRow getNewRow(Activity activity, boolean setBackground);

}
