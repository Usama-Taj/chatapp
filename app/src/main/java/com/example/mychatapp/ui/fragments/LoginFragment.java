package com.example.mychatapp.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mychatapp.R;
import com.example.mychatapp.core.login.LoginContract;
import com.example.mychatapp.core.login.LoginPresenter;
import com.example.mychatapp.ui.activities.RegisterActivity;
import com.example.mychatapp.ui.activities.UserListingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;

public class LoginFragment extends Fragment implements View.OnClickListener, LoginContract.View {

    private LoginPresenter mLoginPresenter;

    private EditText mETxtEmail, mETxtPassword;
    private Button mBtnLogin, mBtnRegister;

    private ProgressDialog mProgressDialog;

    public String username;
    public String uid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        username = "";
        init();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        switch (viewId) {
            case R.id.btn_login:
                onLogin(view);
                break;
            case R.id.btn_login_register:
                onRegister(view);
                break;
        }
    }
    public void getUserdata(){
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    username = dataSnapshot.child("username").getValue(String.class);
                    mProgressDialog.dismiss();
                    Toast.makeText(getActivity(),"Welcome "+username,Toast.LENGTH_SHORT).show();
                    saveData(username,dataSnapshot.child("email").getValue(String.class));
                    UserListingActivity.startActivity(getActivity(),
                    Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onLoginSuccess(String message) {
        getUserdata();
    }

    @Override
    public void onLoginFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
    }


    //Following Pattren
    public static LoginFragment newInstance() {
        Bundle args = new Bundle();
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Bind all UI views
    private void bindViews(View view) {
        mETxtEmail = (EditText) view.findViewById(R.id.login_email);
        mETxtPassword = (EditText) view.findViewById(R.id.login_password);
        mBtnLogin = (Button) view.findViewById(R.id.btn_login);
        mBtnRegister = (Button) view.findViewById(R.id.btn_login_register);
    }

    //Start Activity when Register Button is CLicked
    private void onRegister(View view) {
        RegisterActivity.startActivity(getActivity());
    }

    //Cllaed When Log in Button is CLicked
    private void onLogin(View view) {
        String emailId = mETxtEmail.getText().toString();
        String password = mETxtPassword.getText().toString();
        if(!validateUser())
        {
            Toast.makeText(getActivity(),"Please! Enter the correct format of fields",Toast.LENGTH_LONG).show();
        }else
        {
            mLoginPresenter.login(getActivity(), emailId, password);
            mProgressDialog.show();
        }

    }

    //Set Dummy Credentials when Activity Starts
//    private void setDummyCredentials() {
//        mETxtEmail.setText("usama@mail.com");
//        mETxtPassword.setText("123456");
//    }

    //Call the Presenster and set progress dialogue
    private void init() {
        mLoginPresenter = new LoginPresenter(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.start_loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mBtnLogin.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        //setDummyCredentials();
    }
    private void saveData(String username,String email){
        SharedPreferences preferences = getActivity().getSharedPreferences("chat",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username",username);
        editor.putString("email",email);
        editor.commit();
    }
    private boolean validateUser() {
        if (mETxtEmail.getText().toString().equals("") || mETxtPassword.getText().toString().equals("")) {
            return false;
        }
        else {
            if(!mETxtEmail.getText().toString().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
                return false;
            }
            else {
                mETxtEmail.setText(mETxtEmail.getText().toString().trim());
                return true;
            }
        }
    }
}
