package com.example.library_alpha.util;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.library_alpha.readingRoom.CountAdapter;
import com.example.library_alpha.readingRoom.Function;

import java.util.Locale;

public class TimerTest {

     boolean timerCheck = false;
     long mTimeLeftInMillis1;
     CountDownTimer mCountDownTimer1;
     Context context;
     CountAdapter.MyViewHolder myViewHolder;

    public TimerTest(CountAdapter.MyViewHolder myViewHolder, Context context, long mTimeLeftInMillis1){
        this.myViewHolder = myViewHolder;
        this.context = context;
        this.mTimeLeftInMillis1 = mTimeLeftInMillis1;
    }

    public void startTimer() {
        mCountDownTimer1 = new CountDownTimer(mTimeLeftInMillis1, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis1 = millisUntilFinished;
                updateCountDownText(myViewHolder);
            }

            @Override
            public void onFinish() {
                Function function = new Function();
                mTimeLeftInMillis1 = 0;
                timerCheck = false;
            }
        }.start();
        timerCheck = true;
    }


    public void pauseTimer() {
        mCountDownTimer1.cancel();
        timerCheck = false;
    }


    public void updateCountDownText(CountAdapter.MyViewHolder myViewHolder) {
        int hours = (int) (mTimeLeftInMillis1 / 3600000);
        int minutes = (int) (mTimeLeftInMillis1 % 3600000) / 60000;
        int seconds = (int) ((mTimeLeftInMillis1 % 3600000) % 60000) / 1000;

        Log.e("TimerTest", "test1");
        String timeLeftFormatted1 = String.format(Locale.getDefault(), "%02d : %02d", hours, minutes);
        myViewHolder.textview.setText(timeLeftFormatted1);

    }
}
