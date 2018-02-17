package com.demo.situations;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class LogInPage extends AppCompatActivity implements View.OnClickListener {

    EditText editTextemail, editTextpassword;
    Button signinbutton, signupbutton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Intent main_activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
        mAuth = FirebaseAuth.getInstance();
        editTextemail = findViewById(R.id.editTextemail);
        editTextpassword = findViewById(R.id.editTextpassword);

        signinbutton = findViewById(R.id.signinbutton);
        signupbutton = findViewById(R.id.signupbutton);

        signinbutton.setOnClickListener(this);
        signupbutton.setOnClickListener(this);

        main_activity = new Intent(this,MainActivity.class);
    }

    @Override
    public void onStart(){
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(this,MainActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.signinbutton:
                SignIn();
                break;

            case R.id.signupbutton:
                CreateNewUser();
                break;
        }
    }

    private void SignIn() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        if(email.isEmpty()){
            editTextemail.setError("Email Required");
        }
        if(password.isEmpty()) {
            editTextpassword.setError("Password required");
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(main_activity);
                        }
                        else{
                            try{
                                throw task.getException();
                            }
                            catch (FirebaseNetworkException e){
                                ShowAlert("No Network!");
                            }
                            catch (FirebaseAuthInvalidUserException e){
                                ShowAlert("Account with this email doesn't exist!");
                            }
                            catch (FirebaseAuthInvalidCredentialsException e){
                                ShowAlert("the password is invalid");
                            }
                            catch (FirebaseException e){
                                ShowAlert(e.getMessage());
                            }
                            catch (Exception e) {
                                ShowAlert(e.getMessage());
                            }
                        }
                    }
                });
    }

    private void CreateNewUser() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        if(email.isEmpty()){
            editTextemail.setError("Email Required");
        }
        if(password.isEmpty()){
            editTextpassword.setError("Password required");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            if (currentUser != null)
                                startActivity(main_activity);
                        }
                        else{
                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException e){
                                ShowAlert("Email already in use.");
                            }
                            catch (FirebaseNetworkException e){
                                ShowAlert("No network");
                            }
                            catch (Exception e) {
                                ShowAlert(e.getMessage());
                            }
                        }
                    }
                });

    }

    private void ShowAlert(String msg){
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        myAlert.create();
        myAlert.show();
    }
}
