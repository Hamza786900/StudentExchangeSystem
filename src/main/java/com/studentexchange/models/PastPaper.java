package com.studentexchange.models;

import com.studentexchange.enums.*;

public class PastPaper extends ForSaleItem {

    private String exam_board;
    private int year;

    public PastPaper(String title, User uploader, String description,
                     Category category, GradeLevel grade, String subject,
                     Condition condition, float market_price, float price,
                     String exam_board, int year) {

        super(title, uploader, description, category, grade, subject,
                condition, market_price, price);

        this.exam_board = exam_board;
        this.year = year;
    }

    @Override
    public String getDetails() {
        return "Past Paper: " + getTitle() +
                " (" + exam_board + " " + year + ")";
    }
}
