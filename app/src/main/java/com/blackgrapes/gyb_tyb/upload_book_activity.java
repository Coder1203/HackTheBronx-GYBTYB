package com.blackgrapes.gyb_tyb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.android.animations.Technique;

import java.util.UUID;

import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class upload_book_activity extends AppCompatActivity {

    ImageView book_img;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    CircularProgressButton upload_btn;
    EditText title_edt, topic_edt, writer_edt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        book_img = findViewById(R.id.book_img);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        upload_btn = findViewById(R.id.upload_btn);
        title_edt = findViewById(R.id.editTextTitle);
        topic_edt = findViewById(R.id.editTextTopic);
        writer_edt = findViewById(R.id.editTextWriter);
        sharedPreferences = getSharedPreferences("user", 0);
        editor = sharedPreferences.edit();

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(book_img.getDrawable() != getDrawable(R.drawable.book_upload) && topic_edt.getText().toString().trim().length() > 0 && title_edt.getText().toString().trim().length() > 0 && writer_edt.getText().toString().trim().length() > 0){

                    uploadpicture();

                }else{

                    MotionToast.Companion.createToast(upload_book_activity.this,
                            "Upload Failed!",
                            "Please provide all the details and upload the image for the book!",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.SHORT_DURATION,
                            ResourcesCompat.getFont(upload_book_activity.this, www.sanju.motiontoast.R.font.helvetica_regular));

                }

            }
        });

        book_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

    }
    public void choosePicture(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode==RESULT_OK && data!= null && data.getData()!=null){

            imageUri = data.getData();
            book_img.setImageURI(imageUri);
        }
    }

    public void uploadpicture(){
        String randomKey = UUID.randomUUID().toString();
        StorageReference book_img = storageReference.child(randomKey+imageUri.getLastPathSegment());
        book_img.putFile(imageUri);
        Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(upload_book_activity.this, home_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

            }
        }, 300);
        FirebaseDatabase.getInstance().getReference().child("Books").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Integer num_book_int = Integer.parseInt(task.getResult().child("number").getValue().toString());
                num_book_int++;
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("index").setValue(num_book_int);
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("title").setValue(title_edt.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("writer").setValue(writer_edt.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("image").setValue(randomKey+imageUri.getLastPathSegment());
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("topic").setValue(topic_edt.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("email").setValue(sharedPreferences.getString("email","Not provided"));
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("number").setValue(sharedPreferences.getString("number","Not provided"));
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("state").setValue(sharedPreferences.getString("state","Not provided"));
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("city").setValue(sharedPreferences.getString("city","Not provided"));
                FirebaseDatabase.getInstance().getReference().child("Books").child(String.valueOf(num_book_int)).child("address").setValue(sharedPreferences.getString("address","Not provided"));
                FirebaseDatabase.getInstance().getReference().child("Books").child("number").setValue(num_book_int);
                Integer finalNum_book_int = num_book_int;
                FirebaseDatabase.getInstance().getReference().child(sharedPreferences.getString("uid","error")).child("Books").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                        Integer num_book_int_my = Integer.parseInt(task.getResult().child("number").getValue().toString());
                        num_book_int_my++;
                        FirebaseDatabase.getInstance().getReference().child(sharedPreferences.getString("uid","error")).child("Books").child(String.valueOf(num_book_int_my)).child("index").setValue(finalNum_book_int);
                        FirebaseDatabase.getInstance().getReference().child(sharedPreferences.getString("uid","error")).child("Books").child(String.valueOf(num_book_int_my)).child("title").setValue(title_edt.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child(sharedPreferences.getString("uid","error")).child("Books").child(String.valueOf(num_book_int_my)).child("writer").setValue(writer_edt.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child(sharedPreferences.getString("uid","error")).child("Books").child("number").setValue(num_book_int_my);

                    }
                });
            }
        });
        MotionToast.Companion.createToast(this,
                "Success",
                "Uploaded successfully!",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular));



    }

}