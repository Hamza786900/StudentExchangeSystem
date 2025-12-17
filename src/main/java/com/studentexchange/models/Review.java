package com.studentexchange.models;

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
            // Validate all parameters before creating object
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Comment cannot be null or empty");
            }
            if (comment.trim().length() > 1000) {
                throw new IllegalArgumentException("Comment cannot exceed 1000 characters");
            }
            if (reviewed_user == null) {
                throw new IllegalArgumentException("Reviewed user cannot be null");
            }
            if (reviewer_user == null) {
                throw new IllegalArgumentException("Reviewer user cannot be null");
            }
            // Prevent self-review
            if (reviewed_user.equals(reviewer_user)) {
                throw new IllegalArgumentException("User cannot review themselves");
            }
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction cannot be null");
            }
            // Check if reviewer is part of the transaction
            if (!transaction.getBuyer().equals(reviewer_user) && !transaction.getSeller().equals(reviewer_user)) {
                throw new IllegalArgumentException("Reviewer must be either buyer or seller in the transaction");
            }
            // Check if reviewed user is part of the transaction
            if (!transaction.getBuyer().equals(reviewed_user) && !transaction.getSeller().equals(reviewed_user)) {
                throw new IllegalArgumentException("Reviewed user must be either buyer or seller in the transaction");
            }
            counter++;
            if (counter < 0) {
                throw new IllegalStateException("Review counter overflow");
            }
            this.review_id = "REVIEW_" + String.format("%03d", counter);
            this.rating = rating;
            this.comment = comment.trim();
            this.review_date = new Date();
            this.reviewed_user = reviewed_user;
            this.reviewer_user = reviewer_user;
            this.transaction = transaction;
            this.is_verified_purchase = true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create Review: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to create Review: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error creating Review: " + e.getMessage());
        }
    }

    public int getRating() {
        if (rating < 1 || rating > 5) {
            throw new IllegalStateException("Rating is in invalid state");
        }
        return rating;
    }

    public String getComment() {
        if (comment == null || comment.isEmpty()) {
            throw new IllegalStateException("Comment is not set");
        }
        return comment;
    }

    public boolean isIs_verified_purchase() {
        return is_verified_purchase;
    }

    public String getReview_id() {
        if (review_id == null || review_id.isEmpty()) {
            throw new IllegalStateException("Review ID is not set");
        }
        return review_id;
    }

    public Date getReview_date() {
        if (review_date == null) {
            throw new IllegalStateException("Review date is not set");
        }
        return new Date(review_date.getTime());
    }

    public User getReviewed_user() {
        if (reviewed_user == null) {
            throw new IllegalStateException("Reviewed user is not set");
        }
        return reviewed_user;
    }

    public User getReviewer_user() {
        if (reviewer_user == null) {
            throw new IllegalStateException("Reviewer user is not set");
        }
        return reviewer_user;
    }

    public Transaction getTransaction() {
        if (transaction == null) {
            throw new IllegalStateException("Transaction is not set");
        }
        return transaction;
    }

    @Override
    public String toString() {
        try {
            String reviewId = getReview_id();
            int currentRating = getRating();
            String currentComment = getComment();
            Date reviewDate = getReview_date();
            String reviewedUserName = getReviewed_user().getName();
            String reviewerUserName = getReviewer_user().getName();
            return String.format(
                    "ID: %s | Rating: %d/5 | Comment: %s | Date: %s | Reviewed: %s | Reviewer: %s",
                    reviewId,
                    currentRating,
                    currentComment.length() > 50 ? currentComment.substring(0, 50) + "..." : currentComment,
                    reviewDate,
                    reviewedUserName != null ? reviewedUserName : "Unknown",
                    reviewerUserName != null ? reviewerUserName : "Unknown"
            );
        } catch (Exception e) {
            return "Review [Error in toString(): " + e.getMessage() + "]";
        }
    }
}