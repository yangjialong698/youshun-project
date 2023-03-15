package com.ennova.pubinfostore.utils;

public enum ProblemStatusEnum {

    //反馈状态(0-未解决 1-已解决 2-待确认 3-待解决)
    UNSOLVED(0,"未解决"), SOLVED(1,"已解决"), CONFIRMED(2,"待确认"), TOBESOLVED(3,"待解决");

    private int code;
    private String name;

    ProblemStatusEnum(int code, String name) {
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

    public static ProblemStatusEnum getProblemStatusEnum(int code){
        for (ProblemStatusEnum problemStatusEnum : ProblemStatusEnum.values()) {
            if (problemStatusEnum.getCode() == code){
                return problemStatusEnum;
            }
        }
        return null;
    }

}
