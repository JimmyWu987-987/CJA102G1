package com.farmtastic.common.enums;

// 狀態 enum
public enum IsActive implements PersistableEnum<Byte> {
	INACTIVE((byte) 0), // 未啟用
	ACTIVE((byte) 1); // 啟用

	private final byte code;

	IsActive(byte code) {
		this.code = code;
	}

	@Override
	public Byte getCode() {
		return code;
	}

	public static IsActive fromCode(Byte code) {
		for (IsActive s : values()) {
			if (s.code == code)
				return s;
		}
		throw new IllegalArgumentException("未知啟用狀態: " + code);
	}
}
