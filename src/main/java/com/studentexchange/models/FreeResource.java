package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.GradeLevel;

import java.util.HashMap;
import java.util.Map;

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
            this.file_url = file_url;
            this.is_university_paper = is_university_paper;
            this.university = university;
            this.course_code = course_code;
            this.year = year;
            this.semester = semester;
            this.exam_type = exam_type;
            this.has_solutions = has_solutions;
            this.is_official = is_official;
            this.file_size = file_size;
            this.file_format = file_format;
            this.download_count = 0;
        } catch (Exception e) {
            System.out.println("Error creating FreeResource: " + e.getMessage());
        }
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        try {
            this.file_url = file_url;
        } catch (Exception e) {
            System.out.println("Error setting file url: " + e.getMessage());
        }
    }

    public boolean isIs_university_paper() {
        return is_university_paper;
    }

    public void setIs_university_paper(boolean is_university_paper) {
        try {
            this.is_university_paper = is_university_paper;
        } catch (Exception e) {
            System.out.println("Error setting university paper flag: " + e.getMessage());
        }
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        try {
            this.university = university;
        } catch (Exception e) {
            System.out.println("Error setting university: " + e.getMessage());
        }
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        try {
            this.course_code = course_code;
        } catch (Exception e) {
            System.out.println("Error setting course code: " + e.getMessage());
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        try {
            this.semester = semester;
        } catch (Exception e) {
            System.out.println("Error setting semester: " + e.getMessage());
        }
    }

    public String getExam_type() {
        return exam_type;
    }

    public void setExam_type(String exam_type) {
        try {
            this.exam_type = exam_type;
        } catch (Exception e) {
            System.out.println("Error setting exam type: " + e.getMessage());
        }
    }

    public boolean isHas_solutions() {
        return has_solutions;
    }

    public void setHas_solutions(boolean has_solutions) {
        try {
            this.has_solutions = has_solutions;
        } catch (Exception e) {
            System.out.println("Error setting solutions flag: " + e.getMessage());
        }
    }

    public boolean isIs_official() {
        return is_official;
    }

    public void setIs_official(boolean is_official) {
        try {
            this.is_official = is_official;
        } catch (Exception e) {
            System.out.println("Error setting official flag: " + e.getMessage());
        }
    }

    public float getFile_size() {
        return file_size;
    }

    public void setFile_size(float file_size) {
        try {
            this.file_size = file_size;
        } catch (Exception e) {
            System.out.println("Error setting file size: " + e.getMessage());
        }
    }

    public String getFile_format() {
        return file_format;
    }

    public void setFile_format(String file_format) {
        try {
            this.file_format = file_format;
        } catch (Exception e) {
            System.out.println("Error setting file format: " + e.getMessage());
        }
    }

    public int getDownload_count() {
        return download_count;
    }

    @Override
    public String getDetails() {
        try {
            return String.format("Free Resource: %s - %s (%s) - Downloads: %d",
                    getTitle(), university, course_code, download_count);
        } catch (Exception e) {
            return "Error getting resource details";
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            return file_url != null && !file_url.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean matchesSearch(String keyword) {
        try {
            if (keyword == null || keyword.isEmpty()) {
                return false;
            }
            String lowerKeyword = keyword.toLowerCase();

            String title = (getTitle() != null) ? getTitle() : "";
            String desc = (getDescription() != null) ? getDescription() : "";
            String subject = (getSubject() != null) ? getSubject() : "";
            String uni = (university != null) ? university : "";
            String course = (course_code != null) ? course_code : "";

            return title.toLowerCase().contains(lowerKeyword) ||
                    desc.toLowerCase().contains(lowerKeyword) ||
                    uni.toLowerCase().contains(lowerKeyword) ||
                    course.toLowerCase().contains(lowerKeyword) ||
                    subject.toLowerCase().contains(lowerKeyword);
        } catch (Exception e) {
            System.out.println("Error searching resource: " + e.getMessage());
            return false;
        }
    }

    public void incrementDownload() {
        try {
            this.download_count++;
        } catch (Exception e) {
            System.out.println("Error incrementing download count: " + e.getMessage());
        }
    }

    public Map<String, String> getDownloadStats() {
        Map<String, String> stats = new HashMap<>();
        try {
            stats.put("Total Downloads", String.valueOf(download_count));
            stats.put("File Size", file_size + " MB");
            stats.put("Format", file_format != null ? file_format : "Unknown");
            stats.put("Popularity", getPopularity());
        } catch (Exception e) {
            System.out.println("Error getting download stats: " + e.getMessage());
        }
        return stats;
    }

    public Map<String, String> getFileInfo() {
        Map<String, String> info = new HashMap<>();
        try {
            info.put("File Size", file_size + " MB");
            info.put("Format", file_format != null ? file_format : "Unknown");
            info.put("URL", file_url != null ? file_url : "Not available");
        } catch (Exception e) {
            System.out.println("Error getting file info: " + e.getMessage());
        }
        return info;
    }

    public boolean isDownloadable() {
        try {
            return file_url != null && !file_url.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getUniversityInfo() {
        try {
            return String.format("%s - %s",
                    university != null ? university : "Unknown",
                    course_code != null ? course_code : "N/A");
        } catch (Exception e) {
            return "Unknown - N/A";
        }
    }

    public boolean validateUniversityPaper() {
        try {
            return is_university_paper &&
                    university != null && !university.isEmpty() &&
                    course_code != null && !course_code.isEmpty() &&
                    year > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public String getExamDetails() {
        try {
            return String.format("Year: %d, Semester: %s, Type: %s",
                    year,
                    semester != null ? semester : "N/A",
                    exam_type != null ? exam_type : "N/A");
        } catch (Exception e) {
            return "Exam details not available";
        }
    }

    public String getResourceTypeDescription() {
        try {
            if (is_university_paper) {
                return "University Past Paper";
            }
        } catch (Exception e) {
            // ignored
        }
        return "Free Educational Resource";
    }

    public boolean isOfficialPaper() {
        return is_official;
    }

    public boolean hasSolutions() {
        return has_solutions;
    }

    public String getPopularity() {
        try {
            if (download_count > 100) {
                return "Very Popular";
            } else if (download_count > 50) {
                return "Popular";
            } else if (download_count > 10) {
                return "Moderate";
            }
        } catch (Exception e) {
            // ignored
        }
        return "New";
    }

    @Override
    public String toString() {
        try {
            return "University: " + getUniversity() +
                    " Course code: " + getCourse_code() +
                    " Year: " + getYear() +
                    " Semester: " + getSemester() +
                    " exam type: " + getExam_type() +
                    " is official: " + isIs_official();
        } catch (Exception e) {
            return "Error displaying FreeResource";
        }
    }
}
