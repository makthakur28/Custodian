package com.example.custodian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.custodian.Modals.UserModal;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class RegisterUser extends AppCompatActivity {

    TextInputEditText enterphone, enterName, enterPass, confirmPass;
    Button register;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        enterphone = findViewById(R.id.ETPhoneNumber);
        enterName = findViewById(R.id.edtxt_name);
        enterPass = findViewById(R.id.edtxt_password);
        confirmPass = findViewById(R.id.edtxt_confirmPass);
        register = findViewById(R.id.register_btn);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });


    }

    public boolean validateName() {
        String Name = enterName.getText().toString();

        if (Name.isEmpty()) {
            enterName.setError("Field cannot be null ");
            return false;
        } else {
            enterName.setError(null);
            return true;
        }
    }

    public boolean validatePass() {
        String Password = enterPass.getText().toString().trim();
        String PassFormat = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";

        if (Password.isEmpty()) {
            enterPass.setError("Field cannot be null ");
            return false;
        } else if (!Password.matches(PassFormat)) {
            enterPass.setError("Password is too weak!");
            return false;
        } else {
            enterPass.setError(null);
            return true;
        }
    }

    public boolean validatePhone() {
        String Phone = String.valueOf(enterphone.getText());
        if (Phone.isEmpty()) {
            enterphone.setError("Field cannot be null ");
            return false;
        } else {
            enterphone.setError(null);
            return true;
        }
    }

    public boolean validateConfPass() {
        String ConPass = confirmPass.getText().toString();
        String Pass = enterPass.getText().toString();

        if (!ConPass.equals(Pass)) {
            confirmPass.setError("Password not matched");
            return false;
        } else {
            confirmPass.setError(null);
            return true;
        }
    }


    private void registerUser() {

        if (!validateConfPass() | !validateName() | !validatePass() | !validatePhone()) return;

        String mobileNo = enterphone.getText().toString().trim();
        String Name = enterName.getText().toString().trim();
        String Password = enterPass.getText().toString().trim();

        Intent i = new Intent(RegisterUser.this, OtpVerification.class);
        i.putExtra("phone", mobileNo);
        i.putExtra("name", Name);
        i.putExtra("password", Password);
        startActivity(i);
        finish();

    }

}