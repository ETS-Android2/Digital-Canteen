package com.example.canteenapp.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.canteenapp.NoInternet;
import com.example.canteenapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
  Button cancle, pay;
  TextView dashname, dashquantity, dashprize, dashtotal, tokenuser, hititems, orderredytext, turn;
  CardView dashcard;
  LinearLayout templineratlayout;
  ProgressBar dashprogess;
  long maxid = 0;

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
    templineratlayout = root.findViewById(R.id.templineratlayout);
    templineratlayout.setVisibility(View.GONE);
    dashname = root.findViewById(R.id.dashfoodname);
    dashquantity = root.findViewById(R.id.dashquantity);
    orderredytext = root.findViewById(R.id.orderredytext);
    dashprize = root.findViewById(R.id.dashprize);
    dashtotal = root.findViewById(R.id.dashtotal);
    cancle = root.findViewById(R.id.cancle);
    dashcard = root.findViewById(R.id.dashcard);
    tokenuser = root.findViewById(R.id.tokenuser);
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReferencehistory = firebaseDatabase.getReference("History");
    databaseReference10 = firebaseDatabase.getReference("Confirmed");
    databaseReference4 = firebaseDatabase.getReference("Todayshits");
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
          databaseReferenceconfromid = firebaseDatabase.getReference("Confirmed").child(id);
          databaseReferenceconfromid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

              if (snapshot.getValue() != null) {
                dashprogess.setVisibility(View.GONE);
                dashcard.setVisibility(View.VISIBLE);
                dashname.setText(snapshot.child("foodName").getValue().toString());
                dashquantity.setText(snapshot.child("foodCount").getValue().toString());
                dashprize.setText(snapshot.child("foodPrize").getValue().toString());
                dashtotal.setText(snapshot.child("total").getValue().toString());
                tokenuser.setText(snapshot.child("userId").getValue().toString());
                if (snapshot.child("notificationid").exists()) {
                  orderredytext.setText("Your order is Ready");
                  templineratlayout.setVisibility(View.VISIBLE);
                }
                final int token = Integer.parseInt(snapshot.child("userId").getValue().toString());
                databaseReference10.addValueEventListener(new ValueEventListener() {
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
                      String turnText = "Your turn is after " + counteroflineno + " " + "People";
                      turn.setText(turnText);

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


    cancle.setOnClickListener(view -> {
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("userName").setValue(username);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("foodName").setValue(foodname);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("foodCount").setValue(foodcount);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("foodPrize").setValue(foodprize);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("total").setValue(total);
      databaseReferencehistory.child(String.valueOf(maxid + 1)).child("comment").setValue("REMOVED BY USER");
      firebaseDatabase.getReference("Confirmed").child(id).removeValue();
      firebaseDatabase.getReference("Users").child(userID).child("id").removeValue();
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
    ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
  }
}