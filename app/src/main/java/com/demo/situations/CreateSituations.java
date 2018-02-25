package com.demo.situations;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class CreateSituations extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_situations);
        drawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        ResetMenuItemTextColor(navigationView);

    }

    private void SetTextColor(MenuItem menuItem,@ColorRes int color){
        SpannableString item = new SpannableString(menuItem.getTitle().toString());
        item.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, item.length(), 0);
        menuItem.setTitle(item);
    }

    private void ResetMenuItemTextColor(NavigationView navigationView){
        for(int i=0;i<navigationView.getMenu().size();i++){
            SetTextColor(navigationView.getMenu().getItem(i),R.color.common_google_signin_btn_text_dark_default);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        /*switch(item.getItemId()){
            case R.id.nav_drawer_menu_item_home:
                Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_drawer_menu_item_settings:
                Toast.makeText(this,"Settings",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_drawer_menu_item_logout:
                FirebaseAuth.getInstance().signOut();
                GoogleSignInClient client = GoogleSignIn.getClient(this,gso);
                if(client != null)
                    client.signOut();
                startActivity(new Intent(this,LogInPage.class));
                break;
        }*/


        return true;
    }
}
