package com.blackgrapes.gyb_tyb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.jaredrummler.android.animations.Technique;

public class MainActivity extends AppCompatActivity {

    ImageView bg_img, logo_img;
    TextView name_txt;
    LottieAnimationView lottie_anim;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        sharedPreferences = getSharedPreferences("user", 0);
        editor = sharedPreferences.edit();

        //widgets
        bg_img = findViewById(R.id.bg_image);
        logo_img = findViewById(R.id.logo);
        name_txt = findViewById(R.id.textView);
        lottie_anim = findViewById(R.id.animation_lottie);

        bg_img.animate().translationY(-3300).setDuration(1500).setStartDelay(3000);
        logo_img.animate().translationY(2700).setDuration(1500).setStartDelay(3000);
        name_txt.animate().translationY(2400).setDuration(1500).setStartDelay(3000);
        lottie_anim.animate().translationY(1600).setDuration(1500).setStartDelay(3000);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String name = sharedPreferences.getString("uid","false");
                Intent intent;
                if(!name.equals("false")){
                    Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
                    intent = new Intent(getApplicationContext(), home_activity.class);
                    final Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


                            startActivity(intent);

                            finish();

                        }
                    }, 300);
                }else{
                    intent = new Intent(getApplicationContext(), register_activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);


                    startActivity(intent);

                    finish();
                }

            }
        }, 5000);
    }




    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}