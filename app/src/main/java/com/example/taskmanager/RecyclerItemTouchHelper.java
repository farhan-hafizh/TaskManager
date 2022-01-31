package com.example.taskmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.Adapter.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter;

    public RecyclerItemTouchHelper(ToDoAdapter adapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter=adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return false;
    }
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){
        final int pos = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT) {
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this task? This action cannot be undone!");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteTask(pos);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            adapter.editTask(pos);
        }
    }
    @Override
    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dx, float dy, int action, boolean isCurrentlyActive){
        super.onChildDraw(canvas,recyclerView,viewHolder,dx,dy,action,isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int cornerOffset = 20;

        if(dx>0){//swipe right
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.colorPrimaryDark));
        }else {
            icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.ic_baseline_delete);
            background = new ColorDrawable(Color.RED);
        }
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight())/2;//put in middle
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) /2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if(dx>0){
            int iconLeft = itemView.getLeft()+iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int)dx) + cornerOffset,itemView.getBottom());
        }else if(dx<0) {
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + (int)dx - cornerOffset, itemView.getTop(), itemView.getRight(),itemView.getBottom());
        }else{
            background.setBounds(0,0,0,0);
        }
        background.draw(canvas);
        icon.draw(canvas);
    }
}
