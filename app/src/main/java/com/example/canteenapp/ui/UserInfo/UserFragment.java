package com.example.canteenapp.ui.UserInfo;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.canteenapp.Util.CanteenUtil;
import com.example.canteenapp.MainActivity;
import com.example.canteenapp.NoInternet;
import com.example.canteenapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserFragment extends Fragment {

    private static final int RESULT_OK = -1;
    private UserViewModel notificationsViewModel;
TextView name,email,phone,usertotal;
FirebaseAuth firebaseAuth;
FirebaseDatabase firebaseDatabase;
DatabaseReference databaseReference,databaseReference4;
FirebaseUser user;
String userID;
Button logout,resetdata;
String NAME;
StorageReference storageReference;
ImageView picture;
CardView cardView;
ProgressBar progressBar;
private static  final int ImageBack = 1;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        name = root.findViewById(R.id.userSectionNsme);
        email = root.findViewById(R.id.userSectionEmail);
        usertotal=root.findViewById(R.id.usertotal);
        phone = root.findViewById(R.id.userSectionPhone);
        cardView = root.findViewById(R.id.dashcard);
        progressBar = root.findViewById(R.id.progressBarpic);
        progressBar.setVisibility(View.GONE);
        picture = root.findViewById(R.id.picture);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        databaseReference4 = firebaseDatabase.getReference().child("TotalMoneyofUser");
        databaseReference = firebaseDatabase.getReference().child("Users").child(userID);
        storageReference = FirebaseStorage.getInstance().getReference().child(userID);
        logout = root.findViewById(R.id.logout);
        resetdata=root.findViewById(R.id.resetdata);
        resetdata.setOnClickListener(v -> {
            databaseReference4.child(userID).removeValue();
          usertotal.setText("0");
        });
        logout.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        });
        return root;
    }
    public void Uploaddata(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                ImageBack);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ImageBack){
            if (resultCode == RESULT_OK) {
                progressBar.setVisibility(View.VISIBLE);
                Uri ImageData = data.getData();
                    final StorageReference imagename = storageReference.child("image"+ImageData.getLastPathSegment());
                    imagename.putFile(ImageData).addOnSuccessListener(taskSnapshot -> imagename.getDownloadUrl().addOnSuccessListener(uri -> {

                      String hashMap = String.valueOf(uri);
                        databaseReference.child("image").setValue(hashMap).addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(),"ImageUploaded",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        });
                    }));
            }
            }
    }



    @Override
    public void onStart() {
        super.onStart();
        if(!isInternetAvailable()){
            startActivity( new Intent(getContext(), NoInternet.class));
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("image").getValue()!=null){

                        Picasso.get().load(snapshot.child("image").getValue().toString()).rotate(90).into(picture);
                    }
                        String EMAIL = snapshot.child("email").getValue().toString();
                         NAME = snapshot.child("name").getValue().toString();
                        String PHONE = snapshot.child("phone").getValue().toString();
                        name.setText(NAME);
                        email.setText(EMAIL);
                        phone.setText(PHONE);
                databaseReference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue()!=null){
                            if(snapshot.hasChild(userID)){
                                String userT=snapshot.child(userID).getValue().toString();
                                usertotal.setText(userT);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });picture.setOnClickListener(view -> Uploaddata(view));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                name.setText("Error Loading");
                email.setText("Error Loading");
                phone.setText("Error Loading");
            }
        });
    }

    public boolean isInternetAvailable() {
       ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
       return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

    }
}