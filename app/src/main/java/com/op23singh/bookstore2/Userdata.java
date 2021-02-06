package com.op23singh.bookstore2;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Userdata extends Activity {
    protected ArrayList<Book> currentlyreading=new ArrayList<>();
    protected ArrayList<Book> wanttoread=new ArrayList<>();
    public ArrayList<Book> allreadyread=new ArrayList<>();
    protected ArrayList<Book> favouritebooks=new ArrayList<>();
    protected DatabaseReference reference;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public ArrayList<Book> getCurrentlyreading() {
        reference=(FirebaseDatabase.getInstance().getReference("Users").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()))).child("Currentlyreading");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot:snapshot.getChildren()) {
                    currentlyreading.add(datasnapshot.getValue(Book.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return currentlyreading;
    }

    public ArrayList<Book> getWanttoread() {
        reference=(FirebaseDatabase.getInstance().getReference("Users").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()))).child("Wanttoread");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot:snapshot.getChildren()) {
                    wanttoread.add(datasnapshot.getValue(Book.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return wanttoread;
    }

    public ArrayList<Book> getAllreadyread() {
        reference=(FirebaseDatabase.getInstance().getReference("Users").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()))).child("Alreadyread");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot:snapshot.getChildren()) {
                    allreadyread.add(datasnapshot.getValue(Book.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return allreadyread;
    }

    public ArrayList<Book> getFavouritebooks() {
        reference=(FirebaseDatabase.getInstance().getReference("Users").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()))).child("Favourties");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot datasnapshot:snapshot.getChildren()) {
                    favouritebooks.add(datasnapshot.getValue(Book.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return favouritebooks;
    }
}
