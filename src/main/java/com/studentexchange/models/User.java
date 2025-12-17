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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone cannot be null or empty");
            }
            String trimmedPhone = phone.trim();
            String cleanPhone = trimmedPhone.replaceAll("[\\s\\-()]", "");
            if (cleanPhone.length() < 10 || cleanPhone.length() > 12) {
                throw new IllegalArgumentException("Phone number must be 10 to 12 digits (e.g., 03001234567 or 923001234567)");
            }
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

    public boolean isIs_verified() {
        return is_verified;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public String getUser_id() {
        return user_id;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public List<Transaction> getTransactionsAsBuyer() {
        return new ArrayList<>(transactions_as_buyer);
    }

    public List<Transaction> getTransactionsAsSeller() {
        return new ArrayList<>(transactions_as_seller);
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

    @Override
    public String toString() {
        try {
            return "ID: " + getUser_id() + " Name: " + getName() + " Email: " + getEmail() + " Phone: " + getPhone() + " Credit points: " + getCredit_points() + " rating: " + getAverage_rating() + " Verified: " + isIs_verified();
        } catch (Exception e) {
            return "Error generating string representation: " + e.getMessage();
        }
    }
}