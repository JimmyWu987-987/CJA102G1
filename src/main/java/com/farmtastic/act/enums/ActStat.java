package com.farmtastic.act.enums;

public enum ActStat {
	PENDING(1, "待審核"), APPROVED(2, "審核通過"), REJECTED(3, "審核未過"), REVISED(4, "已編輯送審, 待審核");

	private final int code;
	private final String desc;

	ActStat(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public static String getActStatDesc(Integer code) {
		if (code == null)
			return "";
		for (ActStat status : ActStat.values()) {
			if (Integer.valueOf(status.getCode()).equals(code)) {
				return status.getDesc();
			}
		}
		return "";
	}
}
