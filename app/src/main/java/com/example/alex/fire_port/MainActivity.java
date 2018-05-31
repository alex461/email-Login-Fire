package com.example.alex.fire_port;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private Button btnCreateAccount;
    private Button btnSign;

    private EditText edtEmail;
    private EditText edtpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateAccount    =findViewById(R.id.login_bt_creater);
        btnSign             =findViewById(R.id.login_bt_singin);

        edtEmail            =findViewById(R.id.login_user);
        edtpassword         =findViewById(R.id.login_pass);

        inicialize();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            createAccount(edtEmail.getText().toString(),edtpassword.getText().toString());
            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            singIn(edtEmail.getText().toString(),edtpassword.getText().toString());

            }
        });
        
    }

    private void inicialize() {

        firebaseAuth =FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
                if(firebaseUser !=null){
                    Log.w(TAG,"onAuthStateChanged = signes_in "+firebaseUser.getUid());
                    Log.w(TAG,"onAuthStateChanged = signes_in "+firebaseUser.getEmail());
                }else{
                    Log.w(TAG,"onAuthStateChanged = signes_out");
                }

            }
        };
    }


    private void createAccount(String email,String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Create account Success", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Create account Unsuccess", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    private void singIn(String email,String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Authentication Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,Main2Activity.class));
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, "Aunthentication Unsuccess", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}
