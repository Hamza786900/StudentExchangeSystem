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
        try {
            this.items = new ArrayList<>();
            this.categories = new HashMap<>();
            this.updated_date = new Date();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Catalog: " + e.getMessage());
        }
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public List<Item> getItemsBySeller(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }
            List<Item> userItems = new ArrayList<>();
            for (Item item : items) {
                try {
                    if (item.getUploader().equals(user)) {
                        userItems.add(item);
                    }
                } catch (Exception e) {
                    System.err.println("Error checking item uploader: " + e.getMessage());
                }
            }
            return userItems;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get items by seller: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting items by seller: " + e.getMessage());
        }
    }

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
            if (item.getTitle() == null || item.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Item title cannot be null or empty");
            }
            if (item.getUploader() == null) {
                throw new IllegalArgumentException("Item uploader cannot be null");
            }
            this.items.add(item);
            updateCategories();
            this.updated_date = new Date();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add item: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error adding item: " + e.getMessage());
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
                try {
                    if (item.matchesSearch(trimmedKeyword)) {
                        results.add(item);
                    }
                } catch (Exception e) {
                    System.err.println("Error searching item: " + e.getMessage());
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
            if (minPrice != null && minPrice < 0) {
                throw new IllegalArgumentException("Minimum price cannot be negative");
            }
            if (maxPrice != null && maxPrice < 0) {
                throw new IllegalArgumentException("Maximum price cannot be negative");
            }
            if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
                throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
            }
            for (Item item : items) {
                try {
                    boolean matches = true;
                    if (category != null && item.getCategory() != category) {
                        matches = false;
                    }
                    if (grade != null && item.getGrade() != grade) {
                        matches = false;
                    }
                    if (subject != null && !subject.trim().isEmpty()) {
                        String itemSubject = item.getSubject();
                        if (itemSubject == null || !itemSubject.toLowerCase().contains(subject.toLowerCase().trim())) {
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
                    } else {
                        if (minPrice != null || maxPrice != null || condition != null) {
                            matches = false;
                        }
                    }
                    if (matches) {
                        filtered.add(item);
                    }
                } catch (Exception e) {
                    System.err.println("Error filtering item: " + e.getMessage());
                }
            }
            return filtered;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to filter items: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error filtering items: " + e.getMessage());
        }
    }

    public void updateCategories() {
        try {
            categories.clear();
            for (Item item : items) {
                try {
                    Category cat = item.getCategory();
                    if (cat != null) {
                        categories.put(cat, categories.getOrDefault(cat, 0) + 1);
                    }
                } catch (Exception e) {
                    System.err.println("Error updating category for item: " + e.getMessage());
                }
            }
            this.updated_date = new Date();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update categories: " + e.getMessage());
        }
    }

    public Date getUpdated_date() {
        if (updated_date == null) {
            throw new IllegalStateException("Updated date is not set");
        }
        return new Date(updated_date.getTime());
    }
}