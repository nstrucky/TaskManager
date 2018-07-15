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
import com.ventoray.taskmanager.web.WebAsyncTask;
import com.ventoray.taskmanager.web.WebQueryUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.ventoray.taskmanager.web.WebApiConstants.BASE_URL;

public class TaskListActivity extends AppCompatActivity
        implements TaskListAdapter.OnTaskClickedListener,
        WebAsyncTask.OnWebTaskCompleteListener {

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

        try {
            URL url = new URL(BASE_URL + "/tasks");
            new WebAsyncTask(this, WebQueryUtils.generateSSLContext(this)).execute(url);
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
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
        Intent intent = new Intent(getApplicationContext(), TaskDetails.class);
        intent.putExtra(TASK_PARCELABLE_KEY, task);
        startActivity(intent);
    }


    /**
     * @param tasks
     */
    @Override
    public void onWebTaskComplete(Task[] tasks) {
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

        taskList.clear();
        taskList.addAll(Arrays.asList(tasks));
        taskListAdapter.notifyDataSetChanged();
    }

}
