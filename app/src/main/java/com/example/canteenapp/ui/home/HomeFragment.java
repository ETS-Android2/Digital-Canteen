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

import com.example.canteenapp.Nointernet;
import com.example.canteenapp.R;
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

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    FirebaseDatabase firebaseDatabase,firebaseDatabase2;
    DatabaseReference databaseReference,databaseReference2,databaseReference3,databaseReference4,databaseReference5;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    String userID,content,a;
    String username;
    String foodname,foodprize,foodcount;
    ProgressBar progressBar;
    int foodtotal=0;
    long maxid;
    Map<String, Object> value = new HashMap<>();
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Foodcard");
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        floatingActionButton = root.findViewById(R.id.floatingActionButton1);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase2 = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        progressBar = root.findViewById(R.id.progressBar);
        databaseReference2 = firebaseDatabase2.getReference().child("Users").child(userID).child("OrderdIteam");
        databaseReference3 = firebaseDatabase2.getReference("Confirmorder");
        databaseReference4 = firebaseDatabase.getReference("Users").child(userID).child("name");
        databaseReference5 = firebaseDatabase.getReference("Users").child(userID);
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        int counteroflineno=0;
                        for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                            counteroflineno=0;
                            counteroflineno=Integer.parseInt(postSnapshot.getKey());
                            }
                            maxid=counteroflineno;
                        }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
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
                    View buttomsheetview = LayoutInflater.from(getContext()).inflate(R.layout.confromationscreen, (RelativeLayout) root.findViewById(R.id.rlayout));
                    TextView namefood = buttomsheetview.findViewById(R.id.name);
                    TextView countfood = buttomsheetview.findViewById(R.id.count);
                    TextView prizefood = buttomsheetview.findViewById(R.id.prize);
                    final TextView totalfood = buttomsheetview.findViewById(R.id.total);
                    buttomsheetview.findViewById(R.id.confrom).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            databaseReference2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.getValue() != null) {

                                        value.put("foodname", foodname);
                                        value.put("foodprize", foodprize);
                                        value.put("foodcount", foodcount);
                                        value.put("total", String.valueOf(foodtotal));
                                        value.put("name", username);
                                        value.put("userid", String.valueOf(maxid + 1));
                                        value.put("uniqueID", String.valueOf(userID));
                                    }
                                    else{

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
                                        if (getContext()!=null){
                                            Toast.makeText(getContext(), "Order already in Place", Toast.LENGTH_LONG).show();
                                            bottomSheetDialog.dismiss();
                                            databaseReference5.removeEventListener(this);

                                        }
                                        else {}
                                    }
                                    else{
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
                    String fname = "";
                    String fcount = "";
                    String fprize = "";
                    int ashishpr=0;
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
                        fname = fname + "\n" + afinal[name];
                        fcount = fcount + "\n" + afinal[count];
                        total = total + Integer.parseInt(afinal[prize]) * Integer.parseInt(afinal[count]);
                        fprize = fprize + "\n" + Integer.parseInt(afinal[prize]) * Integer.parseInt(afinal[count]);
                        name = name + 3;
                        count = count + 3;
                        prize = prize + 3;


                    }


                    if (total!=0){


                        foodname = fname;
                        foodname = fname;
                        foodprize = fprize;
                        foodcount = fcount;
                        foodtotal = total;
                    namefood.setText(fname);
                    countfood.setText(fcount);
                    prizefood.setText(fprize);
                    totalfood.setText(String.valueOf(total));

                    bottomSheetDialog.setContentView(buttomsheetview);
                    bottomSheetDialog.show();}
                    else {
                        Toast.makeText(getContext(),"Please order something",Toast.LENGTH_SHORT).show();

                    }
                }
                catch (Exception e){

                }
            }

        });

        return root;
    }


    public boolean isInternetAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public void onStart() {

        super.onStart();
        if(isInternetAvailable()==false){
            Intent intent = new Intent(getContext(), Nointernet.class);
                startActivity(intent);
        }
        databaseReference2.removeValue();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(databaseReference,Model.class).build();
        FirebaseRecyclerAdapter<Model,ViewHolderHome> firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Model, ViewHolderHome>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ViewHolderHome viewHolderHome, final int i, @NonNull final Model model) {

                    viewHolderHome.foodName.setText(model.getFoodname());
                    viewHolderHome.foodPrize.setText(model.getFoodprize());
                    Picasso.get().load(model.getFoodpicture()).into(viewHolderHome.foodPicture);
                    progressBar.setVisibility(View.GONE);
                    }
                    @NonNull
                    @Override
                    public ViewHolderHome onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homefragement_style,parent,false);
                        return new ViewHolderHome(view);

                    }
                };
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setItemViewCacheSize(900);
        recyclerView.setAdapter(firebaseRecyclerAdapter);





    }

}