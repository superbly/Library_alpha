package com.example.library_alpha;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.library_alpha.module.SeatCount;
import com.example.library_alpha.readingRoom.ReadingRoomList;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import com.example.library_alpha.user.MyInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static com.example.library_alpha.LoginActivity.loginId;
import static com.example.library_alpha.LoginActivity.loginStatus;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    Button main_reading_room_select_button, main_my_room_button, main_login_button, main_logout_button;
    TextView loginText;
    Intent intent;
    ImageView img;
    ViewFlipper v_flipper;

    public static boolean test1 = false;

    int images[] = {R.drawable.image1, R.drawable.image2, R.drawable.image3};


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onBackPressed();

        main_reading_room_select_button = findViewById(R.id.mainReadingRoomSelectButton);
        main_my_room_button = findViewById(R.id.mainMyReadingRoomButton);
        main_login_button = findViewById(R.id.mainLoginButton);
        main_logout_button = findViewById(R.id.mainLogoutButton);
        loginText = (TextView)findViewById(R.id.loginText);
        img = findViewById(R.id.imageView1);
        v_flipper = findViewById(R.id.image_slide);

        if(loginStatus){
            loginText.setText("로그아웃");

            // 바코드
            String strBarcode = loginId + 1;
            Bitmap barcode = createBarcode(strBarcode);
            img.setImageBitmap(barcode);
            img.invalidate();
        }

        //seatCountDbSet();

        // 이미지 슬라이드
        for (int image : images) {
            flipperImages(image);
        }

        // 열람실조회 클릭했을 때
        main_reading_room_select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), ReadingRoomList.class);
                startActivity(intent);
            }
        });

        // 내 열람실 정보 클릭했을 때
        main_my_room_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginStatus) {
                    intent = new Intent(getApplicationContext(), MyInfo.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "로그인 해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 로그인 클릭했을 때
        main_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginStatus) { // 로그인이 되어있으면
                    loginStatus = false;
                    loginId = "";
                    loginText.setText("로그인");
                    Toast.makeText(getApplicationContext(),"로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

                } else {
                    intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 방지
    }

    // 이미지 슬라이드
    public void flipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);       // 이미지 추가
        v_flipper.setFlipInterval(3000);    // 이미지 딜레이 시간
        v_flipper.setAutoStart(true);       // 자동시작 유무
        v_flipper.setInAnimation(this, android.R.anim.slide_in_left);
    }


    // DB 좌석수 정보 셋
    public void seatCountDbSet() {
        SeatCount seatCount = new SeatCount(Integer.toString(72));
        Map<String, Object> postValues = seatCount.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/seat_count/" + "nowSeatCount", postValues);
        databaseReference.updateChildren(childUpdates);
    }


    // 바코드 생성
    private Bitmap createBarcode(String code) {
        Bitmap bitmap = null;
        MultiFormatWriter gen = new MultiFormatWriter();

        try {
            final int WIDTH = 840;
            final int HEIGHT = 160;

            BitMatrix bytemap = gen.encode(code, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
            bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);

            for (int i = 0; i < WIDTH; ++i) {
                for (int j = 0; j < HEIGHT; ++j)
                    bitmap.setPixel(i, j, bytemap.get(i, j) ? Color.BLACK : Color.WHITE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }


}
