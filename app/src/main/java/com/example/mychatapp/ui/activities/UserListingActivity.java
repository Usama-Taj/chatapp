package com.example.mychatapp.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapp.R;
import com.example.mychatapp.core.logout.LogoutContract;
import com.example.mychatapp.models.UsersListing;
import com.example.mychatapp.ui.adapters.UsersAdapater;
import com.example.mychatapp.utils.Global;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserListingActivity extends AppCompatActivity implements LogoutContract.View{
    private Toolbar mToolbar;
    //Viewss
    RecyclerView myView;
    //Adapter
    UsersAdapater usersAdapater;

    //Firebase
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseDatabase database;

    //References
    DatabaseReference userRef;
    DatabaseReference usersRef;

    //Users List
    ArrayList<UsersListing> userArrayList;

    //Listener
    ValueEventListener valueEventListener;

    //Uri
    Uri imageuri;

    //Userdata
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_listing);
        bindViews();
        bindReferences();
        init();
        getUserstoList();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_option:
                logout();
                break;
            case R.id.language_urdu:
                Global.LANGUAGE = "Urdu";
                break;
            case R.id.language_english:
                Global.LANGUAGE = "English";
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindReferences() {
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    //show Alert Message Confirmation Box for LOGOUT



    //When user Logout Successfully
    @Override
    public void onLogoutSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        LoginActivity.startIntent(this,
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //When Logout Fails
    @Override
    public void onLogoutFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UserListingActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int flags) {
        Intent intent = new Intent(context, UserListingActivity.class);
        intent.setFlags(flags);
        context.startActivity(intent);
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        myView = (RecyclerView) findViewById(R.id.users_list);
        userArrayList = new ArrayList<UsersListing>();
    }

    private void init() {
        //set the toolbar
        SharedPreferences prefs = getSharedPreferences("chat", MODE_PRIVATE);
        String restoredText = prefs.getString("username", null);
        mToolbar.setTitle(restoredText);
        setSupportActionBar(mToolbar);
        // set the login screen fragment
    }
    public void showUserslist(ArrayList<UsersListing> arrayList){
        usersAdapater = new UsersAdapater(UserListingActivity.this,arrayList);
        myView.setLayoutManager(new LinearLayoutManager(UserListingActivity.this));
        myView.setAdapter(usersAdapater);
    }
    void makeUnique(ArrayList<UsersListing> arrayList){
        Set<UsersListing> set = new HashSet(arrayList);
        set.toArray();
        userArrayList = new ArrayList<UsersListing>(set);
    }
    public void getUserstoList(){
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        reference = FirebaseDatabase.getInstance().getReference().child("users");
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                userArrayList.clear();
                for (final DataSnapshot ds: dataSnapshot.getChildren()){
                    storageReference.child(ds.child("uid").getValue(String.class)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
//                            Log.i("uid1",FirebaseAuth.getInstance().getCurrentUser().getUid()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//                            Log.i("uid2",ds.child("uid").getValue(String.class)+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//                            Log.i("email",ds.child("email").getValue(String.class)+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                            imageuri = uri;
                            if(!ds.child("uid").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                userArrayList.add(new UsersListing(imageuri.toString(), ds.child("username").getValue(String.class),ds.child("email").getValue(String.class)));
                                showUserslist(userArrayList);
                            }
                            else if (ds.child("uid").getValue(String.class).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                Global.CURRENT_USER_IMAGE = imageuri;
                                Global.CURRENT_USER_UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Global.CURRENT_USER_EMAIL = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                Global.CURRENT_USER_NAME = ds.child("username").getValue(String.class);
                            }
                        }
                    });
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        reference.addValueEventListener(valueEventListener);



    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedPreferences.Editor editor = getSharedPreferences("chat", MODE_PRIVATE).edit();
                        editor.putString("email", "");
                        editor.putString("username", "");
                        editor.apply();
                        FirebaseAuth.getInstance().signOut();
                        LoginActivity.startIntent(UserListingActivity.this);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
