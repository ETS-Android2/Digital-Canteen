package com.example.canteenapp.OrderList;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.canteenapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class OrderIteam extends AppCompatActivity {
   private FirebaseDatabase firebaseDatabase11;
    private DatabaseReference databaseReference,databaseReference2,databaseReference4,databaseReference11,databaseReferencesuser;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Modelorder, OrderViewHolder> firebaseRecyclerAdapter;
    ProgressBar orderProgess;
    long maxid =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_iteam);
        firebaseDatabase11 = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase11.getReference("Confirmorder");
        databaseReference11= firebaseDatabase11.getReference("Todayshits");
        databaseReference4 = firebaseDatabase11.getReference().child("TotalMoneyofUser");
        databaseReference2 = firebaseDatabase11.getReference("History");
        databaseReferencesuser = firebaseDatabase11.getReference().child("Users");
        orderProgess = findViewById(R.id.orderprogess);
        orderProgess.setVisibility(View.VISIBLE);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView =(RecyclerView) findViewById(R.id.recyclervireforiteam);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    @Override
    public void onStart() {
        super.onStart();
        final FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Modelorder>().setQuery(databaseReference,Modelorder.class).build();
        firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<Modelorder, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, final int i, @NonNull final Modelorder modelorder) {
                        orderProgess.setVisibility(View.GONE);
                            orderViewHolder.username.setText(modelorder.getName());
                            orderViewHolder.topfoodname.setText(modelorder.getFoodname());
                            orderViewHolder.topfoodcount.setText(modelorder.getFoodcount());
                            orderViewHolder.topfoodprice.setText(modelorder.getFoodprize());
                            orderViewHolder.total.setText(modelorder.getTotal());
                            orderViewHolder.tokenadmin.setText(modelorder.getUserid());
                            orderViewHolder.orderredy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    databaseReference.child(modelorder.getUserid()).child("notificationid").setValue(1);
                                    Toast.makeText(getApplicationContext(),"DONE",Toast.LENGTH_SHORT).show();
                                }
                            });
                            orderViewHolder.done.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ////
                                    databaseReferencesuser.child(modelorder.getUniqueID()).child("id").removeValue();
                                    databaseReference2.child(String.valueOf(maxid+1)).child("username").setValue(modelorder.getName());
                                    databaseReference2.child(String.valueOf(maxid+1)).child("foodname").setValue(modelorder.getFoodname());
                                    databaseReference2.child(String.valueOf(maxid+1)).child("foodcount").setValue(modelorder.getFoodcount());
                                    databaseReference2.child(String.valueOf(maxid+1)).child("foodprize").setValue(modelorder.getFoodprize());
                                    databaseReference2.child(String.valueOf(maxid+1)).child("total").setValue(modelorder.getTotal());
                                    databaseReference2.child(String.valueOf(maxid+1)).child("comment").setValue("REMOVED BY ADMIN");
                                    String a =modelorder.getFoodname();
                                    String b =modelorder.getFoodcount();
                                    a = a.replaceAll("\n",",");
                                    b = b.replaceAll("\n",",");
                                    a = a.replaceFirst(",", "");
                                    b = b.replaceFirst(",", "");
                                    final String[] aa = a.split(",");
                                    final String[] bb = b.split(",");
                                    databaseReference11.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.getValue()!=null){
                                                int count =0;
                                                for(String i:aa){
                                                    if (snapshot.child(aa[count]).exists()){
                                                        int value = Integer.parseInt(snapshot.child(aa[count]).getValue().toString());
                                                        databaseReference11.child(aa[count]).setValue(Integer.parseInt(bb[count])+value);
                                                        count++;
                                                    }
                                                    else{
                                                        databaseReference11.child(aa[count]).setValue(bb[count]);
                                                        count++;
                                                    }
                                                }
                                            }
                                            else {
                                                int count =0;
                                                for(String i:aa) {
                                                        databaseReference11.child(aa[count]).setValue(bb[count]);
                                                        count++;
                                                    }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(modelorder.getUniqueID())){
                                              int total2= Integer.parseInt(snapshot.child(modelorder.getUniqueID()).getValue().toString());
                                                databaseReference4.child(modelorder.getUniqueID()).setValue(String.valueOf(Integer.parseInt(modelorder.total)+total2));//not made for same username
                                            }
                                            else{
                                                databaseReference4.child(modelorder.getUniqueID()).setValue(modelorder.total);//not made for same username
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    databaseReference.child(modelorder.getUserid()).removeValue();

                                }
                            });


                    }

                    @NonNull
                    @Override
                    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderiteamtemplate,parent,false);

                        return new OrderViewHolder(view);

                    }
                };

        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}