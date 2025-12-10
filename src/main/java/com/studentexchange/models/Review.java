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

    public void setRating(int rating) {
        try {
            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }

            // Cannot change rating after 30 days
            if (getAgeInDays() > 30) {
                throw new IllegalStateException("Cannot change rating after 30 days");
            }

            this.rating = rating;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set rating: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set rating: " + e.getMessage());
        }
    }

    public String getComment() {
        if (comment == null || comment.isEmpty()) {
            throw new IllegalStateException("Comment is not set");
        }
        return comment;
    }

    public void setComment(String comment) {
        try {
            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Comment cannot be null or empty");
            }
            if (comment.trim().length() > 1000) {
                throw new IllegalArgumentException("Comment cannot exceed 1000 characters");
            }

            // Cannot change comment after 7 days
            if (getAgeInDays() > 7) {
                throw new IllegalStateException("Cannot change comment after 7 days");
            }

            this.comment = comment.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set comment: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set comment: " + e.getMessage());
        }
    }

    public boolean isIs_verified_purchase() {
        return is_verified_purchase;
    }

    public void setIs_verified_purchase(boolean is_verified_purchase) {
        try {
            // Cannot change verification status
            if (this.is_verified_purchase && !is_verified_purchase) {
                throw new IllegalStateException("Cannot remove verified purchase status");
            }
            this.is_verified_purchase = is_verified_purchase;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set verification status: " + e.getMessage());
        }
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
        return new Date(review_date.getTime()); // Return defensive copy
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

    public boolean validateRating(int rating) {
        try {
            if (rating >= 1 && rating <= 5) {
                setRating(rating); // Use the setter to maintain consistency
                return true;
            } else {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to validate rating: " + e.getMessage());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to validate rating: " + e.getMessage());
        }
    }

    public String getReviewSummary() {
        try {
            String reviewedUserName = getReviewed_user().getName();
            String reviewerUserName = getReviewer_user().getName();
            int currentRating = getRating();
            String currentComment = getComment();
            Date reviewDate = getReview_date();

            return String.format(
                    "Rating:\t\t%d/5\n" +
                            "Comment:\t%s\n" +
                            "Date:\t\t%s\n" +
                            "Reviewer:\t%s\n" +
                            "Reviewed:\t%s\n" +
                            "Verified:\t%s",
                    currentRating,
                    currentComment.length() > 100 ? currentComment.substring(0, 100) + "..." : currentComment,
                    reviewDate,
                    reviewerUserName != null ? reviewerUserName : "Unknown",
                    reviewedUserName != null ? reviewedUserName : "Unknown",
                    is_verified_purchase ? "Yes (Verified Purchase)" : "No"
            );
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate review summary: " + e.getMessage());
        }
    }

    public boolean isPositive() {
        try {
            int currentRating = getRating();
            return currentRating >= 4;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine if review is positive: " + e.getMessage());
        }
    }

    public boolean isNegative() {
        try {
            int currentRating = getRating();
            return currentRating <= 2;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine if review is negative: " + e.getMessage());
        }
    }

    public boolean isNeutral() {
        try {
            int currentRating = getRating();
            return currentRating == 3;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine if review is neutral: " + e.getMessage());
        }
    }

    public long getAgeInDays() {
        try {
            if (review_date == null) {
                throw new IllegalStateException("Review date is not set");
            }

            LocalDate reviewLocalDate = review_date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();

            if (reviewLocalDate.isAfter(now)) {
                throw new IllegalStateException("Review date cannot be in the future");
            }

            return ChronoUnit.DAYS.between(reviewLocalDate, now);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to calculate review age: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error calculating review age: " + e.getMessage());
        }
    }

    public boolean isVerifiedPurchase() {
        return is_verified_purchase;
    }

    public boolean hasComment() {
        try {
            return comment != null && !comment.isEmpty();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if review has comment: " + e.getMessage());
        }
    }

    public String getRatingDescription() {
        try {
            int currentRating = getRating();
            StringBuilder stars = new StringBuilder();

            for (int i = 0; i < currentRating; i++) {
                stars.append("★");
            }
            for (int i = currentRating; i < 5; i++) {
                stars.append("☆");
            }

            // Add textual description
            String description;
            if (currentRating >= 4) {
                description = " (Excellent)";
            } else if (currentRating == 3) {
                description = " (Average)";
            } else {
                description = " (Poor)";
            }

            return stars.toString() + description;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate rating description: " + e.getMessage());
        }
    }

    public boolean isRecent() {
        try {
            long ageInDays = getAgeInDays();
            return ageInDays <= 30;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if review is recent: " + e.getMessage());
        }
    }

    public boolean canBeEdited() {
        try {
            long ageInDays = getAgeInDays();
            return ageInDays <= 7; // Allow edits within 7 days
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if review can be edited: " + e.getMessage());
        }
    }

    public boolean canBeDeleted() {
        try {
            long ageInDays = getAgeInDays();
            return ageInDays <= 3; // Allow deletion within 3 days
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if review can be deleted: " + e.getMessage());
        }
    }

    public String getSentiment() {
        try {
            if (isPositive()) {
                return "Positive";
            } else if (isNegative()) {
                return "Negative";
            } else {
                return "Neutral";
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine review sentiment: " + e.getMessage());
        }
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