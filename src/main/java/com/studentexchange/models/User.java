package com.studentexchange.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String user_id;
    private static int counter=0;
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
            counter++;
            this.user_id = "USER_"+String.format("%03d",counter);
            this.name = name;
            this.cnic = cnic;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.address = address;
            this.registration_date = new Date();
            this.credit_points = 0;
            this.is_verified = false;
            this.average_rating = 0.0f;
            this.transactions_as_buyer = new ArrayList<>();
            this.transactions_as_seller = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        try {
            this.name = name;
        } catch (Exception e) {
            System.out.println("Error setting name: " + e.getMessage());
        }
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        try {
            this.cnic = cnic;
        } catch (Exception e) {
            System.out.println("Error setting CNIC: " + e.getMessage());
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        try {
            this.email = email;
        } catch (Exception e) {
            System.out.println("Error setting email: " + e.getMessage());
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            this.password = password;
        } catch (Exception e) {
            System.out.println("Error setting password: " + e.getMessage());
        }
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        try {
            this.phone = phone;
        } catch (Exception e) {
            System.out.println("Error setting phone: " + e.getMessage());
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        try {
            this.address = address;
        } catch (Exception e) {
            System.out.println("Error setting address: " + e.getMessage());
        }
    }

    public int getCredit_points() {
        return credit_points;
    }

    public void setCredit_points(int credit_points) {
        try {
            this.credit_points = credit_points;
        } catch (Exception e) {
            System.out.println("Error setting credit points: " + e.getMessage());
        }
    }

    public boolean isIs_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        try {
            this.is_verified = is_verified;
        } catch (Exception e) {
            System.out.println("Error setting verification: " + e.getMessage());
        }
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(float average_rating) {
        try {
            this.average_rating = average_rating;
        } catch (Exception e) {
            System.out.println("Error setting average rating: " + e.getMessage());
        }
    }

    public String getUser_id() {
        return user_id;
    }

    public Date getRegistration_date() {
        return registration_date;
    }

    public List<Transaction> getTransactions_as_buyer() {
        return transactions_as_buyer;
    }

    public List<Transaction> getTransactions_as_seller() {
        return transactions_as_seller;
    }

    public void setTransactions_as_buyer(List<Transaction> transactions_as_buyer) {
        try {
            this.transactions_as_buyer = transactions_as_buyer;
        } catch (Exception e) {
            System.out.println("Error setting buyer transactions: " + e.getMessage());
        }
    }

    public void setTransactions_as_seller(List<Transaction> transactions_as_seller) {
        try {
            this.transactions_as_seller = transactions_as_seller;
        } catch (Exception e) {
            System.out.println("Error setting seller transactions: " + e.getMessage());
        }
    }

    public void addCreditPoints(int points) {
        try {
            this.credit_points += points;
        } catch (Exception e) {
            System.out.println("Error adding credit points: " + e.getMessage());
        }
    }

    public boolean useCreditPoints(int points) {
        try {
            if (points>0 && credit_points>=points) {
                this.credit_points -= points;
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error using credit points: " + e.getMessage());
        }
        return false;
    }

    public void addTransactionAsBuyer(Transaction transaction) {
        try {
            this.transactions_as_buyer.add(transaction);
        } catch (Exception e) {
            System.out.println("Error adding buyer transaction: " + e.getMessage());
        }
    }

    public void addTransactionAsSeller(Transaction transaction) {
        try {
            this.transactions_as_seller.add(transaction);
        } catch (Exception e) {
            System.out.println("Error adding seller transaction: " + e.getMessage());
        }
    }

    public float calculateAverageRating() {
        try {
            float totalreview = 0;
            int reviewcount = 0;
            for (int i = 0; i < transactions_as_seller.size(); i++) {
                if (transactions_as_seller.get(i) != null &&
                        transactions_as_seller.get(i).getSeller_review() != null) {
                    totalreview += transactions_as_seller.get(i).getSeller_review().getRating();
                    reviewcount++;
                }
            }
            if(reviewcount>0) {
                average_rating = totalreview / reviewcount;
                return average_rating;
            }
        } catch (Exception e) {
            System.out.println("Error calculating average rating: " + e.getMessage());
        }
        return 0.0f;
    }

    public int getTotalTransactions() {
        try {
            return transactions_as_seller.size()+transactions_as_buyer.size();
        } catch (Exception e) {
            System.out.println("Error getting total transactions: " + e.getMessage());
            return 0;
        }
    }

    public float getTotalSpent(){
        float total=0;
        try {
            for(int i=0;i<transactions_as_buyer.size();i++){
                if(transactions_as_buyer.get(i)!=null &&
                        transactions_as_buyer.get(i).getItem()!=null) {
                    total+=transactions_as_buyer.get(i).getItem().getPrice();
                }
            }
        } catch (Exception e) {
            System.out.println("Error calculating total spent: " + e.getMessage());
        }
        return total;
    }

    public float getTotalEarned(){
        float total=0;
        try {
            for(int i=0;i<transactions_as_seller.size();i++){
                if(transactions_as_seller.get(i)!=null &&
                        transactions_as_seller.get(i).getItem()!=null) {
                    total+=transactions_as_seller.get(i).getItem().getPrice();
                }
            }
        } catch (Exception e) {
            System.out.println("Error calculating total earned: " + e.getMessage());
        }
        return total;
    }

    public boolean hasTransactionHistory(){
        try {
            if(transactions_as_seller.isEmpty() && transactions_as_buyer.isEmpty()){
                return false;
            }
            else{
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error checking transaction history: " + e.getMessage());
            return false;
        }
    }

    public List<Transaction> getRecentTransactions(int count){
        List<Transaction> recent = new ArrayList<>();
        try {
            List<Transaction> allTransactions = new ArrayList<>();
            allTransactions.addAll(transactions_as_buyer);
            allTransactions.addAll(transactions_as_seller);
            int limit = Math.min(count, allTransactions.size());
            for(int i=0; i<limit; i++){
                recent.add(allTransactions.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error getting recent transactions: " + e.getMessage());
        }
        return recent;
    }

    public void updateProfile(String name,String phone,String address){
        try {
            this.name = name;
            this.phone = phone;
            this.address = address;
        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
    }

    public void verifyCNIC(){
        try {
            this.is_verified = true;
        } catch (Exception e) {
            System.out.println("Error verifying CNIC: " + e.getMessage());
        }
    }

    public float getBuyerRating(){
        float totalreview = 0;
        int reviewcount = 0;
        float average=0.0f;
        try {
            for (int i = 0; i < transactions_as_buyer.size(); i++) {
                if (transactions_as_buyer.get(i)!=null &&
                        transactions_as_buyer.get(i).getBuyer_review() != null) {
                    totalreview += transactions_as_buyer.get(i).getBuyer_review().getRating();
                    reviewcount++;
                }
            }
            if(reviewcount>0) {
                average = totalreview / reviewcount;
                return average;
            }
        } catch (Exception e) {
            System.out.println("Error calculating buyer rating: " + e.getMessage());
        }
        return 0.0f;
    }

    public float getSellerRating(){
        float totalreview = 0;
        int reviewcount = 0;
        float average=0.0f;
        try {
            for (int i = 0; i < transactions_as_seller.size(); i++) {
                if (transactions_as_seller.get(i)!=null &&
                        transactions_as_seller.get(i).getSeller_review() != null) {
                    totalreview += transactions_as_seller.get(i).getSeller_review().getRating();
                    reviewcount++;
                }
            }
            if(reviewcount>0) {
                average = totalreview / reviewcount;
                return average;
            }
        } catch (Exception e) {
            System.out.println("Error calculating seller rating: " + e.getMessage());
        }
        return 0.0f;
    }

    @Override
    public String toString() {
        try {
            return "ID: "+getUser_id()+" Name: "+getName()+" Email: "+getEmail()+" Phone: "+getPhone()+" Credit points: "+getCredit_points()+" rating: "+getAverage_rating()+" Verified: "+isIs_verified();
        } catch (Exception e) {
            return "Error displaying user";
        }
    }
}
