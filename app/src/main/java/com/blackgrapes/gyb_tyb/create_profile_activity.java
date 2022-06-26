package com.blackgrapes.gyb_tyb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.android.animations.Technique;

public class create_profile_activity extends AppCompatActivity {

    EditText phone_edt, state_edt, city_edt, address_edt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CircularProgressButton confirm_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        phone_edt = findViewById(R.id.editTextMobile);
        state_edt = findViewById(R.id.editTextState);
        city_edt = findViewById(R.id.editTextCity);
        address_edt = findViewById(R.id.editTextAddress);
        confirm_btn = findViewById(R.id.cirConfirmButton);

        sharedPreferences = getSharedPreferences("user", 0);
        editor = sharedPreferences.edit();

        String number_stored_str = sharedPreferences.getString("number","false");
        String uid_str = sharedPreferences.getString("uid","false");
        if(!number_stored_str.equals("false")){

            phone_edt.setText(number_stored_str);
            phone_edt.setFocusable(false);

        }
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((!(TextUtils.isEmpty(city_edt.getText().toString()))) && (!(TextUtils.isEmpty(state_edt.getText().toString()))) && (!(TextUtils.isEmpty(address_edt.getText().toString()))) && (!(TextUtils.isEmpty(phone_edt.getText().toString()))))

                FirebaseDatabase.getInstance().getReference().child(uid_str).child("city").setValue(city_edt.getText().toString());
                FirebaseDatabase.getInstance().getReference().child(uid_str).child("state").setValue(state_edt.getText().toString());
                FirebaseDatabase.getInstance().getReference().child(uid_str).child("address").setValue(address_edt.getText().toString());
                FirebaseDatabase.getInstance().getReference().child(uid_str).child("number").setValue(phone_edt.getText().toString());
                FirebaseDatabase.getInstance().getReference().child(uid_str).child("Books").child("number").setValue(0);

                editor.putString("city",city_edt.getText().toString());
                editor.putString("state", state_edt.getText().toString());
                editor.putString("address",address_edt.getText().toString());
                editor.putString("number",phone_edt.getText().toString());
                editor.commit();


                Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(create_profile_activity.this, home_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();

                    }
                }, 300);

            }
        });



    }
}