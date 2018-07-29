package com.ventoray.taskmanager.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
            Toast.makeText(this, "Tada", Toast.LENGTH_SHORT).show()
            saveNewTask()
        })
    }


    fun saveNewTask() {
        val taskName : String = textInputLayout_task_name.editText?.text.toString()
        val status : String = textInputLayout_status.editText?.text.toString()

        val urlPostParams : String = "task=" + taskName + "&" + "status=" + status

        TaskWebApi().createTask(urlPostParams, this, this)

    }

    override fun onTaskCreated(taskId: Int, message: String?) {
        //for now do nothing
    }
}
