package com.example.custodian.Adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.custodian.ManageGuardians;
import com.example.custodian.Modals.ContactsModal;
import com.example.custodian.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;

public class RegUsersAdapter extends RecyclerView.Adapter<RegUsersAdapter.ViewHolder> {

    // creating variables for context and array list.
    private final Context context;
    private ArrayList<ContactsModal> contactsModalArrayListTop;


    // creating a constructor
    public RegUsersAdapter(Context context, ArrayList<ContactsModal> contactsModalArrayListTop) {
        this.context = context;
        this.contactsModalArrayListTop = contactsModalArrayListTop;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RegUsersAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.contacts_card, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // getting data from array list in our modal.
        ContactsModal modal = contactsModalArrayListTop.get(position);
        // on below line we are setting data to our text view.
        holder.contactName.setText(modal.getName());
        holder.contactNumber.setText(modal.getPhone_number());

//        if(modal.isInvite()){
//            holder.txt_invite.setVisibility(View.GONE);
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference reference = database.getReference();
//        }
//        else{
        holder.txt_invite.setVisibility(View.GONE);
        // on below line we are adding on click listener to our item of recycler view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference ref = database.getReference("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()));

              //  ref.child("Guardians").setValue(modal.getPhone_number().toString());

                //send notification here
                Toast.makeText(context, "Guardian Added SuccessFully..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsModalArrayListTop.size();
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
