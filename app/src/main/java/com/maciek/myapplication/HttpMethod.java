package com.maciek.myapplication;

/**
 * Created by Maciek on 2016-06-05.
 */
public enum HttpMethod {
    GET("GET"), POST("POST");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
