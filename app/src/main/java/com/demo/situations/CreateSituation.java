package com.demo.situations;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CreateSituation extends AppCompatActivity
        implements View.OnClickListener
                  ,ValueEventListener
                  ,OnItemSelectedListener {


    private Spinner spinnerCategory;
    private Spinner spinnerSubCategory;
    private Button selectSituation;
    private Button videoButton;
    private ArrayAdapter<String> categoryAdaptor;
    private ArrayAdapter<String> subCategoryAdaptor;
    private VideoView situationPreview;
    private DatabaseReference databaseReference;
    private DataSnapshot categorySnapShot;
    private Intent selectSituations;
    private Toolbar toolbar;
    private MenuItem goNextArrow;
    private String UrlForSelectedSituation;
    private boolean videoUrlSet = false;
    private final int RESULT_SELECT_SITUATION = 33;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_situation);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CreateSituation");
        videoButton = findViewById(R.id.videoButton);
        videoButton.setOnClickListener(this);
        selectSituation = findViewById(R.id.buttonSelectSituation);
        selectSituation.setOnClickListener(this);
        selectSituation.setVisibility(View.INVISIBLE);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubCategory = findViewById(R.id.spinnerSubCategory);
        spinnerCategory.setOnItemSelectedListener(this);
        spinnerSubCategory.setOnItemSelectedListener(this);
        spinnerSubCategory.setVisibility(View.INVISIBLE);

        situationPreview = findViewById(R.id.situationPreview);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Situations").child("Category").addListenerForSingleValueEvent(this);
        //storageReference = FirebaseStorage.getInstance().getReference();

        selectSituations = new Intent(this, com.demo.situations.selectSituations.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24px);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.go_next_arrow,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        goNextArrow = menu.findItem(R.id.next);
        goNextArrow.setEnabled(false);
        goNextArrow.getIcon().setAlpha(130);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.next:
                Intent musicactivityIntent = new Intent(this,AddMusic.class);
                if(UrlForSelectedSituation != null){
                    musicactivityIntent.putExtra("selectedSituation",UrlForSelectedSituation);
                    startActivityForResult(musicactivityIntent,23);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.buttonSelectSituation:
                situationPreview.stopPlayback();
                startActivityForResult(selectSituations,RESULT_SELECT_SITUATION);
                break;
            case R.id.videoButton:
                if(situationPreview.isPlaying()){
                    situationPreview.pause();
                }
                else{
                    situationPreview.start();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_SELECT_SITUATION){
            if(resultCode == RESULT_OK){
                String url = data.getStringExtra("selectedSituation");
                UrlForSelectedSituation = url;
                Uri videoUri = Uri.parse(url);
                situationPreview.setVideoURI(videoUri);
                situationPreview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        situationPreview.seekTo(1);
                    }
                });
                videoUrlSet = true;
                situationPreview.start();
                goNextArrow.setEnabled(true);
                goNextArrow.getIcon().setAlpha(255);
            }
            else{
                if(videoUrlSet == true){
                    situationPreview.start();
                }
            }
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        switch(dataSnapshot.getKey())
        {
            case "Category":
                categorySnapShot = dataSnapshot;
                List<String> categories = new ArrayList<>();
                categories.add("Select a Category");
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    categories.add(snapshot.getKey());
                }
                categoryAdaptor = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,categories);
                categoryAdaptor.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(categoryAdaptor);
                break;


        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        LogInPage.ShowAlert(this,databaseError.getMessage());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long id) {
        TextView textView = (TextView) view;
        switch(adapterView.getId()){
            case R.id.spinnerCategory:
                if(i > 0){
                    textView.setTextColor(Color.BLACK);
                    List<String> subCategories = new ArrayList<>();
                    subCategories.add("Select a Sub Category");
                    if(categorySnapShot != null) {
                        String child = adapterView.getItemAtPosition(i).toString();
                        DataSnapshot subSnapShot = categorySnapShot.child(child);
                        for (DataSnapshot snapshot : subSnapShot.getChildren()) {
                            subCategories.add(snapshot.getValue().toString());
                        }
                        subCategoryAdaptor = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,subCategories);
                        subCategoryAdaptor.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        spinnerSubCategory.setAdapter(subCategoryAdaptor);
                        spinnerSubCategory.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    textView.setTextColor(Color.GRAY);
                    spinnerSubCategory.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.spinnerSubCategory:
                if(i > 0){
                    textView.setTextColor(Color.BLACK);
                    selectSituation.setVisibility(View.VISIBLE);
                }
                else{
                    textView.setTextColor(Color.GRAY);
                    selectSituation.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
