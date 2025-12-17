package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;
import java.util.ArrayList;
import java.util.List;

public class Notes extends ForSaleItem {
    private int pages;
    private String format_type;
    private List<String> chapters;
    private boolean is_handwritten;
    private boolean is_scanned;
    private String quality;

    public Notes(String title, User uploader, String description, Category category, GradeLevel grade, String subject, Condition condition, float market_price, float price, int pages, String format_type, boolean is_handwritten, boolean is_scanned, String quality) {
        super(title, uploader, description, category, grade, subject, condition, market_price, price);
        try {
            // Validate constructor parameters
            if (pages <= 0) {
                throw new IllegalArgumentException("Pages must be a positive number");
            }
            if (pages > 1000) {
                throw new IllegalArgumentException("Pages cannot exceed 1000");
            }
            if (format_type == null || format_type.trim().isEmpty()) {
                throw new IllegalArgumentException("Format type cannot be null or empty");
            }
            if (quality == null || quality.trim().isEmpty()) {
                throw new IllegalArgumentException("Quality cannot be null or empty");
            }
            // Validate quality values
            String lowerQuality = quality.toLowerCase().trim();
            if (!isValidQuality(lowerQuality)) {
                throw new IllegalArgumentException("Invalid quality value. Must be 'high', 'medium', or 'low'");
            }
            this.pages = pages;
            this.format_type = format_type.trim();
            this.is_handwritten = is_handwritten;
            this.chapters = new ArrayList<>();
            this.is_scanned = is_scanned;
            this.quality = quality.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create Notes: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error creating Notes: " + e.getMessage());
        }
    }

    private boolean isValidQuality(String quality) {
        return quality.equals("high") ||
                quality.equals("medium") || quality.equals("low");
    }

    public int getPages() {
        if (pages <= 0) {
            throw new IllegalStateException("Page count is not valid");
        }
        return pages;
    }

    public String getFormat_type() {
        if (format_type == null || format_type.isEmpty()) {
            throw new IllegalStateException("Format type is not set");
        }
        return format_type;
    }

    public boolean isIs_handwritten() {
        return is_handwritten;
    }

    public boolean isIs_scanned() {
        return is_scanned;
    }

    public String getQuality() {
        if (quality == null || quality.isEmpty()) {
            throw new IllegalStateException("Quality is not set");
        }
        return quality;
    }

    public List<String> getChapters() {
        // Return a defensive copy
        return new ArrayList<>(chapters);
    }

    @Override
    public boolean matchesSearch(String keyword) {
        try {
            // First check parent class search
            boolean parentMatches = super.matchesSearch(keyword);
            if (parentMatches) {
                return true;
            }
            if (keyword == null || keyword.trim().isEmpty()) {
                return false;
            }
            String lowerKeyword = keyword.toLowerCase().trim();
            // Notes-specific search
            String format = getFormat_type().toLowerCase();
            String quality = getQuality().toLowerCase();

            boolean matchesChapter = false;
            for (String chapter : chapters) {
                if (chapter != null && chapter.toLowerCase().contains(lowerKeyword)) {
                    matchesChapter = true;
                    break;
                }
            }

            if (format.contains(lowerKeyword)) {
                return true;
            }
            if (quality.contains(lowerKeyword)) {
                return true;
            }
            // Search in chapters
            return matchesChapter;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to search notes: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        try {
            String baseString = super.toString();
            String format = getFormat_type();
            int pageCount = getPages();
            String qualityDesc = getQuality(); // Using raw quality here, simplifying from original

            return baseString +
                    " | Format: " + format +
                    " | Pages: " + pageCount +
                    " | Quality: " + qualityDesc +
                    " | Handwritten: " + (is_handwritten ? "Yes" : "No") +
                    " | Scanned: " + (is_scanned ? "Yes" : "No");
        } catch (Exception e) {
            return "Notes [Error in toString(): " + e.getMessage() + "]";
        }
    }
}