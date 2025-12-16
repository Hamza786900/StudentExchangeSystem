package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;

public class ForSaleItem extends Item {

    private float price;
    private float market_price;
    private Condition condition;
    private boolean is_sold;

    public ForSaleItem(String title, User uploader, String description,
                       Category category, GradeLevel grade, String subject,
                       Condition condition, float market_price, float price) {

        super(title, uploader, description, category, grade, subject);

        if (price < 0 || market_price < 0)
            throw new IllegalArgumentException("Price cannot be negative");
        if (condition == null)
            throw new IllegalArgumentException("Condition required");

        this.price = price;
        this.market_price = market_price;
        this.condition = condition;
        this.is_sold = false;
    }

    public float getPrice() { return price; }
    public Condition getCondition() { return condition; }
    public boolean isIs_sold() { return is_sold; }

    void markAsSold(User buyer) {
        this.is_sold = true;
    }

    public String getConditionDescription() {
        switch (condition) {
            case NEW: return "Brand New";
            case GOOD: return "Good Condition";
            case FAIR: return "Fair Condition";
            case POOR: return "Poor Condition";
            default: return "Unknown";
        }
    }

    @Override
    public boolean isAvailable() {
        return !is_sold;
    }

    @Override
    public boolean matchesSearch(String keyword) {
        keyword = keyword.toLowerCase();
        return getTitle().toLowerCase().contains(keyword) ||
                getDescription().toLowerCase().contains(keyword) ||
                getSubject().toLowerCase().contains(keyword);
    }

    @Override
    public String getDetails() {
        return getTitle() + " - Rs." + price + " - " +
                (is_sold ? "SOLD" : "AVAILABLE");
    }
}
