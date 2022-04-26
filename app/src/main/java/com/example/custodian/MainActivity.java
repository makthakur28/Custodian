package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    boolean isPermissionGranted;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    ImageButton menu, locationBtn,notification_btn;
    Button watchbtn,emergencyBtn;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    LatLng latLng;
    LinearLayout GuardianLL;
    private TextView WelcomeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        checkMyPermission();

        watchbtn = findViewById(R.id.watch_over_btn);
        emergencyBtn = findViewById(R.id.Emergency_btn);
        menu = findViewById(R.id.menu_Avatar);
        locationBtn = findViewById(R.id.my_location_btn);
        notification_btn = findViewById(R.id.notification_btn);
        GuardianLL = findViewById(R.id.GuardLL);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        //firebase stuff
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        String phone = getIntent().getStringExtra("phone");
        //reference = database.getReference("Users");

        //permissions
        Dexter.withContext(this).withPermission(Manifest.permission.SEND_SMS).withListener(new PermissionListener() {

            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1002);
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();


        watchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// the message
                String message = "your ward is feeling insecure , keep a watch!!  click on link to check: ";
                String Url = "https://maps.google.com/?q="+latLng.latitude+","+latLng.longitude;

                String finalMsg = message+Url;

                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.SEND_SMS).withListener(new PermissionListener() {

                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Sending request for Watch OVER", Toast.LENGTH_SHORT).show();
                        sendSms(finalMsg);

                        isPermissionGranted = true;
                        //showMap();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                1002);
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

// the phone numbers we want to send to

            }
        });

        emergencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

// the message
                String message = "your ward is in Danger...and need someone !!  click on link to get Direction: ";
                String Url = "https://maps.google.com/?q="+latLng.latitude+","+latLng.longitude;

                String finalMsg = message+Url;

// the phone numbers we want to send to
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.SEND_SMS).withListener(new PermissionListener() {

                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Emergency SOS Sent!", Toast.LENGTH_SHORT).show();
                        sendSms(finalMsg);

                        isPermissionGranted = true;
                        //showMap();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.SEND_SMS},
                                1002);
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

//                try {
//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//                    Toast.makeText(getApplicationContext(), "Message Sent",
//                            Toast.LENGTH_LONG).show();
//                } catch (Exception ex) {
//                    Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
//                            Toast.LENGTH_LONG).show();
//                    ex.printStackTrace();
//                }
            }
        });

        GuardianLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                WelcomeUser = navigationView.findViewById(R.id.Welcome_Note);
//                reference.child(phone).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        String getname = task.getResult().toString();
//                        WelcomeUser.setText(getname);
//                    }
//                });
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });

        notification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(intent);
            }
        });

    }

    private void sendSms(String finalMsg) {

        String[] numbers = {"+919174625434", "+919685260430","+919617769978","+918966974652","+917415525404"};

//                for(String number : numbers) {
//                    sms.sendTextMessage(number, null, finalMsg, null, null);
//                }

        try {
            SmsManager smsManager = SmsManager.getDefault();
            for (String number:
                    numbers) {
                smsManager.sendTextMessage(number, null, finalMsg, null, null);
            }

            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void showDialog() {
         final Dialog dialog = new Dialog(this);
         ImageButton callmebtn,getdirectionsbtn,getlocationbtn,msgmebtn;


         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setContentView(R.layout.bottomsheetlayout);

        callmebtn = dialog.findViewById(R.id.callmenow);
        getdirectionsbtn = dialog.findViewById(R.id.getDirections);
        getlocationbtn = dialog.findViewById(R.id.getlocation);
        msgmebtn = dialog.findViewById(R.id.msg_now);
         dialog.show();
         dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
         dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
         dialog.getWindow().setGravity(Gravity.BOTTOM);

         callmebtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                     ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                 } else {
                     String phoneNo = "tel:" + "9174625434";
                     Intent intent = new Intent(Intent.ACTION_CALL);
                     intent.setData(Uri.parse(phoneNo));
                     startActivity(intent);
                 }
             }
         });
    }


    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {

            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                getMyLocation();

                isPermissionGranted = true;
                //showMap();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1001);
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void getMyLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        if (location!=null) {

                            latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are Here...").icon(BitmapFromVector(getApplicationContext(), R.drawable.marker_pin));

                            googleMap.addMarker(markerOptions);
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            
                        }
                    }
                });
            }
        });
    }


    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.edit_profile:
                startActivity(new Intent(MainActivity.this, EditProfile.class));
                finish();
                break;
            case R.id.manage:
                startActivity(new Intent(MainActivity.this, ManageGuardians.class));
                finish();
                break;
            case R.id.About:
                startActivity(new Intent(MainActivity.this, AboutUs.class));
                finish();
                break;
            case R.id.get_help:
                startActivity(new Intent(MainActivity.this, GetHelp.class));
                finish();
                break;
            case R.id.log_out:
                startActivity(new Intent(MainActivity.this, LoginUser.class));
                mAuth.signOut();
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}