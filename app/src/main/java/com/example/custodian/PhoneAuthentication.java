package com.example.custodian;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class PhoneAuthentication extends AppCompatActivity {

    EditText enterphone;
    Button login;
    CountryCodePicker ccp;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        enterphone = findViewById(R.id.ETPhoneNumber);
        login = findViewById(R.id.login_btn);
        ccp = findViewById(R.id.ccp);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();

        if (user!=null){
            startActivity(new Intent(PhoneAuthentication.this,MainActivity.class));
            finish();
        }
        else{
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("button clicked");
                    Toast.makeText(PhoneAuthentication.this, "button clicked", Toast.LENGTH_SHORT).show();
                    String phone_number = (ccp.getSelectedCountryCodeWithPlus()+enterphone.getText()).toString().trim();

                    if (phone_number.isEmpty()) {
                        enterphone.setError("Phone Number is required");
                        enterphone.requestFocus();
                        return;
                    }
//                    if (phone_number.length()!=13) {
//                        enterphone.setError("Enter a valid Phone Number");
//                        enterphone.requestFocus();
//                        return;
//                    }

                    DatabaseReference databaseReference = database.getReference("Users").child(phone_number);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            System.out.println("something is wrong");
                            if(!snapshot.exists()){
                                Intent intent = new Intent(PhoneAuthentication.this, User_Details.class);
                                intent.putExtra("mobile", phone_number);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(PhoneAuthentication.this, OtpVerification.class);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });
        }


    }

}