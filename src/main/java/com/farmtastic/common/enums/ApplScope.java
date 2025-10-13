package com.farmtastic.common.enums;

public enum ApplScope implements PersistableEnum<Byte> {
	ALL((byte) 0, "全館"), FARMER((byte) 1, "指定小農"), PRODUCT((byte) 2, "指定商品");

	private final Byte code;
	private final String text;

	ApplScope(Byte code, String text) {
		this.code = code;
		this.text = text;
	}

	public Byte getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public static ApplScope fromCode(Byte code) {
		return PersistableEnum.fromCode(ApplScope.class, code);
	}

}
