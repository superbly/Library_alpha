package com.example.library_alpha.user;

import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.library_alpha.R;
import com.example.library_alpha.readingRoom.Function;
import com.example.library_alpha.util.TimeConvert;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import static com.example.library_alpha.LoginActivity.loginId;
import static com.example.library_alpha.LoginActivity.loginStatus;


public class MyInfo extends AppCompatActivity {
    TextView myRoomName, myRoomHakbun, myRoomCurrentSeat, textReservation, textRemainTime, textview;
    Button myRoomReturn, myRoomExtension;
    LinearLayout info, noReservation;

    public long mTimeLeftInMillis;
    public CountDownTimer mCountDownTimer;
    public boolean timerCheck = false;
    public static String remainTimes;
    boolean flag = false;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        setTitle("내 정보");

        myRoomName = (TextView) findViewById(R.id.myRoomName);
        myRoomHakbun = (TextView) findViewById(R.id.myRoomHakbun);
        myRoomCurrentSeat = (TextView) findViewById(R.id.myRoomCurrentSeat);
        textReservation = (TextView) findViewById(R.id.textReservation);
        textRemainTime = (TextView) findViewById(R.id.textRemainTime);
        myRoomReturn = (Button) findViewById(R.id.myRoomReturn);
        myRoomExtension = (Button) findViewById(R.id.myRoomExtension);
        info = (LinearLayout) findViewById(R.id.info);
        noReservation = (LinearLayout) findViewById(R.id.noReservation);
        textview = (TextView) findViewById(R.id.textview);

        if (loginStatus)
            userDetail(loginId);

    }

    protected void onPause() {
        super.onPause();
        Log.e("pauseTest", "dd");
        if(flag)
        mCountDownTimer.cancel();
    }


    // 유저 개인정보 조회
    public void userDetail(String id) {
        Query query = databaseReference.child("User").child(id);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final UserDto userDto = dataSnapshot.getValue(UserDto.class);

                if (userDto.isReservationCheck()) {
                    TimeConvert timeConvert = new TimeConvert(userDto.getRemainTime());
                    mTimeLeftInMillis = timeConvert.getDifferent();
                    Log.e("김한섭1", Long.toString(mTimeLeftInMillis));
                    startTimer(userDto);
                    flag = true;

                } else if (timerCheck)
                    pauseTimer();

                myRoomHakbun.setText(userDto.getId());
                myRoomName.setText(userDto.getName());
                myRoomCurrentSeat.setText(userDto.getRoomNum() + " 열람실 " + userDto.getSeatNum() + " 번 사용중");
                textReservation.setText(userDto.getReservationDate());

                // 반납
                myRoomReturn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCountDownTimer.cancel();
                        Toast.makeText(getApplication(), (userDto.getSeatNum()) + "번 자리가 반납되었습니다.", Toast.LENGTH_SHORT).show();
                        Function function = new Function();
                        function.returnSeat(userDto);
                    }
                });

                // 연장
                myRoomExtension.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Function function = new Function();
                        pauseTimer();
                        function.renew(userDto);
                    }
                });

                if (userDto.isReservationCheck()) {
                    info.setVisibility(View.VISIBLE);
                    myRoomReturn.setVisibility(View.VISIBLE);
                    myRoomExtension.setVisibility(View.VISIBLE);
                    noReservation.setVisibility(View.VISIBLE);
                    noReservation.setVisibility(View.INVISIBLE);

                } else {
                    info.setVisibility(View.INVISIBLE);
                    myRoomReturn.setVisibility(View.INVISIBLE);
                    myRoomExtension.setVisibility(View.INVISIBLE);
                    noReservation.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    public void startTimer(final UserDto userDto) {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                Function function = new Function();
                mTimeLeftInMillis = 0;
                timerCheck = false;
                function.returnSeat(userDto);
            }
        }.start();
        timerCheck = true;
    }

    public void pauseTimer() {
        mCountDownTimer.cancel();
        timerCheck = false;
    }

    public void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 3600000);
        int minutes = (int) (mTimeLeftInMillis % 3600000) / 60000;
        int seconds = (int) ((mTimeLeftInMillis % 3600000) % 60000) / 1000;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d 시간 %02d 분 %02d 초", hours, minutes, seconds);
        textRemainTime.setText(timeLeftFormatted);
    }


}