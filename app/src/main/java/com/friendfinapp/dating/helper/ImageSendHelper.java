package com.friendfinapp.dating.helper;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ImageSendHelper implements Parcelable {
    private String username;
    private String userimage;

    public ImageSendHelper(String username, String userimage) {
        this.username = username;
        this.userimage = userimage;
    }

    public ImageSendHelper() {
        super();
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public ImageSendHelper(Parcel in) {
        this();
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.username = in.readString();
        this.userimage = in.readString();

    }

    public final Parcelable.Creator<ImageSendHelper> CREATOR = new Parcelable.Creator<ImageSendHelper>() {
        public ImageSendHelper createFromParcel(Parcel in) {
            return new ImageSendHelper(in);
        }

        public ImageSendHelper[] newArray(int size) {
            return new ImageSendHelper[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(userimage);
    }
}
