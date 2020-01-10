package com.example.library_alpha.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.library_alpha.LoginActivity;
import com.example.library_alpha.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText inputId, inputPassword, inputName;
    Button btnSignUp;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("회원가입");

        inputId = (EditText)findViewById(R.id.inputId);
        inputPassword = (EditText)findViewById(R.id.inputPassword);
        inputName = (EditText)findViewById(R.id.inputName);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String id = inputId.getText().toString();
                String pw = inputPassword.getText().toString();
                String name = inputName.getText().toString();
                UserDto userDTO = new UserDto(id, pw, name);
                signUP(id, pw, name);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // 회원가입
    private void signUP(String id, String pw, String name) {
        String key = databaseReference.child("User").push().getKey();
        UserDto userDTO = new UserDto(id, pw, name);
        Map<String, Object> postValues = userDTO.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/User/" + id, postValues);
        databaseReference.updateChildren(childUpdates);
    }
}

