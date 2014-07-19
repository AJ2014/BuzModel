package com.example.buzmodel.model;

/**
 * 
 * @author junjiang2
 * 计量单位
 */
public enum EBuzUnit {

	DEGREE(0, "C"), 	// 温度(摄氏度)
	PASCAL(1, "Pa"), 	// 压力(帕斯卡)
	VOLT(2, "V"), 		// 电压(伏特)
	AMPERE(3, "A"); 	// 电流(安培)
	
	private int index;
	private String flag;
	private EBuzUnit(int index, String name) {
		this.index = index;
		this.flag = name;
	}
	
	public String getFlag() {
		return flag;
	}
	
	public int getIndex() {
		return index;
	}
	
	public EBuzUnit getUnitById(int id) {
		for (EBuzUnit unit : EBuzUnit.values()) {
			if (id == unit.getIndex()) {
				return unit;
			}
		}
		return null;
	}
	
	public EBuzUnit getUnitByFlag(String flag) {
		for (EBuzUnit unit : EBuzUnit.values()) {
			if (flag.equalsIgnoreCase(unit.getFlag())) {
				return unit;
			}
		}
		return null;
	}
}
