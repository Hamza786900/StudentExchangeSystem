package com.studentexchange.models;

import com.studentexchange.enums.PaymentMethod;
import com.studentexchange.enums.PaymentStatus;
import com.studentexchange.enums.ShippingStatus;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
            if (buyer.equals(seller)) {
                throw new IllegalArgumentException("Buyer and seller cannot be the same user");
            }
            if (!item.isAvailable()) {
                throw new IllegalArgumentException("Item is not available for sale");
            }
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
            item.markAsSold(buyer, new Date());
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

    public PaymentStatus getPayment_status() {
        if (payment_status == null) {
            throw new IllegalStateException("Payment status is not set");
        }
        return payment_status;
    }

    public ShippingStatus getShipping_status() {
        if (shipping_status == null) {
            throw new IllegalStateException("Shipping status is not set");
        }
        return shipping_status;
    }

    public int getCredits_used() {
        if (credits_used < 0) {
            throw new IllegalStateException("Credits used is in invalid state (negative)");
        }
        return credits_used;
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
        return new Date(transaction_date.getTime());
    }

    public List<Transaction> getTransactionsAsBuyer() {
        // Placeholder for consistency, actual list managed in User
        return new java.util.ArrayList<>();
    }

    public List<Transaction> getTransactionsAsSeller() {
        // Placeholder for consistency, actual list managed in User
        return new java.util.ArrayList<>();
    }


    public Map<String, String> getTransactionStatus() {
        try {
            Map<String, String> status = new HashMap<>();
            String transId = getTransaction_id();
            PaymentStatus payStatus = getPayment_status();
            ShippingStatus shipStatus = getShipping_status();
            PaymentMethod payMethod = getPayment_method();
            int credits = getCredits_used();
            long daysSinceTransaction = getDaysSinceTransaction();

            status.put("Transaction ID", transId);
            status.put("Payment Status", payStatus.name());
            status.put("Shipping Status", shipStatus.name());
            status.put("Payment Method", payMethod.name());
            status.put("Reviews Completed", reviews_completed ? "Yes" : "No");
            status.put("Credits Used", String.valueOf(credits));
            status.put("Total Amount", String.format("Rs. %.2f", calculateTotal()));
            status.put("Is Complete", isComplete() ? "Yes" : "No");
            status.put("Days Since Transaction", String.valueOf(daysSinceTransaction));
            return status;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get transaction status: " + e.getMessage());
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

    public float calculateTotal() {
        try {
            float total = getItem().getPrice();
            if (credits_used > 0) {
                float discount = credits_used * 10.0f;
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
                credits = (int) Math.floor(maxDiscount / 10.0f);
            }
            this.credits_used = credits;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to apply credits: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to apply credits: " + e.getMessage());
        }
    }

    // Minimal setter methods retained for internal logic
    public void updateShippingStatus(ShippingStatus status) {
        try {
            if (status == null) {
                throw new IllegalArgumentException("Shipping status cannot be null");
            }
            if (payment_status != PaymentStatus.COMPLETED &&
                    status != ShippingStatus.NOT_SHIPPED) {
                throw new IllegalStateException("Cannot update shipping status before payment completion");
            }
            if (status == ShippingStatus.SHIPPED &&
                    this.shipping_status == ShippingStatus.DELIVERED) {
                throw new IllegalStateException("Cannot revert from DELIVERED to SHIPPED");
            }
            if (status == ShippingStatus.DELIVERED &&
                    this.shipping_status == ShippingStatus.NOT_SHIPPED) {
                throw new IllegalStateException("Cannot mark as DELIVERED without being SHIPPED first");
            }
            this.shipping_status = status;
            if (shipping_status == ShippingStatus.SHIPPED && shipping_date == null) {
                this.shipping_date = new Date();
            }
            if (shipping_status == ShippingStatus.DELIVERED && delivery_date == null) {
                this.delivery_date = new Date();
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set shipping status: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set shipping status: " + e.getMessage());
        }
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
            if (!item.isAvailable()) {
                throw new IllegalStateException("Item is no longer available for purchase");
            }
            this.payment_method = method;
            this.payment_status = PaymentStatus.COMPLETED;
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


    @Override
    public String toString() {
        try {
            String transId = getTransaction_id();
            Date transDate = getTransaction_date();
            String itemTitle = getItem().getTitle();
            PaymentMethod payMethod = getPayment_method();
            PaymentStatus payStatus = getPayment_status();
            ShippingStatus shipStatus = getShipping_status();
            int credits = getCredits_used();
            Date shipDate = shipping_date;
            Date delDate = delivery_date;
            return String.format(
                    "Transaction ID: %s | Date: %s | Item: %s | " +
                            "Payment Method: %s | Payment Status: %s | " +
                            "Shipping Status: %s | Credits Used: %d | " +
                            "Shipping Date: %s | Delivery Date: %s",
                    transId,
                    transDate,
                    itemTitle,
                    payMethod != null ? payMethod.name() : "Unknown",
                    payStatus != null ? payStatus.name() : "Unknown",
                    shipStatus != null ? shipStatus.name() : "Unknown",
                    credits,
                    shipDate != null ? shipDate : "Not shipped",
                    delDate != null ? delDate : "Not delivered"
            );
        } catch (Exception e) {
            return "Transaction [Error in toString(): " + e.getMessage() + "]";
        }
    }
}