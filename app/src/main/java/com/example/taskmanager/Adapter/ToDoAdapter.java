package com.example.taskmanager.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.AddNewTask;
import com.example.taskmanager.MainActivity;
import com.example.taskmanager.Model.ToDoModel;
import com.example.taskmanager.R;
import com.example.taskmanager.Utils.DatabaseHandler;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> toDoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db,MainActivity activity)
    {
        this.activity=activity;
        this.db=db;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent,false);
        return new ViewHolder(itemView);
    };
    public int getItemCount(){
        return toDoList.size();
    }
    private boolean checkStatus(int n){
        return n!=0;
    }
    public void setTask(List<ToDoModel> toDoList){
        this.toDoList=toDoList;
        notifyDataSetChanged();
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        db.openDatabase();
        ToDoModel item = toDoList.get(position);
        holder.task.setText(item.getTaskDescription());
        holder.task.setChecked(checkStatus(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }
                else {
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }
    public Context getContext(){
        return activity;
    }
    public void editTask(int pos){
        ToDoModel item =toDoList.get(pos);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("taskDescription",item.getTaskDescription());
        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
    public void deleteTask(int pos){
        ToDoModel task = toDoList.get(pos);
        db.deleteTask(task.getId());
        toDoList.remove(pos);
        notifyItemRemoved(pos);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task=view.findViewById(R.id.toDoCheckbox);
        }
    }
}
