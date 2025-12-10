package com.studentexchange.services;

import com.studentexchange.models.Item;
import com.studentexchange.models.User;
import com.studentexchange.models.FreeResource;
import com.studentexchange.models.ForSaleItem;
import com.studentexchange.enums.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreditSystem {
    private Map<String, Integer> credit_rules;
    private Map<Integer, Double> redemption_rules;
    private static final int UPLOAD_CREDITS = 10;
    private static final int CREDITS_PER_RUPEE = 10;

    public CreditSystem() {
        try {
            credit_rules = new HashMap<>();
            redemption_rules = new HashMap<>();
            initializeRules();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create CreditSystem: " + e.getMessage());
        }
    }

    private void initializeRules() {
        try {
            credit_rules.put("FREE_RESOURCE_UPLOAD", UPLOAD_CREDITS);
            credit_rules.put("SALE_COMPLETION", 20);
            credit_rules.put("REVIEW_COMPLETION", 10);
            credit_rules.put("VERIFIED_USER_BONUS", 15);
            credit_rules.put("REFERRAL_BONUS", 25);
            credit_rules.put("FIRST_PURCHASE_BONUS", 30);

            redemption_rules.put(10, 10.0);    // 10 points = Rs. 10 discount
            redemption_rules.put(20, 20.0);    // 20 points = Rs. 20 discount
            redemption_rules.put(50, 50.0);    // 50 points = Rs. 50 discount
            redemption_rules.put(100, 100.0);  // 100 points = Rs. 100 discount
            redemption_rules.put(200, 250.0);  // 200 points = Rs. 250 discount (bonus)

        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize credit rules: " + e.getMessage());
        }
    }

    public Map<String, Integer> getCredit_rules() {
        return new HashMap<>(credit_rules);
    }

    public Map<Integer, Double> getRedemption_rules() {
        return new HashMap<>(redemption_rules);
    }

    public void add_credit_rule(String credit_rule, int credit_amount) {
        try {
            if (credit_rule == null || credit_rule.trim().isEmpty()) {
                throw new IllegalArgumentException("Credit rule name cannot be null or empty");
            }
            if (credit_amount < 0) {
                throw new IllegalArgumentException("Credit amount cannot be negative");
            }
            if (credit_amount > 1000) {
                throw new IllegalArgumentException("Credit amount cannot exceed 1000 points per rule");
            }

            String trimmedRule = credit_rule.trim();
            if (credit_rules.containsKey(trimmedRule)) {
                throw new IllegalArgumentException("Credit rule '" + trimmedRule + "' already exists");
            }

            credit_rules.put(trimmedRule, credit_amount);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add credit rule: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error adding credit rule: " + e.getMessage());
        }
    }

    public void update_credit_rule(String credit_rule, int credit_amount) {
        try {
            if (credit_rule == null || credit_rule.trim().isEmpty()) {
                throw new IllegalArgumentException("Credit rule name cannot be null or empty");
            }
            if (credit_amount < 0) {
                throw new IllegalArgumentException("Credit amount cannot be negative");
            }
            if (credit_amount > 1000) {
                throw new IllegalArgumentException("Credit amount cannot exceed 1000 points per rule");
            }

            String trimmedRule = credit_rule.trim();
            if (!credit_rules.containsKey(trimmedRule)) {
                throw new IllegalArgumentException("Credit rule '" + trimmedRule + "' does not exist");
            }

            credit_rules.put(trimmedRule, credit_amount);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to update credit rule: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error updating credit rule: " + e.getMessage());
        }
    }

    public void add_redemption_rule(int redemption_amount, double discount) {
        try {
            if (redemption_amount <= 0) {
                throw new IllegalArgumentException("Redemption amount must be positive");
            }
            if (discount <= 0) {
                throw new IllegalArgumentException("Discount amount must be positive");
            }
            if (redemption_amount > 10000) {
                throw new IllegalArgumentException("Redemption amount cannot exceed 10000 points");
            }
            if (discount > 10000) {
                throw new IllegalArgumentException("Discount amount cannot exceed Rs. 10000");
            }

            if (redemption_rules.containsKey(redemption_amount)) {
                throw new IllegalArgumentException("Redemption rule for " + redemption_amount + " points already exists");
            }

            double minDiscount = redemption_amount * 0.5;
            if (discount < minDiscount) {
                throw new IllegalArgumentException("Discount must be at least Rs. " + minDiscount +
                        " for " + redemption_amount + " points");
            }

            redemption_rules.put(redemption_amount, discount);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add redemption rule: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error adding redemption rule: " + e.getMessage());
        }
    }

    public void remove_credit_rule(String credit_rule) {
        try {
            if (credit_rule == null || credit_rule.trim().isEmpty()) {
                throw new IllegalArgumentException("Credit rule name cannot be null or empty");
            }

            String trimmedRule = credit_rule.trim();
            if (!credit_rules.containsKey(trimmedRule)) {
                throw new IllegalArgumentException("Credit rule '" + trimmedRule + "' does not exist");
            }

            if (trimmedRule.equals("FREE_RESOURCE_UPLOAD") ||
                    trimmedRule.equals("SALE_COMPLETION") ||
                    trimmedRule.equals("REVIEW_COMPLETION")) {
                throw new IllegalArgumentException("Cannot remove default credit rule: " + trimmedRule);
            }

            credit_rules.remove(trimmedRule);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to remove credit rule: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error removing credit rule: " + e.getMessage());
        }
    }

    public int calculateEarnedPoints(Item item) {
        try {
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }

            int basePoints = UPLOAD_CREDITS;

            if (item instanceof FreeResource) {
                FreeResource resource = (FreeResource) item;
                if (resource.isIs_official() && resource.hasSolutions()) {
                    basePoints += 5;
                }
            }

            if (item instanceof ForSaleItem) {
                ForSaleItem saleItem = (ForSaleItem) item;
                if (saleItem.getCondition() == Condition.NEW) {
                    basePoints += 5;
                }
            }

            return Math.min(basePoints, 100);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to calculate earned points: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error calculating earned points: " + e.getMessage());
        }
    }

    public boolean redeemPoints(User user, int points) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }
            if (points <= 0) {
                throw new IllegalArgumentException("Points must be positive");
            }
            if (points > 10000) {
                throw new IllegalArgumentException("Cannot redeem more than 10000 points at once");
            }

            int userCredits = user.getCredit_points();
            if (userCredits < points) {
                throw new IllegalStateException("User has only " + userCredits + " points, cannot redeem " + points + " points");
            }

            if (!redemption_rules.containsKey(points)) {
                throw new IllegalArgumentException("Redemption amount " + points + " points is not available");
            }

            boolean success = user.useCreditPoints(points);
            if (!success) {
                throw new IllegalStateException("Failed to redeem points from user account");
            }

            return true;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to redeem points: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to redeem points: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error redeeming points: " + e.getMessage());
        }
    }

    public double getEligibleDiscount(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            int points = user.getCredit_points();
            if (points < 0) {
                throw new IllegalStateException("User has negative credit points: " + points);
            }

            double bestDiscount = 0;
            for (Map.Entry<Integer, Double> rule : redemption_rules.entrySet()) {
                if (points >= rule.getKey()) {
                    bestDiscount = Math.max(bestDiscount, rule.getValue());
                }
            }

            double directDiscount = points * (CREDITS_PER_RUPEE / 10.0);

            return Math.max(bestDiscount, directDiscount);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get eligible discount: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to get eligible discount: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting eligible discount: " + e.getMessage());
        }
    }

    public Map<Integer, Double> getAvailableRedemptions(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            int userPoints = user.getCredit_points();
            Map<Integer, Double> available = new HashMap<>();

            for (Map.Entry<Integer, Double> rule : redemption_rules.entrySet()) {
                if (userPoints >= rule.getKey()) {
                    available.put(rule.getKey(), rule.getValue());
                }
            }

            return available;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get available redemptions: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting available redemptions: " + e.getMessage());
        }
    }

    public void addBonusPoints(User user, String reason) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }
            if (reason == null || reason.trim().isEmpty()) {
                throw new IllegalArgumentException("Bonus reason cannot be null or empty");
            }

            String trimmedReason = reason.trim();
            int bonus = credit_rules.getOrDefault(trimmedReason.toUpperCase(), 0);

            if (bonus <= 0) {
                bonus = credit_rules.getOrDefault("BONUS_POINTS", 5);
            }

            if (bonus > 1000) {
                throw new IllegalStateException("Bonus amount " + bonus + " exceeds maximum allowed (1000)");
            }

            user.addCreditPoints(bonus);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add bonus points: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to add bonus points: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error adding bonus points: " + e.getMessage());
        }
    }

    public List<String> getPointHistory(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            List<String> history = new ArrayList<>();

            int currentPoints = user.getCredit_points();
            int totalTransactions = user.getTotalTransactions();
            float totalSpent = user.getTotalSpent();
            float totalEarned = user.getTotalEarned();
            boolean isVerified = user.isIs_verified();

            history.add("Current Points: " + currentPoints);
            history.add("Account Status: " + (isVerified ? "Verified ✓" : "Unverified"));
            history.add("Total Transactions: " + totalTransactions);
            history.add("Total Spent: Rs. " + String.format("%.2f", totalSpent));
            history.add("Total Earned: Rs. " + String.format("%.2f", totalEarned));
            history.add("Available Discount: Rs. " + String.format("%.2f", getEligibleDiscount(user)));

            Map<Integer, Double> redemptions = getAvailableRedemptions(user);
            if (!redemptions.isEmpty()) {
                history.add("Available Redemptions:");
                for (Map.Entry<Integer, Double> redemption : redemptions.entrySet()) {
                    history.add("  " + redemption.getKey() + " points → Rs. " +
                            String.format("%.2f", redemption.getValue()) + " discount");
                }
            }

            return history;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get point history: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting point history: " + e.getMessage());
        }
    }

    public boolean validateRedemption(User user, int points) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }
            if (points <= 0) {
                throw new IllegalArgumentException("Points must be positive");
            }

            int userCredits = user.getCredit_points();
            if (userCredits < points) {
                return false;
            }

            if (!redemption_rules.containsKey(points)) {
                throw new IllegalArgumentException("Redemption amount " + points + " is not available");
            }

            return true;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to validate redemption: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error validating redemption: " + e.getMessage());
        }
    }

    public int getUploadCredits() {
        return UPLOAD_CREDITS;
    }

    public double calculateDiscountAmount(int points) {
        try {
            if (points < 0) {
                throw new IllegalArgumentException("Points cannot be negative");
            }
            if (points > 10000) {
                throw new IllegalArgumentException("Points cannot exceed 10000");
            }

            if (redemption_rules.containsKey(points)) {
                return redemption_rules.get(points);
            }

            double discount = points * (CREDITS_PER_RUPEE / 10.0);

            if (Double.isNaN(discount) || Double.isInfinite(discount)) {
                throw new ArithmeticException("Invalid discount calculation");
            }

            return discount;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to calculate discount amount: " + e.getMessage());
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Failed to calculate discount amount: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error calculating discount amount: " + e.getMessage());
        }
    }

    public int getUserCredits(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            int credits = user.getCredit_points();
            if (credits < 0) {
                throw new IllegalStateException("User has negative credit points: " + credits);
            }

            return credits;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get user credits: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to get user credits: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting user credits: " + e.getMessage());
        }
    }

    public void resetUserCredits(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            int currentCredits = user.getCredit_points();
            if (currentCredits > 10000) {
                throw new IllegalStateException("Cannot reset credits above 10000. Contact administrator.");
            }

            user.setCredit_points(0);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to reset user credits: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to reset user credits: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error resetting user credits: " + e.getMessage());
        }
    }

    public void awardPointsForTransaction(User user, double transactionAmount) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }
            if (transactionAmount < 0) {
                throw new IllegalArgumentException("Transaction amount cannot be negative");
            }

            int points = (int) (transactionAmount / 10);

            points = Math.min(points, 100);

            if (points > 0) {
                user.addCreditPoints(points);
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to award points for transaction: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error awarding points for transaction: " + e.getMessage());
        }
    }

    public Map<String, Object> getSystemCreditStats() {
        try {
            Map<String, Object> stats = new HashMap<>();

            int totalCreditRules = credit_rules.size();
            int totalRedemptionOptions = redemption_rules.size();

            int totalCreditValue = 0;
            for (int credit : credit_rules.values()) {
                totalCreditValue += credit;
            }
            double averageCreditAward = totalCreditRules > 0 ? (double) totalCreditValue / totalCreditRules : 0;

            double bestValueRatio = 0;
            int bestRedemptionPoints = 0;
            for (Map.Entry<Integer, Double> rule : redemption_rules.entrySet()) {
                double ratio = rule.getValue() / rule.getKey();
                if (ratio > bestValueRatio) {
                    bestValueRatio = ratio;
                    bestRedemptionPoints = rule.getKey();
                }
            }

            stats.put("Upload Credits", UPLOAD_CREDITS);
            stats.put("Credits Per Rupee", CREDITS_PER_RUPEE);
            stats.put("Total Credit Rules", totalCreditRules);
            stats.put("Total Redemption Options", totalRedemptionOptions);
            stats.put("Average Credit Award", String.format("%.1f points", averageCreditAward));
            stats.put("Best Redemption Value", bestRedemptionPoints + " points (Rs. " +
                    String.format("%.2f", redemption_rules.getOrDefault(bestRedemptionPoints, 0.0)) + ")");
            stats.put("Value Ratio", String.format("%.2f", bestValueRatio));

            return stats;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get system credit stats: " + e.getMessage());
        }
    }
}