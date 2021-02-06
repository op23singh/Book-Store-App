package com.op23singh.bookstore2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.os.SystemClock;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="h" ;
    private TextView btnregister,btnforgetpassword;
    private EditText editTextEmailAddress,editTextPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser frbuser;
    private ProgressBar progressBar;
    private MenuItem currentuser;
    String nameuser;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnregister=(TextView) findViewById(R.id.btnregister);
        btnregister.setOnClickListener(this);
        btnLogin=findViewById(R.id.btnLogin);
        btnforgetpassword=findViewById(R.id.btnforgetpassword);
        btnforgetpassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        editTextEmailAddress=findViewById(R.id.editTextEmailAddress);
        editTextPassword=findViewById(R.id.editTextPassword);
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        sp=MainActivity.this.getSharedPreferences("login",MODE_PRIVATE);
       if(sp.getBoolean("logged",false)){

            startActivity(new Intent(MainActivity.this,main_Page.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        if(mAuth.getCurrentUser()!=null){
//            currentuser.setTitle(frbuser.getUid().toString());
        }
        menu.findItem(R.id.menulogout).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.hme){
            Toast.makeText(MainActivity.this,"Please login or register first ",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnregister:
                startActivity(new Intent(this,RegisterUser.class));
                break;
            case R.id.btnforgetpassword:
                startActivity(new Intent(this,ForgotPassword.class));
                break;
            case R.id.btnLogin:
                userlogin();
                break;
        }
    }
    private void userlogin(){

        String email=editTextEmailAddress.getText().toString().trim();
        String Password=editTextPassword.getText().toString().trim();
        if(email.isEmpty()){
            editTextEmailAddress.setError("Email is required");
            editTextEmailAddress.requestFocus();
            return ;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmailAddress.setError("Enter a Valid email");
            editTextEmailAddress.requestFocus();
            return ;
        }
        if(Password.isEmpty()){
            editTextPassword.setError("Please enter password");
            editTextPassword.requestFocus();
            return ;
        }
        if(Password.length()<6){
            editTextPassword.setError("minimum length of password must be 6");
            editTextPassword.requestFocus();
            return ;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified()){
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,"user login successfull",Toast.LENGTH_LONG).show();
                        sp.edit().putBoolean("logged",true).apply();
                        frbuser=FirebaseAuth.getInstance().getCurrentUser();
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");
                        String UserId=frbuser.getUid();
                        reference.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User userprofile=snapshot.getValue(User.class);
                                nameuser=userprofile.FirstName;
                                SharedPreferences.Editor editor=sp.edit();
                                        editor.putString("name",userprofile.FirstName).apply();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this,"something went wrong ",Toast.LENGTH_LONG).show();
                            }
                        });
                        startActivity(new Intent(MainActivity.this,main_Page.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this,"Check your email to verify the account",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Failed to Login Please recheck your credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}