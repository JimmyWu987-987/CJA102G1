package com.farmtastic.common.enums;

public enum ApplScope implements PersistableEnum<Byte> {
	ALL((byte) 0, "全館"), FARMER((byte) 1, "指定小農"), PRODUCT((byte) 2, "指定商品");

	private final Byte code;
	private final String label;

	ApplScope(Byte code, String label) {
		this.code = code;
		this.label = label;
	}

	public Byte getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	public static ApplScope fromCode(Byte code) {
		for (ApplScope t : values()) {
			if (t.code == code)
				return t;
		}
		throw new IllegalArgumentException("未知館內範圍 " + code);
	}
}
