package com.image.hackgt.hackgt2018;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    private Button logoutButton;
    private Button uploadButton;
    private Button statsButton;
    private Button nextButton;
    private TextView emailTextView;

    ImageView imageView;

    private static final String TAG = "####MAIN PAGE ACTIVITY";

//    private List<String> ads;
    private List<String> distribArray;
    private int currentIndex;

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
        nextButton = findViewById(R.id.button_next);
        emailTextView = findViewById(R.id.textView_email);
        imageView = findViewById(R.id.imageView);

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
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPressed();
            }
        });

//        String[] tempArr = {"car1", "swim2", "camping3", "dance4", "music5", "car2", "sports6",
//                "running7", "vacation8"};
//        ads = new ArrayList<>(Arrays.asList(tempArr));
//        currentIndex = 0;
//
//        showImage(ads.get(currentIndex));
    }

    private void nextPressed() {
//        int randx = Random.nextInt(distribArray.size());
//        showImage(ads.get(currentIndex));
        int randx = (int) (Math.random() * distribArray.size());
        int randi = ((int) (Math.random() * 9 + 1));
        Log.d(TAG, "Image sampled: " + distribArray.get(randx) + randi);
        showImage(distribArray.get(randx) + randi);
    }

    private void showImage(String imageName) {
        int resId = getResources().getIdentifier(imageName, "drawable", getPackageName());
        imageView.setImageResource(resId);
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
                    // set up distribution of interests/preferences
                    constructDistribution(user.getPreferences());
                    nextPressed(); // to display image immediately after receiving user data
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
    @Override
    public void onResume() {
        super.onResume();
        // check if the intent has the updated preferences attached to them
        if (getIntent().hasExtra("preferences")) {
            // set up distribution of interests/preferences
            constructDistribution((HashMap<String, Integer>) (getIntent()
              .getSerializableExtra("preferences")));

        }
    }
    private void constructDistribution(Map<String, Integer> preferences) {
        List<String> preferenceDist = new ArrayList<>();
        for (String key: preferences.keySet()) {
            int count = preferences.get(key);
            for (int i = 0; i < count; i++) {
                preferenceDist.add(key);
            }
        }
        distribArray = new ArrayList<>(preferenceDist);
    }
}
