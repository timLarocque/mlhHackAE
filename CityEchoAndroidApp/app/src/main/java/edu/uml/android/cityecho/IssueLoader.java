package edu.uml.android.cityecho;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.List;

public class IssueLoader extends AsyncTaskLoader<List<Issue>> {

    private String urlToLoad;
    private Context context;

    public IssueLoader(Context context, String url) {
        super(context);
        this.context = context;
        urlToLoad = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Issue> loadInBackground() {
        if (urlToLoad == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Issue> issues = QueryUtils.fetchIssueData(urlToLoad);
        // Sort based on distance
        if (issues.size() != 0) {
            issues = QueryUtils.sortByDistance(issues);
        }

        return issues;
    }

}
