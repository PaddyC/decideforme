package com.db.decideforme;

import com.db.decideforme.competitors.Competitor;
import com.db.decideforme.criteria.Criterion;
import com.db.decideforme.decision.Decision;

public class DatabaseObjectHelper {
	
	public static String getName(DatabaseObject object) {
		
		String name = "";
		if (object instanceof Decision) {
			Decision thisDecision = (Decision) object;
			name = thisDecision.getName();
		} else if (object instanceof Competitor) {
			Competitor thisCompetitor = (Competitor) object;
			name = thisCompetitor.getDescription();
		} else if (object instanceof Criterion) {
			Criterion thisCriterion = (Criterion) object;
			name = thisCriterion.getDescription();
		}

		return name;
			
	}

}
