package com.example.mychatapp.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapp.APICalls;
import com.example.mychatapp.Constants;
import com.example.mychatapp.R;
import com.example.mychatapp.models.Chat;
import com.example.mychatapp.utils.Global;
import com.example.mychatapp.utils.TranslateResponse;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;

public class ChatAdapter extends RecyclerView.Adapter{
    Context context;
    List<Chat> list;


    public ChatAdapter(Context context, List<Chat> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if(this.list.get(position).sender_name == Global.CURRENT_USER_NAME) {
            return 0;
        }
        else
            return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0){
            ChatMineHolder chatMineHolder = new ChatMineHolder(inflater.inflate(R.layout.chat_mine, parent, false));
            return chatMineHolder;
        }
        else {
            ChatOtherHolder chatOtherHolder = new ChatOtherHolder(inflater.inflate(R.layout.chat_other, parent, false));
            return chatOtherHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat ch = this.list.get(position);
        String sender, message, sender_image;
        //Assign values
        sender = this.list.get(position).getSender_name();
        message = this.list.get(position).getMessage();
        sender_image = this.list.get(position).getSender_image();
        if (this.list.get(position).sender_name == Global.CURRENT_USER_NAME) {
            ChatMineHolder chatMineHolder = (ChatMineHolder) holder;
            ((ChatMineHolder) holder).sender.setText("You");
            if (Global.LANGUAGE == "Urdu"){
                translateText(ch.getMessage(),"en-ur",((ChatMineHolder) holder).message);
            }
            else {
                ((ChatMineHolder) holder).message.setText(message);
            }
            Picasso.with(this.context).load(Uri.parse(sender_image)).into(((ChatMineHolder) holder).sender_image);
        }
        else{
            ChatOtherHolder chatMineHlder = (ChatOtherHolder) holder;
            ((ChatOtherHolder) holder).sender.setText("Him");
            if (Global.LANGUAGE == "Urdu"){
                translateText(ch.getMessage(),"en-ur",((ChatOtherHolder) holder).message);
            }
            else{
                ((ChatOtherHolder) holder).message.setText(message);
            }
            Picasso.with(this.context).load(Uri.parse(sender_image)).into(((ChatOtherHolder) holder).sender_image);
        }
    }

    private void translateText(final String text, final String lang, final TextView message) {

        class getQData extends AsyncTask<Void, Void, Void> {
            String text_to_return ="";
            // ProgressDialog loading;
            String ROOT_URL = "https://translate.yandex.net";

            Retrofit adapter = new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();
            APICalls api = adapter.create(APICalls.class);

            @Override
            protected Void doInBackground(Void... voids) {
                String key = Constants.MY_KEY;
                Log.e("key",key+"|"+lang);
                String lluange = lang;
                Call<TranslateResponse> call = api.translate(key, text, lluange);
                call.enqueue(new Callback<TranslateResponse>() {
                    @Override
                    public void onResponse(retrofit.Response<TranslateResponse> response, Retrofit retrofit) {
                        Log.d("succ", "onResponse:code" + String.valueOf(response.code()));
                        Log.d("error mesg", String.valueOf(response.message()));
                        switch (response.code()) {
                            case 200:
                                TranslateResponse tr = response.body();
                                text_to_return = tr.getText().get(0);
                                message.setText(text_to_return);
                                break;
                            default:

                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("retro error", t.getMessage());
                    }
                });
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        }

        getQData ru = new getQData();
        try {
            ru.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatMineHolder extends RecyclerView.ViewHolder{

        public TextView sender;
        public TextView message;
        public CircleImageView sender_image;
        public LinearLayout linearLayout;

        public ChatMineHolder(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.sender);
            message = itemView.findViewById(R.id.message);
            sender_image = itemView.findViewById(R.id.sender_image);
            linearLayout = itemView.findViewById(R.id.chat_mine_layout);
        }
    }

    public class ChatOtherHolder extends RecyclerView.ViewHolder{

        public TextView sender;
        public TextView message;
        public CircleImageView sender_image;
        public LinearLayout linearLayout;

        public ChatOtherHolder(@NonNull View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.other_sender);
            message = itemView.findViewById(R.id.other_message);
            sender_image = itemView.findViewById(R.id.other_sender_image);
            linearLayout = itemView.findViewById(R.id.chat_other_layout);
        }
    }
}
