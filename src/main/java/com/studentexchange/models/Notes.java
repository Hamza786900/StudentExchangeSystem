package com.studentexchange.models;

import com.studentexchange.enums.*;

public class Notes extends ForSaleItem {

    private int pages;
    private String format_type;
    private boolean is_handwritten;
    private boolean is_scanned;
    private String quality;

    public Notes(String title, User uploader, String description,
                 Category category, GradeLevel grade, String subject,
                 Condition condition, float market_price, float price,
                 int pages, String format_type,
                 boolean is_handwritten, boolean is_scanned, String quality) {

        super(title, uploader, description, category, grade, subject,
                condition, market_price, price);

        this.pages = pages;
        this.format_type = format_type;
        this.is_handwritten = is_handwritten;
        this.is_scanned = is_scanned;
        this.quality = quality;
    }

    @Override
    public String getDetails() {
        return "Notes: " + getTitle() + " - Rs." + getPrice();
    }
}
