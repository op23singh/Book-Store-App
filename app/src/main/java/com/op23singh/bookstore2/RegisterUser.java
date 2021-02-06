package com.op23singh.bookstore2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private ImageView registerimglogo;
    private EditText txtFirstName,txtLastName,txtEmail,txtregisterPassword;
    private Button buttonregister;
    private ProgressBar registerprg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        buttonregister=findViewById(R.id.buttonRegister);
        buttonregister.setOnClickListener(this);
        registerimglogo=findViewById(R.id.registerimglogo);
        //registerimglogo.setOnClickListener(this);
        registerprg=findViewById(R.id.registerprg);
        txtFirstName=findViewById(R.id.txtFirstName);
        txtLastName=findViewById(R.id.txtLastName);
        txtEmail=findViewById(R.id.txtEmail);
        txtregisterPassword=findViewById(R.id.txtregisterPassword);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerimglogo:
                startActivity(new Intent(this,main_Page.class));
                break;
            case R.id.buttonRegister:
                registerUser();
                break;
            default:
                break;
        }
    }

    private void registerUser() {
        String email=txtEmail.getText().toString().trim();
        String FirstName=txtFirstName.getText().toString().trim();
        String LastName=txtLastName.getText().toString().trim();
        String registerPassword=txtregisterPassword.getText().toString().trim();

        if(FirstName.isEmpty()){
            txtFirstName.setError("Please enter Firstname");
            txtFirstName.requestFocus();
            return ;
        }
        if(LastName.isEmpty()){
            txtLastName.setError("Please enter LastName");
            txtLastName.requestFocus();
            return ;
        }
        if(email.isEmpty()){
            txtEmail.setError("Please enter your email");
            txtEmail.requestFocus();
            return ;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError("Please enter a valid Email");
            txtEmail.requestFocus();
            return ;
        }
        if(registerPassword.isEmpty()){
            txtregisterPassword.setError("Password is required!");
            txtregisterPassword.requestFocus();
            return ;
        }
        if(registerPassword.length()<6) {
            txtregisterPassword.setError("Password must be of minimum 6 characters");
            txtregisterPassword.requestFocus();
            return ;
        }
        registerprg.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,registerPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user=new User(FirstName,LastName,email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "user registered successfully", Toast.LENGTH_LONG).show();
                                        registerprg.setVisibility(View.GONE);
                                        startActivity(new Intent(RegisterUser.this,MainActivity.class));
                                    }
                                    else {
                                        Toast.makeText(RegisterUser.this,"Failed to Register TryAgain",Toast.LENGTH_LONG).show();
                                        registerprg.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterUser.this,"Failed to Register TryAgain",Toast.LENGTH_LONG).show();
                            registerprg.setVisibility(View.GONE);
                        }
                    }
                });
    }
}