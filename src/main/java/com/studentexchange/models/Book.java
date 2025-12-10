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

    public void setAuthor(String author) {
        try {
            if (author == null || author.trim().isEmpty()) {
                throw new IllegalArgumentException("Author cannot be null or empty");
            }
            if (author.trim().length() < 2) {
                throw new IllegalArgumentException("Author name must be at least 2 characters long");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change author of a sold book");
            }
            this.author = author.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set author: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set author: " + e.getMessage());
        }
    }

    public String getEdition() {
        if (edition == null || edition.isEmpty()) {
            throw new IllegalStateException("Edition is not set for this book");
        }
        return edition;
    }

    public void setEdition(String edition) {
        try {
            if (edition == null || edition.trim().isEmpty()) {
                throw new IllegalArgumentException("Edition cannot be null or empty");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change edition of a sold book");
            }
            this.edition = edition.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set edition: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set edition: " + e.getMessage());
        }
    }

    public String getPublisher() {
        if (publisher == null || publisher.isEmpty()) {
            throw new IllegalStateException("Publisher is not set for this book");
        }
        return publisher;
    }

    public void setPublisher(String publisher) {
        try {
            if (publisher == null || publisher.trim().isEmpty()) {
                throw new IllegalArgumentException("Publisher cannot be null or empty");
            }
            if (publisher.trim().length() < 2) {
                throw new IllegalArgumentException("Publisher name must be at least 2 characters long");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change publisher of a sold book");
            }
            this.publisher = publisher.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set publisher: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set publisher: " + e.getMessage());
        }
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

    public void setPages(int pages) {
        try {
            if (pages <= 0) {
                throw new IllegalArgumentException("Pages must be a positive number");
            }
            if (pages > 5000) {
                throw new IllegalArgumentException("Pages cannot exceed 5000");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change page count of a sold book");
            }
            this.pages = pages;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set pages: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set pages: " + e.getMessage());
        }
    }

    public boolean isIs_hardcover() {
        return is_hardcover;
    }

    public void setIs_hardcover(boolean is_hardcover) {
        try {
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change cover type of a sold book");
            }
            this.is_hardcover = is_hardcover;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set cover type: " + e.getMessage());
        }
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

    public Map<String, String> getBookDetails() {
        try {
            Map<String, String> details = new HashMap<>();

            String title = getTitle();
            String author = getAuthor();
            String edition = getEdition();
            String publisher = getPublisher();
            int pages = getPages();
            String conditionDesc = getConditionDescription();
            float price = getPrice();

            details.put("Title", title != null ? title : "Unknown Title");
            details.put("Author", author);
            details.put("Edition", edition);
            details.put("Publisher", publisher);
            details.put("Pages", String.valueOf(pages));
            details.put("Cover Type", is_hardcover ? "Hardcover" : "Paperback");
            details.put("Condition", conditionDesc);
            details.put("Price", String.format("Rs. %.2f", price));
            details.put("ISBN", validateISBN() ? "Valid" : "Not Validated");
            details.put("Is Textbook", String.valueOf(isTextbook()));

            return details;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get book details: " + e.getMessage());
        }
    }

    public boolean validateISBN() {

        try {
            String title = getTitle();
            String author = getAuthor();
            return title != null && !title.isEmpty() &&
                    author != null && !author.isEmpty();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to validate ISBN: " + e.getMessage());
        }
    }

    public String getAuthorInfo() {
        try {
            String author = getAuthor();
            return author != null ? author : "Unknown Author";
        } catch (IllegalStateException e) {
            return "Author information unavailable: " + e.getMessage();
        }
    }

    public String getEditionInfo() {
        try {
            String edition = getEdition();
            return edition != null ? edition : "Standard Edition";
        } catch (IllegalStateException e) {
            return "Edition information unavailable: " + e.getMessage();
        }
    }

    public boolean isTextbook() {
        try {
            String title = getTitle();
            if (title == null) {
                return false;
            }

            String lowerTitle = title.toLowerCase();
            GradeLevel grade = getGrade();

            boolean isTextbookByTitle = lowerTitle.contains("textbook") ||
                    lowerTitle.contains("physics") ||
                    lowerTitle.contains("chemistry") ||
                    lowerTitle.contains("mathematics") ||
                    lowerTitle.contains("biology") ||
                    lowerTitle.contains("course") ||
                    lowerTitle.contains("guide") ||
                    lowerTitle.contains("manual");

            boolean isEducationalLevel = grade != null && grade != GradeLevel.UNIVERSITY;

            return isTextbookByTitle || isEducationalLevel;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine if book is textbook: " + e.getMessage());
        }
    }

    public String getPublisherInfo() {
        try {
            String publisher = getPublisher();
            return publisher != null ? publisher : "Unknown Publisher";
        } catch (IllegalStateException e) {
            return "Publisher information unavailable: " + e.getMessage();
        }
    }

    public String getPageInfo() {
        try {
            int pages = getPages();
            if (pages > 0) {
                if (pages < 100) {
                    return pages + " pages (Short)";
                } else if (pages < 500) {
                    return pages + " pages (Medium)";
                } else if (pages < 1000) {
                    return pages + " pages (Long)";
                } else {
                    return pages + " pages (Reference Book)";
                }
            }
            return "Pages not specified";
        } catch (IllegalStateException e) {
            return "Page information unavailable: " + e.getMessage();
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
    public String toString() {
        return super.toString() +
                " Author: " + getAuthor() +
                " Edition: " + getEdition() +
                " Publisher: " + getPublisher() +
                " Pages: " + getPages() +
                " is_hardcover: " + is_hardcover;
    }
}