package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return quality.equals("high") || quality.equals("medium") || quality.equals("low");
    }

    public int getPages() {
        if (pages <= 0) {
            throw new IllegalStateException("Page count is not valid");
        }
        return pages;
    }

    public void setPages(int pages) {
        try {
            if (pages <= 0) {
                throw new IllegalArgumentException("Pages must be a positive number");
            }
            if (pages > 1000) {
                throw new IllegalArgumentException("Pages cannot exceed 1000");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change pages of sold notes");
            }
            this.pages = pages;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set pages: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set pages: " + e.getMessage());
        }
    }

    public String getFormat_type() {
        if (format_type == null || format_type.isEmpty()) {
            throw new IllegalStateException("Format type is not set");
        }
        return format_type;
    }

    public void setFormat_type(String format_type) {
        try {
            if (format_type == null || format_type.trim().isEmpty()) {
                throw new IllegalArgumentException("Format type cannot be null or empty");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change format type of sold notes");
            }
            this.format_type = format_type.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set format type: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set format type: " + e.getMessage());
        }
    }

    public boolean isIs_handwritten() {
        return is_handwritten;
    }

    public void setIs_handwritten(boolean is_handwritten) {
        try {
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change handwritten status of sold notes");
            }
            this.is_handwritten = is_handwritten;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set handwritten flag: " + e.getMessage());
        }
    }

    public boolean isIs_scanned() {
        return is_scanned;
    }

    public void setIs_scanned(boolean is_scanned) {
        try {
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change scanned status of sold notes");
            }
            this.is_scanned = is_scanned;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set scanned flag: " + e.getMessage());
        }
    }

    public String getQuality() {
        if (quality == null || quality.isEmpty()) {
            throw new IllegalStateException("Quality is not set");
        }
        return quality;
    }

    public void setQuality(String quality) {
        try {
            if (quality == null || quality.trim().isEmpty()) {
                throw new IllegalArgumentException("Quality cannot be null or empty");
            }

            String lowerQuality = quality.toLowerCase().trim();
            if (!isValidQuality(lowerQuality)) {
                throw new IllegalArgumentException("Invalid quality value. Must be 'high', 'medium', or 'low'");
            }

            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change quality of sold notes");
            }

            this.quality = quality.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set quality: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set quality: " + e.getMessage());
        }
    }

    public List<String> getChapters() {
        // Return a defensive copy
        return new ArrayList<>(chapters);
    }

    public void addChapter(String chapter) {
        try {
            if (chapter == null || chapter.trim().isEmpty()) {
                throw new IllegalArgumentException("Chapter cannot be null or empty");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot add chapters to sold notes");
            }

            String trimmedChapter = chapter.trim();
            if (chapters.contains(trimmedChapter)) {
                throw new IllegalArgumentException("Chapter '" + trimmedChapter + "' already exists");
            }

            chapters.add(trimmedChapter);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add chapter: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to add chapter: " + e.getMessage());
        }
    }

    public void removeChapter(String chapter) {
        try {
            if (chapter == null || chapter.trim().isEmpty()) {
                throw new IllegalArgumentException("Chapter cannot be null or empty");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot remove chapters from sold notes");
            }

            String trimmedChapter = chapter.trim();
            if (!chapters.contains(trimmedChapter)) {
                throw new IllegalArgumentException("Chapter '" + trimmedChapter + "' not found");
            }

            chapters.remove(trimmedChapter);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to remove chapter: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to remove chapter: " + e.getMessage());
        }
    }

    public Map<String, String> getNoteDetails() {
        try {
            Map<String, String> details = new HashMap<>();

            String title = getTitle();
            int pageCount = getPages();
            String format = getFormat_type();
            String qualityLevel = getQuality();
            float price = getPrice();

            details.put("Title", title != null ? title : "Unknown");
            details.put("Pages", String.valueOf(pageCount));
            details.put("Format", format);
            details.put("Type", is_handwritten ? "Handwritten" : "Printed");
            details.put("Quality", getQualityDescription());
            details.put("Chapters", String.valueOf(getTotalChapters()));
            details.put("Scanned", is_scanned ? "Yes" : "No");
            details.put("Price", String.format("Rs. %.2f", price));
            details.put("Digital", isDigital() ? "Yes" : "No");
            details.put("Condition", getConditionDescription());

            return details;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get note details: " + e.getMessage());
        }
    }

    public String getFormatInfo() {
        try {
            String format = getFormat_type();
            return format + (isDigital() ? " (Digital)" : " (Physical)");
        } catch (IllegalStateException e) {
            return "Format information unavailable: " + e.getMessage();
        } catch (Exception e) {
            return "Standard Format";
        }
    }

    public String getChaptersFormatted() {
        try {
            if (chapters.isEmpty()) {
                return "No chapters listed";
            }
            return String.join(", ", chapters);
        } catch (Exception e) {
            return "No chapters listed";
        }
    }

    public int getTotalChapters() {
        try {
            return chapters.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean containsChapter(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return false;
            }

            String lowerKeyword = keyword.toLowerCase().trim();
            for (String chapter : chapters) {
                if (chapter != null && chapter.toLowerCase().contains(lowerKeyword)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to search chapters: " + e.getMessage());
        }
    }

    public String getQualityDescription() {
        try {
            String qualityLevel = getQuality().toLowerCase();
            switch (qualityLevel) {
                case "high":
                    return "High Quality (Excellent clarity and organization)";
                case "medium":
                    return "Medium Quality (Good readability)";
                case "low":
                    return "Low Quality (Basic notes)";
                default:
                    return qualityLevel;
            }
        } catch (IllegalStateException e) {
            return "Standard Quality";
        } catch (Exception e) {
            return "Standard Quality";
        }
    }

    public boolean isDigital() {
        try {
            String format = getFormat_type().toLowerCase();
            return format.contains("pdf") ||
                    format.contains("digital") ||
                    format.contains("electronic") ||
                    format.contains("doc") ||
                    format.contains("docx") ||
                    format.contains("txt");
        } catch (Exception e) {
            return false;
        }
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

            if (format.contains(lowerKeyword)) {
                return true;
            }
            if (quality.contains(lowerKeyword)) {
                return true;
            }

            // Search in chapters
            return containsChapter(lowerKeyword);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to search notes: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                " Format: " + getFormat_type() +
                " Pages: " + getPages() +
                " Quality: " + getQuality() +
                " Handwritten: " + isIs_handwritten() +
                " Scanned: " + isIs_scanned();
    }
}