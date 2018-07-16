package com.ventoray.taskmanager.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ventoray.taskmanager.R

class TaskDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }



}
