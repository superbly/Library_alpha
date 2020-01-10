package com.example.library_alpha.readingRoom;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.library_alpha.R;
import com.example.library_alpha.module.SeatCount;
import com.example.library_alpha.user.UserDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.library_alpha.readingRoom.ReadingRoom1.totalSeat;


public class ReadingRoomList extends AppCompatActivity {

    Intent intent;
    ProgressBar reading_room1;
    TextView exist_seat1;
    LinearLayout list1;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("열람실 현황");
        setContentView(R.layout.activity_reading_room_list);

        list1 = findViewById(R.id.list1);
        realCount();

    }


    // 해당 열람실의 좌석수 현황 조회 메소드
    public void realCount() {
        Query query = databaseReference.child("seat_count").child("nowSeatCount");
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SeatCount seatCount = dataSnapshot.getValue(SeatCount.class);
                String realCount = seatCount.getNowSeatCount();
                totalSeat = Integer.parseInt(seatCount.getNowSeatCount());
                int realProgress = Integer.parseInt(realCount);

                reading_room1 = (ProgressBar) findViewById(R.id.readingRoom1);
                exist_seat1 = (TextView) findViewById(R.id.existSeat1);

                reading_room1.setProgress(realProgress);
                exist_seat1.setText(realCount);

                list1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getApplicationContext(), ReadingRoom1.class);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        });
    }


}
