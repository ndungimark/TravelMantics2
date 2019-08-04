package com.example.travelmantics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TravealDeals extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText price;
    private EditText location, resort;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traveal_deals);

        // Displaying toolbar icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        location = (EditText) findViewById(R.id.Location);
        price = (EditText) findViewById(R.id.Price);
        resort = (EditText) findViewById(R.id.Resort);
        btnSave = (Button) findViewById(R.id.button);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'travelmantics2' node
        mFirebaseDatabase = mFirebaseInstance.getReference("travelmantics2-aef5d");

        // store app title to 'app_title' node
        mFirebaseInstance.getReference("app_title").setValue("TravelMantics");

        // app_title change listener
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated");

                String appTitle = dataSnapshot.getValue(String.class);

                // update toolbar title
                getSupportActionBar().setTitle(appTitle);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });

         btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myprice = price.getText().toString();
                String mylocation = location.getText().toString();
                String myResort = resort.getText().toString();
                // Check for already existed userId
                    creatDeal(mylocation, myprice,myResort);
            }
        });

        toggleButton();
    }

    // Changing button text
    private void toggleButton() {
        if (TextUtils.isEmpty(userId)) {
            btnSave.setText("Save dEAL");
        } else {
            btnSave.setText("Update Deal");
        }
    }

    private void creatDeal(String mylocation, String myprice,String myResort) {
        // TODO
        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        Deals deal = new Deals(mylocation, myprice,myResort);

        mFirebaseDatabase.child(userId).setValue(deal);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Deals deal = dataSnapshot.getValue(Deals.class);

                // Check for null
                if (deal == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + deal.resort + ", " + deal.price);
                toggleButton();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    }