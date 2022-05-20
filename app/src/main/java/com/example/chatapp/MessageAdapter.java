package com.example.chatapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List ;


public class MessageAdapter extends ArrayAdapter<Message> {


    public MessageAdapter( Context context, int resource,  List<Message> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }
        Message message = getItem(position) ;
        TextView name =  convertView.findViewById(R.id.nameTextView) ;
        TextView editmessage  =  convertView.findViewById(R.id.messageTextView) ;
        ImageView imageView =  convertView.findViewById(R.id.photoImageView) ;



        if(message.getName().compareTo(MainActivity.mUsername) ==0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            editmessage.setLayoutParams(params);
            imageView.setLayoutParams(params);
            editmessage.setBackgroundColor(Color.parseColor("#34B7F1")) ;
        }
        else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT;
            editmessage.setLayoutParams(params);
            imageView.setLayoutParams(params);
            editmessage.setBackgroundColor(Color.WHITE);

        }

            boolean isPhoto = (message.getImageUrl() != null);
            if (isPhoto) {
                //editmessage.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                Glide.with(imageView.getContext()).load(message.getImageUrl()).fitCenter().into(imageView);
                editmessage.setText(message.getMtime());
            } else {
                imageView.setVisibility(View.GONE);
                editmessage.setVisibility(View.VISIBLE);
                editmessage.setText(message.getMessage() + "\n" + message.getMtime() );


            }

            name.setVisibility(View.GONE);


            return convertView;

    }
}
