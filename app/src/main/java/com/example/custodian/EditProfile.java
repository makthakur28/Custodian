package com.example.custodian;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.custodian.Modals.UserModal;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageButton menu;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    FirebaseUser user;
    DatabaseReference reference;

    TextInputEditText edtPhone , edtPass , edtName;
    Button update;
    private String user_password,user_phone,user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        menu = findViewById(R.id.menu_btn);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        edtName = (TextInputEditText) findViewById(R.id.edtxt_name);
        edtPass = (TextInputEditText)findViewById(R.id.edtxt_password);
        edtPhone = (TextInputEditText)findViewById(R.id.edtxt_phone);
        update = findViewById(R.id.update_btn);

        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //reference = FirebaseDatabase.getInstance().getReference("Users").child(Objects.requireNonNull(user.getPhoneNumber()));

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UserModal user01 = snapshot.getValue(UserModal.class);
//                if (user01 != null) {
//                    user_name = user01.getName();
//                    user_phone = user01.getPhoneNumber();
//                    user_password = user01.getPassword();
//                }
//
//
//                edtName.setText(user_name);
//                edtPass.setText(user_password);
//                edtPhone.setText(user_phone);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        fullName.setText(user_name);
//        username.setText(user_username);



//        update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                UpdateDB();
//            }
//        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void UpdateDB() {

        if(isNameChanged() || isPhoneChanged() || isPassChanged()){
            Toast.makeText(this, "Data is updated !! ", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Data is same , Cant Update", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean isPassChanged() {
        String new_password = edtPass.getText().toString();
        if (!user_password.equals(new_password)){
            reference.child("password").setValue(new_password);
            user_password= new_password;
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isPhoneChanged() {
        String new_phone = edtPhone.getText().toString();
        if (!user_phone.equals(new_phone)){
            reference.child("phoneNumber").setValue(new_phone);
            user_phone=new_phone;
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isNameChanged() {
        String new_name = edtName.getText().toString();
        if (!user_name.equals(new_name)){
            reference.child("name").setValue(new_name);
//            fullName.setText(new_name);
            user_name = new_name;
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(EditProfile.this, MainActivity.class));
                finish();
                break;
            case R.id.edit_profile:
                startActivity(new Intent(EditProfile.this, EditProfile.class));
                finish();
                break;
            case R.id.manage:
                startActivity(new Intent(EditProfile.this, ManageGuardians.class));
                finish();
                break;
            case R.id.About:
                startActivity(new Intent(EditProfile.this, AboutUs.class));
                finish();
                break;
            case R.id.get_help:
                startActivity(new Intent(EditProfile.this, GetHelp.class));
                finish();
                break;
            case R.id.log_out:
                startActivity(new Intent(EditProfile.this, LoginUser.class));
                mAuth.signOut();
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}