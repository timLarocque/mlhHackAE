package edu.uml.android.cityecho;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    private Menu mRefresh;

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

        loadData();
    }

    public void loadData() {
        ListView articleListView = (ListView) findViewById(R.id.issueList);
        ProgressBar pb =  (ProgressBar) findViewById(R.id.loading_indicator);
        articleListView.setEmptyView(pb);
        mAdapter = new IssueAdapter(this, new ArrayList<Issue>());

        articleListView.setAdapter(mAdapter);

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
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mRefresh = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(ARTICLE_LOADER_ID, null, this);
                loadData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mRefresh != null) {
            final MenuItem refreshItem = mRefresh
                    .findItem(R.id.menuRefresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_indeterminite_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
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
