package com.op23singh.bookstore2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.snapshot.StringNode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllBooksActivity extends AppCompatActivity {
    private static final String TAG ="s" ;
    private ArrayList<Book> allbooks,currentlyreading,wanttoread,allreadyread,favouritebooks;
    private RecyclerView booksRecView;
    private booksRecViewAdapter Booksadapter;
    private FirebaseAuth mAuth;
    private FirebaseUser frbuser;
    private DatabaseReference reference,reference2;
    private String UserId;
    private MenuItem currentuser;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_books);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        allbooks= new ArrayList<Book>();
        booksRecView=findViewById(R.id.booksRecView);
        Booksadapter =new booksRecViewAdapter(this,"AllBooks");
       booksRecView.setAdapter(Booksadapter);
       booksRecView.setLayoutManager(new LinearLayoutManager(this));
       reference2=FirebaseDatabase.getInstance().getReference("Books");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot:snapshot.getChildren()){
                    allbooks.add(datasnapshot.getValue(Book.class));
                }
                Booksadapter.setBooks(allbooks);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"something went wrong");
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
        System.out.println(sp.getString("name","xyz"));
        menu.findItem(R.id.curuser).setTitle(sp.getString("name","Guest"));
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.hme){
            startActivity(new Intent(AllBooksActivity.this,main_Page.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(id==android.R.id.home){
            onBackPressed();
        }
        else if(id==R.id.menulogout){
            Toast.makeText(AllBooksActivity.this,"user signed out successfully",Toast.LENGTH_SHORT).show();
            SharedPreferences gp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor editor=gp.edit();
            editor.clear();
            editor.apply();
            finishAffinity();
            startActivity(new Intent(AllBooksActivity.this,MainActivity.class));
        }
        return true;
    }
}