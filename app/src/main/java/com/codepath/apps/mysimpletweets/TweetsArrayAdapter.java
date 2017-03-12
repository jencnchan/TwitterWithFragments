package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.mysimpletweets.models.ProfileActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        com.codepath.apps.mysimpletweets.LinkifiedText tvBody = (com.codepath.apps.mysimpletweets.LinkifiedText) convertView.findViewById(R.id.tvBody);

        tvUserName.setText(tweet.getUser().getName());
        tvBody.setText(tweet.getBody());
        ivProfileImage.setImageResource(android.R.color.transparent);
        //Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);
        tvName.setText("@" + tweet.getUser().getScreenName());
        tvRelativeTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tweet.user
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", Parcels.wrap(tweet.getUser()));

                v.getContext().startActivity(i);

            }
        });

        return convertView;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

}
