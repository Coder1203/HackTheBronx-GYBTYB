package com.blackgrapes.gyb_tyb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apachat.loadingbutton.core.customViews.CircularProgressButton;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.android.animations.Technique;

import org.w3c.dom.Text;

public class login_activity extends AppCompatActivity {

    TextView sign_up_txt, forget_txt;
    TextInputLayout password_lay, email_lay;
    ImageView google_btn, facebook_btn;
    CircularProgressButton login_btn;
    GoogleApiClient apiClient;
    String name, email, id, idToken;
    CallbackManager callbackManager;
    LoginButton facebook_login_btn;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    EditText email_edt, password_edt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sharedPreferences = getSharedPreferences("user", 0);
        editor = sharedPreferences.edit();
        callbackManager = CallbackManager.Factory.create();
        firebaseAuth = FirebaseAuth.getInstance();
        google_btn = findViewById(R.id.google_button);
        //facebook_btn = findViewById(R.id.facebook_button);
        sign_up_txt = findViewById(R.id.sign_up_txt);
        login_btn = findViewById(R.id.cirLoginButton);
        forget_txt = findViewById(R.id.forget_txt);
        password_lay = findViewById(R.id.textInputPassword);
        email_lay = findViewById(R.id.textInputEmail);
        email_edt = findViewById(R.id.editTextEmail);
        password_edt = findViewById(R.id.editTextPassword);
        facebook_login_btn = findViewById(R.id.login_button_fc);




        google_btn.setAlpha(0.0f);
        //facebook_btn.setAlpha(0.0f);
        login_btn.setAlpha(0.0f);
        password_lay.setAlpha(0.0f);
        email_lay.setAlpha(0.0f);
        sign_up_txt.setAlpha(0.0f);

        animation();

        FacebookSdk.sdkInitialize(getApplicationContext());

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("308505731498-pvo0n4ao9rfuc2m6f98d253264gq3m13.apps.googleusercontent.com").requestEmail().build();

        apiClient = new GoogleApiClient.Builder(this).enableAutoManage(login_activity.this, null).addApi(Auth.GOOGLE_SIGN_IN_API, options).build();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    editor.putString("uid", user.getUid());
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.commit();
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("name").setValue(name);
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("email").setValue(email);
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("number").setValue(user.getPhoneNumber());
                    //user signed in
                    //user.getUid()

                }

            }
        };

        /*facebook_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebook_login_btn.callOnClick();
            }
        });

         */
        google_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
                startActivityForResult(intent, 1);


            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_Str = email_edt.getText().toString();
                String password_str = password_edt.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email_Str, password_str).addOnCompleteListener(login_activity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            editor.putString("uid", user.getUid());
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.commit();
                            navigate();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(login_activity.this, "Something went wrong! :(",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        sign_up_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(login_activity.this, register_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();

                    }
                }, 300);

            }
        });
        facebook_login_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handlefacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

                Log.d("lol", String.valueOf(e));


            }
        });
    }

    public void animation() {

        ImageView img = findViewById(R.id.login_image);
        TextView txt1 = findViewById(R.id.txt1);
        LinearLayout txt2 = findViewById(R.id.txt2);

        Animation down_anim = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        Animation fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation right_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation left_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        img.startAnimation(fade_in);

        txt1.startAnimation(fade_in);
        txt2.startAnimation(fade_in);
        forget_txt.startAnimation(fade_in);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                google_btn.setAlpha(1.0f);
                //facebook_btn.setAlpha(1.0f);
                login_btn.setAlpha(1.0f);
                password_lay.setAlpha(1.0f);
                email_lay.setAlpha(1.0f);
                sign_up_txt.setAlpha(1.0f);
                sign_up_txt.startAnimation(fade_in);
                password_lay.startAnimation(down_anim);
                email_lay.startAnimation(down_anim);
                //facebook_btn.startAnimation(left_in);
                google_btn.startAnimation(right_in);
                login_btn.startAnimation(down_anim);


            }
        }, 1100);

    }

    public void handlefacebookToken(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(login_activity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    editor.putString("uid", user.getUid());
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.commit();
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("name").setValue(name);
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("email").setValue(email);
                    FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("number").setValue(user.getPhoneNumber());
                    navigate();

                } else {
                    Toast.makeText(login_activity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (authStateListener != null) {

            FirebaseAuth.getInstance().signOut();
            //Toast.makeText(this, "You have successfully signed out!", Toast.LENGTH_SHORT).show();

        }
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {

            //FirebaseAuth.getInstance().signOut();
            //Toast.makeText(this, "You have successfully signed out!", Toast.LENGTH_SHORT).show();
            firebaseAuth.removeAuthStateListener(authStateListener);

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {


            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //System.out.println("aasdasdas"+result);
            handleSignIn(result);


        }
    }
    public void handleSignIn(GoogleSignInResult googleSignInResult) {

        if (!googleSignInResult.isSuccess()) {

            Toast.makeText(login_activity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();


        } else {

            GoogleSignInAccount account = googleSignInResult.getSignInAccount();
            id = account.getId();
            idToken = account.getIdToken();
            name = account.getDisplayName();
            email = account.getEmail();
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            auth_result(credential);

        }

    }


    public void auth_result(AuthCredential credential) {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(login_activity.this, "Something went wrong with firebase auth!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    editor.putString("uid", user.getUid());
                    editor.putString("name", name);
                    editor.putString("email", email);
                    editor.commit();
                    navigate();

                }

            }
        });

    }
    public void navigate() {

        Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(login_activity.this, home_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

            }
        }, 300);

    }
}