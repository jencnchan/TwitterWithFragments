package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SmartFragmentStatePagerAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.ComposeDialogFragment;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.ProfileActivity;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeDialogFragment.ComposeDialogListener {

//    private TweetListFragment fragmentTweetsList;
//    private TwitterClient client;
//    private ArrayList<Tweet> tweets;
//    private TweetsArrayAdapter aTweets;
//    private ListView lvTweets;
//   private RelativeLayout swipeContainer;

    ViewPager vpPager;
    User user;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        setContentView(R.layout.activity_timeline);

        // Get the viewpager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        // Set the viewpager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // Find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the tabstrip to the viewpager
        tabStrip.setViewPager(vpPager);

        // Get the account info
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                // My current user account's info
                //         getSupportActionBar().setTitle("@" + user.getScreenName());
                //          populateProfileHeader(user);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });


//        swipeContainer =  (RelativeLayout) findViewById(R.id.activity_timeline);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                fetchTimelineAsync(0);
//            }
//        });
//        // Configure the refreshing colors
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);


//        lvTweets = (ListView) findViewById(lvTweets);
//        tweets = new ArrayList<>();
//        aTweets = new TweetsArrayAdapter(this, tweets);
//        lvTweets.setAdapter(aTweets);

//        lvTweets.setOnScrollListener(new EndlessScrollListener() {
//            @Override
//            public boolean onLoadMore(int page, int totalItemsCount) {
//                populateTimeline(aTweets.getItem(aTweets.getCount() - 1).getUid() - 1);
//                return true;
//            }
//        });
//        setListViewListener();

//        client = TwitterApplication.getRestClient();
//        populateTimeline(-1);
//
//        if(savedInstanceState == null) {
//            // Access the fragment
//            fragmentTweetsList = (TweetListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);
//        }
    }

//    private void setListViewListener() {
//        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = new Intent(TimelineActivity.this, TweetDetailActivity.class);
//                i.putExtra("tweet", Parcels.wrap(aTweets.getItem(position)));
//                i.putExtra("user", Parcels.wrap(aTweets.getItem(position).getUser()));
//                startActivity(i);
//            }
//        });
//    }

//    public void fetchTimelineAsync(int page) {
//        // Send the network request to fetch the updated data
//        // `client` here is an instance of Android Async HTTP
//        // getHomeTimeline is an example endpoint.
//        client.getHomeTimeLine(-1, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                aTweets.clear();
//                aTweets.addAll(Tweet.fromJsonArray(response));
//                aTweets.notifyDataSetChanged();
////                swipeContainer.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("DEBUG", "Fetch timeline error: " + errorResponse);
//            }
//        });
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                showComposeDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onProfileView(MenuItem mi) {

        // Launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", Parcels.wrap(user));
        startActivity(i);
    }


    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialogFragment composeDialogFragment = ComposeDialogFragment.newInstance("Compose");
        composeDialogFragment.show(fm, "fragment_edit_name");
    }

    // Return the order of the fragements in the view pager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {
        private String tabTitles[] = {"Home", "Mentions"};

        // Adapter gets the manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        // The order and creation of fragments within the pager
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        // Return the tab title
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        // How many fragments there are to swipe between?
        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }


    @Override
    public void onFinishComposeDialog(String status) {
//        updateStatus(status);
        TweetsPagerAdapter tweetsPagerAdapter = (TweetsPagerAdapter) vpPager.getAdapter();
        HomeTimelineFragment homeFragment = (HomeTimelineFragment) tweetsPagerAdapter.getRegisteredFragment(0);
        homeFragment.updateStatus(status);
    }

//    private void populateTimeline(long maxId) {
//        client.getHomeTimeLine(maxId, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                fragmentTweetsList.addAll(Tweet.fromJsonArray(response));
// //               aTweets.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("DEBUG", errorResponse.toString());
//            }
//        });
//    }

//    private void updateStatus(String status) {
// //       client.updateStatus(status, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
// //               aTweets.insert(Tweet.fromJson(response), 0);
// //               aTweets.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("DEBUG", errorResponse.toString());
//            }
//        });
//    }
}
