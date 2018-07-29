package com.ventoray.taskmanager.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ventoray.taskmanager.R;
import com.ventoray.taskmanager.ui.adapter.TaskListAdapter;
import com.ventoray.taskmanager.web.Task;
import com.ventoray.taskmanager.web.TaskWebApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskListActivity extends AppCompatActivity
        implements TaskListAdapter.OnTaskClickedListener,
        TaskWebApi.OnTasksRetrievedListener {

    public static final String TASK_PARCELABLE_KEY =
            "com.ventoray.taskmanager.ui.TaskListActivity.task_parcelable_key";

    private final String LOG_TAG = getClass().getCanonicalName();

    List<Task> taskList;
    RecyclerView recyclerView;
    TaskListAdapter taskListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        setUpActionBar();
        setUpRecyclerView();

        new TaskWebApi().getUsersTasks(this, this);
    }

    private void setUpRecyclerView() {
        taskList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_tasks);
        taskListAdapter = new TaskListAdapter(this, taskList, this);


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(taskListAdapter);
        taskListAdapter.notifyDataSetChanged();

    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void onTaskClicked(int position) {
        Task task = taskList.get(position);
        Intent intent = new Intent(getApplicationContext(), TaskDetailsActivity.class);
        intent.putExtra(TASK_PARCELABLE_KEY, task);
        startActivity(intent);
    }


    /**
     * @param tasks
     */
    @Override
    public void onTasksRetrieved(Task[] tasks, boolean serverError, String message) {
        if (tasks == null || tasks.length < 1) {
            Toast.makeText(this, "No tasks retrieved, Task[] is null",
                    Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, "No tasks retrieved, Task[] is null");
            return;
        }

        for (int i = 0; i < tasks.length; i++) {
            Log.d(LOG_TAG, tasks[i].getTaskName());
        }

        if (taskList == null) {
            taskList = new ArrayList<>();
        }

        if (message != null) {
            Log.i(LOG_TAG, message);
        }

        if (serverError) {
            Log.e(LOG_TAG, "Server Error occurred");
        }

        taskList.clear();
        taskList.addAll(Arrays.asList(tasks));
        taskListAdapter.notifyDataSetChanged();
    }

}
