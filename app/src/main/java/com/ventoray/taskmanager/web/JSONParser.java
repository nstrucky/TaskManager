package com.ventoray.taskmanager.web;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static com.ventoray.taskmanager.web.Task.STATUS_DONE;
import static com.ventoray.taskmanager.web.Task.STATUS_ERROR;
import static com.ventoray.taskmanager.web.Task.STATUS_NOT_STARTED;

/**
 * Created by Nick on 7/28/2018.
 */

public class JSONParser {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef ({
            TYPE_STRING,
            TYPE_INT,
            TYPE_DOUBLE,
            TYPE_BOOLEAN
    })
    @interface JavaTypes {}



    /**
     * Constants representing Java object/primitive types.  These are used in parseJsonResponse
     * which determines the type of data to parse from the jsonToParse string passed in as an
     * argument.
     */
    static final int TYPE_STRING = 1001;
    static final int TYPE_INT = 1002;
    static final int TYPE_DOUBLE = 1003;
    static final int TYPE_BOOLEAN = 1004;


    /**
     * Converts String of JSON from server into an array of Task objects and returns a Bundle
     * including a String message
     * @param jsonToParse
     * @return array of Task Objects, returns null if json is null
     */
    public static Bundle parseTasksFromJson(String jsonToParse) {
        Task[] tasks = null;
        Bundle bundle = new Bundle();
        bundle.putString(WebApiConstants.JSONResponseKey.MESSAGE, "parseTAsksFromJson: Success");
        if (jsonToParse == null) {
            bundle.putString(WebApiConstants.JSONResponseKey.MESSAGE, "parseTAsksFromJson: Error_ No data returned by server");
            return bundle;
        }

        try {
            JSONObject fullJson = new JSONObject(jsonToParse);
            boolean serverError = fullJson.getBoolean(WebApiConstants.JSONResponseKey.ERROR);
            JSONArray tasksJArray =
                    fullJson.getJSONArray(WebApiConstants.JSONResponseKey.TASKS);
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
            } else {
                bundle.putString(WebApiConstants.JSONResponseKey.MESSAGE,
                        "parseTAsksFromJson: Error_ No data in tasks array.");
            }
            bundle.putParcelableArray(WebApiConstants.JSONResponseKey.TASKS, tasks);
            bundle.putBoolean(WebApiConstants.JSONResponseKey.ERROR, serverError);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bundle;
    }


    /**
     *
     * @param jsonToParse
     * @param variablesExpected - List which is comprised of Pair objects, an integer representing
     *                          the type of object to be returned  and a String representing the name
     * @return
     */
    static Bundle parseJsonResponse(String jsonToParse,
                                    List<Pair<String, Integer>> variablesExpected) {

        Bundle bundle = new Bundle();
        if (variablesExpected == null || jsonToParse == null) return bundle;
        if (variablesExpected.isEmpty() || jsonToParse.isEmpty()) return bundle;

        try {
            JSONObject fullJson = new JSONObject(jsonToParse);
            for (Pair<String, Integer> pair : variablesExpected) {
                String varName = pair.first;
                int varType = pair.second;

                switch (varType) {
                    case TYPE_STRING:
                        String varStringResult = fullJson.getString(varName);
                        if (varStringResult != null && !varStringResult.isEmpty()) {
                            bundle.putString(varName, varStringResult);
                        }
                        break;

                    case TYPE_INT:
                        int varIntResult = fullJson.getInt(varName);
                        bundle.putInt(varName, varIntResult);
                        break;

                    case TYPE_DOUBLE:
                        int varDoubleResult = fullJson.getInt(varName);
                        bundle.putInt(varName, varDoubleResult);
                        break;

                    case TYPE_BOOLEAN:
                        boolean varBoolResult = fullJson.getBoolean(varName);
                        bundle.putBoolean(varName, varBoolResult);
                        break;

                    default:
                        bundle.putString(varName, "JSONParser Error: Type unknown");
                        break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONParser", e.getMessage());
        }

        return bundle;

    }








}
