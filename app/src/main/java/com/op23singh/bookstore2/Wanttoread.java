package com.op23singh.bookstore2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Wanttoread extends AppCompatActivity {
    ArrayList<Book> wants=new ArrayList<>();
    private RecyclerView booksRecView9;
    private booksRecViewAdapter Booksadapter;
    private FirebaseAuth mAuth;
    private FirebaseUser frbuser;
    private DatabaseReference reference,reference2;
    private String UserId;
    private MenuItem currentuser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanttoread);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final String TAG ="s";
        booksRecView9=findViewById(R.id.booksRecView9);
        Booksadapter =new booksRecViewAdapter(Wanttoread.this,"wanttoread");
        booksRecView9.setAdapter(Booksadapter);
        booksRecView9.setLayoutManager(new LinearLayoutManager(Wanttoread.this));
        reference2=FirebaseDatabase.getInstance().getReference("Users").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        reference2.child("Wanttoread").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot:snapshot.getChildren()){
                    wants.add(datasnapshot.getValue(Book.class));
                }
                System.out.println(wants.size());
                Booksadapter.setBooks(wants);
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
            startActivity(new Intent(Wanttoread.this, main_Page.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(id==android.R.id.home){
            onBackPressed();
        }
        else if(id==R.id.menulogout){
            Toast.makeText(Wanttoread.this,"user signed out successfully",Toast.LENGTH_SHORT).show();
            SharedPreferences gp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor editor=gp.edit();
            editor.clear();
            editor.apply();
            finishAffinity();
            startActivity(new Intent(Wanttoread.this,MainActivity.class));
        }
        return true;
    }
}