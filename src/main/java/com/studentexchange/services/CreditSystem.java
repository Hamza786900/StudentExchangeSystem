package com.studentexchange.services;

import com.studentexchange.models.User;
import com.studentexchange.models.Transaction;

public class CreditSystem {
    private static final int UPLOAD_CREDITS = 10;

    public CreditSystem() { }

    public int getUploadCredits() {
        return UPLOAD_CREDITS;
    }

    public float getCreditValue(int credits) {
        return credits * 10.0f;
    }

    public void addReviewCompletionCredits(User user) {
        user.addCreditPoints(10);
    }

    public void awardPointsForTransaction(User user, float transactionAmount) {
        try {
            if (transactionAmount < 0) {
                throw new IllegalArgumentException("Transaction amount cannot be negative");
            }
            int points = (int) (transactionAmount / 10);
            points = Math.min(points, 100);
            if (points > 0) {
                user.addCreditPoints(points);
            }
        } catch (Exception e) {
        }
    }
}