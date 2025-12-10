package com.studentexchange.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String user_id;
    private static int counter = 0;
    private String name;
    private String cnic;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Date registration_date;
    private int credit_points;
    private boolean is_verified;
    private float average_rating;
    private List<Transaction> transactions_as_buyer;
    private List<Transaction> transactions_as_seller;

    public User(String name, String cnic, String email, String password, String phone, String address) {
        try {
            // Validate constructor parameters
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (cnic == null || cnic.trim().isEmpty()) {
                throw new IllegalArgumentException("CNIC cannot be null or empty");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone cannot be null or empty");
            }
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("Address cannot be null or empty");
            }

            counter++;
            this.user_id = "USER_" + String.format("%03d", counter);
            this.name = name.trim();
            this.cnic = cnic.trim();
            this.email = email.trim();
            this.password = password.trim();
            setPhone(phone);
            this.address = address.trim();
            this.registration_date = new Date();
            this.credit_points = 0;
            this.is_verified = false;
            this.average_rating = 0.0f;
            this.transactions_as_buyer = new ArrayList<>();
            this.transactions_as_seller = new ArrayList<>();
        } catch (IllegalArgumentException e) {
            // Re-throw with additional context
            throw new IllegalArgumentException("Failed to create User: " + e.getMessage(), e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            this.name = name.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set name: " + e.getMessage(), e);
        }
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        try {
            if (cnic == null || cnic.trim().isEmpty()) {
                throw new IllegalArgumentException("CNIC cannot be null or empty");
            }
            this.cnic = cnic.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set CNIC: " + e.getMessage(), e);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format. Must contain '@'");
            }
            this.email = email.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set email: " + e.getMessage(), e);
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            if (password.length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters long");
            }
            this.password = password.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set password: " + e.getMessage(), e);
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone cannot be null or empty");
            }

            String trimmedPhone = phone.trim();

            // Remove common separators
            String cleanPhone = trimmedPhone.replaceAll("[\\s\\-()]", "");

            // Check length
            if (cleanPhone.length() < 10 || cleanPhone.length() > 12) {
                throw new IllegalArgumentException("Phone number must be 10 to 12 digits (e.g., 03001234567 or 923001234567)");
            }

            // Check if it's all digits
            if (!cleanPhone.matches("\\d+")) {
                throw new IllegalArgumentException("Phone number can only contain digits (0-9)");
            }

            this.phone = cleanPhone;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set phone: " + e.getMessage());
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        try {
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("Address cannot be null or empty");
            }
            this.address = address.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set address: " + e.getMessage(), e);
        }
    }

    public int getCredit_points() {
        return credit_points;
    }

    public void setCredit_points(int credit_points) {
        try {
            if (credit_points < 0) {
                throw new IllegalArgumentException("Credit points cannot be negative");
            }
            this.credit_points = credit_points;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set credit points: " + e.getMessage(), e);
        }
    }

    public boolean isIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        try {
            if (average_rating < 0.0f || average_rating > 5.0f) {
                throw new IllegalArgumentException("Average rating must be between 0.0 and 5.0");
            }
            this.average_rating = average_rating;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set average rating: " + e.getMessage(), e);
        }
    }

    public String getUser_id() {
        return user_id;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public List<Transaction> getTransactions_as_buyer() {

        return new ArrayList<>(transactions_as_buyer);
    }

    public List<Transaction> getTransactions_as_seller() {

        return new ArrayList<>(transactions_as_seller);
    }

    public void setTransactions_as_buyer(List<Transaction> transactions_as_buyer) {
        try {
            if (transactions_as_buyer == null) {
                throw new IllegalArgumentException("Buyer transactions list cannot be null");
            }
            this.transactions_as_buyer = new ArrayList<>(transactions_as_buyer);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set buyer transactions: " + e.getMessage(), e);
        }
    }

    public void setTransactions_as_seller(List<Transaction> transactions_as_seller) {
        try {
            if (transactions_as_seller == null) {
                throw new IllegalArgumentException("Seller transactions list cannot be null");
            }
            this.transactions_as_seller = new ArrayList<>(transactions_as_seller);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set seller transactions: " + e.getMessage(), e);
        }
    }

    public void addCreditPoints(int points) {
        try {
            if (points <= 0) {
                throw new IllegalArgumentException("Credit points to add must be positive");
            }
            this.credit_points += points;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add credit points: " + e.getMessage(), e);
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Credit points overflow occurred when adding " + points + " points");
        }
    }

    public boolean useCreditPoints(int points) {
        try {
            if (points <= 0) {
                throw new IllegalArgumentException("Credit points to use must be positive");
            }
            if (credit_points >= points) {
                this.credit_points -= points;
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to use credit points: " + e.getMessage(), e);
        }
    }

    public void addTransactionAsBuyer(Transaction transaction) {
        try {
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction cannot be null");
            }
            this.transactions_as_buyer.add(transaction);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add buyer transaction: " + e.getMessage(), e);
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException("Cannot add transaction to buyer list (list may be immutable)");
        }
    }

    public void addTransactionAsSeller(Transaction transaction) {
        try {
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction cannot be null");
            }
            this.transactions_as_seller.add(transaction);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add seller transaction: " + e.getMessage(), e);
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException("Cannot add transaction to seller list (list may be immutable)");
        }
    }

    public float calculateAverageRating() {
        try {
            float totalreview = 0;
            int reviewcount = 0;
            for (int i = 0; i < transactions_as_seller.size(); i++) {
                Transaction transaction = transactions_as_seller.get(i);
                if (transaction != null && transaction.getSeller_review() != null) {
                    totalreview += transaction.getSeller_review().getRating();
                    reviewcount++;
                }
            }
            if (reviewcount > 0) {
                average_rating = totalreview / reviewcount;
                // Validate the calculated rating
                if (average_rating < 0.0f || average_rating > 5.0f) {
                    throw new IllegalStateException("Calculated average rating " + average_rating + " is outside valid range 0.0-5.0");
                }
                return average_rating;
            }
            return 0.0f;
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Error calculating average rating: division by zero or overflow");
        } catch (NullPointerException e) {
            throw new NullPointerException("Null reference encountered while calculating average rating");
        }
    }

    public int getTotalTransactions() {
        try {
            return transactions_as_seller.size() + transactions_as_buyer.size();
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Integer overflow when calculating total transactions");
        }
    }

    public float getTotalSpent() {
        try {
            float total = 0;
            for (int i = 0; i < transactions_as_buyer.size(); i++) {
                Transaction transaction = transactions_as_buyer.get(i);
                if (transaction != null && transaction.getItem() != null) {
                    total += transaction.getItem().getPrice();
                }
            }
            if (Float.isInfinite(total) || Float.isNaN(total)) {
                throw new ArithmeticException("Total spent calculation resulted in invalid floating point value");
            }
            return total;
        } catch (NullPointerException e) {
            throw new NullPointerException("Null transaction or item encountered while calculating total spent");
        }
    }

    public float getTotalEarned() {
        try {
            float total = 0;
            for (int i = 0; i < transactions_as_seller.size(); i++) {
                Transaction transaction = transactions_as_seller.get(i);
                if (transaction != null && transaction.getItem() != null) {
                    total += transaction.getItem().getPrice();
                }
            }
            if (Float.isInfinite(total) || Float.isNaN(total)) {
                throw new ArithmeticException("Total earned calculation resulted in invalid floating point value");
            }
            return total;
        } catch (NullPointerException e) {
            throw new NullPointerException("Null transaction or item encountered while calculating total earned");
        }
    }

    public boolean hasTransactionHistory() {
        try {
            return !(transactions_as_seller.isEmpty() && transactions_as_buyer.isEmpty());
        } catch (NullPointerException e) {
            throw new IllegalStateException("Transaction lists are not properly initialized");
        }
    }


    public void updateProfile(String name, String phone, String address) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone cannot be null or empty");
            }
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("Address cannot be null or empty");
            }

            this.name = name.trim();
            setPhone(phone);
            this.address = address.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to update profile: " + e.getMessage(), e);
        }
    }

    public void verifyCNIC() {
        this.is_verified = true;
    }

    public float getBuyerRating() {
        try {
            float totalreview = 0;
            int reviewcount = 0;
            float average = 0.0f;
            for (int i = 0; i < transactions_as_buyer.size(); i++) {
                Transaction transaction = transactions_as_buyer.get(i);
                if (transaction != null && transaction.getBuyer_review() != null) {
                    totalreview += transaction.getBuyer_review().getRating();
                    reviewcount++;
                }
            }
            if (reviewcount > 0) {
                average = totalreview / reviewcount;
                // Validate the calculated rating
                if (average < 0.0f || average > 5.0f) {
                    throw new IllegalStateException("Calculated buyer rating " + average + " is outside valid range 0.0-5.0");
                }
                return average;
            }
            return 0.0f;
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Error calculating buyer rating: division by zero or overflow");
        } catch (NullPointerException e) {
            throw new NullPointerException("Null reference encountered while calculating buyer rating");
        }
    }

    public float getSellerRating() {
        try {
            float totalreview = 0;
            int reviewcount = 0;
            float average = 0.0f;
            for (int i = 0; i < transactions_as_seller.size(); i++) {
                Transaction transaction = transactions_as_seller.get(i);
                if (transaction != null && transaction.getSeller_review() != null) {
                    totalreview += transaction.getSeller_review().getRating();
                    reviewcount++;
                }
            }
            if (reviewcount > 0) {
                average = totalreview / reviewcount;
                // Validate the calculated rating
                if (average < 0.0f || average > 5.0f) {
                    throw new IllegalStateException("Calculated seller rating " + average + " is outside valid range 0.0-5.0");
                }
                return average;
            }
            return 0.0f;
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Error calculating seller rating: division by zero or overflow");
        } catch (NullPointerException e) {
            throw new NullPointerException("Null reference encountered while calculating seller rating");
        }
    }

    @Override
    public String toString() {
        try {
            return "ID: " + getUser_id() + " Name: " + getName() + " Email: " + getEmail() + " Phone: " + getPhone() + " Credit points: " + getCredit_points() + " rating: " + getAverage_rating() + " Verified: " + isIs_verified();
        } catch (Exception e) {
            return "Error generating string representation: " + e.getMessage();
        }
    }
}