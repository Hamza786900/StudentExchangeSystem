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
        // Return defensive copy
        return new ArrayList<>(items);
    }

    public Map<Category, Integer> getCategories() {
        // Return defensive copy
        return new HashMap<>(categories);
    }

    public Date getUpdated_date() {
        if (updated_date == null) {
            throw new IllegalStateException("Updated date is not set");
        }
        return new Date(updated_date.getTime()); // Return defensive copy
    }

    public void addItem(Item item) {
        try {
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }

            // Check for duplicate item ID
            String itemId = item.getItem_id();
            for (Item existingItem : items) {
                if (existingItem.getItem_id().equals(itemId)) {
                    throw new IllegalArgumentException("Item with ID " + itemId + " already exists in catalog");
                }
            }

            // Validate item has required fields
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

    public void addAllItems(Collection<Item> itemsToAdd) {
        try {
            if (itemsToAdd == null) {
                throw new IllegalArgumentException("Items collection cannot be null");
            }

            int addedCount = 0;
            for (Item item : itemsToAdd) {
                try {
                    addItem(item);
                    addedCount++;
                } catch (Exception e) {
                    System.err.println("Failed to add item: " + e.getMessage());
                }
            }

            if (addedCount == 0 && !itemsToAdd.isEmpty()) {
                throw new IllegalStateException("Failed to add any items from the collection");
            }

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add items: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to add items: " + e.getMessage());
        }
    }

    public boolean removeItem(String itemId) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                throw new IllegalArgumentException("Item ID cannot be null or empty");
            }

            String trimmedId = itemId.trim();
            boolean removed = false;
            Iterator<Item> iterator = items.iterator();

            while (iterator.hasNext()) {
                Item item = iterator.next();
                try {
                    if (item.getItem_id().equals(trimmedId)) {
                        iterator.remove();
                        removed = true;
                        break;
                    }
                } catch (Exception e) {
                    // Skip items with invalid IDs
                    System.err.println("Error accessing item ID: " + e.getMessage());
                }
            }

            if (removed) {
                updateCategories();
                this.updated_date = new Date();
            }

            return removed;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to remove item: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error removing item: " + e.getMessage());
        }
    }

    public boolean removeItem(Item itemToRemove) {
        try {
            if (itemToRemove == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }

            boolean removed = items.remove(itemToRemove);

            if (removed) {
                updateCategories();
                this.updated_date = new Date();
            }

            return removed;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to remove item: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error removing item: " + e.getMessage());
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
                    // Skip items that fail search
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

            // Validate price range if provided
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

                    // Filter by category
                    if (category != null && item.getCategory() != category) {
                        matches = false;
                    }

                    // Filter by grade
                    if (grade != null && item.getGrade() != grade) {
                        matches = false;
                    }

                    // Filter by subject
                    if (subject != null && !subject.trim().isEmpty()) {
                        String itemSubject = item.getSubject();
                        if (itemSubject == null || !itemSubject.toLowerCase().contains(subject.toLowerCase().trim())) {
                            matches = false;
                        }
                    }

                    // Filter by price and condition for ForSaleItems
                    if (item instanceof ForSaleItem) {
                        ForSaleItem forSale = (ForSaleItem) item;

                        // Filter by price
                        if (minPrice != null && forSale.getPrice() < minPrice) {
                            matches = false;
                        }
                        if (maxPrice != null && forSale.getPrice() > maxPrice) {
                            matches = false;
                        }

                        // Filter by condition
                        if (condition != null && forSale.getCondition() != condition) {
                            matches = false;
                        }
                    } else {
                        // Free resources don't have price or condition
                        if (minPrice != null || maxPrice != null || condition != null) {
                            matches = false;
                        }
                    }

                    if (matches) {
                        filtered.add(item);
                    }

                } catch (Exception e) {
                    // Skip items that fail filtering
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

    public List<Item> sortItems(List<Item> itemsList, String sortBy) {
        try {
            if (itemsList == null) {
                throw new IllegalArgumentException("Items list cannot be null");
            }

            if (itemsList.isEmpty()) {
                return new ArrayList<>(itemsList);
            }

            if (sortBy == null || sortBy.trim().isEmpty()) {
                throw new IllegalArgumentException("Sort key cannot be null or empty");
            }

            List<Item> sorted = new ArrayList<>(itemsList);
            String sortKey = sortBy.toLowerCase().trim();

            switch (sortKey) {
                case "price_asc":
                    sorted.sort((a, b) -> {
                        try {
                            float priceA = (a instanceof ForSaleItem) ? ((ForSaleItem) a).getPrice() : 0;
                            float priceB = (b instanceof ForSaleItem) ? ((ForSaleItem) b).getPrice() : 0;
                            return Float.compare(priceA, priceB);
                        } catch (Exception e) {
                            return 0; // Maintain order if comparison fails
                        }
                    });
                    break;

                case "price_desc":
                    sorted.sort((a, b) -> {
                        try {
                            float priceA = (a instanceof ForSaleItem) ? ((ForSaleItem) a).getPrice() : 0;
                            float priceB = (b instanceof ForSaleItem) ? ((ForSaleItem) b).getPrice() : 0;
                            return Float.compare(priceB, priceA);
                        } catch (Exception e) {
                            return 0; // Maintain order if comparison fails
                        }
                    });
                    break;

                case "date":
                    sorted.sort((a, b) -> {
                        try {
                            return b.getUpload_date().compareTo(a.getUpload_date());
                        } catch (Exception e) {
                            return 0; // Maintain order if comparison fails
                        }
                    });
                    break;

                case "alphabetical":
                    sorted.sort((a, b) -> {
                        try {
                            return a.getTitle().compareToIgnoreCase(b.getTitle());
                        } catch (Exception e) {
                            return 0; // Maintain order if comparison fails
                        }
                    });
                    break;

                case "views":
                    sorted.sort((a, b) -> {
                        try {
                            return Integer.compare(b.getViews(), a.getViews());
                        } catch (Exception e) {
                            return 0; // Maintain order if comparison fails
                        }
                    });
                    break;

                default:
                    throw new IllegalArgumentException("Invalid sort key: " + sortBy +
                            ". Valid keys are: price_asc, price_desc, date, alphabetical, views");
            }

            return sorted;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to sort items: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error sorting items: " + e.getMessage());
        }
    }

    public List<Item> getAvailableItems() {
        try {
            List<Item> available = new ArrayList<>();
            for (Item item : items) {
                try {
                    if (item.isAvailable()) {
                        available.add(item);
                    }
                } catch (Exception e) {
                    // Skip items that fail availability check
                    System.err.println("Error checking item availability: " + e.getMessage());
                }
            }
            return available;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get available items: " + e.getMessage());
        }
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
                    // Skip items that fail user comparison
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

    public List<Item> getItemsByCategory(Category category) {
        try {
            if (category == null) {
                throw new IllegalArgumentException("Category cannot be null");
            }

            List<Item> categoryItems = new ArrayList<>();
            for (Item item : items) {
                try {
                    if (item.getCategory() == category) {
                        categoryItems.add(item);
                    }
                } catch (Exception e) {
                    // Skip items that fail category check
                    System.err.println("Error checking item category: " + e.getMessage());
                }
            }
            return categoryItems;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get items by category: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting items by category: " + e.getMessage());
        }
    }

    public List<Item> getItemsByGrade(GradeLevel grade) {
        try {
            if (grade == null) {
                throw new IllegalArgumentException("Grade cannot be null");
            }

            List<Item> gradeItems = new ArrayList<>();
            for (Item item : items) {
                try {
                    if (item.getGrade() == grade) {
                        gradeItems.add(item);
                    }
                } catch (Exception e) {
                    // Skip items that fail grade check
                    System.err.println("Error checking item grade: " + e.getMessage());
                }
            }
            return gradeItems;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get items by grade: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting items by grade: " + e.getMessage());
        }
    }

    public List<ForSaleItem> getForSaleItems() {
        try {
            List<ForSaleItem> forSale = new ArrayList<>();
            for (Item item : items) {
                try {
                    if (item instanceof ForSaleItem) {
                        forSale.add((ForSaleItem) item);
                    }
                } catch (Exception e) {
                    // Skip items that fail type check
                    System.err.println("Error checking item type: " + e.getMessage());
                }
            }
            return forSale;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get for-sale items: " + e.getMessage());
        }
    }

    public List<FreeResource> getFreeResources() {
        try {
            List<FreeResource> free = new ArrayList<>();
            for (Item item : items) {
                try {
                    if (item instanceof FreeResource) {
                        free.add((FreeResource) item);
                    }
                } catch (Exception e) {
                    // Skip items that fail type check
                    System.err.println("Error checking item type: " + e.getMessage());
                }
            }
            return free;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get free resources: " + e.getMessage());
        }
    }

    public Map<String, Object> getStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();

            int totalItems = items.size();
            int availableItems = getAvailableItems().size();
            int forSaleItems = getForSaleItems().size();
            int freeResources = getFreeResources().size();

            // Calculate average price
            float totalPrice = 0;
            int pricedItems = 0;
            for (Item item : items) {
                if (item instanceof ForSaleItem) {
                    try {
                        totalPrice += ((ForSaleItem) item).getPrice();
                        pricedItems++;
                    } catch (Exception e) {
                        // Skip items with price errors
                    }
                }
            }
            float averagePrice = pricedItems > 0 ? totalPrice / pricedItems : 0;

            stats.put("Total Items", totalItems);
            stats.put("Available Items", availableItems);
            stats.put("For Sale Items", forSaleItems);
            stats.put("Free Resources", freeResources);
            stats.put("Categories", new HashMap<>(categories)); // Defensive copy
            stats.put("Average Price", String.format("Rs. %.2f", averagePrice));
            stats.put("Last Updated", updated_date);

            return stats;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get catalog statistics: " + e.getMessage());
        }
    }

    public Item getItemById(String itemId) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                throw new IllegalArgumentException("Item ID cannot be null or empty");
            }

            String trimmedId = itemId.trim();
            for (Item item : items) {
                try {
                    if (item.getItem_id().equals(trimmedId)) {
                        return item;
                    }
                } catch (Exception e) {
                    // Skip items with ID access errors
                    System.err.println("Error accessing item ID: " + e.getMessage());
                }
            }

            return null;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get item by ID: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting item by ID: " + e.getMessage());
        }
    }

    public List<Item> getPopularItems(int count) {
        try {
            if (count <= 0) {
                throw new IllegalArgumentException("Count must be positive");
            }
            if (count > 1000) {
                throw new IllegalArgumentException("Count cannot exceed 1000");
            }

            List<Item> sorted = new ArrayList<>(items);
            sorted.sort((a, b) -> {
                try {
                    return Integer.compare(b.getViews(), a.getViews());
                } catch (Exception e) {
                    return 0; // Maintain order if comparison fails
                }
            });

            int returnCount = Math.min(count, sorted.size());
            return new ArrayList<>(sorted.subList(0, returnCount));

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get popular items: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting popular items: " + e.getMessage());
        }
    }

    public List<Item> getNewArrivals(int count) {
        try {
            if (count <= 0) {
                throw new IllegalArgumentException("Count must be positive");
            }
            if (count > 1000) {
                throw new IllegalArgumentException("Count cannot exceed 1000");
            }

            List<Item> sorted = new ArrayList<>(items);
            sorted.sort((a, b) -> {
                try {
                    return b.getUpload_date().compareTo(a.getUpload_date());
                } catch (Exception e) {
                    return 0; // Maintain order if comparison fails
                }
            });

            int returnCount = Math.min(count, sorted.size());
            return new ArrayList<>(sorted.subList(0, returnCount));

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get new arrivals: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting new arrivals: " + e.getMessage());
        }
    }

    public List<Item> getRecommendedItems(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            // Simple recommendation algorithm: mix of popular and new items
            List<Item> recommendations = new ArrayList<>();

            // Add popular items
            List<Item> popular = getPopularItems(5);
            recommendations.addAll(popular);

            // Add new arrivals
            List<Item> newArrivals = getNewArrivals(5);
            recommendations.addAll(newArrivals);

            // Remove duplicates
            Set<Item> uniqueItems = new LinkedHashSet<>(recommendations);
            return new ArrayList<>(uniqueItems);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get recommended items: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting recommended items: " + e.getMessage());
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
                    // Skip items with category errors
                    System.err.println("Error updating category for item: " + e.getMessage());
                }
            }
            this.updated_date = new Date();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update categories: " + e.getMessage());
        }
    }

    public void clearCatalog() {
        try {
            items.clear();
            categories.clear();
            this.updated_date = new Date();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear catalog: " + e.getMessage());
        }
    }

    public boolean containsItem(String itemId) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                return false;
            }

            String trimmedId = itemId.trim();
            for (Item item : items) {
                try {
                    if (item.getItem_id().equals(trimmedId)) {
                        return true;
                    }
                } catch (Exception e) {
                    // Skip items with ID access errors
                }
            }

            return false;

        } catch (Exception e) {
            throw new RuntimeException("Failed to check if catalog contains item: " + e.getMessage());
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        return String.format(
                "Catalog{Items: %d, Available: %d, Categories: %d, Last Updated: %s}",
                items.size(),
                getAvailableItems().size(),
                categories.size(),
                getUpdated_date()
        );
    }
}