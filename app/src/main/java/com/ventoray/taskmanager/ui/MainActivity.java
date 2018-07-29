package com.ventoray.taskmanager.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ventoray.taskmanager.R;

public class MainActivity extends AppCompatActivity {


    private Button getAllTasksButton;

    private FloatingActionButton fabCreateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllTasksButton = (Button) findViewById(R.id.button_get_all_tasks);

        fabCreateTask = (FloatingActionButton) findViewById(R.id.fab_add_task);

        setOnClickListeners();
    }


    private void setOnClickListeners() {
        getAllTasksButton.setOnClickListener(onClickListener);
        fabCreateTask.setOnClickListener(onClickListener);
    }




    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            Intent intent;
            switch (id) {
                case R.id.button_get_all_tasks:
                    intent = new Intent(getApplicationContext(), TaskListActivity.class);
                    startActivity(intent);
                    break;

                case R.id.fab_add_task:
                    intent = new Intent(getApplicationContext(), CreateTaskActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

}
