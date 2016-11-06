package edu.uml.android.cityecho;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class IssueLoader extends AsyncTaskLoader<List<Issue>> {

    private String urlToLoad;

    public IssueLoader(Context context, String url) {
        super(context);
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
        return issues;
    }

}
