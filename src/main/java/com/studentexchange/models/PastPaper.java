package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

    public void setExam_board(String exam_board) {
        try {
            if (exam_board == null || exam_board.trim().isEmpty()) {
                throw new IllegalArgumentException("Exam board cannot be null or empty");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change exam board of sold past paper");
            }
            this.exam_board = exam_board.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set exam board: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set exam board: " + e.getMessage());
        }
    }

    public int getYear() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (year < 1900 || year > currentYear + 1) {
            throw new IllegalStateException("Year is not valid");
        }
        return year;
    }

    public void setYear(int year) {
        try {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1900 || year > currentYear + 1) {
                throw new IllegalArgumentException("Year must be between 1900 and " + (currentYear + 1));
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change year of sold past paper");
            }
            this.year = year;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set year: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set year: " + e.getMessage());
        }
    }

    public boolean isHas_answers() {
        return has_answers;
    }

    public void setHas_answers(boolean has_answers) {
        try {
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change answers status of sold past paper");
            }
            this.has_answers = has_answers;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set answers flag: " + e.getMessage());
        }
    }

    public boolean isHas_model_paper() {
        return has_model_paper;
    }

    public void setHas_model_paper(boolean has_model_paper) {
        try {
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change model paper status of sold past paper");
            }
            this.has_model_paper = has_model_paper;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set model paper flag: " + e.getMessage());
        }
    }

    public boolean isIs_solved() {
        return is_solved;
    }

    public void setIs_solved(boolean is_solved) {
        try {
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change solved status of sold past paper");
            }
            this.is_solved = is_solved;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set solved flag: " + e.getMessage());
        }
    }

    public int getTotal_papers() {
        if (total_papers <= 0) {
            throw new IllegalStateException("Total papers is not valid");
        }
        return total_papers;
    }

    public void setTotal_papers(int total_papers) {
        try {
            if (total_papers <= 0) {
                throw new IllegalArgumentException("Total papers must be a positive number");
            }
            if (total_papers > 50) {
                throw new IllegalArgumentException("Total papers cannot exceed 50");
            }
            if (is_compilation && total_papers <= 1) {
                throw new IllegalArgumentException("Compilation must have more than 1 paper");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change total papers of sold past paper");
            }
            this.total_papers = total_papers;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set total papers: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set total papers: " + e.getMessage());
        }
    }

    public String getSubject_code() {
        if (subject_code == null || subject_code.isEmpty()) {
            throw new IllegalStateException("Subject code is not set");
        }
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        try {
            if (subject_code == null || subject_code.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject code cannot be null or empty");
            }
            if (!isValidSubjectCode(subject_code.trim())) {
                throw new IllegalArgumentException("Invalid subject code format");
            }
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change subject code of sold past paper");
            }
            this.subject_code = subject_code.trim();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set subject code: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set subject code: " + e.getMessage());
        }
    }

    public boolean isIs_compilation() {
        return is_compilation;
    }

    public void setIs_compilation(boolean is_compilation) {
        try {
            if (isIs_sold()) {
                throw new IllegalStateException("Cannot change compilation status of sold past paper");
            }
            if (is_compilation && total_papers <= 1) {
                throw new IllegalStateException("Compilation must have more than 1 paper");
            }
            this.is_compilation = is_compilation;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set compilation flag: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set compilation flag: " + e.getMessage());
        }
    }

    public Map<String, String> getPastPaperDetails() {
        try {
            Map<String, String> details = new HashMap<>();

            String title = getTitle();
            String board = getExam_board();
            int paperYear = getYear();
            String subjCode = getSubject_code();
            int totalPapers = getTotal_papers();
            float price = getPrice();
            String subjectName = getSubject();

            details.put("Title", title != null ? title : "Unknown");
            details.put("Exam Board", board);
            details.put("Year", String.valueOf(paperYear));
            details.put("Subject", subjectName != null ? subjectName : "Unknown");
            details.put("Subject Code", subjCode);
            details.put("Total Papers", String.valueOf(totalPapers));
            details.put("Has Answers", has_answers ? "Yes" : "No");
            details.put("Has Model Paper", has_model_paper ? "Yes" : "No");
            details.put("Solved", is_solved ? "Yes" : "No");
            details.put("Compilation", is_compilation ? "Yes" : "No");
            details.put("Recent", isRecent() ? "Yes (Last 2 years)" : "No");
            details.put("Price", String.format("Rs. %.2f", price));
            details.put("Year Range", getYearRange());

            return details;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate past paper details: " + e.getMessage());
        }
    }

    public boolean isRecent() {
        try {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            return (currentYear - year) <= 2;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if past paper is recent: " + e.getMessage());
        }
    }

    public String getExamInfo() {
        try {
            String board = getExam_board();
            String subjCode = getSubject_code();
            int paperYear = getYear();

            return String.format("%s - %s (%d)",
                    board,
                    subjCode,
                    paperYear);
        } catch (Exception e) {
            return "Unknown Exam Info";
        }
    }

    public boolean validateGrade() {
        try {
            GradeLevel grade = getGrade();
            if (grade == null) {
                return false;
            }

            // Check if grade is appropriate for past papers
            return grade == GradeLevel.GRADE_9 ||
                    grade == GradeLevel.GRADE_10 ||
                    grade == GradeLevel.GRADE_11 ||
                    grade == GradeLevel.GRADE_12 ||
                    grade == GradeLevel.UNIVERSITY; // Added university as valid grade
        } catch (Exception e) {
            throw new IllegalStateException("Failed to validate grade: " + e.getMessage());
        }
    }

    public int calculateTotalPapers() {
        try {
            int papers = getTotal_papers();
            if (is_compilation) {
                return papers;
            }
            return Math.min(papers, 1); // Non-compilation has at most 1 paper
        } catch (Exception e) {
            throw new IllegalStateException("Failed to calculate total papers: " + e.getMessage());
        }
    }

    public String getBoardInfo() {
        try {
            String board = getExam_board();
            return board + " Examination Board";
        } catch (IllegalStateException e) {
            return "Board not specified";
        } catch (Exception e) {
            return "Board information unavailable";
        }
    }

    public boolean hasSolutions() {
        try {
            return has_answers || is_solved;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if past paper has solutions: " + e.getMessage());
        }
    }

    public boolean isCompilation() {
        try {
            return is_compilation;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if past paper is compilation: " + e.getMessage());
        }
    }

    public String getYearRange() {
        try {
            if (is_compilation) {
                int totalPapers = getTotal_papers();
                int startYear = year - totalPapers + 1;
                if (startYear < 1900) {
                    startYear = 1900;
                }
                return String.format("%d - %d", startYear, year);
            }
            return String.valueOf(year);
        } catch (Exception e) {
            return "Year unavailable";
        }
    }

    public String getSubjectInfo() {
        try {
            String subjectName = getSubject();
            String code = getSubject_code();

            if (subjectName == null || subjectName.isEmpty()) {
                return code;
            }
            return subjectName + " (" + code + ")";
        } catch (Exception e) {
            return "Subject information unavailable";
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
        return super.toString() +
                " Board: " + getExam_board() +
                " Year: " + getYear() +
                " Subject: " + getSubject_code() +
                " Solved: " + isIs_solved() +
                " Compilation: " + isIs_compilation();
    }
}