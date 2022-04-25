package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginUser extends AppCompatActivity {

    Button Login,register;
    TextInputEditText edtPhone,edtPass;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference ref;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        Login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_btn);
        edtPhone = findViewById(R.id.edtxt_phone);
        edtPass = findViewById(R.id.edtxt_password);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        ref = database.getReference();

        if (user!=null){
            startActivity(new Intent(LoginUser.this,MainActivity.class));
            finish();
        }
        else{
            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LoginNow();
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(LoginUser.this,RegisterUser.class));
                }
            });
        }


    }

    public boolean ValidateUser(){
        String Phone = edtPhone.getText().toString();

        if (Phone.isEmpty()){
            edtPhone.setError("Field cannot be empty");
            return false;
        }
        else{
            edtPhone.setError(null);
            return true;
        }
    }
    public boolean ValidatePass(){
        String pass = edtPass.getText().toString();

        if (pass.isEmpty()){
            edtPass.setError("Field cannot be empty");
            return false;
        }
        else{
            edtPass.setError(null);
            return true;
        }
    }
    private void LoginNow() {
        if(!ValidateUser() | !ValidatePass()){
            return;
        }
        else{
            isUser();
        }
    }

    private void isUser() {

        String userEnteredPhone= "+91"+edtPhone.getText().toString().trim();
        String userEnteredPassword= edtPass.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userEnteredPhone);

        //Query checkUser = reference.orderByChild("phoneNumber").equalTo(userEnteredPhone);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String passwordFromDB = snapshot.child("password").getValue(String.class);

                    if (userEnteredPassword.equals(passwordFromDB)){
                        String name = snapshot.child("name").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String pass = snapshot.child("password").getValue(String.class);

                        Intent intent = new Intent(LoginUser.this,MainActivity.class);
                        intent.putExtra("name",name);
                        intent.putExtra("phone",phone);
                        intent.putExtra("password",pass);
                        startActivity(intent);
                    }
                    else{
                        edtPass.setError("Wrong password !");
                        edtPass.requestFocus();
                    }

                }
                else{
                    edtPhone.setError("No such user exist !");
                    edtPhone.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}