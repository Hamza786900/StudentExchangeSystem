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
            this.exam_board = exam_board;
            this.year = year;
            this.has_answers = has_answers;
            this.has_model_paper = has_model_paper;
            this.is_solved = is_solved;
            this.total_papers = total_papers;
            this.subject_code = subject_code;
            this.is_compilation = is_compilation;
        } catch (Exception e) {
            System.out.println("Error creating PastPaper object: " + e.getMessage());
        }
    }

    public String getExam_board() {
        return exam_board;
    }

    public void setExam_board(String exam_board) {
        try {
            this.exam_board = exam_board;
        } catch (Exception e) {
            System.out.println("Error setting exam board: " + e.getMessage());
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        try {
            this.year = year;
        } catch (Exception e) {
            System.out.println("Error setting year: " + e.getMessage());
        }
    }

    public boolean isHas_answers() {
        return has_answers;
    }

    public void setHas_answers(boolean has_answers) {
        try {
            this.has_answers = has_answers;
        } catch (Exception e) {
            System.out.println("Error setting answers flag: " + e.getMessage());
        }
    }

    public boolean isHas_model_paper() {
        return has_model_paper;
    }

    public void setHas_model_paper(boolean has_model_paper) {
        try {
            this.has_model_paper = has_model_paper;
        } catch (Exception e) {
            System.out.println("Error setting model paper flag: " + e.getMessage());
        }
    }

    public boolean isIs_solved() {
        return is_solved;
    }

    public void setIs_solved(boolean is_solved) {
        try {
            this.is_solved = is_solved;
        } catch (Exception e) {
            System.out.println("Error setting solved flag: " + e.getMessage());
        }
    }

    public int getTotal_papers() {
        return total_papers;
    }

    public void setTotal_papers(int total_papers) {
        try {
            this.total_papers = total_papers;
        } catch (Exception e) {
            System.out.println("Error setting total papers: " + e.getMessage());
        }
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        try {
            this.subject_code = subject_code;
        } catch (Exception e) {
            System.out.println("Error setting subject code: " + e.getMessage());
        }
    }

    public boolean isIs_compilation() {
        return is_compilation;
    }

    public void setIs_compilation(boolean is_compilation) {
        try {
            this.is_compilation = is_compilation;
        } catch (Exception e) {
            System.out.println("Error setting compilation flag: " + e.getMessage());
        }
    }

    public Map<String, String> getPastPaperDetails() {
        Map<String, String> details = new HashMap<>();
        try {
            details.put("Title", getTitle());
            details.put("Exam Board", exam_board != null ? exam_board : "Unknown");
            details.put("Year", String.valueOf(year));
            details.put("Subject Code", subject_code != null ? subject_code : "N/A");
            details.put("Total Papers", String.valueOf(total_papers));
            details.put("Has Answers", has_answers ? "Yes" : "No");
            details.put("Has Model Paper", has_model_paper ? "Yes" : "No");
            details.put("Solved", is_solved ? "Yes" : "No");
            details.put("Compilation", is_compilation ? "Yes" : "No");
            details.put("Price", "Rs. " + getPrice());
        } catch (Exception e) {
            System.out.println("Error generating past paper details: " + e.getMessage());
        }
        return details;
    }

    public boolean isRecent() {
        try {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            return (currentYear - year) <= 2;
        } catch (Exception e) {
            return false;
        }
    }

    public String getExamInfo() {
        try {
            return String.format("%s - %s (%d)",
                    exam_board != null ? exam_board : "Unknown",
                    subject_code != null ? subject_code : "Unknown",
                    year);
        } catch (Exception e) {
            return "Unknown Exam Info";
        }
    }

    public boolean validateGrade() {
        try {
            GradeLevel grade = getGrade();
            return grade == GradeLevel.GRADE_9 ||
                    grade == GradeLevel.GRADE_10 ||
                    grade == GradeLevel.GRADE_11 ||
                    grade == GradeLevel.GRADE_12 ||
                    grade == GradeLevel.ENTRY_TEST;
        } catch (Exception e) {
            return false;
        }
    }

    public int calculateTotalPapers() {
        try {
            return total_papers;
        } catch (Exception e) {
            return 0;
        }
    }

    public String getBoardInfo() {
        try {
            return exam_board != null ? exam_board : "Board not specified";
        } catch (Exception e) {
            return "Board not specified";
        }
    }

    public boolean hasSolutions() {
        try {
            return has_answers || is_solved;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCompilation() {
        try {
            return is_compilation;
        } catch (Exception e) {
            return false;
        }
    }

    public String getYearRange() {
        try {
            if (is_compilation && total_papers > 1) {
                return String.format("%d - %d", year - total_papers + 1, year);
            }
            return String.valueOf(year);
        } catch (Exception e) {
            return "Year unavailable";
        }
    }

    public String getSubjectInfo() {
        try {
            String subj = getSubject() != null ? getSubject() : "Unknown";
            String code = subject_code != null ? " (" + subject_code + ")" : "";
            return subj + code;
        } catch (Exception e) {
            return "Unknown Subject";
        }
    }

    @Override
    public String toString() {
        try {
            return super.toString() +
                    " Board: " + getExam_board() +
                    " Year: " + getYear() +
                    " Subject: " + getSubject_code() +
                    " Solved: " + isIs_solved() +
                    " Compilation: " + isIs_compilation();
        } catch (Exception e) {
            return "PastPaper (error displaying details)";
        }
    }
}
