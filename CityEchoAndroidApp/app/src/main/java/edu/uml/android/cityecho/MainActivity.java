package edu.uml.android.cityecho;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Issue>> {

    private static final int ARTICLE_LOADER_ID = 1;
    private static final String BASE_REQUEST_URL =
            "http://159.203.136.164/echopy/gcp";
    private IssueAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView articleListView = (ListView) findViewById(R.id.issueList);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new IssueAdapter(this, new ArrayList<Issue>());

        articleListView.setAdapter(mAdapter);

        // Set up the onclick listener for add button
        Button addIssue = (Button) findViewById(R.id.addIssueButton);
        addIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, IssueActivity.class);
                startActivity(intent);
            }
        });

        // Get networking info to determine if we have a connection or not
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetcha data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            View loadingIndicator = (View) findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText("No internet connection.");
        }
    }

    @Override
    public Loader<List<Issue>> onCreateLoader(int i, Bundle bundle) {
        return new IssueLoader(this, BASE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Issue>> loader, List<Issue> issues) {
        // Hide loading indicator because the data has been loaded
        try {
            View loadingIndicator = (View) findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            System.out.println("Hmm, couldn't find view.");
        }

        mEmptyStateTextView.setText("Error fetching data.");

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of articles, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (issues != null && !issues.isEmpty()) {
            mAdapter.addAll(issues);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Issue>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
