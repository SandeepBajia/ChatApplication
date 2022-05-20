package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class personal extends AppCompatActivity {


    private static final int Select_picture = 100 ;
    private static String mUsername ;
    private static int RC_SIGN_IN = 1;
    private Button sendbutton;
    private ImageButton photoPickButton ;
    private ListView listView;
    private ArrayList<Message> arrayList;
    private MessageAdapter messageAdapter;
    private EditText editText;
    private ProgressBar mprogressBar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mdatabaseReference;
    private FirebaseStorage mfirebaseStorage ;
    private StorageReference mstorageReference ;
    private ChildEventListener mChildEventListener;
    private Date mdate ;
    private long mtime ;
    private SimpleDateFormat mdateFormat ;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        Intent intent = getIntent();


        String name = intent.getStringExtra("receiver") ;
        String str = intent.getStringExtra("message") ;
        mUsername = intent.getStringExtra("you") ;

        setTitle(name);


        listView = findViewById(R.id.messageListView) ;
        arrayList = new ArrayList<>() ;
        messageAdapter = new MessageAdapter(this , R.layout.item_message , arrayList) ;
        listView.setAdapter(messageAdapter);
        firebaseDatabase = FirebaseDatabase.getInstance() ;
        mdatabaseReference = firebaseDatabase.getReference().child(str) ;
        mfirebaseStorage = FirebaseStorage.getInstance() ;
        mstorageReference = mfirebaseStorage.getReference().child(str) ;
        editText = findViewById(R.id.messageEditText) ;
        sendbutton =  findViewById(R.id.sendButton) ;
        photoPickButton = findViewById(R.id.photoPickerButton) ;
        mprogressBar = findViewById(R.id.progressBar) ;
        mprogressBar.setVisibility(View.INVISIBLE);
        mdateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss") ;


        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdate = new Date() ;

                mtime = mdate.getTime() ;
                Message m = new Message(mUsername , editText.getText().toString() , null , mdateFormat.format(mtime)) ;
                mdatabaseReference.push().setValue(m) ;
                editText.setText("");
            }
        });

        photoPickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent() ;
                intent.setType("image/*") ;
                intent.setAction(Intent.ACTION_GET_CONTENT) ;
                startActivityForResult(Intent.createChooser(intent , "Select Picture") , Select_picture );
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() > 0){
                    sendbutton.setEnabled(true);
                }
                else {
                    sendbutton.setEnabled(false );
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

       attachOnChildListener();







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Select_picture){
            if(resultCode == RESULT_OK){
                Uri imageUri = data.getData() ;
                StorageReference photoRef = mstorageReference.child(imageUri.getLastPathSegment()) ;
                UploadTask uploadTask = photoRef.putFile(imageUri)  ;

                ////////////////////////////////////////////////////////////////////////////////////////////////////

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return photoRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            mdate = new Date() ;
                            mtime = mdate.getTime() ;
                            mdatabaseReference.push().setValue(new Message(mUsername , null , downloadUri.toString() , mdateFormat.format(mtime))) ;
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });











                ////////////////////////////////////////////////////////////////////////////////////////////////////////////

            }
        }
    }

    private void  attachOnChildListener(){

        mdatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class) ;
                //message.setName("You");
                messageAdapter.add(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;


    }
}

 /*sendbutton = (Button) findViewById(R.id.sendButton)  ;
        listView = findViewById(R.id.messageListView) ;
        arrayList = new ArrayList<>() ;
        messageAdapter = new MessageAdapter(this , R.layout.item_message ,  arrayList) ;
        //listView.setAdapter(messageAdapter);
        editText = findViewById(R.id.messageEditText) ;
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar) ;



        //mprogressBar.setVisibility(View.INVISIBLE);
        firebaseDatabase = FirebaseDatabase.getInstance() ;
         databaseReference = firebaseDatabase.getReference().child("messagess") ;
        mfirebaseAuth = FirebaseAuth.getInstance() ;

          attachOnChildListener();


        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message messages  = new Message( mUsername ,  editText.getText().toString() , null) ;

                databaseReference.push().setValue(messages);

                editText.setText("");
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length() > 0){
                    sendbutton.setEnabled(true);
                }
                else {
                    sendbutton.setEnabled(false) ;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



    }

   private void attachOnChildListener(){
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message =  snapshot.getValue(Message.class) ;
                Log.d("mName", message.getName());
                Log.d("mMessage", message.getMessage());
                messageAdapter.add(message) ;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        } ;
        databaseReference.addChildEventListener(mChildEventListener) ;

    }
} */