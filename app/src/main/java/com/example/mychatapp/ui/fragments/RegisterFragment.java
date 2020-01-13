package com.example.mychatapp.ui.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mychatapp.R;
import com.example.mychatapp.core.registeration.RegisterContract;
import com.example.mychatapp.core.registeration.RegisterPresenter;
import com.example.mychatapp.core.users.add.AddUserContract;
import com.example.mychatapp.core.users.add.AddUserPresenter;
import com.example.mychatapp.ui.activities.LoginActivity;
import com.example.mychatapp.ui.activities.UserListingActivity;
import com.example.mychatapp.utils.AlertBox;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class RegisterFragment extends Fragment implements View.OnClickListener, RegisterContract.View, AddUserContract.View {
    private static final String TAG = RegisterFragment.class.getSimpleName();

    private RegisterPresenter mRegisterPresenter;
    private AddUserPresenter mAddUserPresenter;

    private EditText mETxtEmail, mETxtPassword, mETxtConfirmPassword, mETxtUsername;
    private RadioButton mRadioMale, mRadioFemale;
    String mGender;
    private Button mBtnRegister;

    private ProgressDialog mProgressDialog;
    //User Image
    Integer mUserProfile;
    private ImageView mProfileImage;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri image_uri;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_register, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }
    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId){
            case R.id.profile_image:
                setProfileImage();
                break;
        }

        switch (viewId) {
            case R.id.radio_male:
                Log.i("Male","Selected>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                mGender = "Male";
                break;
            case R.id.radio_female:
                Log.i("FeMale","Selected>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                mGender = "Female";
                break;
        }

        switch (viewId) {
            case R.id.btn_register:
                onRegister(view);
                break;
        }
    }

    private void setProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            image_uri = data.getData();
            try {
                 bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);
                // Log.d(TAG, String.valueOf(bitmap));
                mProfileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onRegistrationSuccess(FirebaseUser firebaseUser) {
        mProgressDialog.setMessage(getString(R.string.adding_user_todb));
        Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_SHORT).show();
            LoginActivity.startIntent(getActivity());
        //mAddUserPresenter.addUser(getActivity().getApplicationContext(), firebaseUser);
    }

    @Override
    public void onRegistrationFailure(String message) {
        mProgressDialog.dismiss();
        mProgressDialog.setMessage(getString(R.string.please_wait));
        Log.e(TAG, "onRegistrationFailure: " + message);
        Toast.makeText(getActivity(), "Registration failed!+\n" + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAddUserSuccess(String message) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("chat", MODE_PRIVATE).edit();
        editor.putString("email", mETxtEmail.getText().toString());
        editor.apply();
        sendEmailVerification();
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        UserListingActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

    }
    @Override
    public void onAddUserFailure(String message) {
        mProgressDialog.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private void sendEmailVerification() {
    }

    private void onRegister(View view) {
        String emailId = mETxtEmail.getText().toString();
        String password = mETxtPassword.getText().toString();
        String username = mETxtUsername.getText().toString();
        String gender = mGender;
        if(bitmap == null){
            if(gender.equals("Male")){
                Log.i("mALE",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                image_uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getActivity().getResources().getResourcePackageName(R.drawable.male_50)
                        + '/' + getActivity().getResources().getResourceTypeName(R.drawable.male_50)
                        + '/' + getActivity().getResources().getResourceEntryName(R.drawable.male_50) );
            }
            else if (gender.equals("Female")){
                Log.i("FemALE",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                image_uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                        "://" + getActivity().getResources().getResourcePackageName(R.drawable.female_50)
                        + '/' + getActivity().getResources().getResourceTypeName(R.drawable.female_50)
                        + '/' + getActivity().getResources().getResourceEntryName(R.drawable.female_50));
            }
        }
        if(!validateUser())
        {
            AlertBox.showMessage("Your Email is not Valid","Email Issue",getContext());
        }
        else {
            if (!mETxtPassword.getText().toString().equals(mETxtConfirmPassword.getText().toString())){
                AlertBox.showMessage("Password is not Confirmed","Password Issue",getContext());
            }
            else {
                mRegisterPresenter.register(getActivity(), emailId, password, username, gender, image_uri);
                mProgressDialog.show();
            }
        }

    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0,      decodedByte.length);
    }


    private boolean validateUser() {
        if (mETxtUsername.getText().toString().equals("") || mETxtEmail.getText().toString().equals("") || mETxtPassword.getText().toString().equals("") || mETxtConfirmPassword.getText().toString().equals("")) {
            return false;
        }
        else {
            if(!mETxtEmail.getText().toString().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
                return false;
            }
            else {
                mETxtUsername.setText(mETxtUsername.getText().toString().trim());
                mETxtEmail.setText(mETxtEmail.getText().toString().trim());
                return true;
            }
        }
    }

    private void bindViews(View view) {
        bitmap = null;
        mETxtUsername = (EditText) view.findViewById(R.id.register_username);
        mETxtEmail = (EditText) view.findViewById(R.id.register_email);
        mETxtPassword = (EditText) view.findViewById(R.id.register_password);
        mETxtConfirmPassword = (EditText) view.findViewById(R.id.register_confirm_password);
        mRadioMale = (RadioButton) view.findViewById(R.id.radio_male);
        mRadioFemale = (RadioButton) view.findViewById(R.id.radio_female);
        mBtnRegister = (Button) view.findViewById(R.id.btn_register);
        mProfileImage = (ImageView) view.findViewById(R.id.profile_image);
    }

    private void init() {
        mRegisterPresenter = new RegisterPresenter(this);
        mAddUserPresenter = new AddUserPresenter(this);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mRadioFemale.setOnClickListener(this);
        mRadioMale.setOnClickListener(this);
        mProfileImage.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }


}
