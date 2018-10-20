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
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        float threshold = 0.7f;
        final Button upload = findViewById(R.id.upload_button);
        final ImageView uploaded = findViewById(R.id.uploadImageView);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
                uploaded.setImageBitmap(bitmap);
            }
        });
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
//        Task<List<FirebaseVisionLabel>> result =
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
                          output += "Label: " + labelname + " with confidence: " + confidence + "\n";
                      }
                      Log.d(TAG, output);
                  }
              })
            .addOnFailureListener(
              new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      // Task failed with an exception
                      Toast.makeText(UpdateActivity.this, "Unexpected error occured",
                        Toast.LENGTH_SHORT).show();
                  }
              });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
