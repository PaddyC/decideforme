package com.decideforme.utils;

import java.util.List;

public class StringUtils {
	
    /**
     * Returns the given object as a string (by calling toString() on it). If object is null then
     * the string "null" is returned
     * @return
     */
    public static String objectAsString(Object o) {
        String retVal;
        if (o != null) {
            if (o instanceof Object[]) {
                retVal = arrayAsString((Object[]) o);
            } else if (o instanceof List) {
                retVal = arrayAsString(((List) o).toArray());
            } else {
                retVal = o.toString();
            }
        } else {
            retVal = "<null>";
        }
        return retVal;
    }
    
	/**
	 * Convert an object array to a string by recursively going to each
	 * element in the array and calling it's toString()
	 * @param objectArray
	 * @return
	 */
	private static String arrayAsString(Object[] objectArray) {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("[");
    	for (int i=0; i<objectArray.length; i++) {
    		buffer.append(StringUtils.objectAsString(objectArray[i]));
    		if (i + 1 < objectArray.length) {
    			buffer.append(", ");
    		}
    	}
    	buffer.append("]");
    	return buffer.toString(); 
	}


}
