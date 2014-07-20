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

    public int index;
    public String flag;
	private EBuzUnit(int index, String name) {
		this.index = index;
		this.flag = name;
	}
	
	public EBuzUnit getUnitById(int id) {
		for (EBuzUnit unit : EBuzUnit.values()) {
			if (id == unit.index) {
				return unit;
			}
		}
		return null;
	}
	
	public EBuzUnit getUnitByFlag(String flag) {
		for (EBuzUnit unit : EBuzUnit.values()) {
			if (flag.equalsIgnoreCase(unit.flag)) {
				return unit;
			}
		}
		return null;
	}
}
