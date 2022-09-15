package com.ennova.pubinfotask.utils;

public enum DayTypeEnum {
    DAYNULL(0, ""), DAYTEN(1, "11"), DAYTWENTY(2, "21"), DAYTHIRTY(3, "31");

    private int code;
    private String name;

    DayTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static DayTypeEnum getDayTypeEnum(int code) {
        for (DayTypeEnum dayTypeEnum : DayTypeEnum.values()) {
            if (dayTypeEnum.getCode() == code) {
                return dayTypeEnum;
            }
        }
        return null;
    }

    public static DayTypeEnum getDayTypeEnum(String name) {
        for (DayTypeEnum dayTypeEnum : DayTypeEnum.values()) {
            if (dayTypeEnum.getName().equals(name)) {
                return dayTypeEnum;
            }
        }
        return null;
    }

}
