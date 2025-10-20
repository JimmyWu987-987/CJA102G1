package com.farmtastic.common.enums;

import org.springframework.data.annotation.Transient;

//折價券使用狀態 enum
public enum CpnUseStatus implements PersistableEnum<Byte> {
	UNUSED((byte) 0, "未使用"), USED((byte) 1, "已使用"), EXPIRED((byte) 2, "已過期");

	private final byte code;// 不可被修改
	private final String text;

	CpnUseStatus(byte code, String text) {
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

	public static CpnUseStatus fromCode(Byte code) {
		for (CpnUseStatus t : values()) {
			if (t.code == code)
				return t;
		}
		throw new IllegalArgumentException("無效的狀態" + code);
	}

	@Transient
	public String getUseStatus() {
		return text; // ✅ 傳回中文名稱
	}
}
