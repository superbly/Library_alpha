package com.example.library_alpha.readingRoom;

import android.content.Context;

import android.net.IpSecManager;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.library_alpha.R;
import com.example.library_alpha.user.UserDto;
import com.example.library_alpha.util.TimeConvert;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;


import static com.example.library_alpha.R.drawable.boxholder_seat;
import static com.example.library_alpha.R.drawable.normal_seat;
import static com.example.library_alpha.LoginActivity.loginId;
import static com.example.library_alpha.LoginActivity.loginStatus;
import static com.example.library_alpha.R.drawable.reservation_seat;


public class CountAdapter extends RecyclerView.Adapter<CountAdapter.MyViewHolder> {

    List<SeatDto> seatDto;
    Context context;
    SeatDto seatDto1;
    UserDto userDto;
    ReadingRoom1 room1 = new ReadingRoom1();

    public static long timeValue;
    public static CountDownTimer mCountDownTimer1;

    boolean timerCheck = false;
    long mTimeLeftInMillis1;

    final int roomNum = 1;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    public CountAdapter(Context c, List<SeatDto> seatDto) {
        this.seatDto = seatDto;
        this.context = c;
    }


    @Override
    public CountAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_custom, viewGroup, false);
        return new MyViewHolder(v);
    }


    @Override
    public synchronized void onBindViewHolder(final CountAdapter.MyViewHolder myViewHolder, final int position) {

        if (loginStatus)
            userReservationCheck(loginId);

        final int seatNum = seatDto.get(position).getSeatNum();
        final String seatUserId = seatDto.get(position).getUsedId();
        final boolean seatCheck = seatDto.get(position).isSeatCheck();

        myViewHolder.textview.setText("" + seatNum);

        if (!seatCheck)
            myViewHolder.textview.setBackgroundResource(normal_seat);

        if (position % 4 == 0 && !seatCheck)
            myViewHolder.textview.setBackgroundResource(boxholder_seat);

        if (seatCheck) {
            myViewHolder.textview.setBackgroundResource(reservation_seat);

        }


        myViewHolder.textview.setOnClickListener(new View.OnClickListener() {   // 좌석 클릭시 이벤트

            @Override
            public synchronized void onClick(View v) {   // 좌석 클릭시 이벤트
                Log.e("test", Integer.toString(position));

                if (!loginStatus)

                    Toast.makeText(context, "좌석을 예약하려면 로그인 해주세요.", Toast.LENGTH_SHORT).show();

                else {

                    userReservationCheck(loginId);

                    if (!seatCheck) { // 선택한 좌석이 비어 있있다면

                        if (userDto.isReservationCheck()) {   // 사용자가 다른 자리에 이미 예약되어 있다면

                            room1.moveDialog(position, roomNum, userDto, seatDto);   // 자리 이동 다이얼로그 호출

                        } else

                            room1.reservationDialog(position, seatDto, roomNum, userDto);    // 자리 예약 다이얼로그 호출

                    } else {

                        if (userDto.getSeatNum() == (position + 1))     // 선택한 자리가 본인 자리라면

                            room1.returnRenewDialog(seatDto, userDto);                 // 자리 반환 다이얼로그 호출

                        else

                            Toast.makeText(context, "이미 예약되어 있는 자리 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return this.seatDto.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textview;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            textview = (TextView) itemView.findViewById(R.id.textview);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pb);
        }
    }


    public synchronized void userReservationCheck(String id) {
        Log.d("DBUG", "check");

        Query query = databaseReference.child("User").child(id);
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDto = dataSnapshot.getValue(UserDto.class);
                Log.d("DBUG", "success");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DBUG", "cancelled");
            }
        });
    }

    public  void userReservationCheck1(final int position) {

        Log.d("input id", Integer.toString(position));

        Query query = databaseReference.child("Room").child(Integer.toString(position + 1) + "seat");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("DBUG1", "success");
                     seatDto1 = dataSnapshot.getValue(SeatDto.class);
                    Log.e("시간", "dddddd"+seatDto1.getRemainTime());
                    TimeConvert timeConvert = new TimeConvert(seatDto1.getRemainTime());
                    timeValue = timeConvert.getDifferent();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public  void userReservationCheck3(final String id) {

        Log.d("input id", id);

        Query query = databaseReference.child("User").child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDto = dataSnapshot.getValue(UserDto.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public String updateCountDownText(Long mTimeLeftInMillis1) {
        int hours = (int) (mTimeLeftInMillis1 / 3600000);
        int minutes = (int) (mTimeLeftInMillis1 % 3600000) / 60000;
        int seconds = (int) ((mTimeLeftInMillis1 % 3600000) % 60000) / 1000;

        String timeLeftFormatted1 = String.format(Locale.getDefault(), "%02d : %02d", hours, minutes);
        Log.e("TimerTest", timeLeftFormatted1);
        return timeLeftFormatted1;
    }

}

