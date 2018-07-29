package com.ventoray.taskmanager.web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ventoray.taskmanager.web.Task.STATUS_DONE;
import static com.ventoray.taskmanager.web.Task.STATUS_ERROR;
import static com.ventoray.taskmanager.web.Task.STATUS_NOT_STARTED;

/**
 * Created by Nick on 7/28/2018.
 */

public class JSONParser {

    /**
     * Converts String of JSON from server into an array of Task objects
     * @param jsonToParse
     * @return array of Task Objects, returns null if json is null
     */
    public static Task[] parseTasksFromJson(String jsonToParse) {
        Task[] tasks = null;
        if (jsonToParse == null) {
            return tasks;
        }

        try {
            JSONObject fullJson = new JSONObject(jsonToParse);

            JSONArray tasksJArray = fullJson.getJSONArray(WebApiConstants.HttpPath.TASKS);
            if (tasksJArray != null) {
                tasks = new Task[tasksJArray.length()];

                for (int i = 0; i < tasks.length; i++) {
                    JSONObject jsonObject = tasksJArray.getJSONObject(i);

                    int uniqueId = Integer.valueOf(
                            jsonObject.getString(WebApiConstants.DbVariables.UNIQUE_ID));
                    String taskString = jsonObject.getString(WebApiConstants.DbVariables.TASK);
                    int status = Integer.valueOf(
                            jsonObject.getString(WebApiConstants.DbVariables.TASK_STATUS));
                    String dateString = jsonObject.getString(WebApiConstants.DbVariables.CREATED_AT);

                    if (taskString == null) {
                        taskString = "";
                    }

                    if (status > STATUS_DONE || status < STATUS_NOT_STARTED) {
                        status = STATUS_ERROR;
                    }

                    if (dateString == null) {
                        dateString = "";
                    }

                    Task task = new Task(uniqueId, taskString, status, dateString);
                    tasks[i] = task;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return tasks;
    }


}
