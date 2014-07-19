package com.example.buzmodel.model;

/**
 * 
 * @author junjiang2
 * ������λ
 */
public enum EBuzUnit {

	DEGREE(0, "C"), 	// �¶�(���϶�)
	PASCAL(1, "Pa"), 	// ѹ��(��˹��)
	VOLT(2, "V"), 		// ��ѹ(����)
	AMPERE(3, "A"); 	// ����(����)
	
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
