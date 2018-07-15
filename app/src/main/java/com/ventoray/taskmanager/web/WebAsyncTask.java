package com.ventoray.taskmanager.web;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

/**
 * Created by Nick on 7/8/2018.
 */

public class WebAsyncTask extends AsyncTask<URL, Void, Task[]> {

    public interface OnWebTaskCompleteListener {
        void onWebTaskComplete(Task[] tasks);
    }

    private OnWebTaskCompleteListener listener;
    private SSLContext sslContext;

    public WebAsyncTask(OnWebTaskCompleteListener listener, SSLContext sslContext) {
        this.listener = listener;
        this.sslContext = sslContext;
    }

    @Override
    protected Task[] doInBackground(URL... urls) {
        URL url = urls[0];
        return WebQueryUtils.makeHttpUrlRequest(url, sslContext);
    }

    @Override
    protected void onPostExecute(Task[] tasks) {
        super.onPostExecute(tasks);
        listener.onWebTaskComplete(tasks);
    }
}
