package com.demo.situations;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class selectSituations extends AppCompatActivity implements ValueEventListener {

    private ArrayList<String> dataUrls;
    private VideoViewAdapater videoViewAdapater;
    private GridVideoView selectedVideo;
    private String selectedVideoUrl;
    private MenuItem doneCheckmark;
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_situations);
        GridView gridView = findViewById(R.id.gridview);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        dataUrls = new ArrayList<>();
        videoViewAdapater = new VideoViewAdapater(this,dataUrls);
        videoViewAdapater.setOnItemSelectedListener(new onItemSelectedListener() {
            @Override
            public void getItemSelected(GridVideoView videoView, String VideoUrl) {
                doneCheckmark.setEnabled(true);
                doneCheckmark.getIcon().setAlpha(255);//enable done checkmark
                selectedVideo = videoView;
                selectedVideoUrl = VideoUrl;
            }
        });
        gridView.setAdapter(videoViewAdapater);
        getSupportActionBar().setTitle("SelectSituations");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Situations").child("Category").child("test").child("videos").addListenerForSingleValueEvent(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24px);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_checkmark,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        doneCheckmark = menu.findItem(R.id.done);
        doneCheckmark.setEnabled(false);
        doneCheckmark.getIcon().setAlpha(130);//disable done checkmark
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.done:
                Intent intent = new Intent();
                if(selectedVideoUrl != null) {
                    intent.putExtra("selectedSituation", selectedVideoUrl);
                    setResult(Activity.RESULT_OK, intent);
                }
                else{
                    setResult(Activity.RESULT_CANCELED);
                }
                finish();
                break;
            case R.id.homeAsUp:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED);
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigateUp() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED);
        finish();
        return super.onNavigateUp();
    }

    public void addUrl(String child){
        FirebaseStorage.getInstance().getReference().child(child).getDownloadUrl().addOnSuccessListener(this, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                dataUrls.add(uri.toString());
                videoViewAdapater.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
            addUrl("test/videos/"+snapshot.getValue().toString());
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
