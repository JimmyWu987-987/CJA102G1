package com.farmtastic.common.enums;

//折扣 enum
public enum DiscountType implements PersistableEnum<Byte> {
	FULL_REDUCTION((byte) 0, "滿額折抵"), PERCENTAGE((byte) 1, "百分比折扣");

	private final byte code; // 資料庫代碼
	private final String label; // 中文顯示文字

	DiscountType(byte code, String label) {
		this.code = code;
		this.label = label;
	}

	@Override
	public Byte getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	public static DiscountType fromCode(Byte code) {
		for (DiscountType t : values()) {
			if (t.code == code)
				return t;
		}
		throw new IllegalArgumentException("未知折扣類型: " + code);
	}
}
