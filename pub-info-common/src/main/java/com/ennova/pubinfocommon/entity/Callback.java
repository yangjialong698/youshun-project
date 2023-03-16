package com.ennova.pubinfocommon.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Callback<T> {

    public int errCode;
    public String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T data;

    public Callback<T> setCallback(int errCode, String message) {
        this.errCode = errCode;
        this.message = message;
        return this;
    }

    private Callback<T> setCallback(T object) {
        this.errCode = Callback.INT_SUCCESS;
        this.message = Callback.STRING_SUCCESS;
        this.data = object;
        return this;
    }

    public Callback<T> setCallback(int errCode, String message, T object) {
        this.errCode = errCode;
        this.message = message;
        this.data = object;
        return this;
    }

    private static final int INT_SUCCESS = 0;
    private static final int INT_FAIL = 1;
    private static final String STRING_SUCCESS = "OK";

    public static <T> Callback<T> success() {
        return new Callback<T>().setCallback(INT_SUCCESS,STRING_SUCCESS);
    }
    public static <T> Callback<T> success(T data) {
        return new Callback<T>().setCallback(data);
    }

    public static <T> Callback<T> error(String message) {
        return new Callback<T>().setCallback(INT_FAIL,message);
    }

    public static <T> Callback<T> error(int errCode, String message) {
        return new Callback<T>().setCallback(errCode,message);
    }
    public static <T> Callback<T> error(String message, T data) {
        return new Callback<T>().setCallback(INT_FAIL,message,data);
    }
}
