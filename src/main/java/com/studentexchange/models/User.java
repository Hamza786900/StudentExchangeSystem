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
    private float average_rating;
    private List<Transaction> transactionsAsBuyer;
    private List<Transaction> transactionsAsSeller;

    public User(String name, String cnic, String email, String password, String phone, String address) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (cnic == null || cnic.trim().isEmpty()) {
            throw new IllegalArgumentException("CNIC cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
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
        this.email = email.trim().toLowerCase();
        this.password = password.trim();
        this.phone = phone.trim();
        this.address = address.trim();
        this.registration_date = new Date();
        this.credit_points = 0;
        this.average_rating = 0.0f;
        this.transactionsAsBuyer = new ArrayList<>();
        this.transactionsAsSeller = new ArrayList<>();
    }

    // Getters
    public String getUser_id() { return user_id; }
    public String getName() { return name; }
    public String getCnic() { return cnic; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public Date getRegistration_date() { return new Date(registration_date.getTime()); }
    public int getCredit_points() { return credit_points; }
    public float getAverage_rating() { return average_rating; }
    public List<Transaction> getTransactionsAsBuyer() { return new ArrayList<>(transactionsAsBuyer); }
    public List<Transaction> getTransactionsAsSeller() { return new ArrayList<>(transactionsAsSeller); }

    // Setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim();
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        this.email = email.trim().toLowerCase();
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be null or empty");
        }
        this.phone = phone.trim();
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        this.address = address.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password.trim();
    }

    // Credit points management
    public void addCreditPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        this.credit_points += points;
    }

    public void useCreditPoints(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Points cannot be negative");
        }
        if (points > credit_points) {
            throw new IllegalArgumentException("Insufficient credit points");
        }
        this.credit_points -= points;
    }

    // Transaction management
    public void addTransactionAsBuyer(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        transactionsAsBuyer.add(transaction);
    }

    public void addTransactionAsSeller(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        transactionsAsSeller.add(transaction);
    }

    // Rating calculation
    public void calculateAverageRating() {
        if (transactionsAsSeller.isEmpty()) {
            this.average_rating = 0.0f;
            return;
        }

        int totalRatings = 0;
        int ratingCount = 0;

        for (Transaction t : transactionsAsSeller) {
            Review review = t.getBuyer_review();
            if (review != null && review.getReviewed_user().equals(this)) {
                totalRatings += review.getRating();
                ratingCount++;
            }
        }

        if (ratingCount > 0) {
            this.average_rating = (float) totalRatings / ratingCount;
        } else {
            this.average_rating = 0.0f;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return user_id.equals(user.user_id);
    }

    @Override
    public int hashCode() {
        return user_id.hashCode();
    }
}