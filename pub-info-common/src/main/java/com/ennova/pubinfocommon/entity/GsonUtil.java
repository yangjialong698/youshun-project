package com.ennova.pubinfocommon.entity;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class GsonUtil {

    private static Gson gson = null;

    public static String toJson(Object o) {
        if(gson == null) {
            gson = new Gson();
        }
        return gson.toJson(o);
    }

    public static Object fromJson(String jsonStr, Class<?> c) {
        if(gson == null) {
            gson = new Gson();
        }
        return gson.fromJson(jsonStr, (Type) c);
    }

}
