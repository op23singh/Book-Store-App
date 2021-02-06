package com.op23singh.bookstore2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class activity_book extends AppCompatActivity {
    protected Userdata userdataobj=new Userdata();
    private FirebaseAuth mAuth;
    private FirebaseUser frbuser;
    private booksRecViewAdapter info;
    private DatabaseReference reference,reference2;
    private String UserId;
    private TextView txtbookname,txtauthorname,txtlongdesc,txtpages;
    private ImageView bookimg;
    private Book bb=new Book();
    private Button btnaddcurrentlyreading,btnaddwanttoread,btnaddfavourite,btnaddalreadyread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtbookname=findViewById(R.id.txtbookname);
        txtauthorname=findViewById(R.id.txtauthorname);
        txtlongdesc=findViewById(R.id.txtlongdesc);
        txtpages=findViewById(R.id.txtpages);
        bookimg=findViewById(R.id.bookimg);
        btnaddcurrentlyreading=findViewById(R.id.btnaddcurrentlyreading);
        btnaddalreadyread=findViewById(R.id.btnaddalreadyread);
        btnaddfavourite=findViewById(R.id.btnaddfavourite);
        btnaddwanttoread=findViewById(R.id.btnaddwanttoread);
        setData();
        handlealreadyread(bb);
        handlecurrentlyreading(bb);
        handlefavourites(bb);
        handlewanttoread(bb);
        reference2=FirebaseDatabase.getInstance().getReference("Users").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        btnaddcurrentlyreading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2.child("Currentlyreading").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!handlecurrentlyreading(bb)){
                        reference2.child("Currentlyreading").push().setValue(bb);
                        Toast.makeText(activity_book.this,"Book added successfully to Currentlyreading section",Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(activity_book.this,Currentlyreading.class));

                        }
                        else{
                            Toast.makeText(activity_book.this,"this is already present in your Currently reading section",Toast.LENGTH_SHORT).show();
                            btnaddcurrentlyreading.setEnabled(false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        btnaddalreadyread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2.child("Alreadyread").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!handlealreadyread(bb)){
                            reference2.child("Alreadyread").push().setValue(bb);
                            Toast.makeText(activity_book.this,"Book added successfully to Already section section",Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(activity_book.this,Alreadyread.class));
                        }
                        else {
                            btnaddalreadyread.setEnabled(false);
                            Toast.makeText(activity_book.this,"this is already present in your already read section",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        btnaddfavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2.child("Favourites").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!handlefavourites(bb)) {
                            reference2.child("Favourties").push().setValue(bb);
                            Toast.makeText(activity_book.this,"Book added successfully to Favourites section",Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(activity_book.this,Favourites.class));
                        }
                        else {
                            Toast.makeText(activity_book.this,"this is already present in your favourite section",Toast.LENGTH_SHORT).show();
                            btnaddfavourite.setEnabled(false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        btnaddwanttoread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference2.child("Wanttoread").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!handlewanttoread(bb)) {
                            reference2.child("Wanttoread").push().setValue(bb);
                            Toast.makeText(activity_book.this, "Book added successfully to Wishlist section", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(activity_book.this,Wanttoread.class));
                        }
                        else {
                            Toast.makeText(activity_book.this,"this is already present in your wishlist section",Toast.LENGTH_SHORT).show();
                            btnaddwanttoread.setEnabled(false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private boolean handlealreadyread(Book incoming) {
        boolean current=false;
        ArrayList<Book> handlealreadyreadbooks=userdataobj.getAllreadyread();
        for(Book b:handlealreadyreadbooks){
            System.out.println("incoming->"+incoming.getId()+" "+"alreadypresent-> "+b.getId());
           if(incoming.getId().equals(b.getId())){
               btnaddalreadyread.setEnabled(false);
                return true;
            }
        }
        return false;
    }
    private boolean handlecurrentlyreading(Book incoming) {
        boolean current=false;
        ArrayList<Book> handlecurrentlyreadingbooks=userdataobj.getCurrentlyreading();
        for(Book b:handlecurrentlyreadingbooks){
            System.out.println("incoming->"+incoming.getId()+" "+"alreadypresent-> "+b.getId());
            if(incoming.getId().equals(b.getId())){
                btnaddcurrentlyreading.setEnabled(false);
                return true;
            }
        }
        return false;
    }
    private boolean handlewanttoread(Book incoming) {
        boolean current=false;
        ArrayList<Book> handlewanttoreadbooks=userdataobj.getWanttoread();
        for(Book b:handlewanttoreadbooks){
            System.out.println("incoming->"+incoming.getId()+" "+"alreadypresent-> "+b.getId());
            if(incoming.getId().equals(b.getId())){
                btnaddwanttoread.setEnabled(false);
                return true;
            }
        }
        return false;
    }
    private boolean handlefavourites(Book incoming) {
        boolean current=false;
        ArrayList<Book> handlefavourite=userdataobj.getFavouritebooks();
        for(Book b:handlefavourite){
            System.out.println("incoming->"+incoming.getId()+" "+"alreadypresent-> "+b.getId());
            if(incoming.getId().equals(b.getId())){
                btnaddfavourite.setEnabled(false);
                return true;
            }
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        SharedPreferences sp=getSharedPreferences("login",MODE_PRIVATE);
        System.out.println(sp.getString("name","xyz"));
        menu.findItem(R.id.curuser).setTitle(sp.getString("name","Guest"));
//        frbuser= FirebaseAuth.getInstance().getCurrentUser();
//        reference= FirebaseDatabase.getInstance().getReference("Users");
//        UserId=frbuser.getUid();
//        reference.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User userprofile=snapshot.getValue(User.class);
//                if(userprofile!=null) {
//                    menu.findItem(R.id.curuser).setTitle(userprofile.FirstName);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(activity_book.this,"something went wrong ",Toast.LENGTH_LONG);
//            }
//        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.hme){
            startActivity(new Intent(activity_book.this,main_Page.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        else if(id==android.R.id.home){
            onBackPressed();
        }
        else if(id==R.id.menulogout){
            Toast.makeText(activity_book.this,"user signed out successfully",Toast.LENGTH_SHORT).show();
            SharedPreferences gp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor editor=gp.edit();
            editor.clear();
            editor.apply();
            finishAffinity();
            startActivity(new Intent(activity_book.this,MainActivity.class));
        }
        return true;
    }
    public void setData() {
        bb= (Book)getIntent().getSerializableExtra("B00k");
            try{
            txtbookname.setText(bb.getName().toString());
            txtauthorname.setText(bb.getAuthor().toString());
            txtpages.setText(bb.getPages().toString());
            txtlongdesc.setText(bb.getLongDesc().toString());
            Glide.with(this).load(bb.getImageUrl().toString()).into(bookimg);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}