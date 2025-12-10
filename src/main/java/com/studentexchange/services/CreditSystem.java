package com.studentexchange.services;

import com.studentexchange.models.Item;
import com.studentexchange.models.User;

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
            e.printStackTrace();
        }
    }

    private void initializeRules() {
        try {
            credit_rules.put("FREE_RESOURCE_UPLOAD", UPLOAD_CREDITS);
            credit_rules.put("BONUS_POINTS", 5);

            redemption_rules.put(10, 10.0);
            redemption_rules.put(20, 20.0);
            redemption_rules.put(50, 50.0);
            redemption_rules.put(100, 100.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getCredit_rules() {
        return credit_rules;
    }

    public Map<Integer, Double> getRedemption_rules() {
        return redemption_rules;
    }

    public void add_credit_rule(String credit_rule, int credit_amount) {
        try {
            if (credit_rule == null || credit_rule.isEmpty())
                throw new IllegalArgumentException("Invalid credit rule name");
            if (credit_amount < 0)
                throw new IllegalArgumentException("Credit amount cannot be negative");

            credit_rules.put(credit_rule, credit_amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_redemption_rule(int redemption_amount, double discount) {
        try {
            if (redemption_amount <= 0 || discount <= 0)
                throw new IllegalArgumentException("Invalid redemption rule");

            redemption_rules.put(redemption_amount, discount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int calculateEarnedPoints(Item item) {
        try {
            if (item == null) throw new IllegalArgumentException("Item cannot be null");
            return UPLOAD_CREDITS;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean redeemPoints(User user, int points) {
        try {
            if (user == null || points <= 0) {
                throw new IllegalArgumentException("Invalid user or points");
            }
            if (user.getCredit_points() >= points) {
                return user.useCreditPoints(points);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getEligibleDiscount(User user) {
        try {
            if (user == null) throw new IllegalArgumentException("User cannot be null");
            int points = user.getCredit_points();
            return points * (CREDITS_PER_RUPEE / 10.0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public void addBonusPoints(User user, String reason) {
        try {
            if (user != null) {
                int bonus = credit_rules.getOrDefault("BONUS_POINTS", 5);
                user.addCreditPoints(bonus);
            } else {
                throw new IllegalArgumentException("User cannot be null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getPointHistory(User user) {
        List<String> history = new ArrayList<>();
        try {
            if (user != null) {
                history.add("Current Points: " + user.getCredit_points());
                history.add("Total Transactions: " + user.getTotalTransactions());
            } else {
                throw new IllegalArgumentException("User cannot be null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    public boolean validateRedemption(User user, int points) {
        try {
            if (user == null || points <= 0)
                throw new IllegalArgumentException("Invalid user or points");

            return user.getCredit_points() >= points;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUploadCredits() {
        return UPLOAD_CREDITS;
    }

    public double calculateDiscountAmount(int points) {
        try {
            if (points < 0) throw new IllegalArgumentException("Points cannot be negative");
            return points * CREDITS_PER_RUPEE;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public int getUserCredits(User user) {
        try {
            if (user == null) throw new IllegalArgumentException("User cannot be null");
            return user.getCredit_points();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void resetUserCredits(User user) {
        try {
            if (user != null) {
                user.setCredit_points(0);
            } else {
                throw new IllegalArgumentException("User cannot be null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getSystemCreditStats() {
        Map<String, Object> stats = new HashMap<>();
        try {
            stats.put("Upload Credits", UPLOAD_CREDITS);
            stats.put("Credits Per Rupee", CREDITS_PER_RUPEE);
            stats.put("Total Rules", credit_rules.size());
            stats.put("Redemption Options", redemption_rules.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
}
