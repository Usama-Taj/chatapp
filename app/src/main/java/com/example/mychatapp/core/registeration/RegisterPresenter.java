package com.example.mychatapp.core.registeration;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

//import com.google.firebase.auth.FirebaseUser;

public class RegisterPresenter implements RegisterContract.Presenter, RegisterContract.OnRegistrationListener {
    private RegisterContract.View mRegisterView;
    private RegisterInteractor mRegisterInteractor;

    public RegisterPresenter(RegisterContract.View registerView) {
        this.mRegisterView = registerView;
        mRegisterInteractor = new RegisterInteractor(this);
    }

    @Override
    public void register(final Activity activity,final String email,final String password , final String username, final String gender, final Uri profile_image) {
        mRegisterInteractor.performFirebaseRegistration(activity, email, password,username, gender, profile_image);
    }
    @Override
    public void onSuccess(FirebaseUser firebaseUser) {
        mRegisterView.onRegistrationSuccess(firebaseUser);
    }

    @Override
    public void onFailure(String message) {
        mRegisterView.onRegistrationFailure(message);
    }
}