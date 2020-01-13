package com.example.mychatapp.core.registeration;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mychatapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterInteractor implements RegisterContract.Interactor {
    private static final String TAG = RegisterInteractor.class.getSimpleName();
    private FirebaseAuth mAuthentication;
    private FirebaseUser mUser;
    private RegisterContract.OnRegistrationListener mOnRegistrationListener;
    private StorageReference mStorageReference;


    public RegisterInteractor(RegisterContract.OnRegistrationListener onRegistrationListener) {
        this.mOnRegistrationListener = onRegistrationListener;
    }

    public void UploadFile(Activity activity, String userid, Uri uri){
        mStorageReference = FirebaseStorage.getInstance().getReference("Images").child(userid);
        mStorageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri downloadUrl = taskSnapshot.getResult().getMetadata().getReference().getDownloadUri();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public void performFirebaseRegistration(final Activity activity, final String email, final String password, final String username, final String gender, final Uri profile_image) {
        mAuthentication.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.e(TAG, "performFirebaseRegistration:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            mOnRegistrationListener.onFailure(task.getException().getMessage());
                        } else {
                            // Add the user to users table.
                            DatabaseReference database= FirebaseDatabase.getInstance().getReference().child("users");
                            Log.e("sdfsd",task.getResult().getUser().getDisplayName()+"");
                            //User user = new User(task.getResult().getUser().getUid(), email,task.getResult().getUser().getDisplayName());
                            User user = new User(task.getResult().getUser().getUid(),
                                    username,
                                    email,
                                    task.getResult().getUser().getDisplayName(),
                                    password,
                                    gender);
                            database.child(task.getResult().getUser().getUid()).setValue(user);
                            UploadFile(activity,task.getResult().getUser().getUid(),profile_image);
                            mOnRegistrationListener.onSuccess(task.getResult().getUser());
                        }
                    }
                });
    }
}