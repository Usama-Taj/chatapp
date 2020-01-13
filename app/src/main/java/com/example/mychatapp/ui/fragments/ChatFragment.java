package com.example.mychatapp.ui.fragments;

import androidx.fragment.app.Fragment;

import com.example.mychatapp.core.chat.ChatContract;
import com.example.mychatapp.models.Chat;

public class ChatFragment extends Fragment implements ChatContract.View
{

    @Override
    public void onSendMessageSuccess() {

    }

    @Override
    public void onSendMessageFailure(String message) {

    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {

    }

    @Override
    public void onGetMessagesFailure(String message) {

    }
}
