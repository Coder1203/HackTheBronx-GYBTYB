package com.blackgrapes.gyb_tyb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.android.animations.Technique;

import java.util.ArrayList;

public class my_books_activity extends AppCompatActivity {

    public ArrayList<String> Title = new ArrayList<>();
    public ArrayList<String> Book_no = new ArrayList<>();
    public ArrayList<String> Writer = new ArrayList<>();
    public ArrayList<String> Everything = new ArrayList<>();
    ArrayList<Integer> tempArrayList = new ArrayList<Integer>();
    ListView listView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView book_no_txt;
    EditText search_edt;



    CardView back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        search_edt = findViewById(R.id.editTextsearch);
        listView = findViewById(R.id.listView);
        sharedPreferences = getSharedPreferences("user", 0);
        editor = sharedPreferences.edit();
        book_no_txt = findViewById(R.id.book_no);
        back_btn = findViewById(R.id.back_btn);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(my_books_activity.this, home_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();

                    }
                }, 300);
            }
        });


        search_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                int textlength = cs.length();
                tempArrayList = new ArrayList<>();
                for (String c : Everything) {
                    if (textlength <= c.length()) {
                        if (c.toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(Everything.indexOf(c));
                        }
                    }
                }
                System.out.println(tempArrayList);
                ArrayList<String> tTitle = new ArrayList<>();
                ArrayList<String> tBook_no = new ArrayList<>();
                ArrayList<String> tWriter = new ArrayList<>();
                for(int a : tempArrayList){

                    tTitle.add(Title.get(a));
                    tBook_no.add(Book_no.get(a));
                    tWriter.add(Writer.get(a));



                }
                System.out.println(tTitle);
                System.out.println(tBook_no);
                System.out.println(tWriter);

                update(tTitle, tBook_no, tWriter);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child(sharedPreferences.getString("uid","error")).child("Books").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                int book_num = Integer.parseInt(task.getResult().child("number").getValue().toString());
                book_no_txt.setText(String.valueOf(book_num));
                for (int i = 1; i <= book_num; i++) {

                    FirebaseDatabase.getInstance().getReference().child(sharedPreferences.getString("uid","error")).child("Books").child(String.valueOf(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            String title_str = task.getResult().child("title").getValue().toString();
                            String writer_str = task.getResult().child("writer").getValue().toString();
                            String index_str = task.getResult().child("index").getValue().toString();
                            Title.add(title_str);
                            Writer.add(writer_str);
                            Book_no.add("#" + index_str);
                            Everything.add(title_str + " " + writer_str + " #" + index_str);
                            System.out.println(Title);


                            update(Title, Book_no, Writer);
                        }
                    });

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(my_books_activity.this, i, Toast.LENGTH_SHORT).show();
                TextView index = view.findViewById(R.id.index);
                String index_str = index.getText().toString();

                Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
                final Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(my_books_activity.this, view_book_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("index",index_str.substring(1));
                        startActivity(intent);
                        finish();

                    }
                }, 300);


            }
        });

    }

    public void update(ArrayList<String> title, ArrayList<String> book_no, ArrayList<String> writer) {
        my_books_activity.MyListAdapter adapter = new MyListAdapter(my_books_activity.this, title, book_no, writer);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setVisibility(View.VISIBLE);


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();\
        Technique.FADE_OUT.getComposer().duration(300).playOn(findViewById(R.id.main_lay));
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(my_books_activity.this, home_activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();

            }
        }, 300);
    }

    public class MyListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        ArrayList<String> mTitle = new ArrayList<>();
        ArrayList<String> mBook_no = new ArrayList<>();
        ArrayList<String> mWriter = new ArrayList<>();


        public MyListAdapter(Activity context, ArrayList<String> title, ArrayList<String> book_no, ArrayList<String> writer) {
            super(context, R.layout.book, Title);
            // TODO Auto-generated constructor stub

            this.mTitle = title;
            this.mBook_no = book_no;
            this.mWriter = writer;
            this.context = context;

        }

        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.book, null, true);
            TextView titleText = (TextView) rowView.findViewById(R.id.title);
            TextView writerText = (TextView) rowView.findViewById(R.id.writer);
            TextView indexText = (TextView) rowView.findViewById(R.id.index);

            try {
                titleText.setText(mTitle.get(position));
                writerText.setText(mWriter.get(position));
                indexText.setText(mBook_no.get(position));
            } catch (Exception e){
                titleText.setText("");
                writerText.setText("");
                indexText.setText("");
            }

            return rowView;


        }

    }
}