package com.example.canteenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canteenapp.FoodList.Food;
import com.example.canteenapp.History.IteamHistory;
import com.example.canteenapp.OrderList.OrderIteam;
import com.example.canteenapp.Registration.Registration;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView email,password;
    Button button;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.Emailoflogin);
        password = findViewById(R.id.Passwordlogin);
        button = findViewById(R.id.loginbutton);
        progressBar = findViewById(R.id.progressBarLoginPage);
        progressBar.setVisibility(View.GONE);
        firebaseAuth = FirebaseAuth.getInstance();
      final TextView passwordToggler = findViewById(R.id.passwordToogle);
        passwordToggler.setText("SHOW");
       //THIS IS FOR PASSWORD SHOW METHORD
        passwordToggler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String state = passwordToggler.getText().toString();

                if(state.equals("SHOW")){
                    passwordToggler.setText("HIDE");
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                     }
                else {
                    passwordToggler.setText("SHOW");
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        //TISH IS FOR BUTOON ONLY


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if (Email.matches("admin")&&Password.matches("adduser")){
                    Toast.makeText(MainActivity.this,"working",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Registration.class);
                    startActivity(intent);
                }

                if (Email.matches("admin")&& Password.matches("addfood")){
                    Toast.makeText(MainActivity.this,"working",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), Food.class);
                    startActivity(intent);
                }
                if (Email.matches("admin")&& Password.matches("history")){
                    Toast.makeText(MainActivity.this,"working",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), IteamHistory.class);
                    startActivity(intent);
                }

                    if (Email.matches("admin")&& Password.matches("order")){
                        Toast.makeText(MainActivity.this,"working",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), OrderIteam.class);
                        startActivity(intent);
                }
                if (TextUtils.isEmpty(Email)){
                    Toast.makeText(MainActivity.this,"Email is missing",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    Toast.makeText(MainActivity.this,"Password is missing",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.length()<8){
                    Toast.makeText(MainActivity.this,"Password too short",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        Intent intent = new Intent(getApplicationContext(),ButtomNavgation.class);
                                        startActivity(intent);

                                    } else
                                        {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
    public boolean isInternetAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
        {
            connected = false;
        }
        return connected;

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!=null){
            Intent intent = new Intent(MainActivity.this,ButtomNavgation.class);
            startActivity(intent);
        }
        if(isInternetAvailable()==false){
            Intent intent = new Intent(MainActivity.this, Nointernet.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}