package com.melon.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
@Data
public class R {
    private int code;
    private String message;
    private Map<String, Object> data = new HashMap<>();

    public static R ok(){

        R r = new R();
        r.setCode(HttpStatus.OK.value());
        r.setMessage("Succeed to process API request");
        return r;
    }
    public static R error(){
        R r = new R();
        r.setCode(-1);
        r.setMessage("Failed to process API request");
        return r;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R message(String message){
        this.message = message;
        return this;
    }
}
