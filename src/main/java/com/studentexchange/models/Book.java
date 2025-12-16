package com.studentexchange.models;

import com.studentexchange.enums.*;

public class Book extends ForSaleItem {

    private String author;
    private String edition;
    private String publisher;
    private int pages;
    private boolean is_hardcover;

    public Book(String title, User uploader, String description,
                Category category, GradeLevel grade, String subject,
                Condition condition, float market_price, float price,
                String author, String edition, String publisher,
                int pages, boolean is_hardcover) {

        super(title, uploader, description, category, grade, subject,
                condition, market_price, price);

        this.author = author;
        this.edition = edition;
        this.publisher = publisher;
        this.pages = pages;
        this.is_hardcover = is_hardcover;
    }

    @Override
    public String getDetails() {
        return "Book: " + getTitle() + " by " + author +
                " - Rs." + getPrice();
    }
}
