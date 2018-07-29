package com.ventoray.taskmanager.web;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import static com.ventoray.taskmanager.web.JSONParser.TYPE_BOOLEAN;
import static com.ventoray.taskmanager.web.JSONParser.TYPE_DOUBLE;
import static com.ventoray.taskmanager.web.JSONParser.TYPE_INT;
import static com.ventoray.taskmanager.web.JSONParser.TYPE_STRING;
import static com.ventoray.taskmanager.web.WebApiConstants.BASE_URL;
import static com.ventoray.taskmanager.web.WebApiConstants.HttpParam.TASK;
import static com.ventoray.taskmanager.web.WebApiConstants.HttpPath.TASKS;
import static com.ventoray.taskmanager.web.WebApiConstants.JSONResponseKey.ERROR;
import static com.ventoray.taskmanager.web.WebApiConstants.JSONResponseKey.MESSAGE;
import static com.ventoray.taskmanager.web.WebApiConstants.JSONResponseKey.TASK_ID;

/**
 * Created by Nick on 7/28/2018.
 */

public class TaskWebApi {

    public interface OnTasksRetrievedListener {
        void onTasksRetrieved(Task[] tasks, boolean serverError, String message);
    }

    public interface OnTaskCreatedListener {
        void onTaskCreated(int taskId, boolean serverError, String message);
    }


    /**
     *
     * @param context
     * @param listener
     */
    public void getUsersTasks(Context context, OnTasksRetrievedListener listener) {
        try {
            URL url = new URL(BASE_URL + "/" + TASKS);
            new RetrieveUsersTasksTask(WebQueryUtils.generateSSLContext(context), listener)
                    .execute(url);
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

    /**
     *
     * @param taskName
     * @param context
     * @param listener
     */
    public void createTask(String taskName, Context context, OnTaskCreatedListener listener) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            stringBuilder.append(URLEncoder.encode(TASK, "UTF-8"));
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(taskName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String urlPostParams = stringBuilder.toString();

        try {
            URL url = new URL(BASE_URL + "/tasks");
            new CreateTaskAsyncTask(urlPostParams,
                    WebQueryUtils.generateSSLContext(context),
                    listener)
                    .execute(url);
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

    public void deleteTask() {

    }

    public void updateTask(Task task) {

    }


    /**
     *
     */
    static class RetrieveUsersTasksTask extends AsyncTask<URL, Void, String> {

        private OnTasksRetrievedListener listener;
        private SSLContext sslContext;

        public RetrieveUsersTasksTask(SSLContext sslContext, OnTasksRetrievedListener listener) {
            this.listener = listener;
            this.sslContext = sslContext;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            return WebQueryUtils.makeHttpUrlRequest(url, sslContext,
                    WebApiConstants.HttpMethod.GET,
                    null);
        }

        @Override
        protected void onPostExecute(String jsonToParse) {
            super.onPostExecute(jsonToParse);
            Bundle bundle = JSONParser.parseTasksFromJson(jsonToParse);
            Task[] tasks = (Task[]) bundle.getParcelableArray(WebApiConstants.JSONResponseKey.TASKS);
            boolean serverError = bundle.getBoolean(ERROR);
            String message = bundle.getString(MESSAGE);
            listener.onTasksRetrieved(tasks, serverError, message);
        }
    }

    /**
     *
     */
    static class CreateTaskAsyncTask extends AsyncTask<URL, Void, String> {

        String postUrlParams;
        OnTaskCreatedListener listener;
        SSLContext sslContext;


        public CreateTaskAsyncTask(String postUrlParams,
                                   SSLContext sslContext, OnTaskCreatedListener listener) {
            this.postUrlParams = postUrlParams;
            this.sslContext = sslContext;
            this.listener = listener;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            return WebQueryUtils.makeHttpUrlRequest(url,
                    sslContext,
                    WebApiConstants.HttpMethod.POST,
                    postUrlParams);
        }

        @Override
        protected void onPostExecute(String jsonToParse) {
            super.onPostExecute(jsonToParse);
            handleJson(jsonToParse);
        }


        private void handleJson(String jsonToParse) {
            List<Pair<String, Integer>> expectedResults = new ArrayList<>();

            expectedResults.add(new Pair<>(MESSAGE, TYPE_STRING));
            expectedResults.add(new Pair<>(ERROR, TYPE_BOOLEAN));
            expectedResults.add(new Pair<>(TASK_ID, TYPE_INT));

            Bundle bundle = JSONParser.parseJsonResponse(jsonToParse, expectedResults);
            String message = bundle.getString(MESSAGE);
            boolean serverError = bundle.getBoolean(ERROR);
            int taskCreated = bundle.getInt(TASK_ID);

            if (message == null) message = "no message!";

            listener.onTaskCreated(taskCreated, serverError, message);
        }
    }





}
