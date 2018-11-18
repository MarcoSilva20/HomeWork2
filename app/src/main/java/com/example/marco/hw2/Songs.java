package com.example.marco.hw2;

import android.os.Parcel;
import android.os.Parcelable;

public class Songs implements Parcelable {
    public String title;
    public String photo;
    public String artist;
    public String release_data;

    public Songs(){

    }

    public Songs(String title,String artist,String release_data){
        this.title = title;
        this.artist = artist;
        this.release_data = release_data;
    }

    public Songs(String title,String artist,String release_data,String photo){
        this.title = title;
        this.artist = artist;
        this.release_data = release_data;
        this.photo = photo;
    }

    protected Songs(Parcel in) {
        title = in.readString();
        photo = in.readString();
        artist = in.readString();
        release_data = in.readString();
    }

    void addPicPath(String pPath){
        photo = pPath;
    }



    public static final Creator<Songs> CREATOR = new Creator<Songs>() {
        @Override
        public Songs createFromParcel(Parcel in) {
            return new Songs(in);
        }

        @Override
        public Songs[] newArray(int size) {
            return new Songs[size];
        }
    };

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(photo);
        dest.writeString(artist);
        dest.writeString(release_data);
    }
}
