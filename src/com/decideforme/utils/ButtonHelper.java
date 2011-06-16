package com.decideforme.utils;

import android.widget.Button;

public interface ButtonHelper {
	
    public static final int ACTIVITY_CREATE=0;
    public static final int ACTIVITY_EDIT=1;
    public static final int ACTIVITY_DELETE=2;
    
	
	public Button getNewButton(int viewId);
	
	public Button getEditButton(int viewId);
	
	public Button getDeleteButton(int viewId);

}
