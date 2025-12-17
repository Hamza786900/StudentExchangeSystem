package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;

public class Book extends ForSaleItem {
    private String author;
    private String edition;
    private String publisher;
    private int pages;
    private boolean is_hardcover;

    public Book(String title, User uploader, String description, Category category, GradeLevel grade, String subject, Condition condition, float market_price, float price, String author, String edition, String publisher, int pages, boolean is_hardcover) {
        super(title, uploader, description, category, grade, subject, condition, market_price, price);
        try {
            if (author == null || author.trim().isEmpty()) {
                throw new IllegalArgumentException("Author cannot be null or empty");
            }
            if (edition == null || edition.trim().isEmpty()) {
                throw new IllegalArgumentException("Edition cannot be null or empty");
            }
            if (publisher == null || publisher.trim().isEmpty()) {
                throw new IllegalArgumentException("Publisher cannot be null or empty");
            }
            if (pages <= 0) {
                throw new IllegalArgumentException("Pages must be a positive number");
            }
            if (pages > 5000) {
                throw new IllegalArgumentException("Pages cannot exceed 5000");
            }
            this.author = author.trim();
            this.edition = edition.trim();
            this.publisher = publisher.trim();
            this.pages = pages;
            this.is_hardcover = is_hardcover;
            if (this.author.length() < 2) {
                throw new IllegalArgumentException("Author name is too short");
            }
            if (this.publisher.length() < 2) {
                throw new IllegalArgumentException("Publisher name is too short");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create Book: " + e.getMessage());
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Failed to create Book: " + e.getMessage());
        }
    }

    public String getAuthor() {
        if (author == null || author.isEmpty()) {
            throw new IllegalStateException("Author is not set for this book");
        }
        return author;
    }

    public String getEdition() {
        if (edition == null || edition.isEmpty()) {
            throw new IllegalStateException("Edition is not set for this book");
        }
        return edition;
    }

    public String getPublisher() {
        if (publisher == null || publisher.isEmpty()) {
            throw new IllegalStateException("Publisher is not set for this book");
        }
        return publisher;
    }

    public int getPages() {
        if (pages <= 0) {
            throw new IllegalStateException("Page count is not valid for this book");
        }
        if (pages > 5000) {
            throw new IllegalStateException("Page count is unreasonably high");
        }
        return pages;
    }

    public boolean isIs_hardcover() {
        return is_hardcover;
    }

    @Override
    public boolean matchesSearch(String keyword) {
        try {
            boolean parentMatches = super.matchesSearch(keyword);
            if (parentMatches) {
                return true;
            }
            if (keyword == null || keyword.isEmpty()) {
                return false;
            }
            String lowerKeyword = keyword.toLowerCase();
            String author = getAuthor();
            String publisher = getPublisher();
            String edition = getEdition();
            if (author != null && author.toLowerCase().contains(lowerKeyword)) {
                return true;
            }
            if (publisher != null && publisher.toLowerCase().contains(lowerKeyword)) {
                return true;
            }
            if (edition != null && edition.toLowerCase().contains(lowerKeyword)) {
                return true;
            }
            return false;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to search book: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error while searching book: " + e.getMessage());
        }
    }

    @Override
    public boolean canBePurchased() {
        try {
            if (!super.canBePurchased()) {
                return false;
            }
            String author = getAuthor();
            String publisher = getPublisher();
            int pages = getPages();
            return author != null && !author.isEmpty() &&
                    publisher != null && !publisher.isEmpty() &&
                    pages > 0;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine if book can be purchased: " + e.getMessage());
        }
    }

    @Override
    public String getDetails() {
        try {
            String title = getTitle();
            String author = getAuthor();
            float price = getPrice();
            String condition = getConditionDescription();
            boolean isSold = isIs_sold();
            return String.format("Book: %s by %s - Price: Rs.%.2f - Condition: %s - %s",
                    title,
                    author,
                    price,
                    condition,
                    isSold ? "SOLD" : "AVAILABLE");
        } catch (Exception e) {
            return "Error getting book details: " + e.getMessage();
        }
    }

    @Override
    public String toString() {
        try {
            String superString = super.toString();
            String author = getAuthor();
            String edition = getEdition();
            String publisher = getPublisher();
            int pages = getPages();
            return superString +
                    " Author: " + (author != null ? author : "Unknown") +
                    " Edition: " + (edition != null ? edition : "N/A") +
                    " Publisher: " + (publisher != null ? publisher : "Unknown") +
                    " Pages: " + pages +
                    " is_hardcover: " + is_hardcover;
        } catch (Exception e) {
            return "Book [Error in toString(): " + e.getMessage() + "]";
        }
    }
}