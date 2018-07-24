package com.ventoray.taskmanager.ui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ventoray.taskmanager.R;

public class MainActivity extends AppCompatActivity {


    private Button getAllTasksButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAllTasksButton = (Button) findViewById(R.id.button_get_all_tasks);

        Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
        setOnClickListeners();
    }


    private void setOnClickListeners() {
        getAllTasksButton.setOnClickListener(onClickListener);
    }




    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();

            switch (id) {
                case R.id.button_get_all_tasks:
                    Intent intent = new Intent(getApplicationContext(), TaskListActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

}
