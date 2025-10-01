package com.farmtastic.common.enums;

//折扣 enum
public enum DiscountType implements PersistableEnum<Byte> {
	FULL_REDUCTION((byte) 0), // 滿額折抵
	PERCENTAGE((byte) 1); // 百分比

	private final byte code;// 不可被修改

	DiscountType(byte code) {
		// 設定 ，所以寫此建構
		this.code = code;
	}

	@Override
	public Byte getCode() {
		return code;
	}

	public static DiscountType fromCode(Byte code) {
		for (DiscountType t : values()) {
			if (t.code == code)
				return t;
		}
		throw new IllegalArgumentException("未知折扣類型: " + code);
	}
}
