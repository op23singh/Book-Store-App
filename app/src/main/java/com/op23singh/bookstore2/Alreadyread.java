package com.op23singh.bookstore2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Alreadyread extends AppCompatActivity{
    ArrayList<Book> alrea=new ArrayList<>();
    private RecyclerView alreadyreadbooksRecView;
    private booksRecViewAdapter Booksadapter;
    private FirebaseAuth mAuth;
    private FirebaseUser frbuser;
    private DatabaseReference reference,reference2;
    private String UserId;
    private MenuItem currentuser;
    protected Userdata obj=new Userdata();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alreadyread);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String TAG ="s";
        alreadyreadbooksRecView=findViewById(R.id.alreadyreadbooksRecView);
        Booksadapter =new booksRecViewAdapter(this,"alreadyread");
        alreadyreadbooksRecView.setAdapter(Booksadapter);
        alreadyreadbooksRecView.setLayoutManager(new LinearLayoutManager(this));
        reference2=FirebaseDatabase.getInstance().getReference("Users").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        reference2.child("Alreadyread").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot:snapshot.getChildren()){
                    alrea.add(datasnapshot.getValue(Book.class));
                }
                Booksadapter.setBooks(alrea);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"something went wrong");
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
        System.out.println(sp.getString("name","xyz"));
        menu.findItem(R.id.curuser).setTitle(sp.getString("name","Guest"));
    return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.hme) {
            startActivity(new Intent(Alreadyread.this, main_Page.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(id==android.R.id.home){
            onBackPressed();
        }
        else if(id==R.id.menulogout){
            Toast.makeText(Alreadyread.this,"user signed out successfully",Toast.LENGTH_SHORT).show();
            SharedPreferences gp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor editor=gp.edit();
            editor.clear();
            editor.apply();
            finishAffinity();
            startActivity(new Intent(Alreadyread.this,MainActivity.class));
        }
        return true;
    }
}