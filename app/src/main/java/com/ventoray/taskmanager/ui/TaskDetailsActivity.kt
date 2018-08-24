package com.ventoray.taskmanager.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ventoray.taskmanager.R
import com.ventoray.taskmanager.ui.TaskListActivity.TASK_PARCELABLE_KEY
import com.ventoray.taskmanager.web.Task
import com.ventoray.taskmanager.web.Task.*
import com.ventoray.taskmanager.web.TaskWebApi
import kotlinx.android.synthetic.main.activity_task_details.*

class TaskDetailsActivity : TaskWebApi.OnTaskEditedListener, AppCompatActivity() {


    var task : Task = Task()//there must be another way to initialize a variable for this scope
    var taskStatus : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        task = intent.getParcelableExtra<Task>(TASK_PARCELABLE_KEY)
        editText_name.setText(task.taskName)
        setStatusText(task.status)
        setClickListeners()

    }


    val clickListener = object : View.OnClickListener {
        override fun onClick(view: View?) {
            if (view == null) return
            var viewId = view.id

            when(viewId) {
                button_not_started.id -> {setStatusText(STATUS_NOT_STARTED)}
                button_in_progress.id -> {setStatusText(STATUS_IN_PROGRESS)}
                button_completed.id -> {setStatusText(STATUS_DONE)}
            }
        }
    }


    fun setClickListeners() {
        button_not_started.setOnClickListener(clickListener)
        button_in_progress.setOnClickListener(clickListener)
        button_completed.setOnClickListener(clickListener)
    }


    /**
     * Super cool FUNction (lol) that sets the status string and integer
     */
    fun setStatusText(statusInt : Int) {
        var statusString : String

        when(statusInt) {
            STATUS_NOT_STARTED -> {statusString = getString(R.string.not_started)
                taskStatus = STATUS_NOT_STARTED}
            STATUS_IN_PROGRESS -> {
                statusString = getString(R.string.in_progress)
                taskStatus = STATUS_IN_PROGRESS
            }
            STATUS_DONE -> {
                statusString = getString(R.string.completed)
                taskStatus = STATUS_DONE
            }
            else -> {
                statusString = getString(R.string.status_error)
                taskStatus = STATUS_ERROR
            }
        }
        textView_title.setText(statusString)

        task.status = taskStatus
        TaskWebApi().updateTask(task, this, this)



    }

    override fun onTaskCreated(taskId: Int, serverError: Boolean, message: String?) {

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
