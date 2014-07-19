package com.example.buzmodel.model;

public enum EBuzQuality {

	NORMAL(0),
	WARN(1),
	ERROR(2);
	
	private int index;
	EBuzQuality(int index) {
		this.index = index;
	}
	
}
