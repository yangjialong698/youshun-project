package com.ennova.pubinfopurchase.utils;

public enum RejectsStatusEnum {

    //步骤状态(1-质量发起人 2-生产部 3-多部门 4-质量经理 5-最终处置意见 )
    ONE(1,"质量发起人"), TWO(2,"生产部"), THREE(3,"多部门"), FOUR(4,"质量经理"), FIVE(5,"最终处置意见");

    private int code;
    private String name;

    RejectsStatusEnum(int code, String name) {
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

    public static RejectsStatusEnum getProblemStatusEnum(int code){
        for (RejectsStatusEnum rejectsStatusEnum : RejectsStatusEnum.values()) {
            if (rejectsStatusEnum.getCode() == code){
                return rejectsStatusEnum;
            }
        }
        return null;
    }

}
