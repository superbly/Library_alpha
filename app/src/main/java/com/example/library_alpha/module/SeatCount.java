package com.example.library_alpha.module;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class SeatCount {

    private String nowSeatCount = "72";

    public SeatCount(){

    }

    public SeatCount(String nowSeatCount){
        this.nowSeatCount = nowSeatCount;
    }

    public String getNowSeatCount() {
        return nowSeatCount;
    }

    public void setNowSeatCount(String nowSeatCount) {
        this.nowSeatCount = nowSeatCount;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nowSeatCount", nowSeatCount);
        return result;
    }
}
