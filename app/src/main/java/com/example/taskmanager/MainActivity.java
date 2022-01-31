package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.taskmanager.Adapter.ToDoAdapter;
import com.example.taskmanager.Model.ToDoModel;
import com.example.taskmanager.Utils.DatabaseHandler;
import com.example.taskmanager.Utils.SessionManagement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{
    private RecyclerView taskRecyclerView;
    private ToDoAdapter taskAdapter;
    private List<ToDoModel> taskList;
    private FloatingActionButton fab;
    private ImageButton imageButton;

    private DatabaseHandler db;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        session = new SessionManagement(getApplicationContext());
        HashMap<String,String> user = session.getUserDetails();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter= new ToDoAdapter(db,this);
        taskRecyclerView.setAdapter(taskAdapter);

        fab= findViewById(R.id.fab);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(taskAdapter));
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        taskList = db.getAllTasks();
        Collections.reverse(taskList);

        taskAdapter.setTask(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
           }
        });
        imageButton= findViewById(R.id.imgBtn);
        Picasso.get().load(user.get(SessionManagement.KEY_AVATAR)).into(imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuickStartActivity.class);
                startActivity(intent);
            }
        });
        BannerView bannerView = findViewById(R.id.hw_banner_view);

        bannerView.loadAd(new AdParam.Builder().build());
        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this);

        BannerView bottomBannerView = findViewById(R.id.hw_banner_view);
        AdParam adParam = new AdParam.Builder().build();
        bottomBannerView.loadAd(adParam);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        taskAdapter.setTask(taskList);
    }
}