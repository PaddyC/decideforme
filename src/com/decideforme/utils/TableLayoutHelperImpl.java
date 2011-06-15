package com.decideforme.utils;

import com.decideforme.R;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

public class TableLayoutHelperImpl implements TableLayoutHelper {

	public TableRow getNewRow(Activity activity, boolean setBackground) {
		TableRow tableRow = new TableRow(activity);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		tableRow.setGravity(Gravity.CLIP_HORIZONTAL);
		tableRow.setPadding(1, 1, 1, 1);
		if (setBackground) {
			tableRow.setBackgroundResource(R.drawable.textfield_default);
		}
		return tableRow;
	}

}
