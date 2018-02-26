package com.demo.situations;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateSituation extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    Button logoutButton;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_situation);

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

    }


    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.logoutButton:
                LogOut();
                break;
        }
    }

    private void LogOut(){
        mAuth.signOut();
        GoogleSignInClient client = GoogleSignIn.getClient(this,gso);
        if(client != null)
            client.signOut();
        startActivity(new Intent(this,LogInPage.class));
    }
}