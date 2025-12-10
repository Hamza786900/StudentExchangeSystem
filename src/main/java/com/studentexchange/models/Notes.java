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
            this.pages = pages;
            this.format_type = format_type;
            this.is_handwritten = is_handwritten;
            this.chapters = new ArrayList<>();
            this.is_scanned = is_scanned;
            this.quality = quality;
        } catch (Exception e) {
            System.out.println("Error creating Notes: " + e.getMessage());
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

    public String getFormat_type() {
        return format_type;
    }

    public void setFormat_type(String format_type) {
        try {
            this.format_type = format_type;
        } catch (Exception e) {
            System.out.println("Error setting format type: " + e.getMessage());
        }
    }

    public boolean isIs_handwritten() {
        return is_handwritten;
    }

    public void setIs_handwritten(boolean is_handwritten) {
        try {
            this.is_handwritten = is_handwritten;
        } catch (Exception e) {
            System.out.println("Error setting handwritten flag: " + e.getMessage());
        }
    }

    public boolean isIs_scanned() {
        return is_scanned;
    }

    public void setIs_scanned(boolean is_scanned) {
        try {
            this.is_scanned = is_scanned;
        } catch (Exception e) {
            System.out.println("Error setting scanned flag: " + e.getMessage());
        }
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        try {
            this.quality = quality;
        } catch (Exception e) {
            System.out.println("Error setting quality: " + e.getMessage());
        }
    }

    public List<String> getChapters() {
        return chapters;
    }

    public void addChapter(String chapter) {
        try {
            if (chapter != null && !chapter.isEmpty()) {
                chapters.add(chapter);
            }
        } catch (Exception e) {
            System.out.println("Error adding chapter: " + e.getMessage());
        }
    }

    public Map<String, String> getNoteDetails() {
        Map<String, String> details = new HashMap<>();
        try {
            details.put("Title", getTitle());
            details.put("Pages", String.valueOf(pages));
            details.put("Format", format_type != null ? format_type : "Not specified");
            details.put("Type", is_handwritten ? "Handwritten" : "Printed");
            details.put("Quality", quality != null ? quality : "Standard");
            details.put("Chapters", String.valueOf(getTotalChapters()));
            details.put("Scanned", is_scanned ? "Yes" : "No");
            details.put("Price", "Rs. " + getPrice());
        } catch (Exception e) {
            System.out.println("Error getting note details: " + e.getMessage());
        }
        return details;
    }

    public String getFormatInfo() {
        try {
            return format_type != null ? format_type : "Standard Format";
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
            if (keyword == null || keyword.isEmpty()) {
                return false;
            }
            String lowerKeyword = keyword.toLowerCase();
            for (String chapter : chapters) {
                if (chapter != null && chapter.toLowerCase().contains(lowerKeyword)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error searching chapter: " + e.getMessage());
        }
        return false;
    }

    public String getQualityDescription() {
        try {
            if (quality == null) {
                return "Standard Quality";
            }
            switch (quality.toLowerCase()) {
                case "high":
                    return "High Quality";
                case "medium":
                    return "Medium Quality";
                case "low":
                    return "Low Quality";
                default:
                    return quality;
            }
        } catch (Exception e) {
            return "Standard Quality";
        }
    }

    public boolean isDigital() {
        try {
            if (format_type == null) {
                return false;
            }
            String format = format_type.toLowerCase();
            return format.contains("pdf") || format.contains("digital") || format.contains("electronic");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        try {
            return super.toString() +
                    " Format: " + getFormat_type() +
                    " Pages: " + getPages() +
                    " Quality: " + getQuality() +
                    " Handwritten: " + isIs_handwritten() +
                    " Scanned: " + isIs_scanned();
        } catch (Exception e) {
            return "Error displaying Notes";
        }
    }
}
