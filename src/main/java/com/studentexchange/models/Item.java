package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.GradeLevel;
import java.util.Date;

public abstract class Item {
    private String item_id;
    private static int counter=0;
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
            counter++;
            this.item_id = "ITEM_"+String.format("%03d",counter);
            this.title = title;
            this.description = description;
            this.uploader = uploader;
            this.upload_date = new Date();
            this.category = category;
            this.grade = grade;
            this.subject = subject;
            this.views = 0;
        } catch (Exception e) {
            System.out.println("Error creating Item: " + e.getMessage());
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        try {
            this.title = title;
        } catch (Exception e) {
            System.out.println("Error setting title: " + e.getMessage());
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        try {
            this.description = description;
        } catch (Exception e) {
            System.out.println("Error setting description: " + e.getMessage());
        }
    }

    public GradeLevel getGrade() {
        return grade;
    }

    public void setGrade(GradeLevel grade) {
        try {
            this.grade = grade;
        } catch (Exception e) {
            System.out.println("Error setting grade: " + e.getMessage());
        }
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        try {
            this.subject = subject;
        } catch (Exception e) {
            System.out.println("Error setting subject: " + e.getMessage());
        }
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        try {
            this.views = views;
        } catch (Exception e) {
            System.out.println("Error setting views: " + e.getMessage());
        }
    }

    public String getItem_id() {
        return item_id;
    }

    public User getUploader() {
        return uploader;
    }

    public Date getUpload_date() {
        return upload_date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        try {
            this.category = category;
        } catch (Exception e) {
            System.out.println("Error setting category: " + e.getMessage());
        }
    }

    public abstract String getDetails();
    public abstract boolean isAvailable();
    public abstract boolean matchesSearch(String keyword);

    public void incrementViews(){
        try {
            this.views++;
        } catch (Exception e) {
            System.out.println("Error incrementing views: " + e.getMessage());
        }
    }

    public void updateDetails(String title, String description){
        try {
            this.title = title;
            this.description = description;
        } catch (Exception e) {
            System.out.println("Error updating details: " + e.getMessage());
        }
    }

    public String getCategoryName(){
        try {
            if(this.category != null) {
                return this.category.name();
            }
        } catch (Exception e) {
            System.out.println("Error getting category name: " + e.getMessage());
        }
        return null;
    }

    public String getGradeName(){
        try {
            if(this.grade != null) {
                return this.grade.name();
            }
        } catch (Exception e) {
            System.out.println("Error getting grade name: " + e.getMessage());
        }
        return null;
    }

    public String getSellerInfo(){
        try {
            if(this.uploader != null){
                return String.format("Name: %s, Rating: %.2f",
                        uploader.getName(),
                        uploader.getAverage_rating());
            }
        } catch (Exception e) {
            System.out.println("Error getting seller info: " + e.getMessage());
        }
        return "No Seller Info";
    }

    @Override
    public String toString(){
        try {
            return "Title: " + getTitle() +
                    " Grade: " + getGrade() +
                    " Subject: " + getSubject() +
                    " Category: " + getCategory() +
                    " Uploader: " + (getUploader()!=null?getUploader().getName():"N/A");
        } catch (Exception e) {
            return "Error displaying item";
        }
    }
}
