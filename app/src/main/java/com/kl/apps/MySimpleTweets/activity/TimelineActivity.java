package com.kl.apps.MySimpleTweets.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.kl.apps.MySimpleTweets.EndlessScrollListener;
import com.kl.apps.MySimpleTweets.R;
import com.kl.apps.MySimpleTweets.TwitterApplication;
import com.kl.apps.MySimpleTweets.TwitterClient;
import com.kl.apps.MySimpleTweets.adapter.TweetArrayAdapter;
import com.kl.apps.MySimpleTweets.data.QueryReq;
import com.kl.apps.MySimpleTweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.builder.api.TwitterApi;

import java.util.ArrayList;

public class TimelineActivity extends ActionBarActivity {

    public static final Integer REQUEST_CODE = 200;

    public static final String TAG = "devdev";

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetArrayAdapter tweetAdapter;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetArrayAdapter(this, tweets);
        lvTweets.setAdapter(tweetAdapter);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int pageLabel, int totalItemsCount) {
                Log.i(TAG, "onLoadMore, page" + pageLabel);
                QueryReq req = new QueryReq();
                req.setSinceIdByPage(pageLabel - 1);
                populateTimeLine(req);
            }
        });

        client = TwitterApplication.getRestClient();
		QueryReq req = new QueryReq();
        req.setSinceIdByPage(0);
        populateTimeLine(req);
    }

    private void populateTimeLine(QueryReq req) {
        client.getHomeTimeLine(req, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d(TAG, jsonArray.toString());
                tweetAdapter.addAll(Tweet.fromJSONArray(jsonArray));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, errorResponse.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.compose_post) {
            onComposeClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requectCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requectCode == REQUEST_CODE) {
            Tweet newTweet = (Tweet) data.getSerializableExtra("tweet");
            tweets.add(0, newTweet);
            tweetAdapter.notifyDataSetChanged();
            lvTweets.setSelectionAfterHeaderView();
        }
    }

    private void onComposeClick() {
        Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
