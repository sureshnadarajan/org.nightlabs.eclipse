package org.nightlabs.eclipse.ui.pdfviewer.util;


public class Utilities {
	
	public static int doubleToInt(double pageProperty) {
		return (int)(Math.ceil(pageProperty));
	}
	public static int doubleToIntRoundedDown(double pageProperty) {
		return (int)(Math.floor(pageProperty));
	}
	
	
	
}
