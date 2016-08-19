package nl.loemba.fons.zwave2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by nly98945 on 29/09/2014.
 * Modified from http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
 * http://developer.android.com/reference/java/net/HttpURLConnection.html#method
 */

public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public ServiceHandler() {
    }

    public String makeServiceCall(String urlStr, int method) {
        StringBuilder builder = new StringBuilder();
        Boolean fail = Boolean.FALSE;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // Checking http request method type
            if (method == POST) {
                urlConnection.setRequestMethod("POST");

            } else if (method == GET) {
                urlConnection.setRequestMethod("GET");
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String current = null;
            while((current = in.readLine()) != null)
            {
                builder.append(current).append("\n");
            }
            in.close();
            //fail = (builder.toString().equals("null\n"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            fail = Boolean.TRUE;
        }
        if (!fail)
            return builder.toString();
        else
            return "fail";
    }
}

