package edu.uml.android.cityecho;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static final String API_KEY = "AIzaSyDBBHh--w4mJcHZGMiwfLMYEdrEsR97wtc";

    private static final String BASE_GOOGLE_REQUEST_URL =
            "https://maps.googleapis.com/maps/api/staticmap";

    private QueryUtils() {}

    public static List<Issue> fetchIssueData(String requestUrl) {
        // Create URL object
        String jsonResponse = null;
        try {
            URL url = new URL(requestUrl);
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
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Issue> issues = extractIssuesFromJson(jsonResponse);

        return issues;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Issue> extractIssuesFromJson(String issueJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(issueJSON)) {
            return null;
        }

        List<Issue> issues = new ArrayList<>();

        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(issueJSON);
            JSONArray issueArray = baseJsonResponse.getJSONArray("result");

            for (int i = 0; i < issueArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentArticle = issueArray.getJSONObject(i);

                int streetNum = currentArticle.getInt("street_num");
                String streetName = currentArticle.getString("street_name");
                String city = currentArticle.getString("city");
                String state = currentArticle.getString("state");
                String type = currentArticle.getString("issueType");
                Bitmap map = null;
                try {
                    String mapUrl = streetNum + "+" + streetName + "+" + city + "," + state;
                    Uri baseUri = Uri.parse(BASE_GOOGLE_REQUEST_URL);
                    Uri.Builder uriBuilder = baseUri.buildUpon();
                    uriBuilder.appendQueryParameter("key", API_KEY);
                    uriBuilder.appendQueryParameter("center", mapUrl);
                    uriBuilder.appendQueryParameter("size","640x400");
                    uriBuilder.appendQueryParameter("zoom","18");
                    uriBuilder.appendQueryParameter("markers", "color:blue|label:I|" + mapUrl);
                    String request_url = uriBuilder.toString();
                    URL imageURL = new URL(request_url);
                    map = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                } catch (IOException e) {
                    Log.e("ERROR", "Problem loading the thumbnail.", e);
                }

                Issue issue = new Issue(streetNum, streetName, city, state, type, map);

                issues.add(issue);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }

        // Return the list of earthquakes
        return issues;
    }
}