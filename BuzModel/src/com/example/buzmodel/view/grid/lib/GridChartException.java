package com.example.buzmodel.view.grid.lib;

public class GridChartException extends Exception {

	public static enum ExceptionCode {
		ERROR_INVALID_DATA_SET,
		ERROR_UNKNOWN
	}
	public ExceptionCode mCode;
	
	public GridChartException(ExceptionCode code) {
		mCode = code;
	}
	
}
