package com.example.buzmodel.model;

import java.util.Date;

/**
 * 
 * @author junjiang2
 * �������ʵ��
 */
public class TBuz {

	private int			index;	// ���ID ע�⣺�������ݿ��Զ����ɵļ�¼ID
	private String 		name; 	// �����
	private EBuzUnit 	unit;	// �����λ
	private EBuzQuality quality;// ״̬
	private float 		value;	// ֵ
	private long		date;	// ʱ���
	
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
