package com.image.hackgt.hackgt2018;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText emailEditText;
    EditText monthEditText;
    EditText dayEditText;
    EditText yearEditText;
    EditText passwordEditText;
    EditText passwordRepeatEditText;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    String TAG = "###Registration Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button registerButton = findViewById(R.id.button_register);
        Button backButton= findViewById(R.id.button_back);
        nameEditText = findViewById(R.id.editText_name);
        emailEditText = findViewById(R.id.editText_email);
        monthEditText = findViewById(R.id.editText_month);
        dayEditText = findViewById(R.id.editText_day);
        yearEditText = findViewById(R.id.editText_year);
        passwordEditText = findViewById(R.id.editText_password);
        passwordRepeatEditText = findViewById(R.id.editText_password_repeat);

        // Firebase stuff initializers
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRegisterPressed();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void onRegisterPressed() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordRepeat = passwordRepeatEditText.getText().toString().trim();
        int month;
        int day;
        int year;

        if (name.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Name must be filled",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Email must be filled",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty() || passwordRepeat.isEmpty()) {
            Toast.makeText(RegistrationActivity.this, "Password must be filled",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            month = Integer.parseInt(monthEditText.getText().toString().trim());
            day = Integer.parseInt(dayEditText.getText().toString().trim());
            year = Integer.parseInt(yearEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(RegistrationActivity.this, "Date of Birth must be filled",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(RegistrationActivity.this, "Invalid Email Entered",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (month < 1 || month > 12 || day < 1 || day > 31 || year > 2018) {
            Toast.makeText(RegistrationActivity.this, "Invalid Date Entered",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(passwordRepeat)) {
            Toast.makeText(RegistrationActivity.this, "Passwords don't match",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        final User user = new User(email, password, name, month + "/" + day + "/" + year,
                null);
        Log.d(TAG, user.toString());
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this,
                                    user.getUsername() + " added!", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String UID = firebaseUser.getUid();

                            // Add to DB
                            myRef.child("users").child(UID).setValue(user);

                            Intent intent = new Intent(RegistrationActivity.this,
                                    FeedActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("username", user.getUsername());
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(RegistrationActivity.this,
                                        user.getUsername() + " already exists",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrationActivity.this,
                                        task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
