package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.GradeLevel;
import java.util.Date;
import java.util.Objects;

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

    public Item(String title, User uploader, String description,
                Category category, GradeLevel grade, String subject) {

        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be empty");
        if (uploader == null)
            throw new IllegalArgumentException("Uploader cannot be null");
        if (category == null || grade == null)
            throw new IllegalArgumentException("Category and Grade required");
        if (subject == null || subject.trim().isEmpty())
            throw new IllegalArgumentException("Subject required");

        counter++;
        this.item_id = "ITEM_" + String.format("%03d", counter);
        this.title = title.trim();
        this.description = description.trim();
        this.uploader = uploader;
        this.category = category;
        this.grade = grade;
        this.subject = subject.trim();
        this.upload_date = new Date();
    }

    public String getItem_id() { return item_id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public User getUploader() { return uploader; }
    public Date getUpload_date() { return new Date(upload_date.getTime()); }
    public Category getCategory() { return category; }
    public GradeLevel getGrade() { return grade; }
    public String getSubject() { return subject; }

    public abstract String getDetails();
    public abstract boolean isAvailable();
    public abstract boolean matchesSearch(String keyword);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return item_id.equals(item.item_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item_id);
    }
}
