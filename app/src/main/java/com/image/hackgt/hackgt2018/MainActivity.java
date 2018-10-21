package com.image.hackgt.hackgt2018;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.editText_email);
        passwordEditText = findViewById(R.id.editText_password);

        Button cancelButton = findViewById(R.id.button_cancel);
        loginButton = findViewById(R.id.button_login);
        Button registerButton = findViewById(R.id.button_registration);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

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
//        Intent intent = new Intent(MainActivity.this,
//                UpdateActivity.class);
//        startActivity(intent);
        //Toast.makeText(MainActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
    }

    private void onRegisterPressed() {
        Intent intent = new Intent(MainActivity.this,
                RegistrationActivity.class);
        startActivity(intent);
    }

    private void onLoginPressed() {
        loginButton.setEnabled(false);
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            //progressDialog.dismiss();
            loginButton.setEnabled(true);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login succeeded.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this,
                                    FeedActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Incorrect username" +
                                    " or password.", Toast.LENGTH_SHORT).show();
                        }
                        loginButton.setEnabled(true);
                    }
                });

    }
}
