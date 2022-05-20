package com.example.chatapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String CONTENT_AUTHORITY = "com.example.android.chatapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final int  RC_SIGNIN = 1 ;
    public static final int Profile_Photo = 10 ;

    public static  String mUsername = ""   ;
    private ListView listView ;
    private ArrayList<Friend> arrayList ;
    private FriendAdapter mfriendAdapter ;
    private FirebaseDatabase mfirebaseDatabase ;
    private DatabaseReference mdatabaseReference ;
    private ChildEventListener mchildEventListener ;
    private FirebaseAuth mfirebaseAuth ;
    private FirebaseAuth.AuthStateListener mauthStateListener ;
    private FirebaseMessaging mfirebaseMessaging ;
    private DatabaseReference databaseReference ;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview) ;
        arrayList = new ArrayList<>() ;
        //arrayList.add(new Friend("sandeep" , null)) ;
        //arrayList.add(new Friend("anand" , null)) ;
        mfriendAdapter = new FriendAdapter(this , R.layout.users , arrayList) ;
        listView.setAdapter(mfriendAdapter) ;

        mfirebaseDatabase = FirebaseDatabase.getInstance() ;
        mdatabaseReference = mfirebaseDatabase.getReference().child("Userss") ;
        databaseReference = mfirebaseDatabase.getReference()  ;
        mfirebaseAuth = FirebaseAuth.getInstance() ;






        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this , personal.class) ;

                Friend friend  = (Friend) adapterView.getItemAtPosition(i) ;
                String string = friend.getMusername() ;


                intent.putExtra("receiver" , string ) ;
                intent.putExtra("you" , mUsername) ;

                 if(string.compareTo(mUsername)  >=0  ){
                    string = string + mUsername ;
                }
                else {
                    string = mUsername + string ;
                }
                intent.putExtra("message" , string ) ;
                startActivity(intent);
            }
        }) ;





        mauthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser() ;
                if(user != null ) {
                    mUsername = user.getDisplayName() ;
                    onSignedInitialize(user.getDisplayName());

                }
                else {

                    List<AuthUI.IdpConfig> provider = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()) ;
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
                            .setAvailableProviders(provider).build() ,  RC_SIGNIN);
                }
            }
        } ;



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGNIN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this , SettingActivity.class ) ;
                startActivity(intent);
                if(mUsername.compareTo("") ==0){
                    finish();
                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign In Canceled ", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if(requestCode == Profile_Photo ){
            if(resultCode == RESULT_OK){
                Uri profileUri = data.getData() ;

                //mdatabaseReference.child(mUsername).child(new Friend(mUsername , ))
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mfirebaseAuth.addAuthStateListener(mauthStateListener) ;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.setting:
                Intent i = new Intent(MainActivity.this , SettingActivity.class) ;
                startActivity(i);
                return true ;
            case R.id.signout:
                //mdatabaseReference.child(mUsername).removeValue() ;
                AuthUI.getInstance().signOut(this) ;
                return true ;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater() ;
        menuInflater.inflate(R.menu.menu , menu);
        return true ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mfirebaseAuth.removeAuthStateListener(mauthStateListener);
        deAttachOnChildListener();
        mfriendAdapter.clear();

    }

    private void onSignedInitialize(String name ){
       // mUsername = name ;
        Friend f = new Friend(mUsername , "https://firebasestorage.googleapis.com/v0/b/chatapp-50e0e.appspot.com/o/default%20profile%20image.jpeg?alt=media&token=061c60fd-5aec-4ed5-b015-06f6f26fd891") ;
        //mdatabaseReference.child(mUsername).setValue(f) ;


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        databaseReference = databaseReference.child(mUsername) ;

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Friend friend = snapshot.getValue(Friend.class) ;
                if(friend== null ){
                    mdatabaseReference.child(mUsername).setValue(f) ;
                }
                else {
                    mdatabaseReference.child(mUsername).setValue(new Friend(mUsername , friend.mimageurl)) ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }) ;



        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        attachOnChildListener();

    }

    private void attachOnChildListener(){
        mchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Friend fr = snapshot.getValue(Friend.class) ;

                if(fr.getMusername().compareTo(mUsername) !=0   ){
                    Log.d(mUsername , fr.getMusername()) ;
                    mfriendAdapter.add(fr);
                }
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
        mdatabaseReference.addChildEventListener(mchildEventListener) ;




    }

    private void deAttachOnChildListener(){
        mdatabaseReference.removeEventListener(mchildEventListener);
    }


}

