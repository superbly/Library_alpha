package com.example.library_alpha.user;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserDto {

    private String id;
    private String pw;
    private String name;
    private int roomNum;
    private int seatNum;
    private String reservationDate = "";
    private String remainTime = "";
    private boolean reservationCheck = false;

    public UserDto() {

    }


    public UserDto(String id, String pw, String name) {
        this.id = id;
        this.pw = pw;
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public boolean isReservationCheck() {
        return reservationCheck;
    }

    public void setReservationCheck(boolean reservationCheck) {
        this.reservationCheck = reservationCheck;
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();

        result.put("id", id);
        result.put("pw", pw);
        result.put("name", name);
        result.put("roomNum", roomNum);
        result.put("seatNum", seatNum);
        result.put("reservationDate", reservationDate);
        result.put("remainTime", remainTime);
        result.put("reservationCheck", reservationCheck);


        return result;
    }

}
