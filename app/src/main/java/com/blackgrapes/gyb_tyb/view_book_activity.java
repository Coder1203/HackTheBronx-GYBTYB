package com.blackgrapes.gyb_tyb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.android.animations.Technique;

import java.io.File;

public class view_book_activity extends AppCompatActivity {

    ImageView book_img;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;
    CircularProgressButton upload_btn;
    EditText title_edt, topic_edt, writer_edt;
    TextView details_txt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar loading;
    CardView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        details_txt = findViewById(R.id.address_etc);
        book_img = findViewById(R.id.book_img);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        upload_btn = findViewById(R.id.req_btn);
        title_edt = findViewById(R.id.editTextTitle);
        topic_edt = findViewById(R.id.editTextTopic);
        writer_edt = findViewById(R.id.editTextWriter);
        sharedPreferences = getSharedPreferences("user", 0);
        editor = sharedPreferences.edit();
        loading = findViewById(R.id.loading);
        back_btn = findViewById(R.id.back_btn);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(view_book_activity.this, home_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();

                    }
                }, 300);
            }
        });
        title_edt.setFocusable(false);
        topic_edt.setFocusable(false);
        writer_edt.setFocusable(false);

        final String[] email = new String[1];
        final String[] name = new String[1];
        final String[] number = new String[1];


        Bundle bundle = getIntent().getExtras();
        String index_str =bundle.getString("index");

        FirebaseDatabase.getInstance().getReference().child("Books").child(index_str).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                String title_str = task.getResult().child("title").getValue().toString();
                String topic_str = task.getResult().child("topic").getValue().toString();
                String writer_str = task.getResult().child("writer").getValue().toString();
                String img_ID = task.getResult().child("image").getValue().toString();
                String value = "State : ";


                if((task.getResult().child("email").getValue().toString()).equals(sharedPreferences.getString("email","error"))){
                    upload_btn.setVisibility(View.GONE);
                }
                email[0] = task.getResult().child("email").getValue().toString();
                name[0] = task.getResult().child("title").getValue().toString();
                number[0] = task.getResult().child("number").getValue().toString();
                value+=task.getResult().child("state").getValue().toString()+"\nCity : ";
                value+=task.getResult().child("city").getValue().toString()+"\nEmail : ";
                value+=task.getResult().child("email").getValue().toString()+"\nAddress line 1 : ";
                value+=task.getResult().child("address").getValue().toString()+"\nNumber : ";
                value+=task.getResult().child("number").getValue().toString();

                details_txt.setText(value);



                storageReference = FirebaseStorage.getInstance().getReference(img_ID);
                try{

                    File localfile = File.createTempFile("tempfile",".jpg");
                    storageReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            book_img.setImageBitmap(bitmap);
                            loading.setVisibility(View.GONE);
                            book_img.setVisibility(View.VISIBLE);
                        }
                    });

                }catch (Exception e){

                    Toast.makeText(view_book_activity.this, "Error occured", Toast.LENGTH_SHORT).show();

                }

                title_edt.setText(title_str);
                topic_edt.setText(topic_str);
                writer_edt.setText(writer_str);

            }
        });
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mail_obj = new Intent(Intent.ACTION_SEND);
                mail_obj.putExtra(Intent.EXTRA_EMAIL,email);
                mail_obj.putExtra(Intent.EXTRA_SUBJECT,"I want your book "+name[0]+" uploaded at GYB TYB");
                mail_obj.putExtra(Intent.EXTRA_TEXT,"I want your book "+name[0]+" uploaded at GYB TYB");
                mail_obj.putExtra(Intent.EXTRA_PHONE_NUMBER,number[0]);
                mail_obj.setType("message/rfc882");
                startActivity(Intent.createChooser(mail_obj,"Choose an email client : "));
            }
        });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();\
        Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(view_book_activity.this, home_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

            }
        }, 300);
    }
}