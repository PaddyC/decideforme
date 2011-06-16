package com.decideforme.utils;

import android.widget.Button;
import android.widget.TextView;

public interface ViewHelper {
	
    public static final int ACTIVITY_CREATE=0;
    public static final int ACTIVITY_EDIT=1;
    public static final int ACTIVITY_DELETE=2;
    
    public TextView getTextView(int viewId, String text, int style, boolean setBackground, boolean clickable);
	
	public Button getNewButton(int viewId);
	
	public Button getEditButton(int viewId);
	
	public Button getDeleteButton(int viewId);

}
