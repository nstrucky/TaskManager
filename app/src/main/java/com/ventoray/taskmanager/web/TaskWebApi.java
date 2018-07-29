package com.ventoray.taskmanager.web;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import static com.ventoray.taskmanager.web.WebApiConstants.BASE_URL;

/**
 * Created by Nick on 7/28/2018.
 */

public class TaskWebApi {

    public interface OnTasksRetrievedListener {
        void onTasksRetrieved(Task[] tasks);
    }

    public interface OnTaskCreatedListener {
        void onTaskCreated(int taskId, String message);
    }


    /**
     *
     * @param context
     * @param listener
     */
    public void getUsersTasks(Context context, OnTasksRetrievedListener listener) {
        try {
            URL url = new URL(BASE_URL + "/tasks");
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
     * @param postUrlParams
     * @param context
     * @param listener
     */
    public void createTask(String postUrlParams, Context context, OnTaskCreatedListener listener) {

        try {
            URL url = new URL(BASE_URL + "/tasks");
            new CreateTaskAsyncTask(postUrlParams,
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

    public void updateTask() {

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
        protected void onPostExecute(String tasksString) {
            super.onPostExecute(tasksString);
            Task[] tasks = JSONParser.parseTasksFromJson(tasksString);
            listener.onTasksRetrieved(tasks);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            listener.onTaskCreated(0, s);
        }


    }


}
