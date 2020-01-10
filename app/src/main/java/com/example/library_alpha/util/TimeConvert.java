package com.example.library_alpha.util;


import android.util.Log;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeConvert {

    long curDateTime;
    long different;
    long hour;
    long minute;
    long second;
    long reqDateTime;
    Date reqDate;


    public TimeConvert(String date) {

        //요청시간 String
        String reqDateStr = date;

        //현재시간 Date
        Date curDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");

        //요청시간을 Date로 parsing 후 time가져오기
        this.reqDate = null;

        try {
            this.reqDate = dateFormat.parse(reqDateStr);
            Log.e("남은시간1",String.valueOf(reqDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.reqDateTime = reqDate.getTime();

        //현재시간을 요청시간의 형태로 format 후 time 가져오기
        try {
            curDate = dateFormat.parse(dateFormat.format(curDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.curDateTime = curDate.getTime();

        //시, 분, 초로 표현
        this.different = reqDateTime - curDateTime;
        Log.e("testsdfadsfa",String.valueOf(different));
        this.hour = different / 3600000;
        this.minute = (different % 3600000) / 60000;
        this.second = ((different % 3600000) % 60000) / 1000;


    }
    public long getDifferent(){
        Log.e("계산된 시간값", Long.toString(different));
        return different;
    }

    public long getHour() {
        return hour;
    }

    public long getMinute() {
        return minute;
    }

    public long getSecond() {
        return second;
    }

}
