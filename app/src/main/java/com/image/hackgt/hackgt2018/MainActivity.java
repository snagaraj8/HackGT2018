package com.image.hackgt.hackgt2018;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cancelButton = findViewById(R.id.button_cancel);
        Button loginButton = findViewById(R.id.button_login);
        Button registerButton = findViewById(R.id.button_registration);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelPressed();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginPressed();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterPressed();
            }
        });
    }

    private void onCancelPressed() {
        Intent intent = new Intent(MainActivity.this,
                UpdateActivity.class);
        startActivity(intent);
        Toast.makeText(MainActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
    }

    private void onRegisterPressed() {
        Intent intent = new Intent(MainActivity.this,
                RegistrationActivity.class);
        startActivity(intent);
    }

    private void onLoginPressed() {
        Toast.makeText(MainActivity.this, "Log in pressed!", Toast.LENGTH_SHORT).show();
    }
}
