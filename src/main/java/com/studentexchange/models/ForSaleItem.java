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
            this.price = price;
            this.condition = condition;
            this.market_price = market_price;
            this.is_sold = false;
            this.sale_date = null;
            this.buyer = null;
            this.discount_percentage = calculateDiscount();
        } catch (Exception e) {
            System.out.println("Error creating ForSaleItem: " + e.getMessage());
        }
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        try {
            this.price = price;
            this.discount_percentage = calculateDiscount();
        } catch (Exception e) {
            System.out.println("Error setting price: " + e.getMessage());
        }
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        try {
            this.condition = condition;
        } catch (Exception e) {
            System.out.println("Error setting condition: " + e.getMessage());
        }
    }

    public float getMarket_price() {
        return market_price;
    }

    public void setMarket_price(float market_price) {
        try {
            this.market_price = market_price;
            this.discount_percentage = calculateDiscount();
        } catch (Exception e) {
            System.out.println("Error setting market price: " + e.getMessage());
        }
    }

    public boolean isIs_sold() {
        return is_sold;
    }

    public void setIs_sold(boolean is_sold) {
        try {
            this.is_sold = is_sold;
        } catch (Exception e) {
            System.out.println("Error setting sold status: " + e.getMessage());
        }
    }

    public float getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(float discount_percentage) {
        try {
            this.discount_percentage = discount_percentage;
        } catch (Exception e) {
            System.out.println("Error setting discount percentage: " + e.getMessage());
        }
    }

    public Date getSale_date() {
        return sale_date;
    }

    public User getBuyer() {
        return buyer;
    }

    @Override
    public String getDetails() {
        try {
            return "Item ID: " + getItem_id() + " Name: " + getTitle() + " Price: " + getPrice() + " Condition: " + getCondition() + " Sold: " + isIs_sold();
        } catch (Exception e) {
            return "Error getting item details";
        }
    }

    @Override
    public boolean isAvailable() {
        try {
            return !isIs_sold();
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

            return title.toLowerCase().contains(lowerKeyword) ||
                    desc.toLowerCase().contains(lowerKeyword) ||
                    subject.toLowerCase().contains(lowerKeyword);
        } catch (Exception e) {
            System.out.println("Error matching search: " + e.getMessage());
            return false;
        }
    }

    public float calculateDiscount() {
        try {
            if (market_price > 0) {
                return ((market_price - price) / market_price) * 100;
            }
        } catch (Exception e) {
            System.out.println("Error calculating discount: " + e.getMessage());
        }
        return 0.0f;
    }

    public void markAsSold(User buyer, Date saleDate) {
        try {
            this.is_sold = true;
            this.buyer = buyer;
            this.sale_date = saleDate;
        } catch (Exception e) {
            System.out.println("Error marking item as sold: " + e.getMessage());
        }
    }

    public boolean validatePrice() {
        try {
            return price <= market_price && price > 0;
        } catch (Exception e) {
            System.out.println("Error validating price: " + e.getMessage());
            return false;
        }
    }

    public Map<String, String> getPriceDetails() {
        Map<String, String> details = new HashMap<>();
        try {
            details.put("Price", String.valueOf(price));
            details.put("Market Price", String.valueOf(market_price));
            details.put("Discount", String.format("%.2f%%", discount_percentage));
            details.put("Savings", String.valueOf(calculateSavings()));
        } catch (Exception e) {
            System.out.println("Error getting price details: " + e.getMessage());
        }
        return details;
    }

    public boolean isPriceValid() {
        try {
            return validatePrice();
        } catch (Exception e) {
            return false;
        }
    }

    public String getConditionDescription() {
        try {
            if (condition == null) {
                return "Unknown";
            }
            switch (condition) {
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
        } catch (Exception e) {
            return "Unknown";
        }
    }

    public float calculateSavings() {
        try {
            if (market_price > price) {
                return market_price - price;
            }
        } catch (Exception e) {
            System.out.println("Error calculating savings: " + e.getMessage());
        }
        return 0.0f;
    }

    public boolean canBePurchased() {
        try {
            return isAvailable() && validatePrice();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        try {
            return super.toString() + " Price: " + getPrice() + " Condition: " + getCondition() + " Discount: " + getDiscount_percentage() + " Date: " + getSale_date() + " Sold: " + isIs_sold();
        } catch (Exception e) {
            return "Error displaying ForSaleItem";
        }
    }
}
