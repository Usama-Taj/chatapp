package com.example.mychatapp.core.users.add;

import android.content.Context;

//import com.google.firebase.auth.FirebaseUser;

public class AddUserInteractor implements AddUserContract.Interactor {
    private AddUserContract.OnUserDatabaseListener mOnUserDatabaseListener;

    public AddUserInteractor(AddUserContract.OnUserDatabaseListener onUserDatabaseListener) {
        this.mOnUserDatabaseListener = onUserDatabaseListener;
    }

//    @Override
//    public void addUserToDatabase(final Context context, FirebaseUser firebaseUser) {
//    }
}
