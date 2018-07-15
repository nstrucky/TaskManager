package com.ventoray.taskmanager.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ventoray.taskmanager.R;
import com.ventoray.taskmanager.web.Task;

import java.util.List;

/**
 * Created by Nick on 7/8/2018.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {


    public interface OnTaskClickedListener {
        void onTaskClicked(int position);
    }

    List<Task> taskList;
    OnTaskClickedListener taskClickedListener;
    Context context;

    public TaskListAdapter(Context context, List<Task> taskList,
                           OnTaskClickedListener taskClickedListener) {
        this.context = context;
        this.taskList = taskList;
        this.taskClickedListener = taskClickedListener;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
            .from(context).inflate(R.layout.list_item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        String status = determineStatus(task.getStatus());

        holder.taskNameTextView.setText(task.getTaskName());
        holder.createdTimeTextView.setText(task.getMysqlTimestamp());
        holder.statusTextView.setText(status);
    }

    @Override
    public int getItemCount() {
        if (taskList == null || taskList.isEmpty()) {
            return 0;
        }
        return taskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView taskNameTextView;
        TextView statusTextView;
        TextView createdTimeTextView;

        public TaskViewHolder(View itemView) {
            super(itemView);

            taskNameTextView = (TextView) itemView.findViewById(R.id.textView_task_name);
            statusTextView = (TextView) itemView.findViewById(R.id.text_view_status);
            createdTimeTextView = (TextView) itemView.findViewById(R.id.text_view_timestamp);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            taskClickedListener.onTaskClicked(position);
        }
    }

    /**
     * converts the status code from db into a readable string to display in UI
     * @param statusCode - from mysql DB - note that unless one of the status from Task class are
     *                   passed as an argument, this method will return with the error string.
     * @return - String
     */
    private String determineStatus(int statusCode) {
        switch (statusCode) {
            case Task.STATUS_NOT_STARTED:
                return context.getResources().getString(R.string.status_not_started);
            case Task.STATUS_IN_PROGRESS:
                return context.getResources().getString(R.string.status_in_progress);
            case Task.STATUS_DONE:
                return context.getResources().getString(R.string.status_done);
            default:
                return context.getResources().getString(R.string.status_error);
        }
    }
}
