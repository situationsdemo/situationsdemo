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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogInPage extends AppCompatActivity implements View.OnClickListener {

    EditText editTextemail, editTextpassword;
    Button signinbutton, signupbutton, googlesigninButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Intent main_activity, createSituations;
    GoogleSignInOptions gso;
    final private int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        editTextemail = findViewById(R.id.editTextemail);
        editTextpassword = findViewById(R.id.editTextpassword);

        signinbutton = findViewById(R.id.signinbutton);
        signupbutton = findViewById(R.id.signupbutton);
        googlesigninButton = findViewById(R.id.googlesigninButton);
        signinbutton.setOnClickListener(this);
        signupbutton.setOnClickListener(this);
        googlesigninButton.setOnClickListener(this);

        main_activity = new Intent(this,Main2Activity.class);
    }

    @Override
    public void onStart(){
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(main_activity);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.signinbutton:
                SignIn();
                break;
            case R.id.googlesigninButton:
                SigninWithGoogle();
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
                            ThrowException(task.getException());
                        }
                    }
                });
    }

    private void SigninWithGoogle(){

        Intent _intent = GoogleSignIn.getClient(this,gso).getSignInIntent();
        startActivityForResult(_intent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                FirebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                ThrowException(task.getException());
            }
        }
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
                                ThrowException(task.getException());
                        }
                    }
                });

    }


    private void FirebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            startActivity(main_activity);
                        } else {
                            ThrowException(task.getException());
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

    private void ThrowException(Exception exception){
        try{
            throw exception;
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
        catch (FirebaseAuthUserCollisionException e){
            ShowAlert("Email already in use.");
        }
        catch (FirebaseException e){
            ShowAlert(e.getMessage());
        }
        catch (Exception e){

        }
    }
}
