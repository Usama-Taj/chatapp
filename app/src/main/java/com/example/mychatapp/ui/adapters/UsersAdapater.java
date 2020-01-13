package com.example.mychatapp.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychatapp.R;
import com.example.mychatapp.models.UsersListing;
import com.example.mychatapp.ui.activities.ChatActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapater extends RecyclerView.Adapter<UsersAdapater.UsersHolder> {
    Context context;
    List<UsersListing> list;

    public UsersAdapater(Context context, List<UsersListing> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater;
        UsersHolder usersHolder;

        inflater = LayoutInflater.from(this.context);
        usersHolder = new UsersHolder(inflater.inflate(R.layout.users_layout, parent,false));
        return usersHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersHolder holder, final int position) {
        String userimage, username, useremail;
        //Assign Values
        username = this.list.get(position).getUsername();
        userimage = this.list.get(position).getUserimage();
        useremail = this.list.get(position).getUseremail();

        //Set Holder
        holder.user_name.setText(username);
        holder.user_email.setText(useremail);
            Picasso.with(this.context).load(Uri.parse(userimage)).into(holder.user_image);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"Helllllllllllllllllll",Toast.LENGTH_SHORT).show();

                ChatActivity.startActivity(context,UsersAdapater.this.list.get(position).getUsername(),UsersAdapater.this.list.get(position).getUseremail(),UsersAdapater.this.list.get(position).getUserimage());
                //start chat activity
            }
        });
    }
    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class UsersHolder extends RecyclerView.ViewHolder{
        public TextView user_name;
        public TextView user_email;
        public CircleImageView user_image;
        public LinearLayout linearLayout;

        public UsersHolder(@NonNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.user_name);
            user_image = itemView.findViewById(R.id.user_image);
            user_email = itemView.findViewById(R.id.user_email);
            linearLayout = itemView.findViewById(R.id.nested_layout);
        }
    }
}
