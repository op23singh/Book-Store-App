package com.op23singh.bookstore2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class main_Page extends AppCompatActivity {
    private static final String TAG ="sdf" ;
    private Button lstbtn,btncurrent,btnalreadyread,btnwanttoread,btnfavourite,btnabout,btnlogout;
    private TextView txtlicence;
    private ImageView image;
    private RelativeLayout main_page_parent;
    private FirebaseAuth mAuth;
    private FirebaseUser frbuser;
    private DatabaseReference reference;
    private String UserId;
    private MenuItem currentuser;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__page);
        intitViews();
        lstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(main_Page.this,AllBooksActivity.class);
                startActivity(intent);
            }
        });
        btnalreadyread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_Page.this,Alreadyread.class));
            }
        });
        btnwanttoread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_Page.this,Wanttoread.class));
            }
        });
        btncurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_Page.this,Currentlyreading.class));
            }
        });
        btnfavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(main_Page.this,Favourites.class));
            }
        });
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main_Page.this,"user signed out successfully",Toast.LENGTH_LONG).show();
                SystemClock.sleep(500);
                mAuth.signOut();
                startActivity(new Intent(main_Page.this,MainActivity.class));
            }
        });
        btnabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(main_Page.this,"mail us @ op23singh@gmail.com",Toast.LENGTH_LONG).show ();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
         sp=getSharedPreferences("login",MODE_PRIVATE);
         if(sp.getString("name","Guest").equals("Guest")){
             frbuser=FirebaseAuth.getInstance().getCurrentUser();
             DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
             String UserId=frbuser.getUid();
             reference.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                     User userprofile=snapshot.getValue(User.class);
                     SharedPreferences.Editor editor=sp.edit();
                     editor.putString("name",userprofile.FirstName).apply();
                     menu.findItem(R.id.curuser).setTitle(userprofile.FirstName);
                 }
                 @Override
                 public void onCancelled(@NonNull DatabaseError error) {
                     Toast.makeText(main_Page.this,"something went wrong ",Toast.LENGTH_LONG).show();
                 }
             });
         }
        menu.findItem(R.id.curuser).setTitle(sp.getString("name","Guest"));
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.hme){
            Toast.makeText(this,"you are already on home",Toast.LENGTH_SHORT).show();
        }
        else if(id==R.id.menulogout){
            Toast.makeText(main_Page.this,"user signed out successfully",Toast.LENGTH_SHORT).show();
            SharedPreferences gp=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor editor=gp.edit();
            editor.clear();
            editor.apply();
            finishAffinity();
            startActivity(new Intent(main_Page.this,MainActivity.class));
        }
        return true;
    }
    private void intitViews() {
        mAuth= FirebaseAuth.getInstance();
        main_page_parent=findViewById(R.id.main_page_parent);
        lstbtn=findViewById(R.id.lstbtn);
        btncurrent=findViewById(R.id.btnfavourite);
        btncurrent=findViewById(R.id.btncurrent);
        btnalreadyread=findViewById(R.id.btnadalreadyread);
        btnwanttoread=findViewById(R.id.btnwanttoread);
        btnfavourite=findViewById(R.id.btnfavourite);
        btnabout=findViewById(R.id.btnabout);
        txtlicence=findViewById(R.id.txtlicence);
        image=findViewById(R.id.image);
        btnlogout=findViewById(R.id.btnlogout);
    }
}