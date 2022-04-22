package com.example.custodian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.custodian.Modals.UserModal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User_Details extends AppCompatActivity {

    TextInputEditText edtDOB,edtPhone,edtName;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        edtName = findViewById(R.id.edtxt_name);
        edtDOB = findViewById(R.id.edtxt_dob);
        edtPhone = findViewById(R.id.edtxt_phone);

        continueBtn = findViewById(R.id.btn_continue);

        String Phone = getIntent().getStringExtra("mobile");

        edtPhone.setText(Phone);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name =edtName.getText().toString().trim();
                String  DOB = edtDOB.getText().toString().trim();
                //String  phone = edtPhone.getText().toString().trim();

                if(TextUtils.isEmpty(DOB)){
                    edtDOB.setError("Email is required!");
                    return;
                }
                if(TextUtils.isEmpty(Name)){
                    edtName.setError("Name is required!");
                    return;
                }
//                if(TextUtils.isEmpty(phone)){
//                    edtPhone.setError("Name is required!");
//                    return;
//                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Phone);

                UserModal user = new UserModal(Name,Phone,DOB);
                databaseReference.setValue(user);
                startActivity(new Intent(User_Details.this,OtpVerification.class));
            }
        });


    }
}