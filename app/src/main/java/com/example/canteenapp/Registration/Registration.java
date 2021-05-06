package com.example.canteenapp.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import android.widget.TextView;
import android.widget.Toast;

import com.example.canteenapp.MainActivity;
import com.example.canteenapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {
    TextView email,phone,fullname,password;
    Button button;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        email = findViewById(R.id.Email);
        phone = findViewById(R.id.Phone);
        fullname = findViewById(R.id.Fullname);
        password = findViewById(R.id.Password);
        button = findViewById(R.id.signup);
        progressBar = findViewById(R.id.progressBarReg);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email = email.getText().toString();
                final String Phone = phone.getText().toString();
                final String Fullname = fullname.getText().toString();
                final String Password = password.getText().toString();
                if(TextUtils.isEmpty(Email)){
                    Toast.makeText(Registration.this,"Email is missing",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Phone)){
                    Toast.makeText(Registration.this,"Phone is missing",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Fullname)){
                    Toast.makeText(Registration.this,"Name is missing",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    Toast.makeText(Registration.this,"Password is missing",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.length()<6){
                    Toast.makeText(Registration.this,"Too Short Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                if (Password.length()>=8){
                    firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registration.this, "User Added Successfully",
                                                Toast.LENGTH_SHORT).show();
                                        // Sign in success, update UI with the signed-in user's information

                                        User user = new User(
                                                Email,
                                                Fullname,
                                                Phone,
                                                Password

                                        );
                                        FirebaseDatabase.getInstance().getReference("Users").
                                                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(Registration.this, "sucess",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Registration.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });
                }
                else {
                    Toast.makeText(Registration.this,"We are updating",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}