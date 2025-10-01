package com.farmtastic.common.enums;

//折價券使用狀態 enum
public enum CpnUseStatus implements PersistableEnum<Byte> {
	UNUSED((byte) 0), USED((byte) 1), EXPIRED((byte) 2);

	private final byte code;// 不可被修改

	CpnUseStatus(byte code) {
		this.code = code;
	}

	@Override
	public Byte getCode() {
		return code;
	}

	public static CpnUseStatus fromCode(Byte code) {
		for (CpnUseStatus t : values()) {
			if (t.code == code)
				return t;
		}
		throw new IllegalArgumentException("無效的狀態" + code);
	}
}
