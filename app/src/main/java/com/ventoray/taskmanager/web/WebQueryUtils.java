package com.ventoray.taskmanager.web;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import static com.ventoray.taskmanager.web.WebApiConstants.HTTP_RESPONSE_OK;
import static com.ventoray.taskmanager.web.WebApiConstants.TEST_API_KEY;

/**
 * Created by Nick on 7/8/2018.
 */

public class WebQueryUtils {

    public static final String LOG_TAG = "WebQueryUtils";


    public static SSLContext generateSSLContext(Context context) throws CertificateException, IOException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // From https://www.washington.edu/itconnect/security/ca/load-der.crt
        InputStream caInput = new BufferedInputStream(context.getResources().getAssets()
                .open("server.crt")
        );
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }


    public static Object[] makeHttpUrlRequest(URL url, SSLContext sslContext) {

        InputStream in = null;
        String jsonToParse = null;
        HttpsURLConnection urlConnection = null;
        Log.d(LOG_TAG, "Opening url connection for " + url.toString());

        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });

            //TODO remove test api key
            urlConnection.setRequestProperty("authorization", TEST_API_KEY);
            Log.d(LOG_TAG, "Authorization: " +
                    urlConnection.getRequestProperty("authorization"));

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            Log.d(LOG_TAG, "response code " + urlConnection.getResponseCode() + "\n" +
                    urlConnection.getResponseMessage());
            if (urlConnection.getResponseCode() == HTTP_RESPONSE_OK) {
                in = urlConnection.getInputStream();
                jsonToParse = getResponseFromHTTPUrl(in);

            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.d(LOG_TAG, e.getMessage());
                }
            }
        }
        return parseTasksFromJson(jsonToParse);
    }


    /**
     * This method uses a BufferedReader to read each line of the http response and append to a
     * StringBuilder.
     *
     * @param in InputStream created by the HttpUrlConnection in makeHttpUrlRequest()
     * @return
     */
    private static String getResponseFromHTTPUrl(InputStream in) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);

            }
        } catch (IOException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
        Log.d(LOG_TAG, "+++++++++++++++++++++++++++++++++++++++" + stringBuilder.toString());
        return stringBuilder.toString();
    }


    private static Task[] parseTasksFromJson(String jsonToParse) {
        Task[] tasks = null;

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
