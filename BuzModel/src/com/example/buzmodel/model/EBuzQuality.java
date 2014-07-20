package com.example.buzmodel.model;

import android.graphics.Color;

public enum EBuzQuality {

	NORMAL(0, Color.GREEN),
	WARN(1, Color.YELLOW),
	ERROR(2, Color.RED);
	
	public int index;
    public int color;

	EBuzQuality(int index, int color) {
		this.index = index;
        this.color = color;
	}
	
}
