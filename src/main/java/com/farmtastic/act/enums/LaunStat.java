package com.farmtastic.act.enums;

public enum LaunStat {
	OFFLAUNCH(0, "下架中"),
    ONLAUNCH(1, "上架中");
    
    private final int code;
    private final String desc;
    
    LaunStat(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }

    public static String getLaunStatDesc(Integer code) {
        if (code == null) return "";
        for (LaunStat status : LaunStat.values()) {
            if (Integer.valueOf(status.getCode()).equals(code)) {
                return status.getDesc();
            }
        }
        return "";
    }
}
