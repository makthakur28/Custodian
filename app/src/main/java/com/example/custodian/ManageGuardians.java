package com.example.custodian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.custodian.Adapters.ContactsAdapter;
import com.example.custodian.Adapters.RegUsersAdapter;
import com.example.custodian.Modals.ContactsModal;
import com.example.custodian.Modals.UserModal;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class ManageGuardians extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView contactsRV,RegUserRV;
    ImageButton menu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    ContactsAdapter contactRVAdapter;
    ProgressBar progressBar;
    RegUsersAdapter RegAdapter;
    ArrayList<ContactsModal> contactsModalArrayList;
    ArrayList<ContactsModal> contactsModalArrayListTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_guardians);
        contactsRV = findViewById(R.id.contactsList);
        RegUserRV = findViewById(R.id.RegUserList);
        menu = findViewById(R.id.menu_btn);
        progressBar = findViewById(R.id.idPBLoading);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        contactsModalArrayList = new ArrayList<>();
        contactsModalArrayListTop = new ArrayList<>();

        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        // calling method to prepare our recycler view.
        prepareContactRV();

        // calling a method to request permissions.
        requestPermissions();

        //add new recycler to fetch users from firebase
        //prepareRegUserRV();

    }

    private void getUserDetails(ContactsModal newUser) {
        // in this method we are preparing our recycler view with adapter.
        RegAdapter = new RegUsersAdapter(this, contactsModalArrayListTop);

        // on below line we are setting layout manager.
        RegUserRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users");
        Query query = reference.orderByChild("phoneNumber").equalTo(newUser.getPhone_number());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String  phone = "",
                            name = "";
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        if(childSnapshot.child("phone").getValue()!=null)
                            phone = childSnapshot.child("phone").getValue().toString();
                        if(childSnapshot.child("name").getValue()!=null)
                            name = childSnapshot.child("name").getValue().toString();


                        ContactsModal mUser = new ContactsModal(name, phone,true);
                        if (name.equals(phone))
                            for(ContactsModal mContactIterator : contactsModalArrayList){
                                if(mContactIterator.getPhone_number().equals(mUser.getPhone_number())){
                                    mUser.setName(mContactIterator.getName());
                                }
                            }

                        contactsModalArrayListTop.add(mUser);
                        RegAdapter.notifyDataSetChanged();
                        return;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        for (ContactsModal user:contactsModalArrayList
//             ) {
//
//        }

        // on below line we are setting adapter to our recycler view.
        RegUserRV.setAdapter(RegAdapter);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // in this on create options menu we are calling
//        // a menu inflater and inflating our menu file.
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_search, menu);
//        // on below line we are getting our menu item as search view item
//        MenuItem searchViewItem = menu.findItem(R.id.app_bar_search);
//        // on below line we are creating a variable for our search view.
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
//        // on below line we are setting on query text listener for our search view.
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // on query submit we are clearing the focus for our search view.
//                searchView.clearFocus();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // on changing the text in our search view we are calling
//                // a filter method to filter our array list.
//                filter(newText.toLowerCase());
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }

//    private void filter(String text) {
//        // in this method we are filtering our array list.
//        // on below line we are creating a new filtered array list.
//        ArrayList<ContactsModal> filteredlist = new ArrayList<>();
//        // on below line we are running a loop for checking if the item is present in array list.
//        for (ContactsModal item : contactsModalArrayList) {
//            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
//                // on below line we are adding item to our filtered array list.
//                filteredlist.add(item);
//            }
//        }
//        // on below line we are checking if the filtered list is empty or not.
//        if (filteredlist.isEmpty()) {
//            Toast.makeText(this, "No Contact Found", Toast.LENGTH_SHORT).show();
//        } else {
//            // passing this filtered list to our adapter with filter list method.
//            contactRVAdapter.filterList(filteredlist);
//        }
//    }

    private void requestPermissions() {
        Dexter.withContext(this)
                // below line is use to request the number of
                // permissions which are required in our app.
                .withPermissions(Manifest.permission.READ_CONTACTS,
                        // below is the list of permissions
                        Manifest.permission.SEND_SMS)
                // after adding permissions we are
                // calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do you work now
                            getContacts();
                            Toast.makeText(ManageGuardians.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently,
                            // we will show user a dialog message.
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // this method is called when user grants some
                        // permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError dexterError) {
                // we are displaying a toast message for error message.
                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        })
                // below line is use to run the permissions
                // on same thread and to check the permissions
                .onSameThread().check();
    }

    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(ManageGuardians.this);

        // below line is the title
        // for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called on click on positive
                // button and on clicking shit button we
                // are redirecting our user from our app to the
                // settings page of our app.
                dialog.cancel();
                // below is the intent from which we
                // are redirecting our user.
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called when
                // user click on negative button.
                dialog.cancel();
            }
        });
        // below line is used
        // to display our dialog
        builder.show();
    }

    @SuppressLint("Range")
    private void getContacts() {

        progressBar.setVisibility(View.VISIBLE);
        // this method is use to read contact from users device.
        // on below line we are creating a string variables for
        // our contact id and display name.
        String contactId = "";
        String displayName = "";
        // on below line we are calling our content resolver for getting contacts
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        // on blow line we are checking the count for our cursor.
        if (cursor.getCount() > 0) {
            // if the count is greater than 0 then we are running a loop to move our cursor to next.
            while (cursor.moveToNext()) {
                // on below line we are getting the phone number.
                @SuppressLint("Range") int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    // we are checking if the has phone number is > 0
                    // on below line we are getting our contact id and user name for that contact
                    contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // on below line we are calling a content resolver and making a query
                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{contactId},
                            null);
                    // on below line we are moving our cursor to next position.
                    if (phoneCursor.moveToNext()) {
                        // on below line we are getting the phone number for our users and then adding the name along with phone number in array list.
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //Log.d("check", "getContacts: "+displayName+" : "+phoneNumber);
                        ContactsModal newUser = new ContactsModal(displayName, phoneNumber,false);
                        contactsModalArrayList.add(newUser);
                        getUserDetails(newUser);
                    }
                    // on below line we are closing our phone cursor.
                    phoneCursor.close();
                }
            }
        }
        // on below line we are closing our cursor.
        cursor.close();
        // on below line we are hiding our progress bar and notifying our adapter class.
        progressBar.setVisibility(View.GONE);
        contactRVAdapter.notifyDataSetChanged();
    }

    private boolean[] userExist(String phoneNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users").child(phoneNumber);
        final boolean[] check = {false};
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    check[0] =true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return check;
    }

    private void prepareContactRV() {
        // in this method we are preparing our recycler view with adapter.

        contactRVAdapter = new ContactsAdapter(this, contactsModalArrayList);

        // on below line we are setting layout manager.
        contactsRV.setLayoutManager(new LinearLayoutManager(this));
        // on below line we are setting adapter to our recycler view.
        contactsRV.setAdapter(contactRVAdapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(ManageGuardians.this, MainActivity.class));
                finish();
                break;
            case R.id.edit_profile:
                startActivity(new Intent(ManageGuardians.this, EditProfile.class));
                finish();
                break;
            case R.id.manage:
                startActivity(new Intent(ManageGuardians.this, ManageGuardians.class));
                finish();
                break;
            case R.id.About:
                startActivity(new Intent(ManageGuardians.this, AboutUs.class));
                finish();
                break;
            case R.id.get_help:
                startActivity(new Intent(ManageGuardians.this, GetHelp.class));
                finish();
                break;
            case R.id.log_out:
                startActivity(new Intent(ManageGuardians.this, LoginUser.class));
                mAuth.signOut();
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}