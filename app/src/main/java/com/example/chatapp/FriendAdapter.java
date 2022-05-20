package com.example.chatapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class FriendAdapter extends ArrayAdapter<Friend> {
        public FriendAdapter(@NonNull Context context, int resource, @NonNull List<Friend> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView == null){
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.users, parent, false);
            }

            TextView username = convertView.findViewById(R.id.usernameTextView) ;
            ImageView profilePicture = convertView.findViewById(R.id.profileImageView) ;

            Friend user = getItem(position) ;
            if(user.getMimageurl() == null){
                username.setText(user.getMusername());

                //Glide.with(profilePicture.getContext()).load("https://firebasestorage.googleapis.com/v0/b/chatapp-50e0e.appspot.com/o/default%20profile%20image.jpeg?alt=media&token=061c60fd-5aec-4ed5-b015-06f6f26fd891").fitCenter().into(profilePicture)  ;

            }

            else {
                username.setText(user.getMusername());
                Glide.with(profilePicture.getContext()).load(user.getMimageurl()).fitCenter().into(profilePicture)  ;
            }


            return convertView ;

        }
    }


