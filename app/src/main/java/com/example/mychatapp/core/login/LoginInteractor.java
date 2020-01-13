package com.example.mychatapp.core.login;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mychatapp.utils.Constants;
import com.example.mychatapp.utils.SharedPrefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LoginInteractor {

    private LoginContract.OnLoginListener mOnLoginListener;

    public LoginInteractor(LoginContract.OnLoginListener onLoginListener) {
        this.mOnLoginListener = onLoginListener;
    }

    public void performFirebaseLogin(final Activity activity,final String email,final String password) {
        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "performFirebaseLogin:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            mOnLoginListener.onSuccess(task.getResult().toString());
                            updateFirebaseToken(task.getResult().getUser().getUid(),
                                    new SharedPrefUtil(activity.getApplicationContext()).getString(Constants.ARG_FIREBASE_TOKEN, null));
                        } else {
                            mOnLoginListener.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }
    private void updateFirebaseToken(String uid, String token) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.ARG_USERS)
                .child(uid)
                .child(Constants.ARG_FIREBASE_TOKEN)
                .setValue(token);
    }
}
