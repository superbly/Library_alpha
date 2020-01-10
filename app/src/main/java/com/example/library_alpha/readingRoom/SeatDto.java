package com.example.library_alpha.readingRoom;


import com.example.library_alpha.module.SeatCount;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class SeatDto {

    private int seatNum;
    private String usedId = "";
    private boolean seatCheck = false;
    private String remainTime = "";

    public SeatDto(int seatNum) {
        this.seatNum = seatNum;
    }

    public SeatDto(int seatNum, String usedId, boolean seatCheck, String remainTime) {
        this.seatNum = seatNum;
        this.usedId = usedId;
        this.seatCheck = seatCheck;
        this.remainTime = remainTime;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public String getUsedId() {
        return usedId;
    }

    public void setUsedId(String usedId) {
        this.usedId = usedId;
    }

    public boolean isSeatCheck() {
        return seatCheck;
    }

    public void setSeatCheck(boolean seatCheck) {
        this.seatCheck = seatCheck;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public SeatDto() {

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("seatNum", seatNum);
        result.put("usedId", usedId);
        result.put("seatCheck", seatCheck);
        result.put("remainTime", remainTime);
        return result;
    }
}
