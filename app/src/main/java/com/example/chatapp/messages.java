package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;





import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class messages extends AppCompatActivity {

    private static String mUsername = "sandeep";
    private static int RC_SIGN_IN = 1;
    private Button sendbutton;
    private ListView listView;
    private ArrayList<Message> arrayList;
    private MessageAdapter messageAdapter;
    private EditText editText;
    private ProgressBar mprogressBar;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mauthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        ///////////////////////////////////////////////////

         /*   Intent intent = getIntent() ;
            Uri uri = intent.getData() ;
            setTitle(uri.getPath());

























            ///////////////////////////////////////////////////////////////////////////////////////////////

            sendbutton = (Button) findViewById(R.id.sendButton)  ;
            listView = findViewById(R.id.messageListView) ;
            arrayList = new ArrayList<>() ;
            messageAdapter = new MessageAdapter(this , R.layout.item_message ,  arrayList) ;
            listView.setAdapter(messageAdapter);
            editText = findViewById(R.id.messageEditText) ;
            mprogressBar = (ProgressBar) findViewById(R.id.progressBar) ;
            mprogressBar.setVisibility(View.INVISIBLE);
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




            /*mauthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser() ;
                    if(user != null){
                        //user sign in
                        onSignedInInitialize(user.getDisplayName());

                    }
                    else {
                        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build() );
                        startActivityForResult(AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(providers).build() , RC_SIGN_IN ) ;


                    }

                }
            } ; */


    }

      /*  @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater menuInflater = getMenuInflater() ;
            menuInflater.inflate(R.menu.menu , menu);
            return true ;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.signout:
                    AuthUI.getInstance().signOut(this) ;
                    return true ;
            }
            return super.onOptionsItemSelected(item) ;
        }

        @Override
        protected void onPause() {
            super.onPause();
            mfirebaseAuth.removeAuthStateListener(mauthStateListener);
            deAttachChildListener();
            messageAdapter.clear();
        }

        //@Override
       /* protected void onResume() {
            super.onResume();
            mfirebaseAuth.addAuthStateListener(mauthStateListener);
        } */

       /* private void onSignedInInitialize( String name ){
            mUsername = name ;
            attachOnChildListener();

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == RC_SIGN_IN){
                if(resultCode == RESULT_OK){
                    Toast.makeText(messages.this , "Signed In" , Toast.LENGTH_SHORT).show();
                }
                else if(resultCode == RESULT_CANCELED){
                    Toast.makeText(this, "Sign Is Canceled", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        private void onSignedOutCleanup(){
            mUsername = "sandeep" ;
            messageAdapter.clear();
            deAttachChildListener();
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

        private void deAttachChildListener(){
            databaseReference.removeEventListener(mChildEventListener);
        }
    }

        */
}


