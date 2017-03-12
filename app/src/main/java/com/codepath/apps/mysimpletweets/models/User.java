package com.codepath.apps.mysimpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Parcelable {
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;

    private String tagline;
    private int followersCount;
    private int followingsCount;

    public int getFollowersCount() {
        return followersCount;
    }

    public String getTagline() {
        return tagline;
    }

    public int getFriendCount() {
        return followingsCount;
    }


    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    // Empty constructor needed by the Parceler library
    public User() {
    }

    public User(Parcel in) {
        name = in.readString();
        uid = in.readLong();
        screenName = in.readString();
        profileImageUrl = in.readString();
        tagline = in.readString();
        followersCount = in.readInt();
        followingsCount = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static User fromJson(JSONObject json) {
        User u = new User();
        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
            u.tagline = json.getString("description");
            u.followersCount = json.getInt("followers_count");
            u.followingsCount = json.getInt("friends_counts");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeLong(uid);
        parcel.writeString(screenName);
        parcel.writeString(profileImageUrl);
        parcel.writeString(tagline);
        parcel.writeInt(followersCount);
        parcel.writeInt(followingsCount);
    }
}
