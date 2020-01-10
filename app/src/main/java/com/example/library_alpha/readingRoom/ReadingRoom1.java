package com.example.library_alpha.readingRoom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadingRoom1 extends AppCompatActivity {

    long now;
    Date date;
    RecyclerView recyclerview;
    GridLayoutManager layout;
    List<SeatDto> count = new ArrayList<SeatDto>();
    static boolean check = false;
    static boolean flag = true;
    public static int totalSeat = 0;
    Function function = new Function();
    SeatDto test;
    static Context context;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_reading_room1);

        setTitle("일반 열람실1");

        context = this;

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        layout = new GridLayoutManager(this, 4);

        recyclerview.setHasFixedSize(true);

        recyclerview.setLayoutManager(layout);

        count = new ArrayList<>();

        seatSet();

        //dbCreate();



    }

    protected void onPause(){
        super.onPause();
        flag = false;
        Log.e("pauseTest", "test");
    }


    private String reservationTime() {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");
        now = System.currentTimeMillis();
        date = new Date(now);
        return format2.format(date);
    }


    private String displayTime() {
        SimpleDateFormat format = new SimpleDateFormat("조회일자 : yyyy년 MM월 dd일 \n현재시간 : HH시 mm분 ss초");
        now = System.currentTimeMillis();
        date = new Date(now);
        return format.format(date);
    }


    // 예약 다이얼로그 메소드
    public void reservationDialog(final int position, final List<SeatDto> seatDto, final int roomNum, final UserDto userDto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("예약(현재좌석 : " + (position + 1) + "번)");
        builder.setMessage(displayTime() + "\n\n예약하시겠습니까?").setCancelable(false).setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                function.reservationSeat(position, roomNum, userDto, seatDto, reservationTime());
                Toast.makeText(context, (position + 1) + "번 자리가 예약되었습니다.", Toast.LENGTH_SHORT).show();
                TimeConvert timeConvert = new TimeConvert(userDto.getRemainTime());
                Long timeValue = timeConvert.getDifferent();
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "취소", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // 이동 다이얼로그 메소드
    public void moveDialog(final int position, final int roomNum, final UserDto userDto, final List<SeatDto> seatDto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("이동(변경좌석 : " + (position + 1) + "번)");
        builder.setMessage(displayTime() + "\n\n이동하시겠습니까?").setCancelable(false).setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                function.moveSeat(position, roomNum, userDto, seatDto);
                Toast.makeText(context, "이동 완료", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "이동 취소", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // 반납 연장 다이얼로그 메소드
    public void returnRenewDialog(final List<SeatDto> seatDto, final UserDto userDto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("반납/연장");
        builder.setMessage(displayTime() + "\n\n반납/연장하시겠습니까?");
        builder.setPositiveButton("연장", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                function.renew(userDto);
                Toast.makeText(context, "연장 완료", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("반납", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, userDto.getSeatNum() + "번 자리가 반납되었습니다.", Toast.LENGTH_SHORT).show();
                function.returnSeat(userDto);

            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "취소되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // 현재 화면이 새로 호출될 때마다 좌석을 새로 그려주는 메소드
    private void seatSet() {

        for (int i = 1; i <= 72; i++) {
            Query query = databaseReference.child("Room").child(Integer.toString(i) + "seat");
            query.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    check = false;
                    test = dataSnapshot.getValue(SeatDto.class);
                    SeatDto seatDto = test;

                    if (count.size() >= 72)
                        count.set(test.getSeatNum() - 1, seatDto);
                    else
                        count.add(seatDto);

                    Log.e("seatTest", Integer.toString(test.getSeatNum()));
                    recyclerview.setAdapter(new CountAdapter(getApplication(), count));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("loadPost:onCancelled", databaseError.toException());
                }

            });

        }

    }


    // DB 좌석 생성기
    private void dbCreate() {
        String key = databaseReference.child("Room").push().getKey();

        for (int i = 1; i <= 72; i++) {     // 좌석 수
            SeatDto seatDto = new SeatDto(i);
            Map<String, Object> postValues = seatDto.toMap();
            Map<String, Object> seatUpdates = new HashMap<>();
            seatUpdates.put("/Room/" + i + "seat", postValues);
            databaseReference.updateChildren(seatUpdates);
        }
    }

}
