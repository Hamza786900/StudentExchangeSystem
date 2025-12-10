package com.studentexchange.models;

import com.studentexchange.enums.PaymentMethod;
import com.studentexchange.enums.PaymentStatus;
import com.studentexchange.enums.ShippingStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        try {
            // Validate all parameters
            if (buyer == null) {
                throw new IllegalArgumentException("Buyer cannot be null");
            }
            if (seller == null) {
                throw new IllegalArgumentException("Seller cannot be null");
            }
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            if (payment_method == null) {
                throw new IllegalArgumentException("Payment method cannot be null");
            }

            // Prevent buying from self
            if (buyer.equals(seller)) {
                throw new IllegalArgumentException("Buyer and seller cannot be the same user");
            }

            // Validate item availability
            if (!item.isAvailable()) {
                throw new IllegalArgumentException("Item is not available for sale");
            }

            // Validate item can be purchased
            if (!item.canBePurchased()) {
                throw new IllegalArgumentException("Item cannot be purchased");
            }

            counter++;
            if (counter < 0) {
                throw new IllegalStateException("Transaction counter overflow");
            }

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

            // Mark item as sold
            //item.markAsSold(buyer, new Date());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create Transaction: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to create Transaction: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error creating Transaction: " + e.getMessage());
        }
    }

    public PaymentMethod getPayment_method() {
        if (payment_method == null) {
            throw new IllegalStateException("Payment method is not set");
        }
        return payment_method;
    }

    public void setPayment_method(PaymentMethod payment_method) {
        try {
            if (payment_method == null) {
                throw new IllegalArgumentException("Payment method cannot be null");
            }
            if (payment_status == PaymentStatus.COMPLETED) {
                throw new IllegalStateException("Cannot change payment method after payment is completed");
            }
            this.payment_method = payment_method;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set payment method: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set payment method: " + e.getMessage());
        }
    }

    public PaymentStatus getPayment_status() {
        if (payment_status == null) {
            throw new IllegalStateException("Payment status is not set");
        }
        return payment_status;
    }

    public void setPayment_status(PaymentStatus payment_status) {
        try {
            if (payment_status == null) {
                throw new IllegalArgumentException("Payment status cannot be null");
            }

            // Validate state transitions
            if (this.payment_status == PaymentStatus.COMPLETED &&
                    payment_status != PaymentStatus.COMPLETED) {
                throw new IllegalStateException("Cannot revert from COMPLETED payment status");
            }

            if (this.payment_status == PaymentStatus.FAILED &&
                    payment_status != PaymentStatus.FAILED &&
                    payment_status != PaymentStatus.PENDING) {
                throw new IllegalStateException("Can only revert FAILED status to PENDING");
            }

            this.payment_status = payment_status;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set payment status: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set payment status: " + e.getMessage());
        }
    }

    public ShippingStatus getShipping_status() {
        if (shipping_status == null) {
            throw new IllegalStateException("Shipping status is not set");
        }
        return shipping_status;
    }

    public void setShipping_status(ShippingStatus shipping_status) {
        try {
            if (shipping_status == null) {
                throw new IllegalArgumentException("Shipping status cannot be null");
            }

            // Validate payment is completed before shipping
            if (payment_status != PaymentStatus.COMPLETED &&
                    shipping_status != ShippingStatus.NOT_SHIPPED) {
                throw new IllegalStateException("Cannot update shipping status before payment completion");
            }

            // Validate state transitions
            if (shipping_status == ShippingStatus.SHIPPED &&
                    this.shipping_status == ShippingStatus.DELIVERED) {
                throw new IllegalStateException("Cannot revert from DELIVERED to SHIPPED");
            }

            if (shipping_status == ShippingStatus.DELIVERED &&
                    this.shipping_status == ShippingStatus.NOT_SHIPPED) {
                throw new IllegalStateException("Cannot mark as DELIVERED without being SHIPPED first");
            }

            this.shipping_status = shipping_status;

            // Update shipping date when first shipped
            if (shipping_status == ShippingStatus.SHIPPED && shipping_date == null) {
                this.shipping_date = new Date();
            }

            // Update delivery date when delivered
            if (shipping_status == ShippingStatus.DELIVERED && delivery_date == null) {
                this.delivery_date = new Date();
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set shipping status: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set shipping status: " + e.getMessage());
        }
    }

    public boolean isReviews_completed() {
        return reviews_completed;
    }

    public void setReviews_completed(boolean reviews_completed) {
        // This is calculated automatically, but allow manual setting for edge cases
        if (reviews_completed && (buyer_review == null || seller_review == null)) {
            throw new IllegalStateException("Cannot mark reviews as completed when reviews are missing");
        }
        this.reviews_completed = reviews_completed;
    }

    public int getCredits_used() {
        if (credits_used < 0) {
            throw new IllegalStateException("Credits used is in invalid state (negative)");
        }
        return credits_used;
    }

    public void setCredits_used(int credits_used) {
        try {
            if (credits_used < 0) {
                throw new IllegalArgumentException("Credits cannot be negative");
            }
            if (payment_status == PaymentStatus.COMPLETED) {
                throw new IllegalStateException("Cannot change credits used after payment is completed");
            }

            // Validate buyer has enough credits
            if (credits_used > 0 && buyer.getCredit_points() < credits_used) {
                throw new IllegalArgumentException("Buyer does not have enough credit points");
            }

            this.credits_used = credits_used;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set credits used: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set credits used: " + e.getMessage());
        }
    }

    public String getTransaction_id() {
        if (transaction_id == null || transaction_id.isEmpty()) {
            throw new IllegalStateException("Transaction ID is not set");
        }
        return transaction_id;
    }

    public User getBuyer() {
        if (buyer == null) {
            throw new IllegalStateException("Buyer is not set");
        }
        return buyer;
    }

    public User getSeller() {
        if (seller == null) {
            throw new IllegalStateException("Seller is not set");
        }
        return seller;
    }

    public ForSaleItem getItem() {
        if (item == null) {
            throw new IllegalStateException("Item is not set");
        }
        return item;
    }

    public Date getTransaction_date() {
        if (transaction_date == null) {
            throw new IllegalStateException("Transaction date is not set");
        }
        return new Date(transaction_date.getTime()); // Return defensive copy
    }

    public Date getShipping_date() {
        return shipping_date != null ? new Date(shipping_date.getTime()) : null; // Return defensive copy
    }

    public Review getBuyer_review() {
        return buyer_review;
    }

    public Date getDelivery_date() {
        return delivery_date != null ? new Date(delivery_date.getTime()) : null; // Return defensive copy
    }

    public Review getSeller_review() {
        return seller_review;
    }

    public void completePayment(PaymentMethod method) {
        try {
            if (method == null) {
                throw new IllegalArgumentException("Invalid payment method: cannot be null");
            }

            if (payment_status == PaymentStatus.COMPLETED) {
                throw new IllegalStateException("Payment already completed");
            }

            if (payment_status == PaymentStatus.FAILED) {
                throw new IllegalStateException("Cannot complete a failed payment. Please retry payment");
            }

            // Validate item is still available (in case it was sold elsewhere)
            if (!item.isAvailable()) {
                throw new IllegalStateException("Item is no longer available for purchase");
            }

            this.payment_method = method;
            this.payment_status = PaymentStatus.COMPLETED;

            // Deduct credits if used
            if (credits_used > 0) {
                boolean creditsDeducted = buyer.useCreditPoints(credits_used);
                if (!creditsDeducted) {
                    throw new IllegalStateException("Failed to deduct credit points");
                }
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to complete payment: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to complete payment: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error completing payment: " + e.getMessage());
        }
    }

    public void updateShippingStatus(ShippingStatus status) {
        try {
            if (status == null) {
                throw new IllegalArgumentException("Shipping status cannot be null");
            }

            if (payment_status != PaymentStatus.COMPLETED) {
                throw new IllegalStateException("Cannot update shipping before payment completion");
            }

            // Use the setter to maintain consistency
            setShipping_status(status);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to update shipping status: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to update shipping status: " + e.getMessage());
        }
    }

    public void confirmDelivery() {
        try {
            if (shipping_status != ShippingStatus.SHIPPED) {
                throw new IllegalStateException("Cannot confirm delivery before shipping");
            }

            if (delivery_date != null) {
                throw new IllegalStateException("Delivery already confirmed");
            }

            setShipping_status(ShippingStatus.DELIVERED);

        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to confirm delivery: " + e.getMessage());
        }
    }

    public void addBuyerReview(Review review) {
        try {
            if (!canSubmitReview(buyer)) {
                throw new IllegalStateException("Buyer cannot submit review at this time");
            }

            if (review == null) {
                throw new IllegalArgumentException("Review cannot be null");
            }

            // Validate review is for the seller
            if (!review.getReviewed_user().equals(seller)) {
                throw new IllegalStateException("Buyer review must be for the seller");
            }

            // Validate review is by the buyer
            if (!review.getReviewer_user().equals(buyer)) {
                throw new IllegalStateException("Review must be submitted by the buyer");
            }

            // Validate transaction matches
            if (!review.getTransaction().equals(this)) {
                throw new IllegalStateException("Review transaction must match this transaction");
            }

            this.buyer_review = review;
            checkReviewsCompleted();

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add buyer review: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to add buyer review: " + e.getMessage());
        }
    }

    public void addSellerReview(Review review) {
        try {
            if (!canSubmitReview(seller)) {
                throw new IllegalStateException("Seller cannot submit review at this time");
            }

            if (review == null) {
                throw new IllegalArgumentException("Review cannot be null");
            }

            // Validate review is for the buyer
            if (!review.getReviewed_user().equals(buyer)) {
                throw new IllegalStateException("Seller review must be for the buyer");
            }

            // Validate review is by the seller
            if (!review.getReviewer_user().equals(seller)) {
                throw new IllegalStateException("Review must be submitted by the seller");
            }

            // Validate transaction matches
            if (!review.getTransaction().equals(this)) {
                throw new IllegalStateException("Review transaction must match this transaction");
            }

            this.seller_review = review;
            checkReviewsCompleted();

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add seller review: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to add seller review: " + e.getMessage());
        }
    }

    private void checkReviewsCompleted() {
        try {
            if (buyer_review != null && seller_review != null) {
                this.reviews_completed = true;

                // Add credit points to both users for completing reviews
                buyer.addCreditPoints(10); // Reward for completing review
                seller.addCreditPoints(10); // Reward for completing review
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check reviews completion: " + e.getMessage());
        }
    }

    public boolean canSubmitReview(User user) {
        try {
            if (user == null) {
                return false;
            }

            if (shipping_status != ShippingStatus.DELIVERED) {
                return false;
            }

            // Check time limit (reviews within 30 days of delivery)
            if (delivery_date != null) {
                long daysSinceDelivery = TimeUnit.DAYS.convert(
                        new Date().getTime() - delivery_date.getTime(),
                        TimeUnit.MILLISECONDS
                );
                if (daysSinceDelivery > 30) {
                    return false;
                }
            }

            if (user.equals(buyer) && buyer_review == null) {
                return true;
            }
            if (user.equals(seller) && seller_review == null) {
                return true;
            }
            return false;

        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if user can submit review: " + e.getMessage());
        }
    }

    public Map<String, String> getTransactionStatus() {
        try {
            Map<String, String> status = new HashMap<>();

            String transId = getTransaction_id();
            PaymentStatus payStatus = getPayment_status();
            ShippingStatus shipStatus = getShipping_status();
            PaymentMethod payMethod = getPayment_method();
            int credits = getCredits_used();

            status.put("Transaction ID", transId);
            status.put("Payment Status", payStatus.name());
            status.put("Shipping Status", shipStatus.name());
            status.put("Payment Method", payMethod.name());
            status.put("Reviews Completed", reviews_completed ? "Yes" : "No");
            status.put("Credits Used", String.valueOf(credits));
            status.put("Total Amount", String.format("Rs. %.2f", calculateTotal()));
            status.put("Is Complete", isComplete() ? "Yes" : "No");
            status.put("Days Since Transaction", String.valueOf(getDaysSinceTransaction()));

            return status;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get transaction status: " + e.getMessage());
        }
    }

    public float calculateTotal() {
        try {
            float total = getItem().getPrice();
            if (credits_used > 0) {
                float discount = credits_used * 10.0f; // Each credit worth Rs. 10
                total = Math.max(0, total - discount);
            }

            if (Float.isNaN(total) || Float.isInfinite(total)) {
                throw new ArithmeticException("Invalid total calculation");
            }

            return total;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to calculate total: " + e.getMessage());
        }
    }

    public boolean isComplete() {
        try {
            return payment_status == PaymentStatus.COMPLETED &&
                    shipping_status == ShippingStatus.DELIVERED &&
                    reviews_completed;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if transaction is complete: " + e.getMessage());
        }
    }

    public Map<String, Review> getReviews() {
        try {
            Map<String, Review> reviews = new HashMap<>();
            if (buyer_review != null) {
                reviews.put("buyer", buyer_review);
            }
            if (seller_review != null) {
                reviews.put("seller", seller_review);
            }
            return reviews;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get reviews: " + e.getMessage());
        }
    }

    public long getDaysSinceTransaction() {
        try {
            Date transDate = getTransaction_date();
            long diff = new Date().getTime() - transDate.getTime();
            if (diff < 0) {
                throw new IllegalStateException("Transaction date cannot be in the future");
            }
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to calculate days since transaction: " + e.getMessage());
        }
    }

    public boolean isDelivered() {
        try {
            return shipping_status == ShippingStatus.DELIVERED;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if delivered: " + e.getMessage());
        }
    }

    public boolean isPaid() {
        try {
            return payment_status == PaymentStatus.COMPLETED;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check if paid: " + e.getMessage());
        }
    }

    public void applyCredits(int credits) {
        try {
            if (credits < 0) {
                throw new IllegalArgumentException("Credits cannot be negative");
            }

            if (payment_status == PaymentStatus.COMPLETED) {
                throw new IllegalStateException("Cannot apply credits after payment is completed");
            }

            if (credits > buyer.getCredit_points()) {
                throw new IllegalArgumentException("Buyer does not have enough credit points");
            }

            float maxDiscount = getItem().getPrice();
            float requestedDiscount = credits * 10.0f;

            if (requestedDiscount > maxDiscount) {
                // Calculate maximum credits that can be used
                credits = (int) Math.floor(maxDiscount / 10.0f);
            }

            this.credits_used = credits;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to apply credits: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to apply credits: " + e.getMessage());
        }
    }

    public long getDeliveryTime() {
        try {
            if (shipping_date != null && delivery_date != null) {
                long diff = delivery_date.getTime() - shipping_date.getTime();
                if (diff < 0) {
                    throw new IllegalStateException("Delivery date cannot be before shipping date");
                }
                return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            }
            return -1;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to calculate delivery time: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return " ID: " + getTransaction_id() +
                " Date: " + getTransaction_date() +
                " Item: " + getItem().getTitle() +
                " Payment method: " + getPayment_method() +
                " Payment status: " + getPayment_status() +
                " Shipping status: " + getShipping_status() +
                " Credits used: " + getCredits_used() +
                " Shipping date: " + getShipping_date() +
                " delivery date: " + getDelivery_date();
    }
}