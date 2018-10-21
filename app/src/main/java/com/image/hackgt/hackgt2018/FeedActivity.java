package com.image.hackgt.hackgt2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button uploadButton;
    private Button statsButton;
    private TextView emailTextView;

    private static final String TAG = "####MAIN PAGE ACTIVITY";

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        logoutButton = findViewById(R.id.button_logout);
        uploadButton = findViewById(R.id.button_upload);
        statsButton = findViewById(R.id.button_stats);
        emailTextView = findViewById(R.id.textView_email);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        setupUserInfo();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutPressed();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPressed();
            }
        });
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statsPressed();
            }
        });
    }

    private void statsPressed() {
        Intent intent = new Intent(FeedActivity.this,
                StatsActivity.class);
        startActivity(intent);
    }

    private void uploadPressed() {
        Intent intent = new Intent(FeedActivity.this,
                UpdateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        logoutPressed();
    }

    private void logoutPressed() {
        mAuth.signOut();
        user = null;
        Toast.makeText(FeedActivity.this, "Logged out!", Toast.LENGTH_SHORT)
                .show();
        Intent intent = new Intent(FeedActivity.this,
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setupUserInfo() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String UID = mAuth.getCurrentUser().getUid();
                    user = dataSnapshot.child("users").child(UID).getValue(User.class);
                    emailTextView.setText(user.getName());
                } catch (NullPointerException e) {
                    user = null;
                    emailTextView.setText("No User Detected");
                }
                myRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
