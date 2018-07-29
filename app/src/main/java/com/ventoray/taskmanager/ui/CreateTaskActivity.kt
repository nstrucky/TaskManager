package com.ventoray.taskmanager.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ventoray.taskmanager.R
import com.ventoray.taskmanager.web.TaskWebApi
import kotlinx.android.synthetic.main.activity_create_task.*

class CreateTaskActivity : TaskWebApi.OnTaskCreatedListener, AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        button_save.setOnClickListener(View.OnClickListener {
            saveNewTask()
        })
    }


    fun saveNewTask() {
        val taskName : String = textInputLayout_task_name.editText?.text.toString()
        TaskWebApi().createTask(taskName, this, this)

    }

    override fun onTaskCreated(taskId: Int, serverError : Boolean, message: String?) {
        if (serverError) {
            Toast.makeText(this, "Error creating task.", Toast.LENGTH_SHORT)
                    .show()
        }

        Log.i("CreateTaskActivity", message)
        Log.i("CreateTaskActivity", "Task created: " + taskId)
        Log.i("CreateTaskActivity", "Server Error: " + serverError)

    }
}
