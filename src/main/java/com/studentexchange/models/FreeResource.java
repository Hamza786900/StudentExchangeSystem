package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.GradeLevel;

public class FreeResource extends Item {
    private String file_url;
    private boolean is_university_paper;
    private String university;
    private String course_code;
    private int year;
    private String semester;
    private String exam_type;
    private boolean has_solutions;
    private boolean is_official;
    private float file_size;
    private String file_format;
    private int download_count;

    public FreeResource(String title, User uploader, String description, Category category, GradeLevel grade, String subject, String file_url, boolean is_university_paper, String university, String course_code, int year, String semester, String exam_type, boolean has_solutions, boolean is_official, float file_size, String file_format) {
        super(title, uploader, description, category, grade, subject);
        try {
            // Validate file-related parameters
            if (file_url == null || file_url.trim().isEmpty()) {
                throw new IllegalArgumentException("File URL cannot be null or empty");
            }
            if (!isValidUrl(file_url)) {
                throw new IllegalArgumentException("Invalid file URL format");
            }
            // Validate university paper specific parameters
            if (is_university_paper) {
                if (university == null || university.trim().isEmpty()) {
                    throw new IllegalArgumentException("University cannot be null or empty for university papers");
                }
                if (course_code == null || course_code.trim().isEmpty()) {
                    throw new IllegalArgumentException("Course code cannot be null or empty for university papers");
                }
                if (year < 1900 || year > 2100) {
                    throw new IllegalArgumentException("Year must be between 1900 and 2100");
                }
            }
            // Validate file size
            if (file_size <= 0) {
                throw new IllegalArgumentException("File size must be positive");
            }
            if (file_size > 1000) { // 1000 MB = 1GB limit
                throw new IllegalArgumentException("File size cannot exceed 1000 MB");
            }
            // Validate file format
            if (file_format == null || file_format.trim().isEmpty()) {
                throw new IllegalArgumentException("File format cannot be null or empty");
            }
            if (!isValidFileFormat(file_format)) {
                throw new IllegalArgumentException("Unsupported file format: " + file_format);
            }
            this.file_url = file_url.trim();
            this.is_university_paper = is_university_paper;
            this.university = university != null ? university.trim() : null;
            this.course_code = course_code != null ? course_code.trim() : null;
            this.year = year;
            this.semester = semester != null ? semester.trim() : null;
            this.exam_type = exam_type != null ?
                    exam_type.trim() : null;
            this.has_solutions = has_solutions;
            this.is_official = is_official;
            this.file_size = file_size;
            this.file_format = file_format.trim();
            this.download_count = 0;
            // Additional validation
            if (Float.isNaN(this.file_size) || Float.isInfinite(this.file_size)) {
                throw new IllegalArgumentException("File size is not a valid number");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create FreeResource: " + e.getMessage());
        }
    }

    private boolean isValidUrl(String url) {
        // Basic URL validation
        return url != null &&
                (url.startsWith("http://") || url.startsWith("https://") ||
                        url.startsWith("ftp://") || url.startsWith("file://"));
    }

    private boolean isValidFileFormat(String format) {
        String[] allowedFormats = {"pdf", "doc", "docx", "txt", "ppt", "pptx", "xls", "xlsx", "jpg", "png", "zip", "rar"};
        String lowerFormat = format.toLowerCase();
        for (String allowed : allowedFormats) {
            if (lowerFormat.equals(allowed)) {
                return true;
            }
        }
        return false;
    }

    public String getFile_url() {
        if (file_url == null || file_url.isEmpty()) {
            throw new IllegalStateException("File URL is not available");
        }
        return file_url;
    }

    public boolean isIs_university_paper() {
        return is_university_paper;
    }

    public String getUniversity() {
        if (is_university_paper && (university == null || university.isEmpty())) {
            throw new IllegalStateException("University is required for university papers");
        }
        return university;
    }

    public String getCourse_code() {
        if (is_university_paper && (course_code == null || course_code.isEmpty())) {
            throw new IllegalStateException("Course code is required for university papers");
        }
        return course_code;
    }

    public int getYear() {
        if (is_university_paper && year <= 0) {
            throw new IllegalStateException("Year is not valid for university paper");
        }
        return year;
    }

    public String getSemester() {
        return semester;
    }

    public String getExam_type() {
        return exam_type;
    }

    public boolean isHas_solutions() {
        return has_solutions;
    }

    public boolean isIs_official() {
        return is_official;
    }

    public float getFile_size() {
        if (Float.isNaN(file_size) || Float.isInfinite(file_size)) {
            throw new IllegalStateException("File size is not a valid number");
        }
        if (file_size <= 0) {
            throw new IllegalStateException("File size must be positive");
        }
        return file_size;
    }

    public String getFile_format() {
        if (file_format == null || file_format.isEmpty()) {
            throw new IllegalStateException("File format is not set");
        }
        return file_format;
    }

    public int getDownload_count() {
        if (download_count < 0) {
            throw new IllegalStateException("Download count cannot be negative");
        }
        return download_count;
    }

    @Override
    public String getDetails() {
        try {
            String title = getTitle();
            String uni = getUniversity();
            String course = getCourse_code();
            int downloads = getDownload_count();
            if (title == null) {
                throw new IllegalStateException("Title is null");
            }
            return String.format("Free Resource: %s - %s (%s) - Downloads: %d",
                    title,
                    uni != null ? uni : "Unknown",
                    course != null ? course : "N/A",
                    downloads);
        } catch (IllegalStateException e) {
            return "Error getting resource details: " + e.getMessage();
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            return file_url != null && !file_url.isEmpty() && isValidUrl(file_url);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check availability: " + e.getMessage());
        }
    }

    @Override
    public boolean matchesSearch(String keyword) {
        try {
            if (keyword == null || keyword.isEmpty()) {
                return false;
            }
            String title = getTitle();
            String description = getDescription();
            String subject = getSubject();
            if (title == null || description == null || subject == null) {
                return false;
            }
            String lowerKeyword = keyword.toLowerCase();
            boolean matches = title.toLowerCase().contains(lowerKeyword) ||
                    description.toLowerCase().contains(lowerKeyword) ||
                    subject.toLowerCase().contains(lowerKeyword);
            // Additional university paper specific search
            if (is_university_paper) {
                String uni = getUniversity();
                String course = getCourse_code();
                if (uni != null && uni.toLowerCase().contains(lowerKeyword)) {
                    matches = true;
                }
                if (course != null && course.toLowerCase().contains(lowerKeyword)) {
                    matches = true;
                }
            }
            return matches;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to match search: " + e.getMessage());
        } catch (NullPointerException e) {
            throw new NullPointerException("Null reference encountered while searching");
        }
    }

    public void incrementDownload() {
        try {
            if (download_count == Integer.MAX_VALUE) {
                throw new ArithmeticException("Cannot increment download count: maximum integer value reached");
            }
            if (!isAvailable()) {
                throw new IllegalStateException("Cannot increment download count for unavailable resource");
            }
            this.download_count++;
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Failed to increment download count: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to increment download count: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        try {
            if (is_university_paper) {
                String uni = getUniversity();
                String course = getCourse_code();
                int yr = getYear();
                String sem = getSemester();
                String exam = getExam_type();
                return "University: " + (uni != null ? uni : "Unknown") +
                        " Course code: " + (course != null ? course : "N/A") +
                        " Year: " + yr +
                        " Semester: " + (sem != null ? sem : "N/A") +
                        " exam type: " + (exam != null ? exam : "N/A") +
                        " is official: " + isIs_official();
            }
            return super.toString() + " [Free Resource]";
        } catch (Exception e) {
            return "FreeResource [Error in toString(): " + e.getMessage() + "]";
        }
    }
}