package com.farmtastic.common.enums;

// 狀態 enum
public enum IsActive implements PersistableEnum<Byte> {
	INACTIVE((byte) 0, "未啟用"), // 未啟用
	ACTIVE((byte) 1, "啟用"); // 啟用

	private final byte code;
	private final String text;

	IsActive(byte code, String text) {
		this.code = code;
		this.text = text;
	}

	@Override
	public Byte getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public static IsActive fromCode(Byte code) {
		return PersistableEnum.fromCode(IsActive.class, code);
	}

}
