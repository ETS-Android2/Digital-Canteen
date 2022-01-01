package com.example.canteenapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenapp.NoInternet;
import com.example.canteenapp.R;
import com.example.canteenapp.constant.FireBaseConstant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
  private HomeViewModel homeViewModel;
  FirebaseDatabase firebaseDatabase, firebaseDatabase2;
  DatabaseReference databaseReference, databaseReference2, databaseReference3, databaseReference4, databaseReference5,databaseReferenceUserNameAndPicture;
  FirebaseAuth firebaseAuth;
  FirebaseUser user;
  RecyclerView recyclerView;
  FloatingActionButton floatingActionButton;
  String userID, content, a;
  String username;
  String foodname, foodprize, foodcount;
  RelativeLayout homePageMainLayout;
  ImageView homePageUserImage;
  TextView homePageUserName;
  int foodtotal = 0;
  long maxid;
  Map<String, Object> value = new HashMap<>();

  public View onCreateView(@NonNull final LayoutInflater inflater,
                           final ViewGroup container, final Bundle savedInstanceState) {
    homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    final View root = inflater.inflate(R.layout.fragment_home, container, false);
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference(FireBaseConstant.FOOD_CARD);
    recyclerView = root.findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    floatingActionButton = root.findViewById(R.id.floatingActionButton1);
    homePageUserImage = root.findViewById(R.id.homePageUserImage);
    homePageUserName=root.findViewById(R.id.homePageUserName);
    homePageMainLayout= root.findViewById(R.id.homePageMainLayout);
    firebaseAuth = FirebaseAuth.getInstance();
    firebaseDatabase2 = FirebaseDatabase.getInstance();
    user = firebaseAuth.getCurrentUser();
    userID = user.getUid();
    user = firebaseAuth.getCurrentUser();
    userID = user.getUid();
    databaseReferenceUserNameAndPicture = firebaseDatabase.getReference().child(FireBaseConstant.USERS).child(userID);
    databaseReference2 = firebaseDatabase2.getReference().child(FireBaseConstant.USERS).child(userID).child(FireBaseConstant.ORDERED_ITEM);
    databaseReference3 = firebaseDatabase2.getReference(FireBaseConstant.CONFIRMED);
    databaseReference4 = firebaseDatabase.getReference(FireBaseConstant.USERS).child(userID).child(FireBaseConstant.NAME);
    databaseReference5 = firebaseDatabase.getReference(FireBaseConstant.USERS).child(userID);
    databaseReference3.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()) {
          int counteroflineno = 0;
          for (DataSnapshot postSnapshot : snapshot.getChildren()) {
            counteroflineno = Integer.parseInt(postSnapshot.getKey());
          }
          maxid = counteroflineno;
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
      }
    });
    databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.getValue() != null) {
          username = snapshot.getValue().toString();
          databaseReference4.removeEventListener(this);
        }
        databaseReference4.removeEventListener(this);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
    databaseReference2.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.getValue() != null) {
          content = snapshot.getValue().toString();
          databaseReference.removeEventListener(this);
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
          final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDilougeTheme);
          View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.confromationscreen, (RelativeLayout) root.findViewById(R.id.rlayout));
          TextView nameFood = bottomSheetView.findViewById(R.id.name);
          TextView countFood = bottomSheetView.findViewById(R.id.count);
          TextView prizeFood = bottomSheetView.findViewById(R.id.prize);
          final TextView totalFood = bottomSheetView.findViewById(R.id.total);
          bottomSheetView.findViewById(R.id.confrom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              databaseReference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if (snapshot.getValue() != null) {

                    value.put("foodName", foodname);
                    value.put("foodPrize", foodprize);
                    value.put("foodCount", foodcount);
                    value.put("total", String.valueOf(foodtotal));
                    value.put("name", username);
                    value.put("userId", String.valueOf(maxid + 1));
                    value.put("uniqueId", String.valueOf(userID));
                    value.put("time", System.currentTimeMillis());
                  } else {

                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

              });
              databaseReference5.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                  if (snapshot.child("id").getValue() != null) {
                    if (getContext() != null) {
                      Toast.makeText(getContext(), "Please remove your last order before ordering new.", Toast.LENGTH_LONG).show();
                      bottomSheetDialog.dismiss();
                      databaseReference5.removeEventListener(this);

                    } else {
                    }
                  } else {
                    databaseReference3.child(String.valueOf(maxid + 1)).updateChildren(value);
                    databaseReference5.child("id").setValue(String.valueOf(maxid + 1));
                    databaseReference5.removeEventListener(this);
                    databaseReference2.removeEventListener(this);
                    Navigation.findNavController(root).navigate(R.id.navigation_dashboard);
                  }
                  databaseReference5.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
              });

              bottomSheetDialog.dismiss();
              databaseReference2.removeValue();

            }

          });
          String fName = "";
          String fCount = "";
          String fPrize = "";
          int total = 0;
          a = content;
          a = a.replaceAll("=", " ,");
          a = a.replaceAll(" ", "");
          a = a.replace("{", "");
          a = a.replace("}", "");
          String[] afinal = a.split(",");
          int s = afinal.length / 3;
          int name = 0;
          int count = 1;
          int prize = 2;


          for (int start = 1; start <= s; start++) {
            fName = fName + "\n" + afinal[name];
            fCount = fCount + "\n" + afinal[count];
            total = total + Integer.parseInt(afinal[prize]) * Integer.parseInt(afinal[count]);
            fPrize = fPrize + "\n" + Integer.parseInt(afinal[prize]) * Integer.parseInt(afinal[count]);
            name = name + 3;
            count = count + 3;
            prize = prize + 3;

          }

          if (total != 0) {
            foodname = fName;
            foodname = fName;
            foodprize = fPrize;
            foodcount = fCount;
            foodtotal = total;
            nameFood.setText(fName);
            countFood.setText(fCount);
            prizeFood.setText(fPrize);
            totalFood.setText(String.valueOf(total));

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
          } else {
            Toast.makeText(getContext(), "Please order something", Toast.LENGTH_SHORT).show();
          }
        } catch (Exception e) {
        }
      }
    });
    return root;
  }

  @Override
  public void onStart() {
    homePageMainLayout.setVisibility(View.GONE);
    super.onStart();
    if (!isInternetAvailable()) {
      Intent intent = new Intent(getContext(), NoInternet.class);
      startActivity(intent);
    }
    Thread thread1 = new Thread(()->{
      databaseReferenceUserNameAndPicture.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
          if (snapshot.child("image").getValue() != null) {
            Picasso.get().load(snapshot.child("image").getValue().toString()).into(homePageUserImage);
          }
          homePageUserName.setText(snapshot.child("name").getValue().toString().split(" ")[0]);
          homePageMainLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
      });
    });
   Thread thread2 = new Thread(()->{
     databaseReference2.removeValue();
     FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(databaseReference, Model.class).build();
     FirebaseRecyclerAdapter<Model, ViewHolderHome> firebaseRecyclerAdapter = new
             FirebaseRecyclerAdapter<Model, ViewHolderHome>(options) {
               @Override
               protected void onBindViewHolder(@NonNull final ViewHolderHome viewHolderHome, final int i, @NonNull final Model model) {

                 viewHolderHome.foodName.setText(model.getFoodName());
                 viewHolderHome.foodPrize.setText(model.getFoodPrize());
                 Picasso.get().load(model.getFoodPicture()).into(viewHolderHome.foodPicture);
               }

               @NonNull
               @Override
               public ViewHolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homefragement_style, parent, false);
                 return new ViewHolderHome(view);
               }
             };
     firebaseRecyclerAdapter.startListening();
     firebaseRecyclerAdapter.notifyDataSetChanged();
     recyclerView.setItemViewCacheSize(900);
     recyclerView.setAdapter(firebaseRecyclerAdapter);
   });
   thread1.setPriority(Thread.MAX_PRIORITY);
   thread2.setPriority(Thread.MAX_PRIORITY);
   thread2.start();
   thread1.start();
  }

  public boolean isInternetAvailable() {
    ConnectivityManager connectivityManager = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
  }
}