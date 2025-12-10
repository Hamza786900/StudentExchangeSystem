package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.GradeLevel;

import java.util.Date;

public abstract class Item {
    private String item_id;
    private static int counter = 0;
    private String title;
    private String description;
    private User uploader;
    private Date upload_date;
    private Category category;
    private GradeLevel grade;
    private String subject;
    private int views;

    public Item(String title, User uploader, String description, Category category, GradeLevel grade, String subject) {
        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be null or empty");
            }
            if (uploader == null) {
                throw new IllegalArgumentException("Uploader cannot be null");
            }
            if (description == null) {
                throw new IllegalArgumentException("Description cannot be null");
            }
            if (category == null) {
                throw new IllegalArgumentException("Category cannot be null");
            }
            if (grade == null) {
                throw new IllegalArgumentException("Grade level cannot be null");
            }
            if (subject == null || subject.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject cannot be null or empty");
            }

            counter++;
            if (counter < 0) {
                throw new IllegalStateException("Item counter overflow");
            }

            this.item_id = "ITEM_" + String.format("%03d", counter);
            this.title = title.trim();
            this.description = description.trim();
            this.uploader = uploader;
            this.upload_date = new Date();
            this.category = category;
            this.grade = grade;
            this.subject = subject.trim();
            this.views = 0;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create Item: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to create Item due to internal state error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while creating Item: " + e.getMessage());
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be null or empty");
            }
            this.title = title.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set title: " + e.getMessage());
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        try {
            if (description == null) {
                throw new IllegalArgumentException("Description cannot be null");
            }
            this.description = description.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set description: " + e.getMessage());
        }
    }

    public GradeLevel getGrade() {
        return grade;
    }

    public void setGrade(GradeLevel grade) {
        try {
            if (grade == null) {
                throw new IllegalArgumentException("Grade level cannot be null");
            }
            this.grade = grade;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set grade: " + e.getMessage());
        }
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        try {
            if (subject == null || subject.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject cannot be null or empty");
            }
            this.subject = subject.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set subject: " + e.getMessage());
        }
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        try {
            if (views < 0) {
                throw new IllegalArgumentException("Views cannot be negative");
            }
            this.views = views;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set views: " + e.getMessage());
        }
    }

    public String getItem_id() {
        return item_id;
    }

    public User getUploader() {
        if (uploader == null) {
            throw new IllegalStateException("Uploader is not set for this item");
        }
        return uploader;
    }

    public Date getUpload_date() {
        if (upload_date == null) {
            throw new IllegalStateException("Upload date is not set for this item");
        }
        return new Date(upload_date.getTime());
    }

    public Category getCategory() {
        if (category == null) {
            throw new IllegalStateException("Category is not set for this item");
        }
        return category;
    }

    public void setCategory(Category category) {
        try {
            if (category == null) {
                throw new IllegalArgumentException("Category cannot be null");
            }
            this.category = category;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set category: " + e.getMessage());
        }
    }

    public abstract String getDetails();

    public abstract boolean isAvailable();

    public abstract boolean matchesSearch(String keyword);

    public void incrementViews() {
        try {
            if (views == Integer.MAX_VALUE) {
                throw new ArithmeticException("Cannot increment views: maximum integer value reached");
            }
            this.views++;
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Failed to increment views: " + e.getMessage());
        }
    }

    public void updateDetails(String title, String description) {
        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be null or empty when updating details");
            }
            if (description == null) {
                throw new IllegalArgumentException("Description cannot be null when updating details");
            }
            this.title = title.trim();
            this.description = description.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to update details: " + e.getMessage());
        }
    }

    public String getCategoryName() {
        try {
            if (this.category != null) {
                return this.category.name();
            }
            throw new IllegalStateException("Category is null, cannot get category name");
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to get category name: " + e.getMessage());
        }
    }

    public String getGradeName() {
        try {
            if (this.grade != null) {
                return this.grade.name();
            }
            throw new IllegalStateException("Grade is null, cannot get grade name");
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to get grade name: " + e.getMessage());
        }
    }

    public String getSellerInfo() {
        try {
            if (this.uploader != null) {
                User uploader = this.uploader;
                String name = uploader.getName();
                float rating = uploader.getAverage_rating();

                if (name == null || name.trim().isEmpty()) {
                    throw new IllegalStateException("Uploader name is not valid");
                }

                if (Float.isNaN(rating) || Float.isInfinite(rating)) {
                    throw new IllegalStateException("Uploader rating is not a valid number");
                }

                return String.format("Name: %s, Rating: %.2f", name, rating);
            }
            return "No Seller Info";
        } catch (NullPointerException e) {
            throw new NullPointerException("Null reference encountered while getting seller info: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to get seller info: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while getting seller info: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        try {
            String title = getTitle();
            GradeLevel grade = getGrade();
            String subject = getSubject();
            Category category = getCategory();
            User uploader = getUploader();

            if (title == null) {
                throw new IllegalStateException("Title is null in toString()");
            }
            if (grade == null) {
                throw new IllegalStateException("Grade is null in toString()");
            }
            if (subject == null) {
                throw new IllegalStateException("Subject is null in toString()");
            }
            if (category == null) {
                throw new IllegalStateException("Category is null in toString()");
            }
            if (uploader == null) {
                throw new IllegalStateException("Uploader is null in toString()");
            }

            String uploaderName = uploader.getName();
            if (uploaderName == null) {
                uploaderName = "Unknown";
            }

            return "Title: " + title +
                    " Grade: " + grade +
                    " Subject: " + subject +
                    " Category: " + category +
                    " Uploader: " + uploaderName;
        } catch (IllegalStateException e) {
            return "Item [ID: " + (item_id != null ? item_id : "Unknown") +
                    ", Error: " + e.getMessage() + "]";
        } catch (Exception e) {
            return "Item [ID: " + (item_id != null ? item_id : "Unknown") +
                    ", Error generating string representation: " + e.getMessage() + "]";
        }
    }
}