package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SettingActivity extends AppCompatActivity {



    public static final int Profile_Photo = 10 ;
    private Button meditUsernameButton ;
    private EditText  mEditUsername ;
    private Button editPhotoButton ;
    private ImageView profilePhoto ;
    private DatabaseReference mdatabaseReference ;
    private StorageReference mstorageReference ;
    private ValueEventListener mvalueEventListener ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("Setting");

        meditUsernameButton = (Button) findViewById(R.id.editUsernameButton) ;
        mEditUsername = (EditText)  findViewById(R.id.editUsername) ;
        meditUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mUsername = mEditUsername.getText().toString() ;
                mEditUsername.setText("");
            }
        });
        editPhotoButton = findViewById(R.id.editProfilePicture) ;
        profilePhoto = findViewById(R.id.settingProfileImage) ;
        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child(MainActivity.mUsername);

        mstorageReference = FirebaseStorage.getInstance().getReference();
        editPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent() ;
                intent.setAction(Intent.ACTION_GET_CONTENT) ;
                intent.setType("image/*") ;
                startActivityForResult(Intent.createChooser(intent , "select profile photo") , Profile_Photo );
            }
        });
    }
    private void addValueEventListener(){
        mvalueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Friend f = snapshot.getValue(Friend.class) ;
                if(f== null ){

                }
                else {
                    Glide.with(profilePhoto.getContext()).load(f.getMimageurl()).fitCenter().centerCrop().into(profilePhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } ;
        mdatabaseReference.addValueEventListener(mvalueEventListener) ;

    }
    @Override
    protected  void onResume(){
        super.onResume();
        addValueEventListener();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mdatabaseReference.removeEventListener(mvalueEventListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Profile_Photo){
            if(resultCode == RESULT_OK){
                Uri imageUri = data.getData() ;
                StorageReference photoRef = mstorageReference.child(imageUri.getLastPathSegment()) ;
                UploadTask uploadTask = photoRef.putFile(imageUri) ;
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
                           mdatabaseReference.setValue(new Friend(MainActivity.mUsername.toString() , downloadUri.toString() )) ;
                            //mdatabaseReference.push().setValue(new Message(mUsername , null , downloadUri.toString() , mdateFormat.format(mtime))) ;
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });

            }
        }
    }
}

