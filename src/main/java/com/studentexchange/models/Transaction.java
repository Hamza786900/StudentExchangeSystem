package com.studentexchange.models;

import com.studentexchange.enums.*;

import java.util.Date;

public class Transaction {

    private String transaction_id;
    private static int counter = 0;

    private User buyer;
    private User seller;
    private ForSaleItem item;
    private PaymentStatus payment_status;

    public Transaction(User buyer, User seller,
                       ForSaleItem item, PaymentMethod method) {

        counter++;
        this.transaction_id = "TX_" + counter;
        this.buyer = buyer;
        this.seller = seller;
        this.item = item;
        this.payment_status = PaymentStatus.PENDING;

        item.markAsSold(buyer);
    }

    public void completePayment() {
        this.payment_status = PaymentStatus.COMPLETED;
    }

    public PaymentStatus getPayment_status() {
        return payment_status;
    }
}
