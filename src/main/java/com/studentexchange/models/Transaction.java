package com.studentexchange.models;

import com.studentexchange.enums.PaymentMethod;
import com.studentexchange.enums.PaymentStatus;
import com.studentexchange.enums.ShippingStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

class InvalidPaymentException extends RuntimeException {
    public InvalidPaymentException(String msg) { super(msg); }
}

class InvalidShippingOperationException extends RuntimeException {
    public InvalidShippingOperationException(String msg) { super(msg); }
}

class ReviewNotAllowedException extends RuntimeException {
    public ReviewNotAllowedException(String msg) { super(msg); }
}

public class Transaction {
    private String transaction_id;
    private static int counter = 0;
    private User buyer;
    private User seller;
    private ForSaleItem item;
    private Date transaction_date;
    private PaymentMethod payment_method;
    private PaymentStatus payment_status;
    private ShippingStatus shipping_status;
    private Date shipping_date;
    private Date delivery_date;
    private Review buyer_review;
    private Review seller_review;
    private boolean reviews_completed;
    private int credits_used;

    public Transaction(User buyer, User seller, ForSaleItem item, PaymentMethod payment_method) {
        // ADDED VALIDATION
        if (buyer == null || seller == null || item == null || payment_method == null) {
            throw new IllegalArgumentException("Buyer, seller, item, and payment method cannot be null.");
        }

        counter++;
        this.transaction_id = "TRANSACTION_" + String.format("%03d", counter);
        this.buyer = buyer;
        this.seller = seller;
        this.item = item;
        this.transaction_date = new Date();
        this.payment_method = payment_method;
        this.payment_status = PaymentStatus.PENDING;
        this.shipping_status = ShippingStatus.NOT_SHIPPED;
        this.shipping_date = null;
        this.delivery_date = null;
        this.buyer_review = null;
        this.seller_review = null;
        this.reviews_completed = false;
        this.credits_used = 0;
    }

    public PaymentMethod getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(PaymentMethod payment_method) {
        if (payment_method == null) throw new InvalidPaymentException("Payment method cannot be null.");
        this.payment_method = payment_method;
    }

    public PaymentStatus getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(PaymentStatus payment_status) {
        if (payment_status == null) throw new InvalidPaymentException("Payment status cannot be null.");
        this.payment_status = payment_status;
    }

    public ShippingStatus getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(ShippingStatus shipping_status) {
        if (shipping_status == null) throw new InvalidShippingOperationException("Shipping status cannot be null.");
        this.shipping_status = shipping_status;
    }

    public boolean isReviews_completed() {
        return reviews_completed;
    }

    public void setReviews_completed(boolean reviews_completed) {
        this.reviews_completed = reviews_completed;
    }

    public int getCredits_used() {
        return credits_used;
    }

    public void setCredits_used(int credits_used) {
        if (credits_used < 0) throw new IllegalArgumentException("Credits cannot be negative.");
        this.credits_used = credits_used;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public ForSaleItem getItem() {
        return item;
    }

    public Date getTransaction_date() {
        return transaction_date;
    }

    public Date getShipping_date() {
        return shipping_date;
    }

    public Review getBuyer_review() {
        return buyer_review;
    }

    public Date getDelivery_date() {
        return delivery_date;
    }

    public Review getSeller_review() {
        return seller_review;
    }

    public void completePayment(PaymentMethod method) {
        if (method == null) throw new InvalidPaymentException("Invalid payment method.");
        if (payment_status == PaymentStatus.COMPLETED)
            throw new InvalidPaymentException("Payment already completed.");

        this.payment_method = method;
        this.payment_status = PaymentStatus.COMPLETED;
    }

    public void updateShippingStatus(ShippingStatus status) {
        if (status == null) throw new InvalidShippingOperationException("Shipping status cannot be null.");
        if (payment_status != PaymentStatus.COMPLETED)
            throw new InvalidShippingOperationException("Cannot update shipping before payment completion.");

        this.shipping_status = status;
        if (status == ShippingStatus.SHIPPED && shipping_date == null) {
            this.shipping_date = new Date();
        }
    }

    public void confirmDelivery() {
        if (shipping_status != ShippingStatus.SHIPPED)
            throw new InvalidShippingOperationException("Cannot confirm delivery before shipping.");

        this.shipping_status = ShippingStatus.DELIVERED;
        this.delivery_date = new Date();
    }

    public void addBuyerReview(Review review) {
        if (!canSubmitReview(buyer))
            throw new ReviewNotAllowedException("Buyer cannot submit review.");

        if (review == null) throw new IllegalArgumentException("Review cannot be null.");

        this.buyer_review = review;
        checkReviewsCompleted();
    }

    public void addSellerReview(Review review) {
        if (!canSubmitReview(seller))
            throw new ReviewNotAllowedException("Seller cannot submit review.");

        if (review == null) throw new IllegalArgumentException("Review cannot be null.");

        this.seller_review = review;
        checkReviewsCompleted();
    }

    private void checkReviewsCompleted() {
        if (buyer_review != null && seller_review != null) {
            this.reviews_completed = true;
        }
    }

    public boolean canSubmitReview(User user) {
        if (shipping_status != ShippingStatus.DELIVERED) {
            return false;
        }
        if (user.equals(buyer) && buyer_review == null) {
            return true;
        }
        if (user.equals(seller) && seller_review == null) {
            return true;
        }
        return false;
    }

    public Map<String, String> getTransactionStatus() {
        Map<String, String> status = new HashMap<>();
        status.put("Transaction ID", transaction_id);
        status.put("Payment Status", payment_status.name());
        status.put("Shipping Status", shipping_status.name());
        status.put("Payment Method", payment_method.name());
        status.put("Reviews Completed", reviews_completed ? "Yes" : "No");
        status.put("Credits Used", String.valueOf(credits_used));
        return status;
    }

    public float calculateTotal() {
        float total = item.getPrice();
        if (credits_used > 0) {
            float discount = credits_used * 10.0f;
            total = Math.max(0, total - discount);
        }
        return total;
    }

    public boolean isComplete() {
        return payment_status == PaymentStatus.COMPLETED &&
                shipping_status == ShippingStatus.DELIVERED &&
                reviews_completed;
    }

    public Map<String, Review> getReviews() {
        Map<String, Review> reviews = new HashMap<>();
        if (buyer_review != null) {
            reviews.put("buyer", buyer_review);
        }
        if (seller_review != null) {
            reviews.put("seller", seller_review);
        }
        return reviews;
    }

    public long getDaysSinceTransaction() {
        long diff = new Date().getTime() - transaction_date.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public boolean isDelivered() {
        return shipping_status == ShippingStatus.DELIVERED;
    }

    public boolean isPaid() {
        return payment_status == PaymentStatus.COMPLETED;
    }

    public void applyCredits(int credits) {
        if (credits < 0) throw new IllegalArgumentException("Credits cannot be negative.");
        this.credits_used = credits;
    }

    public long getDeliveryTime() {
        if (shipping_date != null && delivery_date != null) {
            long diff = delivery_date.getTime() - shipping_date.getTime();
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        }
        return -1;
    }

    @Override
    public String toString() {
        return " ID: " + getTransaction_id() + " Date: " + getTransaction_date() + " Item: " + getItem().getTitle() + " Payment method: " + getPayment_method() + " Payment status: " + getPayment_status() + " Shipping status: " + getShipping_status() + " Credits used: " + getCredits_used() + " Shipping date: " + getShipping_date() + " delivery date: " + getDelivery_date();
    }
}
