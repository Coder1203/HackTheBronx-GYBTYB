package com.blackgrapes.gyb_tyb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.android.animations.Technique;

public class home_activity extends AppCompatActivity {

    TextView txt1, txt3, name_txt, Book_no_txt;
    LinearLayout main_linear, book_linear;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CardView upload_btn,search_btn,my_book_btn, log_out_btn;
    RelativeLayout list_item;
    ImageView profile_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        sharedPreferences = getSharedPreferences("user", 0);
        editor = sharedPreferences.edit();
        String uid_str = sharedPreferences.getString("uid","false");
        txt1 = findViewById(R.id.t1);
        name_txt = findViewById(R.id.name_txt);
        main_linear = findViewById(R.id.main_linear);
        book_linear= findViewById(R.id.book_linear);
        Book_no_txt = findViewById(R.id.book_no);
        //txt3 = findViewById(R.id.t3);
        upload_btn = findViewById(R.id.upload_btn);
        search_btn = findViewById(R.id.search_btn);
        my_book_btn = findViewById(R.id.my_book_btn);
        //list_item = findViewById(R.id.list_item_rel);
        profile_img = findViewById(R.id.profile_img);
        log_out_btn = findViewById(R.id.log_out_btn);
        FacebookSdk.sdkInitialize(getApplicationContext());
        name_txt.setText(sharedPreferences.getString("name","false").toString());

        String city_str = sharedPreferences.getString("city","false");

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_linear));
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(home_activity.this, book_list_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();

                    }
                }, 300);
            }
        });
        my_book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_linear));
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(home_activity.this, my_books_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();

                    }
                }, 300);
            }
        });



        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_linear));
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(home_activity.this, upload_book_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();

                    }
                }, 300);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Books").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                String book_str = task.getResult().child("number").getValue().toString();
                Book_no_txt.setText(book_str);

            }
        });
        if(city_str.equals("false")){

            FirebaseDatabase.getInstance().getReference().child(uid_str).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {

                    if(task.getResult().child("city").exists()){

                        String city_need_Str = task.getResult().child("city").getValue().toString();
                        String state_need_str = task.getResult().child("state").getValue().toString();
                        String address_need_str = task.getResult().child("address").getValue().toString();

                        editor.putString("city",city_need_Str);
                        editor.putString("state",state_need_str);
                        editor.putString("address",address_need_str);
                        editor.commit();


                    }else{
                        Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_linear));
                        final Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(home_activity.this, create_profile_activity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();

                            }
                        }, 300);
                    }

                }
            });



        }


        log_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                GoogleSignIn.getClient(
                        getApplicationContext(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();
                editor.clear().commit();
                navigate();
            }
        });
        animation();

    }
    public void animation() {



        Animation down_anim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        Animation up_anim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation right_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation left_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);

        txt1.setAlpha(0.0f);
        //txt3.setAlpha(0.0f);
        name_txt.setAlpha(0.0f);
        main_linear.setAlpha(0.0f);
        book_linear.setAlpha(0.0f);
//        txt3.setAlpha(0.0f);
        upload_btn.setAlpha(0.0f);
        search_btn.setAlpha(0.0f);
        my_book_btn.setAlpha(0.0f);
//        list_item.setAlpha(0.0f);
        profile_img.setAlpha(0.0f);
        log_out_btn.setAlpha(0.0f);



        Technique.FADE_IN_UP.getComposer().duration(1000).playOn(main_linear);



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txt1.setAlpha(1.0f);
                name_txt.setAlpha(1.0f);
                book_linear.setAlpha(1.0f);
                profile_img.setAlpha(1.0f);

                Technique.BOUNCE_IN_DOWN.getComposer().duration(1000).playOn(book_linear);
                txt1.startAnimation(left_in);
                name_txt.startAnimation(left_in);
                profile_img.startAnimation(right_in);
                //book_linear.startAnimation(down_anim);

            }
        }, 1100);
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                upload_btn.setAlpha(1.0f);
                search_btn.setAlpha(1.0f);
                my_book_btn.setAlpha(1.0f);
                log_out_btn.setAlpha(1.0f);

                upload_btn.startAnimation(up_anim);
                search_btn.startAnimation(up_anim);
                my_book_btn.startAnimation(up_anim);
                Technique.BOUNCE_IN_RIGHT.getComposer().duration(1000).playOn(log_out_btn);

            }
        }, 700);

        /*final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                txt3.setAlpha(1.0f);

                txt3.startAnimation(left_in);



            }
        }, 1100);

         */

    }
    public void navigate() {

        Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_linear));
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(home_activity.this, register_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

            }
        }, 300);

    }
}