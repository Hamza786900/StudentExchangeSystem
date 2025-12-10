package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ForSaleItem extends Item {
    private float price;
    private Condition condition;
    private float market_price;
    private boolean is_sold;
    private Date sale_date;
    private User buyer;
    private float discount_percentage;

    public ForSaleItem(String title, User uploader, String description, Category category, GradeLevel grade, String subject, Condition condition, float market_price, float price) {
        super(title, uploader, description, category, grade, subject);
        try {
            // Validate parameters
            if (condition == null) {
                throw new IllegalArgumentException("Condition cannot be null");
            }
            if (market_price < 0) {
                throw new IllegalArgumentException("Market price cannot be negative");
            }
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            if (price > market_price * 2) {
                throw new IllegalArgumentException("Price cannot be more than double the market price");
            }

            this.price = price;
            this.condition = condition;
            this.market_price = market_price;
            this.is_sold = false;
            this.sale_date = null;
            this.buyer = null;
            this.discount_percentage = calculateDiscount();

            // Validate calculated discount
            if (Float.isNaN(discount_percentage) || Float.isInfinite(discount_percentage)) {
                throw new IllegalStateException("Invalid discount percentage calculated");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create ForSaleItem: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to create ForSaleItem: " + e.getMessage());
        }
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        try {
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            if (is_sold) {
                throw new IllegalStateException("Cannot change price of a sold item");
            }
            this.price = price;
            this.discount_percentage = calculateDiscount();

            // Validate calculated discount
            if (Float.isNaN(discount_percentage) || Float.isInfinite(discount_percentage)) {
                throw new ArithmeticException("Invalid discount percentage calculated after price change");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set price: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set price: " + e.getMessage());
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Failed to set price: " + e.getMessage());
        }
    }

    public Condition getCondition() {
        if (condition == null) {
            throw new IllegalStateException("Condition is not set for this item");
        }
        return condition;
    }

    public void setCondition(Condition condition) {
        try {
            if (condition == null) {
                throw new IllegalArgumentException("Condition cannot be null");
            }
            if (is_sold) {
                throw new IllegalStateException("Cannot change condition of a sold item");
            }
            this.condition = condition;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set condition: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set condition: " + e.getMessage());
        }
    }

    public float getMarket_price() {
        return market_price;
    }

    public void setMarket_price(float market_price) {
        try {
            if (market_price < 0) {
                throw new IllegalArgumentException("Market price cannot be negative");
            }
            if (is_sold) {
                throw new IllegalStateException("Cannot change market price of a sold item");
            }
            this.market_price = market_price;
            this.discount_percentage = calculateDiscount();

            // Validate calculated discount
            if (Float.isNaN(discount_percentage) || Float.isInfinite(discount_percentage)) {
                throw new ArithmeticException("Invalid discount percentage calculated after market price change");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set market price: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to set market price: " + e.getMessage());
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Failed to set market price: " + e.getMessage());
        }
    }

    public boolean isIs_sold() {
        return is_sold;
    }

    public void setIs_sold(boolean is_sold) {
        // This method is kept simple as it's usually controlled by markAsSold
        this.is_sold = is_sold;
    }

    public float getDiscount_percentage() {
        if (Float.isNaN(discount_percentage) || Float.isInfinite(discount_percentage)) {
            throw new IllegalStateException("Discount percentage is not a valid number");
        }
        return discount_percentage;
    }

    public void setDiscount_percentage(float discount_percentage) {
        try {
            if (discount_percentage < 0 || discount_percentage > 100) {
                throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
            }
            if (Float.isNaN(discount_percentage) || Float.isInfinite(discount_percentage)) {
                throw new IllegalArgumentException("Discount percentage must be a valid number");
            }
            this.discount_percentage = discount_percentage;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to set discount percentage: " + e.getMessage());
        }
    }

    public Date getSale_date() {
        if (sale_date == null && is_sold) {
            throw new IllegalStateException("Sale date is not set for sold item");
        }
        if (sale_date != null) {
            return new Date(sale_date.getTime()); // Return defensive copy
        }
        return null;
    }

    public User getBuyer() {
        if (buyer == null && is_sold) {
            throw new IllegalStateException("Buyer is not set for sold item");
        }
        return buyer;
    }

    @Override
    public String getDetails() {
        try {
            String itemId = getItem_id();
            String title = getTitle();
            float price = getPrice();
            Condition condition = getCondition();
            boolean isSold = isIs_sold();

            if (itemId == null) {
                throw new IllegalStateException("Item ID is null");
            }
            if (title == null) {
                throw new IllegalStateException("Title is null");
            }
            if (condition == null) {
                throw new IllegalStateException("Condition is null");
            }

            return "Item ID: " + itemId +
                    " Name: " + title +
                    " Price: " + price +
                    " Condition: " + condition +
                    " Sold: " + isSold;
        } catch (IllegalStateException e) {
            return "Error getting item details: " + e.getMessage();
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            return !isIs_sold();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine availability: " + e.getMessage());
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
            return title.toLowerCase().contains(lowerKeyword) ||
                    description.toLowerCase().contains(lowerKeyword) ||
                    subject.toLowerCase().contains(lowerKeyword);
        } catch (NullPointerException e) {
            throw new NullPointerException("Null reference encountered while matching search");
        } catch (Exception e) {
            throw new RuntimeException("Error while matching search: " + e.getMessage());
        }
    }

    public float calculateDiscount() {
        try {
            if (market_price > 0) {
                if (market_price == price) {
                    return 0.0f;
                }
                float discount = ((market_price - price) / market_price) * 100;

                // Validate the calculated discount
                if (Float.isNaN(discount) || Float.isInfinite(discount)) {
                    throw new ArithmeticException("Invalid discount calculation: market_price=" + market_price + ", price=" + price);
                }
                if (discount < -1000 || discount > 1000) {
                    throw new ArithmeticException("Discount calculation resulted in unreasonable value: " + discount + "%");
                }
                return discount;
            }
            return 0.0f;
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Failed to calculate discount: " + e.getMessage());
        }
    }

    public void markAsSold(User buyer, Date saleDate) {
        try {
            if (buyer == null) {
                throw new IllegalArgumentException("Buyer cannot be null");
            }
            if (saleDate == null) {
                throw new IllegalArgumentException("Sale date cannot be null");
            }
            if (saleDate.after(new Date())) {
                throw new IllegalArgumentException("Sale date cannot be in the future");
            }
            if (is_sold) {
                throw new IllegalStateException("Item is already sold");
            }
            if (!validatePrice()) {
                throw new IllegalStateException("Cannot mark item as sold with invalid price");
            }

            this.is_sold = true;
            this.buyer = buyer;
            this.sale_date = new Date(saleDate.getTime()); // Store defensive copy
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to mark item as sold: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to mark item as sold: " + e.getMessage());
        }
    }

    public boolean validatePrice() {
        try {
            if (price <= 0) {
                return false;
            }
            if (Float.isNaN(price) || Float.isInfinite(price)) {
                return false;
            }
            if (Float.isNaN(market_price) || Float.isInfinite(market_price)) {
                return false;
            }
            return price <= market_price && price > 0;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to validate price: " + e.getMessage());
        }
    }

    public Map<String, String> getPriceDetails() {
        try {
            Map<String, String> details = new HashMap<>();

            float currentPrice = getPrice();
            float currentMarketPrice = getMarket_price();
            float currentDiscount = getDiscount_percentage();
            float savings = calculateSavings();

            details.put("Price", String.format("%.2f", currentPrice));
            details.put("Market Price", String.format("%.2f", currentMarketPrice));
            details.put("Discount", String.format("%.2f%%", currentDiscount));
            details.put("Savings", String.format("%.2f", savings));
            details.put("Is Valid Price", String.valueOf(validatePrice()));

            return details;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get price details: " + e.getMessage());
        }
    }

    public boolean isPriceValid() {
        try {
            return validatePrice();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to check price validity: " + e.getMessage());
        }
    }

    public String getConditionDescription() {
        try {
            Condition currentCondition = getCondition();
            if (currentCondition == null) {
                return "Unknown";
            }
            switch (currentCondition) {
                case NEW:
                    return "Brand New";
                case GOOD:
                    return "Good Condition";
                case FAIR:
                    return "Fair Condition";
                case POOR:
                    return "Poor Condition";
                default:
                    return "Unknown";
            }
        } catch (IllegalStateException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error determining condition";
        }
    }

    public float calculateSavings() {
        try {
            if (market_price > price) {
                float savings = market_price - price;
                if (Float.isNaN(savings) || Float.isInfinite(savings)) {
                    throw new ArithmeticException("Invalid savings calculation");
                }
                if (savings < 0) {
                    throw new ArithmeticException("Negative savings calculated");
                }
                return savings;
            }
            return 0.0f;
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Failed to calculate savings: " + e.getMessage());
        }
    }

    public boolean canBePurchased() {
        try {
            if (!isAvailable()) {
                return false;
            }
            if (!validatePrice()) {
                return false;
            }
            if (getUploader() == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to determine if item can be purchased: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                " Price: " + getPrice() +
                " Condition: " + getCondition() +
                " Discount: " + getDiscount_percentage() + "%" +
                " Date: " + getSale_date() +
                " Sold: " + isIs_sold();
    }
}