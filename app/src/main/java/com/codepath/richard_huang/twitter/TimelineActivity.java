package com.codepath.richard_huang.twitter;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by richard_huang on 4/6/17.
 */

public class TimelineActivity extends ListActivity {
    private List<Tweet> tweets;
    private TweetTimelineListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        tweets = new ArrayList<>();

        FixedTweetTimeline timeline = new FixedTweetTimeline.Builder()
                .setTweets(tweets)
                .build();

        adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(timeline)
                .build();

        setListAdapter(adapter);

        loadTweets();

        Button btn = (Button) findViewById(R.id.btnPost);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText etTweet = (EditText) findViewById(R.id.etTweet);
                Call<Tweet> call = TwitterCore
                        .getInstance()
                        .getApiClient()
                        .getStatusesService()
                        .update(etTweet.getText().toString(), null, null, null, null, null, null, null, null);
                call.enqueue(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                        loadTweets();
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
            }
        });
    }

    public void loadTweets() {
        Call<List<Tweet>> call = TwitterCore
                .getInstance()
                .getApiClient()
                .getStatusesService()
                .homeTimeline(
                        null, null, null, null, null, null, null
                );

        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                FixedTweetTimeline timeline = new FixedTweetTimeline.Builder()
                        .setTweets(result.data)
                        .build();

                adapter = new TweetTimelineListAdapter.Builder(TimelineActivity.this)
                        .setTimeline(timeline)
                        .build();

                setListAdapter(adapter);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }
}
