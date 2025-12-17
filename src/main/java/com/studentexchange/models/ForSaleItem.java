package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;
import java.util.Date;

public class ForSaleItem extends Item {
    private float price;
    private Condition condition;
    private float market_price;
    private boolean is_sold;
    private Date sale_date;
    private User buyer;
    private float discount_percentage;

    public ForSaleItem(String title, User uploader, String description, Category category, GradeLevel grade, String subject, Condition condition, float market_price, float price) {
        super(title, uploader, description, category, grade, subject);
        try {
            if (condition == null) {
                throw new IllegalArgumentException("Condition cannot be null");
            }
            if (market_price < 0) {
                throw new IllegalArgumentException("Market price cannot be negative");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            if (price > market_price * 2) {
                throw new IllegalArgumentException("Price cannot be more than double the market price");
            }
            this.price = price;
            this.condition = condition;
            this.market_price = market_price;
            this.is_sold = false;
            this.sale_date = null;
            this.buyer = null;
            this.discount_percentage = market_price > 0 ? ((market_price - price) / market_price) * 100 : 0.0f;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create ForSaleItem: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to create ForSaleItem: " + e.getMessage());
        }
    }

    public float getPrice() {
        return price;
    }

    public Condition getCondition() {
        if (condition == null) {
            throw new IllegalStateException("Condition is not set for this item");
        }
        return condition;
    }

    public float getMarket_price() {
        return market_price;
    }

    public boolean isIs_sold() {
        return is_sold;
    }

    public void setIs_sold(boolean is_sold) {
        this.is_sold = is_sold;
    }

    @Override
    public String getDetails() {
        try {
            String itemId = getItem_id();
            String title = getTitle();
            float price = getPrice();
            Condition condition = getCondition();
            boolean isSold = isIs_sold();
            if (itemId == null) {
                throw new IllegalStateException("Item ID is null");
            }
            if (title == null) {
                throw new IllegalStateException("Title is null");
            }
            if (condition == null) {
                throw new IllegalStateException("Condition is null");
            }
            return "Item ID: " + itemId +
                    " Name: " + title +
                    " Price: " + price +
                    " Condition: " + condition +
                    " Sold: " + isSold;
        } catch (IllegalStateException e) {
            return "Error getting item details: " + e.getMessage();
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            return !isIs_sold();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine availability: " + e.getMessage());
        }
    }

    @Override
    public boolean matchesSearch(String keyword) {
        try {
            if (keyword == null || keyword.isEmpty()) {
                return false;
            }
            String title = getTitle();
            String description = getDescription();
            String subject = getSubject();
            if (title == null || description == null || subject == null) {
                return false;
            }
            String lowerKeyword = keyword.toLowerCase();
            return title.toLowerCase().contains(lowerKeyword) ||
                    description.toLowerCase().contains(lowerKeyword) ||
                    subject.toLowerCase().contains(lowerKeyword);
        } catch (NullPointerException e) {
            throw new NullPointerException("Null reference encountered while matching search");
        } catch (Exception e) {
            throw new RuntimeException("Error while matching search: " + e.getMessage());
        }
    }

    public void markAsSold(User buyer, Date saleDate) {
        try {
            if (buyer == null) {
                throw new IllegalArgumentException("Buyer cannot be null");
            }
            if (saleDate == null) {
                throw new IllegalArgumentException("Sale date cannot be null");
            }
            if (saleDate.after(new Date())) {
                throw new IllegalArgumentException("Sale date cannot be in the future");
            }
            if (is_sold) {
                throw new IllegalStateException("Item is already sold");
            }
            if (price <= 0 || price > market_price * 2) { // Simplified validation
                throw new IllegalStateException("Cannot mark item as sold with invalid price");
            }
            this.is_sold = true;
            this.buyer = buyer;
            this.sale_date = new Date(saleDate.getTime());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to mark item as sold: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to mark item as sold: " + e.getMessage());
        }
    }

    public String getConditionDescription() {
        try {
            Condition currentCondition = getCondition();
            if (currentCondition == null) {
                return "Unknown";
            }
            switch (currentCondition) {
                case NEW:
                    return "Brand New";
                case GOOD:
                    return "Good Condition";
                case FAIR:
                    return "Fair Condition";
                case POOR:
                    return "Poor Condition";
                default:
                    return "Unknown";
            }
        } catch (IllegalStateException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error determining condition";
        }
    }

    public boolean canBePurchased() {
        try {
            if (!isAvailable()) {
                return false;
            }
            if (price <= 0 || price > market_price * 2) {
                return false;
            }
            if (getUploader() == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine if item can be purchased: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        try {
            String baseString = super.toString();
            float price = getPrice();
            Condition condition = getCondition();
            float discount = discount_percentage;
            Date saleDate = sale_date;
            boolean isSold = isIs_sold();
            return baseString +
                    " Price: " + price +
                    " Condition: " + condition +
                    " Discount: " + discount + "%" +
                    " Sale Date: " + (saleDate != null ? saleDate : "N/A") +
                    " Sold: " + isSold;
        } catch (Exception e) {
            return "ForSaleItem [Error in toString(): " + e.getMessage() + "]";
        }
    }
}