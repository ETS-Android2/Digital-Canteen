package com.example.canteenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class NoInternet extends AppCompatActivity {
  Button checkInternet;
  CanteenUtil canteenUtil = new CanteenUtil();
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_nointernet);
    checkInternet = findViewById(R.id.checkinternt);
    checkInternet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (canteenUtil.isInternetAvailable()) {
          Intent intent = new Intent(NoInternet.this, MainActivity.class);
          startActivity(intent);
        } else {
          Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_LONG).show();
        }
      }
    });
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
  }

}