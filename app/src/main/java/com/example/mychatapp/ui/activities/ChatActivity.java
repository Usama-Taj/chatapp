package com.example.mychatapp.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapp.R;
import com.example.mychatapp.models.Chat;
import com.example.mychatapp.ui.adapters.ChatAdapter;
import com.example.mychatapp.utils.Global;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    private String message;
    private Intent intent;
    private Toolbar mToolbar;
    private DatabaseReference dbReference;
    private ValueEventListener eventListener;
    private EditText mMessage;
    private CircleImageView mSendImage;

    private RecyclerView mChatRecycler;
    private ArrayList<Chat> mChatArraylist;
    private ChatAdapter mChatAdapter;

    public static void startActivity(Context context, String username, String useremail, String userimage) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("useremail", useremail);
        intent.putExtra("userimage", userimage);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_option:
                logout();
                break;
            case R.id.language_urdu:
                Global.LANGUAGE = "Urdu";
                ChatActivity.startActivity(this,intent.getStringExtra("username"),intent.getStringExtra("useremail"),intent.getStringExtra("userimage"));
                finish();
                break;
            case R.id.language_english:
                Global.LANGUAGE = "English";
                ChatActivity.startActivity(this,intent.getStringExtra("username"),intent.getStringExtra("useremail"),intent.getStringExtra("userimage"));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                        LoginActivity.startIntent(ChatActivity.this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        bindViews();
        init();
        retriveMessages();
        showMessages(mChatArraylist);
        //Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void retriveMessages() {
        dbReference = FirebaseDatabase.getInstance().getReference().child("chat_messages");
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatArraylist.clear();
                for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("sender_email").getValue(String.class).equals(Global.CURRENT_USER_EMAIL) && ds.child("reciever_email").getValue(String.class).equals(intent.getStringExtra("useremail"))) {
                        Chat.indicator = "mine";
                        mChatArraylist.add(new Chat(Global.CURRENT_USER_NAME, ds.child("message").getValue(String.class), Global.CURRENT_USER_IMAGE.toString()));
                    } else if (ds.child("reciever_email").getValue(String.class).equals(Global.CURRENT_USER_EMAIL) && ds.child("sender_email").getValue(String.class).equals(intent.getStringExtra("useremail"))) {
                        Chat.indicator = "other";
                        mChatArraylist.add(new Chat(intent.getStringExtra("username"), ds.child("message").getValue(String.class), intent.getStringExtra("userimage")));
                    } else {
                        //
                    }
                }
                showMessages(mChatArraylist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        dbReference.addValueEventListener(eventListener);
    }


    private void showMessages(ArrayList<Chat> arrayList) {
        mChatAdapter = new ChatAdapter(ChatActivity.this, arrayList);
        mChatRecycler.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        mChatRecycler.setAdapter(mChatAdapter);
    }

    private void bindViews() {
        mToolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        mSendImage = (CircleImageView) findViewById(R.id.send_message);
        mMessage = (EditText) findViewById(R.id.type_message);
        mChatRecycler = (RecyclerView) findViewById(R.id.chat_recycler);
        mChatArraylist = new ArrayList<Chat>();
    }

    private void init() {
        mSendImage.setOnClickListener(this);
        mChatArraylist.clear();


        intent = getIntent();
        mToolbar.setTitle(getIntent().getStringExtra("username"));
        // set the toolbar
        //mToolbar.setTitle(intent.getStringExtra("username"));
        setSupportActionBar(mToolbar);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.send_message:
                sendMessage(Global.CURRENT_USER_NAME, intent.getStringExtra("username"), Global.CURRENT_USER_EMAIL, intent.getStringExtra("useremail"), mMessage.getText().toString());
                mMessage.setText("");
        }
    }

    private void sendMessage(String sendername, String recivername, String senderemail, String reciveremail, String message) {
        FirebaseDatabase.getInstance().getReference().child("chat_messages").push().setValue(new Chat(sendername, recivername, senderemail, reciveremail, message));
    }

}