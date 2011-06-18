package com.decideforme.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.db.decideforme.DatabaseObject;
import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.competitors.Competitor.CompetitorColumns;
import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.criteria.Criterion.CriterionColumns;
import com.db.decideforme.decision.Decision;
import com.db.decideforme.decision.Decision.DecisionColumns;
import com.decideforme.R;
import com.decideforme.competitors.CompetitorEdit;
import com.decideforme.competitors.CompetitorHelper;
import com.decideforme.competitors.CompetitorsScreen;
import com.decideforme.criteria.CriteriaHelper;
import com.decideforme.criteria.CriteriaScreen;
import com.decideforme.criteria.CriterionEdit;
import com.decideforme.decision.DecisionHelper;
import com.decideforme.decision.DecisionHome;
import com.decideforme.decision.MyDecisions;
import com.decideforme.report.ReportActivity;

public class ViewHelperImpl implements ViewHelper {
	
	private long mDecisionRowId;
	private Activity mThisActivity;
	private Integer subject;
	private Integer nameColumnIndex;
	private String subjectName;
	
	public ViewHelperImpl(long decisionRowId, Activity activity, Integer subject) {
		setmDecisionRowId(decisionRowId);
		setmThisActivity(activity);
		setSubject(subject);
	}
	
	public TextView getTextView(int viewId, String text, int style, Typeface typeFace, boolean setBackground, boolean clickable) {
    	
		TextView thisTextView = new TextView(getmThisActivity());
		thisTextView.setText(text);
		thisTextView.setId(viewId++);
		thisTextView.setTypeface(typeFace, style);
		thisTextView.setGravity(Gravity.CLIP_HORIZONTAL);
		
		if (setBackground) {
			thisTextView.setBackgroundDrawable(getmThisActivity().getResources().getDrawable(R.drawable.textfield));
		}
		
		if (clickable) {
			thisTextView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View thisView) {
					Intent intent = getDestinationIntentForEdit(thisView, 0);
					getmThisActivity().startActivity(intent);
				}
			});
		}

		return thisTextView;
		
	}
	

	public Button getNewButton(int viewId) {
		
		final Activity thisActivity = getmThisActivity();
		
		Button newButton = new Button(thisActivity);
		newButton.setId(viewId);
		newButton.setBackgroundDrawable(thisActivity.getResources().getDrawable(R.drawable.green_add_button));
		newButton.setGravity(Gravity.CLIP_VERTICAL);
		newButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View thisView) {
				Intent intent = getDestinationIntentForNewObject(thisView); 
		    	thisActivity.startActivity (intent);
			}
		});
		return newButton;
	}

	
	private Intent getDestinationIntentForNewObject(View thisView) {
		
		Intent intent = null;
		switch (getSubject()) {
			case SubjectConstants.DECISION:
				setmDecisionRowId(DecisionHelper.createNewDecision(getmThisActivity()));
				intent = new Intent(getmThisActivity().getApplicationContext(), DecisionHome.class);
		    	intent.putExtra(DecisionColumns._ID, getmDecisionRowId());
				break;
			case SubjectConstants.COMPETITOR:
				long newCompetitorRowID = CompetitorHelper.createCompetitor(getmThisActivity(), getmDecisionRowId());
				intent = new Intent().setClass(getmThisActivity(), CompetitorEdit.class);
				intent.putExtra(CompetitorColumns._ID, newCompetitorRowID);
				intent.putExtra(CompetitorColumns.DECISIONID, getmDecisionRowId());
				break;
			case SubjectConstants.CRITERION:
				long newCriterionRowId = CriteriaHelper.createCriterion(getmThisActivity(), getmDecisionRowId());
				intent = new Intent().setClass(getmThisActivity(), CriterionEdit.class);
				intent.putExtra(CriterionColumns._ID, newCriterionRowId);
				intent.putExtra(CriterionColumns.DECISIONID, getmDecisionRowId());
				break;
		}
		return intent;
	}
	

	public Button getEditButton(int viewId) {
		
		Activity thisActivity = getmThisActivity();
		Button editButton = new Button(thisActivity);
		editButton.setBackgroundDrawable(thisActivity.getResources().getDrawable(R.drawable.blue_edit_button));
		editButton.setId(viewId);
		editButton.setGravity(Gravity.CLIP_VERTICAL);
		editButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View thisView) {
				Intent intent = getDestinationIntentForEdit(thisView, 2);
				getmThisActivity().startActivity(intent);
			}
		});
		
		return editButton;
	}
	
	private Intent getDestinationIntentForEdit(View thisView, Integer offSet) {
		
		Cursor thisCursor = getObject(getmThisActivity(), thisView, offSet);
		Intent intent = null;
		switch (getSubject()) {
		case SubjectConstants.DECISION:
			intent = new Intent().setClass(getmThisActivity(), DecisionHome.class);
			intent.putExtra(DecisionColumns._ID, thisCursor.getLong(Decision.COLUMN_INDEX_ROW_ID));
			break;
		case SubjectConstants.COMPETITOR:
			intent = new Intent().setClass(getmThisActivity(), CompetitorEdit.class);
			intent.putExtra(CompetitorColumns._ID, thisCursor.getLong(Competitor.COLUMN_INDEX_ROW_ID));
			intent.putExtra(CompetitorColumns.DECISIONID, getmDecisionRowId());
			break;
		case SubjectConstants.CRITERION:
			intent = new Intent().setClass(getmThisActivity(), CriterionEdit.class);
			intent.putExtra(CriterionColumns._ID, thisCursor.getLong(DatabaseObject.COLUMN_INDEX_ROW_ID));
			intent.putExtra(CriterionColumns.DECISIONID, getmDecisionRowId());
			break;
		}
		return intent;
	}
	
	public Button getDeleteButton(int viewId) {
		
		Button deleteButton = new Button(getmThisActivity());
		deleteButton.setBackgroundDrawable(getmThisActivity().getResources().getDrawable(R.drawable.red_delete_button));
		deleteButton.setId(viewId);
		deleteButton.setGravity(Gravity.CLIP_VERTICAL);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View thisView) {
				Cursor thisCursor = getObject(getmThisActivity(), thisView, 3);
				displayAreYouSure(thisCursor);
			}
		});
		return deleteButton;
	}

	
	private Cursor getObject(Activity activity, View thisButton, Integer offset) {
		
		Cursor cursorForSubject = null;
		
		switch (getSubject()) {
		case SubjectConstants.DECISION:
			setNameColumnIndex(Decision.COLUMN_INDEX_NAME);
			TextView decisionName = (TextView) activity.findViewById(thisButton.getId() - offset);
			String thisDecisionName = (String) decisionName.getText();
			cursorForSubject = DecisionHelper.getDecision(activity, thisDecisionName);
			break;
		case SubjectConstants.COMPETITOR:
			setNameColumnIndex(Competitor.COLUMN_INDEX_DESCRIPTION);
			TextView competitorName = (TextView) activity.findViewById(thisButton.getId() - offset);
			String thisSubjectName = (String) competitorName.getText();
			cursorForSubject = CompetitorHelper.getCompetitorByName(activity, getmDecisionRowId(), thisSubjectName);
			break;
		case SubjectConstants.CRITERION:
			setNameColumnIndex(Criterion.COLUMN_INDEX_DECRIPTION);
			TextView thisCriterion = (TextView) activity.findViewById(thisButton.getId() - offset);
			String thisCriterionName = (String) thisCriterion.getText();
			cursorForSubject = CriteriaHelper.getCriterionByName(getmThisActivity(), getmDecisionRowId(), thisCriterionName);
			break;
		}
		return cursorForSubject;
	}
	
	
	private void displayAreYouSure(final Cursor thisCursor) {
		
    	AlertDialog.Builder alertbox = new AlertDialog.Builder(getmThisActivity());
    	
    	setSubjectName(thisCursor.getString(getNameColumnIndex()));
    	String wantToDelete = getmThisActivity().getString(R.string.want_to_delete) + getSubjectName() + "'?";
		alertbox.setMessage(wantToDelete);

    	alertbox.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int whichButton) {

	 	    	Integer rowID = thisCursor.getInt(DatabaseObject.COLUMN_INDEX_ROW_ID);
	 	    	deleteRow(rowID);
		    	
		    	Toast.makeText(getmThisActivity().getApplicationContext(), 
	        			getmThisActivity().getString(R.string.deleted) + getSubjectName() + "'", Toast.LENGTH_SHORT).show();
		    	
		    	getmThisActivity().setResult(Activity.RESULT_OK);
		    	
		    	repopulateData();

	         }

			private void deleteRow(Integer rowID) {
				switch (subject) {
	 	    	case SubjectConstants.DECISION:
	 	    		DecisionHelper.deleteDecision(getmThisActivity(), rowID);
	 	    		break;
	 	    	case SubjectConstants.CRITERION:
	 	    		CriteriaHelper.deleteCriterion(getmThisActivity(), rowID);
	 	    		break;
	 	    	case SubjectConstants.COMPETITOR:
	 	    		CompetitorHelper.deleteCompetitor(getmThisActivity(), rowID);
	 	    		break;
	 	    	}
			}

			private void repopulateData() {
				if (getmThisActivity() instanceof MyDecisions) {
		    		MyDecisions myDecisions = (MyDecisions) getmThisActivity();
		    		myDecisions.fillData();
		    	} else if (getmThisActivity() instanceof CompetitorsScreen) {
		    		CompetitorsScreen competitors = (CompetitorsScreen) getmThisActivity();
		    		competitors.fillData();
		    	} else if (getmThisActivity() instanceof CriteriaScreen) {
		    		CriteriaScreen criteria = (CriteriaScreen) getmThisActivity();
		    		criteria.fillData();
		    	}
			}
    	});

		alertbox.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
    	 	public void onClick(DialogInterface arg0, int arg1) {
	        	Toast.makeText(getmThisActivity().getApplicationContext(), 
	        			getmThisActivity().getString(R.string.nothing_deleted), Toast.LENGTH_SHORT).show();
    	 	}
		});
	    
		alertbox.show();
	 }
	

	public Button getReportButton(int viewId) {
		
		Button reportButton = new Button(getmThisActivity());
		reportButton.setBackgroundDrawable(getmThisActivity().getResources().getDrawable(R.drawable.dk_report_button));
		reportButton.setId(viewId);
		reportButton.setGravity(Gravity.CLIP_VERTICAL);
		reportButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View thisView) {
				Intent intent = new Intent().setClass(getmThisActivity(), ReportActivity.class);
				intent.putExtra(DecisionColumns._ID, getmDecisionRowId());
				getmThisActivity().startActivity(intent);
			}
		});
		return reportButton;
		
	}
	
	public TableRow getNewRow(Activity activity, boolean setBackground) {
		TableRow tableRow = new TableRow(activity);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		tableRow.setPadding(1, 1, 1, 1);
		if (setBackground) {
			tableRow.setBackgroundResource(R.drawable.textfield_default);
		}
		return tableRow;
	}

	public void setmDecisionRowId(long mDecisionRowId) {
		this.mDecisionRowId = mDecisionRowId;
	}

	public long getmDecisionRowId() {
		return mDecisionRowId;
	}

	public void setmThisActivity(Activity mThisActivity) {
		this.mThisActivity = mThisActivity;
	}

	public Activity getmThisActivity() {
		return mThisActivity;
	}

	public void setSubject(Integer subject) {
		this.subject = subject;
	}

	public Integer getSubject() {
		return subject;
	}

	public void setNameColumnIndex(Integer nameColumnIndex) {
		this.nameColumnIndex = nameColumnIndex;
	}

	public Integer getNameColumnIndex() {
		return nameColumnIndex;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectName() {
		return subjectName;
	}

}
