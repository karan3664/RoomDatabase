package com.karan_brahmaxatriya.roomdatabasedemo.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karan_brahmaxatriya.roomdatabasedemo.ClickListener;
import com.karan_brahmaxatriya.roomdatabasedemo.DragItemTouchHelper;
import com.karan_brahmaxatriya.roomdatabasedemo.MainActivity;
import com.karan_brahmaxatriya.roomdatabasedemo.R;
import com.karan_brahmaxatriya.roomdatabasedemo.RecyclerViewItemClickListener;
import com.karan_brahmaxatriya.roomdatabasedemo.SwipeItemTouchHelper;
import com.karan_brahmaxatriya.roomdatabasedemo.TasksAdapter;
import com.karan_brahmaxatriya.roomdatabasedemo.database.DatabaseClient;
import com.karan_brahmaxatriya.roomdatabasedemo.database.Task;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private RecyclerView recyclerView;
    List<Task> taskList = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelper mItemTouchHelper2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        getTasks();
        return root;
    }

    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                taskList = DatabaseClient
                        .getInstance(getActivity())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                TasksAdapter adapter = new TasksAdapter(getActivity(), tasks);
                recyclerView.setAdapter(adapter);
                recyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(getActivity(), recyclerView, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
//                        Toast.makeText(getActivity(), "Single Click on position        :" + position,
//                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
                adapter.setDragListener(new TasksAdapter.OnStartDragListener() {
                    @Override
                    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                        mItemTouchHelper.startDrag(viewHolder);
                    }
                });

                ItemTouchHelper.Callback callback = new DragItemTouchHelper(adapter);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(recyclerView);

                ItemTouchHelper.Callback callbacks = new SwipeItemTouchHelper(adapter);
                mItemTouchHelper2 = new ItemTouchHelper(callbacks);
                mItemTouchHelper2.attachToRecyclerView(recyclerView);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        getTasks();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getTasks();
    }
}