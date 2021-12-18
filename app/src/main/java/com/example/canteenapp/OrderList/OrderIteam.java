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
   private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference,databaseReference2,databaseReference4,databaseReference11,databaseReferencesuser;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<ModelOrder, OrderViewHolder> firebaseRecyclerAdapter;
    ProgressBar orderProgress;
    long maxId =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_iteam);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Confirmed");
        databaseReference11= firebaseDatabase.getReference("Todayshits");
        databaseReference4 = firebaseDatabase.getReference().child("TotalMoneyofUser");
        databaseReference2 = firebaseDatabase.getReference("History");
        databaseReferencesuser = firebaseDatabase.getReference().child("Users");
        orderProgress = findViewById(R.id.orderprogess);
        orderProgress.setVisibility(View.VISIBLE);
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId = snapshot.getChildrenCount();
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
        final FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<ModelOrder>().setQuery(databaseReference, ModelOrder.class).build();
        firebaseRecyclerAdapter = new
                FirebaseRecyclerAdapter<ModelOrder, OrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final OrderViewHolder orderViewHolder, final int i, @NonNull final ModelOrder modelorder) {
                        orderProgress.setVisibility(View.GONE);
                            orderViewHolder.username.setText(modelorder.getName());
                            orderViewHolder.topfoodname.setText(modelorder.getFoodName());
                            orderViewHolder.topfoodcount.setText(modelorder.getFoodCount());
                            orderViewHolder.topfoodprice.setText(modelorder.getFoodPrize());
                            orderViewHolder.total.setText(modelorder.getTotal());
                            orderViewHolder.tokenadmin.setText(modelorder.getUserId());
                            orderViewHolder.orderredy.setOnClickListener(v -> {
                                databaseReference.child(modelorder.getUserId()).child("notificationid").setValue(1);
                                Toast.makeText(getApplicationContext(),"DONE",Toast.LENGTH_SHORT).show();
                            });
                            orderViewHolder.done.setOnClickListener(view -> {
                                databaseReferencesuser.child(modelorder.getUniqueId()).child("id").removeValue();
                                databaseReference2.child(String.valueOf(maxId +1)).child("userName").setValue(modelorder.getName());
                                databaseReference2.child(String.valueOf(maxId +1)).child("foodName").setValue(modelorder.getFoodName());
                                databaseReference2.child(String.valueOf(maxId +1)).child("foodCount").setValue(modelorder.getFoodCount());
                                databaseReference2.child(String.valueOf(maxId +1)).child("foodPrize").setValue(modelorder.getFoodPrize());
                                databaseReference2.child(String.valueOf(maxId +1)).child("total").setValue(modelorder.getTotal());
                                databaseReference2.child(String.valueOf(maxId +1)).child("comment").setValue("REMOVED BY ADMIN");
                                String a =modelorder.getFoodName();
                                String b =modelorder.getFoodCount();
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
                                            for(String i1 :aa){
                                                if (snapshot.child(aa[count]).exists()){
                                                    int value = Integer.parseInt(snapshot.child(aa[count]).getValue().toString());
                                                    databaseReference11.child(aa[count]).setValue(Integer.parseInt(bb[count])+value);
                                                }
                                                else{
                                                    databaseReference11.child(aa[count]).setValue(bb[count]);
                                                }
                                                count++;
                                            }
                                        }
                                        else {
                                            int count =0;
                                            for(String i1 :aa) {
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
                                        if (snapshot.hasChild(modelorder.getUniqueId())){
                                          int total2= Integer.parseInt(snapshot.child(modelorder.getUniqueId()).getValue().toString());
                                            databaseReference4.child(modelorder.getUniqueId()).setValue(String.valueOf(Integer.parseInt(modelorder.getTotal())+total2));//not made for same username
                                        }
                                        else{
                                            databaseReference4.child(modelorder.getUniqueId()).setValue(modelorder.getTotal());//not made for same username
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                databaseReference.child(modelorder.getUserId()).removeValue();

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