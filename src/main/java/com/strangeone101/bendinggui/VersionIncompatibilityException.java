package com.strangeone101.bendinggui;

public class VersionIncompatibilityException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VersionIncompatibilityException() {
		super("BendingGUI version is not compatible with ProjectKorra version! Check plugin overview page for more information.");
	}

}
