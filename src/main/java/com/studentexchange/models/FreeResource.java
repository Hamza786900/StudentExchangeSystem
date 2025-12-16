package com.studentexchange.models;

import com.studentexchange.enums.*;

public class FreeResource extends Item {

    private String file_url;

    public FreeResource(String title, User uploader, String description,
                        Category category, GradeLevel grade,
                        String subject, String file_url) {

        super(title, uploader, description, category, grade, subject);
        this.file_url = file_url;
    }

    @Override
    public boolean isAvailable() {
        return file_url != null && !file_url.isEmpty();
    }

    @Override
    public boolean matchesSearch(String keyword) {
        keyword = keyword.toLowerCase();
        return getTitle().toLowerCase().contains(keyword) ||
                getSubject().toLowerCase().contains(keyword);
    }

    @Override
    public String getDetails() {
        return "Free Resource: " + getTitle();
    }
}
