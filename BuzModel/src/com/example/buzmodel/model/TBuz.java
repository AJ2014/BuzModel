package com.example.buzmodel.model;

import java.util.Date;

/**
 * 
 * @author junjiang2
 * 组件数据实体
 */
public class TBuz {

	private int			index;	// 组件ID 注意：不是数据库自动生成的记录ID
	private String 		name; 	// 组件名
	private EBuzUnit 	unit;	// 组件单位
	private EBuzQuality quality;// 状态
	private float 		value;	// 值
	private long		date;	// 时间戳
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public EBuzUnit getUnit() {
		return unit;
	}
	public void setUnit(EBuzUnit unit) {
		this.unit = unit;
	}
	public float getValue() {
		return value;
	}
	public void setValue(float value) {
		this.value = value;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public EBuzQuality getQuality() {
		return quality;
	}
	public void setQuality(EBuzQuality quality) {
		this.quality = quality;
	}
	
}
