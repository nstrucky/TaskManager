package com.ventoray.taskmanager.web;

import android.content.Context;
import android.support.annotation.StringDef;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import static com.ventoray.taskmanager.web.WebApiConstants.TEST_API_KEY;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * Created by Nick on 7/8/2018.
 */

@Retention(RetentionPolicy.SOURCE)
@StringDef({
        WebApiConstants.HttpMethod.GET,
        WebApiConstants.HttpMethod.POST,
        WebApiConstants.HttpMethod.PUT,
        WebApiConstants.HttpMethod.DELETE
})
@interface HttpMethods {}



 class WebQueryUtils {

    static final String LOG_TAG = "WebQueryUtils";

    static String makeHttpUrlRequest(URL url, SSLContext sslContext,
                                           @HttpMethods String httpMethod, String postUrlParams) {

        InputStream in = null;
        String jsonToParse = null;
        HttpsURLConnection urlConnection = null;

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
            urlConnection.setRequestMethod(httpMethod);
            urlConnection.connect();

            if (httpMethod != null && httpMethod.equals(WebApiConstants.HttpMethod.POST) &&
                    postUrlParams != null) {

                Log.i(LOG_TAG, postUrlParams);

//                urlConnection.setDoOutput(true);
                DataOutputStream write = new DataOutputStream(urlConnection.getOutputStream());
                write.writeBytes(postUrlParams);
                write.flush();
                write.close();
            }

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HTTP_OK || responseCode == HTTP_CREATED) {
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
        return jsonToParse;
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




    /**
     * Generates an SSLContext so android will trust the server's certificate (which should be
     * saved in the assets directory as 'server.crt')
     * @param context
     * @return
     * @throws CertificateException
     * @throws IOException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext generateSSLContext(Context context) throws CertificateException,
            IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
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


}
