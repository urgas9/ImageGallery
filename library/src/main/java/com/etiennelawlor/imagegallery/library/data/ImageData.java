package com.etiennelawlor.imagegallery.library.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by urgas9 on 19. 01. 2016.
 */
public class ImageData implements Parcelable{

    private String id;
    private String imageUrl;
    private boolean liked;

    private String comment;
    private String userAvatarUrl;
    private String userName;
    private String timePosted;

    public ImageData(){
        super();
    }
    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTimePosted() {
        return timePosted;
    }

    public void setTimePosted(String timePosted) {
        this.timePosted = timePosted;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ImageData(Parcel p){
        super();
        this.id = p.readString();
        this.imageUrl = p.readString();
        this.liked = p.readByte() != 0;
        this.comment = p.readString();
        this.userAvatarUrl = p.readString();
        this.userName = p.readString();
        this.timePosted = p.readString();

    }

    @Override
    public String toString() {
        return "ImageData{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", liked=" + liked +
                ", comment='" + comment + '\'' +
                ", userAvatarUrl='" + userAvatarUrl + '\'' +
                ", userName='" + userName + '\'' +
                ", timePosted='" + timePosted + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeString(comment);
        dest.writeString(userAvatarUrl);
        dest.writeString(userName);
        dest.writeString(timePosted);
    }
}
