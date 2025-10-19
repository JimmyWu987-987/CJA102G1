package com.farmtastic.act.enums;

// 待刪
public enum RegStat {
	NORMAL(0, "正常"),
	CONFIRMED(1, "成團"),
	CANCELLED_BY_NOT_ENOUGH(2, "不成團，取消"),
	MODIFIED(3, "已完成"),
	CANCELLED(4, "取消");


	private final int code;
	private final String desc;

	RegStat(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
	return desc;
    }
    
	public static String getRegStatDesc(Integer code) {
		if (code == null) return "";
		for (RegStat status : RegStat.values()) {
			if (Integer.valueOf(status.getCode()).equals(code)) {
				return status.getDesc();
			}
		}
		return "";
    }
}
