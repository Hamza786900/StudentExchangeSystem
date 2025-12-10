package com.studentexchange.models;


import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;

import java.util.HashMap;
import java.util.Map;

public class Book extends ForSaleItem {
    private String author;
    private String edition;
    private String publisher;
    private int pages;
    private boolean is_hardcover;

    public Book(String title, User uploader, String description, Category category, GradeLevel grade, String subject, Condition condition, float market_price, float price, String author, String edition, String publisher, int pages, boolean is_hardcover) {
        super(title, uploader, description, category, grade, subject, condition, market_price, price);
        try {
            this.author = author;
            this.edition = edition;
            this.publisher = publisher;
            this.pages = pages;
            this.is_hardcover = is_hardcover;
        } catch (Exception e) {
            System.out.println("Error creating Book: " + e.getMessage());
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        try {
            this.author = author;
        } catch (Exception e) {
            System.out.println("Error setting author: " + e.getMessage());
        }
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        try {
            this.edition = edition;
        } catch (Exception e) {
            System.out.println("Error setting edition: " + e.getMessage());
        }
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        try {
            this.publisher = publisher;
        } catch (Exception e) {
            System.out.println("Error setting publisher: " + e.getMessage());
        }
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        try {
            this.pages = pages;
        } catch (Exception e) {
            System.out.println("Error setting pages: " + e.getMessage());
        }
    }

    public boolean isIs_hardcover() {
        return is_hardcover;
    }

    public void setIs_hardcover(boolean is_hardcover) {
        try {
            this.is_hardcover = is_hardcover;
        } catch (Exception e) {
            System.out.println("Error setting hardcover flag: " + e.getMessage());
        }
    }


    public Map<String, String> getBookDetails() {
        Map<String, String> details = new HashMap<>();
        try {
            details.put("Title", getTitle());
            details.put("Author", author != null ? author : "Unknown");
            details.put("Edition", edition != null ? edition : "N/A");
            details.put("Publisher", publisher != null ? publisher : "Unknown");
            details.put("Pages", String.valueOf(pages));
            details.put("Cover Type", is_hardcover ? "Hardcover" : "Paperback");
            details.put("Condition", getConditionDescription());
            details.put("Price", "Rs. " + getPrice());
        } catch (Exception e) {
            System.out.println("Error getting book details: " + e.getMessage());
        }
        return details;
    }

    public boolean validateISBN() {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getAuthorInfo() {
        try {
            return author != null ? author : "Unknown Author";
        } catch (Exception e) {
            return "Unknown Author";
        }
    }

    public String getEditionInfo() {
        try {
            return edition != null ? edition : "Standard Edition";
        } catch (Exception e) {
            return "Standard Edition";
        }
    }

    public boolean isTextbook() {
        try {
            if (getTitle() == null) {
                return false;
            }
            String title = getTitle().toLowerCase();
            return title.contains("textbook") ||
                    title.contains("physics") ||
                    title.contains("chemistry") ||
                    title.contains("mathematics") ||
                    title.contains("biology") ||
                    getGrade() != GradeLevel.UNIVERSITY;
        } catch (Exception e) {
            System.out.println("Error checking if textbook: " + e.getMessage());
            return false;
        }
    }

    public String getPublisherInfo() {
        try {
            return publisher != null ? publisher : "Unknown Publisher";
        } catch (Exception e) {
            return "Unknown Publisher";
        }
    }

    public String getPageInfo() {
        try {
            return pages > 0 ? pages + " pages" : "Pages not specified";
        } catch (Exception e) {
            return "Pages not specified";
        }
    }

    @Override
    public String toString() {
        try {
            return super.toString() + " Author: " + getAuthor() + " Edition: " + getEdition() + " Publisher: " + getPublisher() + " Pages: " + getPages() + " is_hardcover: " + is_hardcover;
        } catch (Exception e) {
            return "Error displaying Book";
        }
    }
}
