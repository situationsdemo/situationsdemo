package com.demo.situations;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
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
        startActivity(new Intent(this,LogInPage.class));
    }
}
