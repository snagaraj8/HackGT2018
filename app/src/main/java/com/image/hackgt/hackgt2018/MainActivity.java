package com.image.hackgt.hackgt2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cancelButton = findViewById(R.id.button_cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelPressed();
            }
        });
    }

    private void onCancelPressed() {
        Intent intent = new Intent(MainActivity.this,
                UpdateActivity.class);
        startActivity(intent);
    }
}
