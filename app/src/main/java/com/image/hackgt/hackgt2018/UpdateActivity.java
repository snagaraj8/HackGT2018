package com.image.hackgt.hackgt2018;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    public static final int GET_FROM_GALLERY = 3;
    private static final String TAG = "UPDATE_ACTIVITY";
    final float threshold = 0.5f;
    private ImageView uploaded;
    private Bitmap bitmap;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        uploaded = findViewById(R.id.uploadImageView);
        bitmap = null;
        final Button upload = findViewById(R.id.upload_button);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadClicked(threshold);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        getUserInfo();
    }

    private void uploadClicked(float threshold) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore
            .Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                uploaded.setImageBitmap(bitmap);
                // Configure the image labeler
                FirebaseVisionLabelDetectorOptions options =
                  new FirebaseVisionLabelDetectorOptions.Builder()
                    .setConfidenceThreshold(threshold)
                    .build();
                // Run the image labeler
                // reads the image
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
                final FirebaseVisionLabelDetector detector = FirebaseVision.getInstance()
                  .getVisionLabelDetector(options); // is the instance of the label detector
                Task<List<FirebaseVisionLabel>> result =
                  detector.detectInImage(image)
                    .addOnSuccessListener(
                      new OnSuccessListener<List<FirebaseVisionLabel>>() {
                          @Override
                          public void onSuccess(List<FirebaseVisionLabel> labels) {
                              // Task completed successfully
                              String output = "";
                              for (FirebaseVisionLabel label: labels) {
                                  String labelname = label.getLabel();
                                  //                          String entityId = label.getEntityId();
                                  float confidence = label.getConfidence();
                                  output += "Label: " + labelname + " with confidence: "
                                        + confidence + "\n";
                              }
                              Log.d(TAG, output);
                              Toast.makeText(UpdateActivity.this, "Uploaded!",
                                Toast.LENGTH_SHORT).show();
                          }
                      })
                    .addOnFailureListener(
                      new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              // Task failed with an exception
                              Toast.makeText(UpdateActivity.this,
                                "Unexpected error occurred", Toast.LENGTH_SHORT).show();
                          }
                      });
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    private void getUserInfo() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String UID = mAuth.getCurrentUser().getUid();
                    user = dataSnapshot.child("users").child(UID).getValue(User.class);
                    Log.d(TAG, "user info\n" + user);
                } catch (NullPointerException e) {
                    user = null;
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
