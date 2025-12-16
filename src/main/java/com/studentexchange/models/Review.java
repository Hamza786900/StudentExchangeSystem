package com.studentexchange.models;

import java.util.Date;

public class Review {

    private int rating;
    private String comment;
    private Date review_date;

    public Review(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
        this.review_date = new Date();
    }

    public int getRating() { return rating; }
    public String getComment() { return comment; }
}
