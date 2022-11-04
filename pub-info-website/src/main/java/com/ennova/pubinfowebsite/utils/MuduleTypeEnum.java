package com.ennova.pubinfowebsite.utils;

public enum MuduleTypeEnum {

    //模块类型：1-机加摇臂 2-摇臂轴 3-摇臂后处理 4-装配
    ROCKER(1,"机加摇臂"), ROCKSHAFT(2,"摇臂轴"),AFTEREATMENT(3,"摇臂后处理"), ASSEMBLE(4,"装配");

    private int code;
    private String name;

    MuduleTypeEnum(int code, String name) {
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

    public static MuduleTypeEnum getMuduleTypeEnum(int code){
        for (MuduleTypeEnum muduleTypeEnum : MuduleTypeEnum.values()) {
            if (muduleTypeEnum.getCode() == code){
                return muduleTypeEnum;
            }
        }
        return null;
    }

}
