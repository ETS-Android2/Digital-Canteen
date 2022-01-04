package com.example.canteenapp.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.canteenapp.NoInternet;
import com.example.canteenapp.R;
import com.example.canteenapp.Util.CanteenUtil;
import com.example.canteenapp.constant.FireBaseConstant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {


  private FirebaseDatabase firebaseDatabase;

  private DatabaseReference databaseReferencehistory, databaseReferenceuserid, databaseReferenceconfromid, databaseReference4, databaseReference10;
  FirebaseAuth firebaseAuth;
  FirebaseUser user;
  List<String> userIdList = new ArrayList();
  String userID, id, hitText = "";
  String username, foodname, foodcount, foodprize, total;
  Button cancel;
  Button pay;
  TextView dashName, dashQuantity, dashPrize, dashTotal, tokenuser, hititems, orderredytext, turn, timeDash;
  CardView dashcard;
  ProgressBar dashprogess;
  long maxid = 0, time;
  public static int STATUS = 01;

  public View onCreateView(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState) {
    final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

    firebaseAuth = FirebaseAuth.getInstance();
    firebaseDatabase = FirebaseDatabase.getInstance();
    user = firebaseAuth.getCurrentUser();
    userID = user.getUid();
    dashprogess = root.findViewById(R.id.dashprogess);
    dashprogess.setVisibility(View.VISIBLE);
    hititems = root.findViewById(R.id.hititems);
    turn = root.findViewById(R.id.turn);
    dashName = root.findViewById(R.id.dashfoodname);
    dashQuantity = root.findViewById(R.id.dashquantity);
    orderredytext = root.findViewById(R.id.orderredytext);
    dashPrize = root.findViewById(R.id.dashprize);
    dashTotal = root.findViewById(R.id.dashtotal);
    timeDash = root.findViewById(R.id.timeDashBoard);
    cancel = root.findViewById(R.id.cancle);
    dashcard = root.findViewById(R.id.dashcard);
    tokenuser = root.findViewById(R.id.tokenuser);
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReferencehistory = firebaseDatabase.getReference(FireBaseConstant.HISTORY);
    databaseReference10 = firebaseDatabase.getReference(FireBaseConstant.CONFIRMED);
    databaseReference4 = firebaseDatabase.getReference(FireBaseConstant.TODAYS_HITS);
    databaseReference4.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.getValue() != null) {
          String a = snapshot.getValue().toString();
          a = a.replace("{", "");
          a = a.replace("}", "");
          String[] aa = a.split(",");
          if (aa.length != 1) {
            for (int j = 0; j < aa.length; j++) {
              for (int i = 0; i < aa.length; i++) {
                int num1 = Integer.parseInt(aa[j].substring(aa[j].indexOf("=") + 1));
                int num2 = Integer.parseInt(aa[i].substring(aa[i].indexOf("=") + 1));
                if (num1 >= num2) {
                  String temp = aa[i];
                  aa[i] = aa[j];
                  aa[j] = temp;
                }
              }
            }
            int count = 0;

            for (String ikkk : aa) {
              hitText = hitText + aa[count].replace("=", " = ") + "\n\n";
              count++;
            }
          } else {
            hitText = a;
            hititems.setText(hitText);
          }
          hititems.setText(hitText);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
    pay = root.findViewById(R.id.pay);
    pay.setOnClickListener(v -> {
      String url = "https://esewa.com.np/#/home";
      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(url));
      startActivity(i);
    });
    databaseReferenceuserid = firebaseDatabase.getReference("Users").child(userID);
    dashcard.setVisibility(View.GONE);
    databaseReferencehistory.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
          maxid = snapshot.getChildrenCount();
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
    databaseReferenceuserid.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.child("id").getValue() != null) {
          id = snapshot.child("id").getValue().toString();
          databaseReferenceconfromid = firebaseDatabase.getReference(FireBaseConstant.CONFIRMED).child(id);
          databaseReferenceconfromid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

              if (snapshot.getValue() != null) {
                dashprogess.setVisibility(View.GONE);
                dashcard.setVisibility(View.VISIBLE);
                String foodName = snapshot.child("foodName").getValue().toString();

                dashName.setText(foodName);
                String foodCount = snapshot.child("foodCount").getValue().toString();
                foodCount = foodCount.replaceFirst("\n", "");
                dashQuantity.setText(foodCount);
                String foodPrize = snapshot.child("foodPrize").getValue().toString();
                foodPrize = foodPrize.replaceFirst("\n", "");
                dashPrize.setText(foodPrize);
                dashTotal.setText(snapshot.child("total").getValue().toString());
                tokenuser.setText(snapshot.child("userId").getValue().toString());
                timeDash.setText(CanteenUtil.ConvertMilliSecondsToPrettyTime(Long.parseLong(snapshot.child("time").getValue().toString())));

                if (snapshot.child(FireBaseConstant.NOTIFICATION_ID).exists()) {
                  orderredytext.setText("Your order is Ready");
                  orderredytext.setVisibility(View.VISIBLE);
                }
                final int token = Integer.parseInt(snapshot.child("userId").getValue().toString());
                databaseReference10.addValueEventListener(new ValueEventListener() {
                  @RequiresApi(api = Build.VERSION_CODES.O)
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                      int counteroflineno = 0;
                      for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        userIdList.add(postSnapshot.getKey());
                        if (token == Integer.parseInt(postSnapshot.getKey())) {
                          counteroflineno++;
                          break;
                        }
                        counteroflineno++;
                      }
                      counteroflineno = counteroflineno - 1;
                      String turnText = "Your turn is after " + counteroflineno + " " + "people";
                      turn.setText(turnText);
                      if (counteroflineno == 0) {
                        orderredytext.setVisibility(View.VISIBLE);
                      }
                    }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
                });
                username = snapshot.child("name").getValue().toString();
                foodname = snapshot.child("foodName").getValue().toString();
                foodcount = snapshot.child("foodCount").getValue().toString();
                foodprize = snapshot.child("foodPrize").getValue().toString();
                total = snapshot.child("total").getValue().toString();
                time = Long.parseLong(snapshot.child("time").getValue().toString());
                databaseReferenceuserid.removeEventListener(this);
              } else {
                dashcard.setVisibility(View.GONE);
                dashprogess.setVisibility(View.GONE);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
          });

        } else {
          dashprogess.setVisibility(View.GONE);
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });


    cancel.setOnClickListener(view -> {
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("userName").setValue(username);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("foodName").setValue(foodname);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("foodCount").setValue(foodcount);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("foodPrize").setValue(foodprize);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("total").setValue(total);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("time").setValue(time);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("comment").setValue("REMOVED BY USER");
      firebaseDatabase.getReference(FireBaseConstant.CONFIRMED).child(id).removeValue();
      firebaseDatabase.getReference(FireBaseConstant.USERS).child(userID).child("id").removeValue();
      dashcard.setVisibility(View.GONE);
    });
    return root;
  }


  @Override
  public void onStart() {
    super.onStart();
    if (!isInternetAvailable()) {
      startActivity(new Intent(getContext(), NoInternet.class));
    }
  }


  public boolean isInternetAvailable() {
    try {
      InetAddress ipAddr = InetAddress.getByName("google.com");
      return !ipAddr.equals("");

    } catch (Exception e) {
      return false;
    }
  }
}