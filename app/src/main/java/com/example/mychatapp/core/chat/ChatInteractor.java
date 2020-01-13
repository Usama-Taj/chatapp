package com.example.mychatapp.core.chat;

import android.content.Context;

import com.example.mychatapp.models.Chat;

public class ChatInteractor implements ChatContract.Interactor {
    private static final String TAG = "ChatInteractor";

    private ChatContract.OnSendMessageListener mOnSendMessageListener;
    private ChatContract.OnGetMessagesListener mOnGetMessagesListener;

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener) {
        this.mOnSendMessageListener = onSendMessageListener;
    }

    public ChatInteractor(ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    public ChatInteractor(ChatContract.OnSendMessageListener onSendMessageListener,
                          ChatContract.OnGetMessagesListener onGetMessagesListener) {
        this.mOnSendMessageListener = onSendMessageListener;
        this.mOnGetMessagesListener = onGetMessagesListener;
    }

    @Override
    public void sendMessageToFirebaseUser(final Context context, final Chat chat, final String receiverFirebaseToken) {

    }

    private void sendPushNotificationToReceiver(String username, String message, String uid, String firebaseToken, String receiverFirebaseToken) {
    }

    @Override
    public void getMessageFromFirebaseUser(String senderUid, String receiverUid) {
    }
}

