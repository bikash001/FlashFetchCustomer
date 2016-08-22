package com.buyer.flashfetch.Objects;

/**
 * Created by KRANTHI on 21-08-2016.
 */
public class Notification {

    private String heading;
    private String description;
    private String imageURL;
    private long timeInMillis;

    public Notification(String heading, String description, String imageURL, long timeInMillis){

        this.heading = heading;
        this.description = description;
        this.imageURL = imageURL;
        this.timeInMillis = timeInMillis;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }
}
