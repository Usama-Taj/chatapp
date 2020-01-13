package com.example.mychatapp.core.registeration;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

//import com.google.firebase.auth.FirebaseUser;

public interface RegisterContract {
    interface View {
        void onRegistrationSuccess(FirebaseUser firebaseUser);

        void onRegistrationFailure(String message);
    }

    interface Presenter {
        void register(Activity activity, String email, String password, String username, String gender, Uri profile_image);
    }

    interface Interactor {
        void performFirebaseRegistration(Activity activity, String email, String password, String username, String gender, Uri profile_image);
    }

    interface OnRegistrationListener {
        void onSuccess(FirebaseUser firebaseUser);

        void onFailure(String message);
    }
}
