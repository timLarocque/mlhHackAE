package edu.uml.android.cityecho;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by adam on 11/5/16.
 */

public class SubmitHttpPost extends AsyncTask<JSONObject, Void, String> {

    private static final String POST_URL = "http://159.203.136.164/echopy/mr";

    @Override
    protected String doInBackground(JSONObject... params) {
        JSONObject json = params[0];
        try {
            URL url = new URL(POST_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            try {
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("street_num", json.getString("street_num"))
                        .appendQueryParameter("street_name", json.getString("street_name"))
                        .appendQueryParameter("city", json.getString("city"))
                        .appendQueryParameter("state", json.getString("state"))
                        .appendQueryParameter("type", json.getString("type"))
                        .appendQueryParameter("email", json.getString("email"));
                String query = builder.build().getEncodedQuery();
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                urlConnection.connect();
            } catch (JSONException e) {}
        } catch (IOException e) {}
        return "ok";
    }

}
