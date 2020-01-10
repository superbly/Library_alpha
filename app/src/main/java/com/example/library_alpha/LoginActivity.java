package com.example.library_alpha;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.library_alpha.user.SignUp;
import com.example.library_alpha.user.UserDto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText idEditText, passwordEditText;
    Button loginButton, signUpButton;
    Intent intent;

    public static boolean loginStatus = false;
    public static String loginId = "";
    public static UserDto userDto;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        idEditText = findViewById(R.id.editId);
        passwordEditText = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        // 로그인
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();
                String pw = passwordEditText.getText().toString();

                if (id.equals("") || pw.equals(""))
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                else
                    loginCheck(id, pw);
            }
        });


        // 회원가입
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

    }


    // 로그인 체크
    public void loginCheck(final String id, final String pw) {
        final Query query = databaseReference.child("User");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot datasnapshot) {

                if (datasnapshot.hasChild(id))
                    if (datasnapshot.child(id).child("pw").getValue().equals(pw)) {
                        Log.e("loginCheck : ", "로그인되었습니다.");
                        loginStatus = true;
                        loginId = id;
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else
                        Log.e("loginCheck : ", "비밀번호가 틀립니다.");
                else
                    Log.e("loginCheck : ", "해당 아이디가 존재하지 않습니다.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadUser:onCancelled", databaseError.toException());
            }
        });
    }
}
