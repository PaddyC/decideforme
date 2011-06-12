package com.decideforme.utils;

import android.app.Activity;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

public class TableLayoutHelperImpl implements TableLayoutHelper {

	public TableRow getNewRow(Activity activity) {
		TableRow tableRow = new TableRow(activity);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		tableRow.setPadding(0, 1, 0, 1);
		return tableRow;
	}

}
