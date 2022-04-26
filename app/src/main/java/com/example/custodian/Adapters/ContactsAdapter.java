package com.example.custodian.Adapters;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.custodian.MainActivity;
import com.example.custodian.ManageGuardians;
import com.example.custodian.Modals.ContactsModal;
import com.example.custodian.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    // creating variables for context and array list.
    private final Context context;
    private ArrayList<ContactsModal> contactsModalArrayList;

    // creating a constructor
    public ContactsAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayList) {
        this.context = context;
        this.contactsModalArrayList = contactsModalArrayList;
    }

//    // below method is use for filtering data in our array list
//    public void filterList(ArrayList<ContactsModal> filterllist) {
//        // on below line we are passing filtered
//        // array list in our original array list
//        contactsModalArrayList = filterllist;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.contacts_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.ViewHolder holder, int position) {
        // getting data from array list in our modal.
        ContactsModal modal = contactsModalArrayList.get(position);
        // on below line we are setting data to our text view.
        holder.contactName.setText(modal.getName());
        holder.contactNumber.setText(modal.getPhone_number());

//        if(modal.isInvite()){
//            holder.txt_invite.setVisibility(View.GONE);
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference reference = database.getReference();
//        }
//        else{
            holder.txt_invite.setVisibility(View.VISIBLE);
            // on below line we are adding on click listener to our item of recycler view.
            holder.txt_invite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String message = "Download custodian App please!!";

                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(modal.getPhone_number(), null, message, null, null);

                        Toast.makeText(context, "Message Sent",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception ex) {
                        Toast.makeText(context,ex.getMessage().toString(),
                                Toast.LENGTH_LONG).show();
                        ex.printStackTrace();
                    }

//                if(checkIfUser(modal.getPhone_number())){
//                    sendRequest();
//                }
                    //Getting intent and PendingIntent instance
//                Intent intent=new Intent(context, ManageGuardians.class);
//                PendingIntent pi=PendingIntent.getActivity(context, 0, intent,0);
//
//                //Get the SmsManager instance and call the sendTextMessage method to send message
//                SmsManager sms=SmsManager.getDefault();
//                sms.sendTextMessage(modal.getPhone_number(), null, "Download Custodian app!! please", pi,null);
//
//                    Toast.makeText(context, "Message Sent successfully!",
//                            Toast.LENGTH_LONG).show();
                }
            });
        //}

    }

//    private boolean checkIfUser(String phone) {
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("Users");
//    }

    @Override
    public int getItemCount() {
        return contactsModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // on below line creating a variable
        // for our image view and text view.
        private final TextView contactName;
        private final TextView contactNumber;
        private final TextView txt_invite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our image view and text view.
            contactName = itemView.findViewById(R.id.contactName);
            contactNumber = itemView.findViewById(R.id.contactNumber);
            txt_invite = itemView.findViewById(R.id.txt_invite);
        }


    }
}
