package com.studentexchange.models;

import com.studentexchange.enums.Category;
import com.studentexchange.enums.Condition;
import com.studentexchange.enums.GradeLevel;
import java.util.*;

public class Catalog {
    private List<Item> items;
    private Map<Category, Integer> categories;
    private Date updated_date;

    public Catalog() {
        this.items = new ArrayList<>();
        this.categories = new HashMap<>();
        this.updated_date = new Date();
    }

    public List<Item> getItems() { return new ArrayList<>(items); }

    public void addItem(Item item) {
        try {
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            String itemId = item.getItem_id();
            for (Item existingItem : items) {
                if (existingItem.getItem_id().equals(itemId)) {
                    throw new IllegalArgumentException("Item with ID " + itemId + " already exists in catalog");
                }
            }
            this.items.add(item);
            updateCategories();
            this.updated_date = new Date();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add item: " + e.getMessage());
        }
    }

    public boolean removeItem(String itemId) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                throw new IllegalArgumentException("Item ID cannot be null or empty");
            }
            boolean removed = items.removeIf(item -> item.getItem_id().equals(itemId.trim()));
            if (removed) {
                updateCategories();
                this.updated_date = new Date();
            }
            return removed;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to remove item: " + e.getMessage());
        }
    }

    public List<Item> search(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return new ArrayList<>();
            }
            String trimmedKeyword = keyword.trim().toLowerCase();
            List<Item> results = new ArrayList<>();
            for (Item item : items) {
                if (item.matchesSearch(trimmedKeyword)) {
                    results.add(item);
                }
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search catalog: " + e.getMessage());
        }
    }

    public List<Item> filterItems(Category category, GradeLevel grade, Float minPrice, Float maxPrice, String subject, Condition condition) {
        try {
            List<Item> filtered = new ArrayList<>();
            for (Item item : items) {
                boolean matches = true;
                if (category != null && item.getCategory() != category) {
                    matches = false;
                }
                if (grade != null && item.getGrade() != grade) {
                    matches = false;
                }
                if (subject != null && !subject.trim().isEmpty()) {
                    if (item.getSubject() == null || !item.getSubject().toLowerCase().contains(subject.toLowerCase().trim())) {
                        matches = false;
                    }
                }
                if (item instanceof ForSaleItem) {
                    ForSaleItem forSale = (ForSaleItem) item;
                    if (minPrice != null && forSale.getPrice() < minPrice) {
                        matches = false;
                    }
                    if (maxPrice != null && forSale.getPrice() > maxPrice) {
                        matches = false;
                    }
                    if (condition != null && forSale.getCondition() != condition) {
                        matches = false;
                    }
                } else if (minPrice != null || maxPrice != null || condition != null) {
                    matches = false;
                }

                if (matches) {
                    filtered.add(item);
                }
            }
            return filtered;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error filtering items: " + e.getMessage());
        }
    }

    public Item getItemById(String itemId) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                throw new IllegalArgumentException("Item ID cannot be null or empty");
            }
            String trimmedId = itemId.trim();
            for (Item item : items) {
                if (item.getItem_id().equals(trimmedId)) {
                    return item;
                }
            }
            return null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get item by ID: " + e.getMessage());
        }
    }

    public List<Item> getItemsBySeller(User user) {
        List<Item> userItems = new ArrayList<>();
        if (user == null) return userItems;
        for (Item item : items) {
            if (item.getUploader().equals(user)) {
                userItems.add(item);
            }
        }
        return userItems;
    }

    public void updateCategories() {
        categories.clear();
        for (Item item : items) {
            Category cat = item.getCategory();
            if (cat != null) {
                categories.put(cat, categories.getOrDefault(cat, 0) + 1);
            }
        }
        this.updated_date = new Date();
    }
}