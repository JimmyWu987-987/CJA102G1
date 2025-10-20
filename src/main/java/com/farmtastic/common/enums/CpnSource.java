package com.farmtastic.common.enums;

public enum CpnSource implements PersistableEnum<String> {

	REGISTRATION("REGISTRATION", "註冊券"), LOTTERY("LOTTERY", "抽獎券"), BIRTHDAY("BIRTHDAY", "生日券"), EVENT("EVENT", "活動券"),
	OTHER("OTHER", "一般券");

	private final String code;
	private final String text;

	CpnSource(String code, String text) {
		this.code = code;
		this.text = text;
	}

	@Override
	public String getCode() {
		return code;
	}

	public String getText() {
		return text;
	}
}
