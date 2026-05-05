package edu.cit.chan.restoradar.feature.review;

public class ReviewRequest {
    private int rating;
    private String comment;

    public ReviewRequest() {}

    public ReviewRequest(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public int getrating() {
        return rating;
    }
    public void setrating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
