package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;
import java.util.Calendar;

public class PastPaper extends ForSaleItem {
    private String exam_board;
    private int year;
    private boolean has_answers;
    private boolean has_model_paper;
    private boolean is_solved;
    private int total_papers;
    private String subject_code;
    private boolean is_compilation;

    public PastPaper(String title, User uploader, String description, Category category, GradeLevel grade, String subject, Condition condition, float market_price, float price, String exam_board, int year, boolean has_answers, boolean has_model_paper, boolean is_solved, int total_papers, String subject_code, boolean is_compilation) {
        super(title, uploader, description, category, grade, subject, condition, market_price, price);
        try {
            // Validate constructor parameters
            if (exam_board == null || exam_board.trim().isEmpty()) {
                throw new IllegalArgumentException("Exam board cannot be null or empty");
            }
            // Validate year
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1900 || year > currentYear + 1) { // Allow next year for upcoming exams
                throw new IllegalArgumentException("Year must be between 1900 and " + (currentYear + 1));
            }
            // Validate total_papers
            if (total_papers <= 0) {
                throw new IllegalArgumentException("Total papers must be a positive number");
            }
            if (total_papers > 50) {
                throw new IllegalArgumentException("Total papers cannot exceed 50");
            }
            // Validate compilation consistency
            if (is_compilation && total_papers <= 1) {
                throw new IllegalArgumentException("Compilation must have more than 1 paper");
            }
            // Validate subject code format (basic validation)
            if (subject_code == null || subject_code.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject code cannot be null or empty");
            }
            if (!isValidSubjectCode(subject_code.trim())) {
                throw new IllegalArgumentException("Invalid subject code format");
            }
            this.exam_board = exam_board.trim();
            this.year = year;
            this.has_answers = has_answers;
            this.has_model_paper = has_model_paper;
            this.is_solved = is_solved;
            this.total_papers = total_papers;
            this.subject_code = subject_code.trim();
            this.is_compilation = is_compilation;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create PastPaper: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error creating PastPaper: " + e.getMessage());
        }
    }

    private boolean isValidSubjectCode(String code) {
        // Basic validation: should be alphanumeric and 2-10 characters
        return code.matches("[A-Za-z0-9\\s-]{2,10}");
    }

    public String getExam_board() {
        if (exam_board == null || exam_board.isEmpty()) {
            throw new IllegalStateException("Exam board is not set");
        }
        return exam_board;
    }

    public int getYear() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (year < 1900 || year > currentYear + 1) {
            throw new IllegalStateException("Year is not valid");
        }
        return year;
    }

    public int getTotal_papers() {
        if (total_papers <= 0) {
            throw new IllegalStateException("Total papers is not valid");
        }
        return total_papers;
    }

    public String getSubject_code() {
        if (subject_code == null || subject_code.isEmpty()) {
            throw new IllegalStateException("Subject code is not set");
        }
        return subject_code;
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
            // Past paper specific search
            String board = getExam_board().toLowerCase();
            String subjCode = getSubject_code().toLowerCase();
            String yearStr = String.valueOf(getYear());
            if (board.contains(lowerKeyword)) {
                return true;
            }
            if (subjCode.contains(lowerKeyword)) {
                return true;
            }
            if (yearStr.contains(lowerKeyword)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to search past papers: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        try {
            String baseString = super.toString();
            String board = getExam_board();
            int paperYear = getYear();
            String subjCode = getSubject_code();
            return baseString +
                    " | Board: " + board +
                    " | Year: " + paperYear +
                    " | Subject: " + subjCode +
                    " | Solved: " + (is_solved ? "Yes" : "No") +
                    " | Compilation: " + (is_compilation ? "Yes" : "No") +
                    " | Papers: " + total_papers;
        } catch (Exception e) {
            return "PastPaper [Error in toString(): " + e.getMessage() + "]";
        }
    }
}