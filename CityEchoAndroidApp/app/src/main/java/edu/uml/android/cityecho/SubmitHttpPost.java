package edu.uml.android.cityecho;

import android.database.CursorJoiner;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by adam on 11/5/16.
 */

public class SubmitHttpPost extends AsyncTask<JSONObject, Void, String> {

    private static final String GET_URL = "http://159.203.136.164/echopy/mr";

    @Override
    protected String doInBackground(JSONObject... params) {
        JSONObject json = params[0];
        String jsonResponse = null;
        Uri baseUri = Uri.parse(GET_URL);
        try {
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("street_num", json.getString("street_num"))
                    .appendQueryParameter("street_name", json.getString("street_name"))
                    .appendQueryParameter("city", json.getString("city"))
                    .appendQueryParameter("state", json.getString("state"))
                    .appendQueryParameter("type", json.getString("type"))
                    .appendQueryParameter("email", json.getString("email"));
            try {
                URL url = new URL(uriBuilder.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                jsonResponse = response.toString();
            } catch (IOException e) {}
        } catch (JSONException e) {}
        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String result) {

    }

}
