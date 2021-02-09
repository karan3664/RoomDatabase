package com.karan_brahmaxatriya.roomdatabasedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.karan_brahmaxatriya.roomdatabasedemo.database.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> implements DragItemTouchHelper.MoveHelperAdapter, SwipeItemTouchHelper.SwipeHelperAdapter {

    private Context mCtx;
    private List<Task> taskList;
    private OnStartDragListener mDragStartListener = null;
    private List<Task> items_swiped = new ArrayList<>();
    public TasksAdapter(Context mCtx, List<Task> taskList) {
        this.mCtx = mCtx;
        this.taskList = taskList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_home, parent, false);
        return new TasksViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(TasksViewHolder holder, int position) {
        Task t = taskList.get(position);
        holder.txtFullName.setText("Name : " + t.getFullname());
        holder.txtMobile.setText("Mobile : " + t.getMobile());
        holder.txtEmail.setText("Email : " + t.getEmail());
        holder.txtGender.setText("Gender : " + t.getGender());
        holder.txtDateOfBirth.setText("Date Of Birth : " + t.getDob());
        holder.txtDeveloper.setText("Developer : " + t.getSpin_value());
        holder.bt_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN && mDragStartListener != null) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        if (taskList.get(position).swiped) {
            items_swiped.remove(taskList.get(position));
            taskList.remove(position);
            notifyItemRemoved(position);
            return;
        }

        taskList.get(position).swiped = true;
        items_swiped.add(taskList.get(position));
        notifyItemChanged(position);
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                for (Task s : items_swiped) {
                    int index_removed = taskList.indexOf(s);
                    if (index_removed != -1) {
                        taskList.remove(index_removed);
                        notifyItemRemoved(index_removed);
                    }
                }
                items_swiped.clear();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }
    public void setDragListener(OnStartDragListener dragStartListener) {
        this.mDragStartListener = dragStartListener;
    }
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(taskList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtFullName, txtMobile, txtEmail, txtGender, txtDateOfBirth, txtDeveloper;
        public ImageButton bt_move;
        public TasksViewHolder(View itemView) {
            super(itemView);

            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtMobile = itemView.findViewById(R.id.txtMobile);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtGender = itemView.findViewById(R.id.txtGender);
            txtDateOfBirth = itemView.findViewById(R.id.txtDateOfBirth);
            txtDeveloper = itemView.findViewById(R.id.txtDeveloper);
            bt_move = (ImageButton) itemView.findViewById(R.id.bt_move);


            itemView.setOnClickListener(this);

           /* recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                    recyclerView, new ClickListener() {
                @Override
                public void onClick(View view, final int position) {
                    //Values are passing to activity & to fragment as well
                    Toast.makeText(MainActivity.this, "Single Click on position        :"+position,
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onLongClick(View view, int position) {
                    Toast.makeText(MainActivity.this, "Long press on position :"+position,
                            Toast.LENGTH_LONG).show();
                }
            }));*/
        }

        @Override
        public void onClick(View view) {
            Task task = taskList.get(getAdapterPosition());

            Intent intent = new Intent(mCtx, AddTaskActivity.class);
            intent.putExtra("task", task);

            mCtx.startActivity(intent);
        }

    }
}
