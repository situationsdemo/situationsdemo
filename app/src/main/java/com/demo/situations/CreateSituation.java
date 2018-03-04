package com.demo.situations;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private ArrayAdapter<String> categoryAdaptor;
    private ArrayAdapter<String> subCategoryAdaptor;
    private VideoView situationPreview;
    private Uri uri;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private DataSnapshot categorySnapShot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_situation);

        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerSubCategory = findViewById(R.id.spinnerSubCategory);
        spinnerCategory.setOnItemSelectedListener(this);
        spinnerSubCategory.setOnItemSelectedListener(this);
        spinnerSubCategory.setVisibility(View.INVISIBLE);

        situationPreview = findViewById(R.id.situationPreview);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Situations").child("Category").addListenerForSingleValueEvent(this);
        storageReference = FirebaseStorage.getInstance().getReference();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        storageReference.child("anim.mp4").getDownloadUrl().addOnSuccessListener(this, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //uri = Uri.parse("gs://situations-31ca4.appspot.com/anim.mp4");
                situationPreview.setVideoURI(uri);
                situationPreview.requestFocus();
                situationPreview.start();
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                LogInPage.ShowAlert(CreateSituation.this,e.getMessage());
            }
        });



    }


    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.logoutButton:

                break;
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
                            subCategories.add(snapshot.getValue(String.class));
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
                }
                else{
                    textView.setTextColor(Color.GRAY);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
