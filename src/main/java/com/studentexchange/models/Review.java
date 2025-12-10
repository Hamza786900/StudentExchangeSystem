package com.studentexchange.models;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Review {
    private String review_id;
    private static int counter = 0;
    private int rating;
    private String comment;
    private Date review_date;
    private User reviewed_user;
    private User reviewer_user;
    private Transaction transaction;
    private boolean is_verified_purchase;

    public Review(int rating, String comment, User reviewed_user, User reviewer_user, Transaction transaction) {
        try {
            counter++;
            this.review_id = "REVIEW_" + String.format("%03d", counter);

            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5.");
            }
            this.rating = rating;

            this.comment = comment;
            this.review_date = new Date();

            if (reviewed_user == null) {
                throw new NullPointerException("Reviewed user cannot be null.");
            }
            this.reviewed_user = reviewed_user;

            if (reviewer_user == null) {
                throw new NullPointerException("Reviewer user cannot be null.");
            }
            this.reviewer_user = reviewer_user;

            if (transaction == null) {
                throw new NullPointerException("Transaction cannot be null.");
            }
            this.transaction = transaction;

            this.is_verified_purchase = true;

        } catch (Exception e) {
            System.out.println("Error creating Review object: " + e.getMessage());
        }
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        try {
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5.");
            }
            this.rating = rating;
        } catch (Exception e) {
            System.out.println("Invalid rating: " + e.getMessage());
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        try {
            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Comment cannot be empty.");
            }
            this.comment = comment;
        } catch (Exception e) {
            System.out.println("Invalid comment: " + e.getMessage());
        }
    }

    public boolean isIs_verified_purchase() {
        return is_verified_purchase;
    }

    public void setIs_verified_purchase(boolean is_verified_purchase) {
        this.is_verified_purchase = is_verified_purchase;
    }

    public String getReview_id() {
        return review_id;
    }

    public Date getReview_date() {
        return review_date;
    }

    public User getReviewed_user() {
        return reviewed_user;
    }

    public User getReviewer_user() {
        return reviewer_user;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public boolean validateRating(int rating) {
        try {
            if (rating <= 5 && rating >= 1) {
                this.rating = rating;
                return true;
            } else {
                throw new IllegalArgumentException("Rating must be between 1 and 5.");
            }
        } catch (Exception e) {
            System.out.println("Validation error: " + e.getMessage());
            return false;
        }
    }

    public String getReviewSummary() {
        try {
            return String.format(
                    "Rating:\t\t%d\nComment:\t%s\nDate:\t\t%s\nReviewer:\t%s\nVerified:\t%b",
                    rating,
                    comment != null ? comment : "No comment",
                    review_date,
                    reviewer_user != null ? reviewer_user.getName() : "Unknown",
                    reviewer_user != null && reviewer_user.isIs_verified()
            );
        } catch (Exception e) {
            return "Error generating summary: " + e.getMessage();
        }
    }

    public boolean isPositive() {
        return rating >= 4;
    }

    public boolean isNegative() {
        return rating <= 2;
    }

    public boolean isNeutral() {
        return rating == 3;
    }

    public long getAgeInDays() {
        try {
            LocalDate reviewLocalDate = review_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            return ChronoUnit.DAYS.between(reviewLocalDate, now);
        } catch (Exception e) {
            System.out.println("Error calculating age: " + e.getMessage());
            return -1;
        }
    }

    public boolean isVerifiedPurchase() {
        return is_verified_purchase;
    }

    public boolean hasComment() {
        try {
            return comment != null && !comment.isEmpty();
        } catch (Exception e) {
            System.out.println("Error checking comment: " + e.getMessage());
            return false;
        }
    }

    public String getRatingDescription() {
        try {
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < rating; i++) {
                stars.append("★");
            }
            for (int i = rating; i < 5; i++) {
                stars.append("☆");
            }
            return stars.toString();
        } catch (Exception e) {
            return "Rating unavailable: " + e.getMessage();
        }
    }

    public boolean isRecent() {
        try {
            return getAgeInDays() <= 30;
        } catch (Exception e) {
            System.out.println("Error checking recency: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String toString() {
        try {
            return "ID: " + getReview_id() +
                    " Rating: " + getRating() +
                    " Comment: " + getComment() +
                    " Date: " + getReview_date() +
                    " Reviewed_user: " + (reviewed_user != null ? reviewed_user.getName() : "Unknown") +
                    " Reviewer_user: " + (reviewer_user != null ? reviewer_user.getName() : "Unknown");
        } catch (Exception e) {
            return "Error converting to string: " + e.getMessage();
        }
    }
}
