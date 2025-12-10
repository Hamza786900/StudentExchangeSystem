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
            e.printStackTrace();
        }
    }

    public List<Item> getItems() {
        return items;
    }

    public Map<Category, Integer> getCategories() {
        return categories;
    }

    public Date getUpdated_date() {
        return updated_date;
    }

    public void addItem(Item item) {
        try {
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            this.items.add(item);
            updateCategories();
            this.updated_date = new Date();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean removeItem(String itemId) {
        try {
            if (itemId == null || itemId.isEmpty()) {
                throw new IllegalArgumentException("Item ID cannot be null or empty");
            }
            boolean removed = items.removeIf(item -> {
                try {
                    return item.getItem_id().equals(itemId);
                } catch (Exception ignored) {
                    return false;
                }
            });
            if (removed) {
                updateCategories();
                this.updated_date = new Date();
            }
            return removed;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Item> search(String keyword) {
        try {
            if (keyword == null || keyword.isEmpty()) {
                return new ArrayList<>();
            }
            List<Item> results = new ArrayList<>();
            for (Item item : items) {
                try {
                    if (item.matchesSearch(keyword)) {
                        results.add(item);
                    }
                } catch (Exception ignored) {}
            }
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Item> filterItems(Category category, GradeLevel grade, Double minPrice, Double maxPrice, String subject, Condition condition) {
        List<Item> filtered = new ArrayList<>();
        try {
            for (Item item : items) {
                boolean matches = true;
                try {
                    if (category != null && item.getCategory() != category) matches = false;
                    if (grade != null && item.getGrade() != grade) matches = false;
                    if (subject != null && !item.getSubject().equalsIgnoreCase(subject)) matches = false;

                    if (item instanceof ForSaleItem) {
                        ForSaleItem forSale = (ForSaleItem) item;
                        if (minPrice != null && forSale.getPrice() < minPrice) matches = false;
                        if (maxPrice != null && forSale.getPrice() > maxPrice) matches = false;
                        if (condition != null && forSale.getCondition() != condition) matches = false;
                    }
                } catch (Exception ignored) {}

                if (matches) filtered.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filtered;
    }

    public List<Item> sortItems(List<Item> itemsList, String sortBy) {
        try {
            if (itemsList == null || itemsList.isEmpty()) {
                return itemsList;
            }
            if (sortBy == null) {
                throw new IllegalArgumentException("Sort key cannot be null");
            }
            List<Item> sorted = new ArrayList<>(itemsList);
            switch (sortBy.toLowerCase()) {
                case "price_asc":
                    sorted.sort((a, b) -> {
                        float priceA = (a instanceof ForSaleItem) ? ((ForSaleItem) a).getPrice() : 0;
                        float priceB = (b instanceof ForSaleItem) ? ((ForSaleItem) b).getPrice() : 0;
                        return Float.compare(priceA, priceB);
                    });
                    break;
                case "price_desc":
                    sorted.sort((a, b) -> {
                        float priceA = (a instanceof ForSaleItem) ? ((ForSaleItem) a).getPrice() : 0;
                        float priceB = (b instanceof ForSaleItem) ? ((ForSaleItem) b).getPrice() : 0;
                        return Float.compare(priceB, priceA);
                    });
                    break;
                case "date":
                    sorted.sort((a, b) -> b.getUpload_date().compareTo(a.getUpload_date()));
                    break;
                case "alphabetical":
                    sorted.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
                    break;
                default:
                    System.err.println("Invalid sort key: " + sortBy);
                    break;
            }
            return sorted;
        } catch (Exception e) {
            e.printStackTrace();
            return itemsList;
        }
    }

    public List<Item> getAvailableItems() {
        List<Item> available = new ArrayList<>();
        try {
            for (Item item : items) {
                try {
                    if (item.isAvailable()) available.add(item);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return available;
    }

    public List<Item> getItemsBySeller(User user) {
        List<Item> userItems = new ArrayList<>();
        try {
            for (Item item : items) {
                try {
                    if (item.getUploader().equals(user)) userItems.add(item);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userItems;
    }

    public List<Item> getItemsByCategory(Category category) {
        List<Item> categoryItems = new ArrayList<>();
        try {
            for (Item item : items) {
                try {
                    if (item.getCategory() == category) categoryItems.add(item);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryItems;
    }

    public List<Item> getItemsByGrade(GradeLevel grade) {
        List<Item> gradeItems = new ArrayList<>();
        try {
            for (Item item : items) {
                try {
                    if (item.getGrade() == grade) gradeItems.add(item);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gradeItems;
    }

    public List<ForSaleItem> getForSaleItems() {
        List<ForSaleItem> forSale = new ArrayList<>();
        try {
            for (Item item : items) {
                try {
                    if (item instanceof ForSaleItem) forSale.add((ForSaleItem) item);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forSale;
    }

    public List<FreeResource> getFreeResources() {
        List<FreeResource> free = new ArrayList<>();
        try {
            for (Item item : items) {
                try {
                    if (item instanceof FreeResource) free.add((FreeResource) item);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return free;
    }

    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        try {
            stats.put("Total Items", items.size());
            stats.put("Available Items", getAvailableItems().size());
            stats.put("For Sale Items", getForSaleItems().size());
            stats.put("Free Resources", getFreeResources().size());
            stats.put("Categories", categories);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }

    public Item getItemById(String itemId) {
        try {
            if (itemId == null) throw new IllegalArgumentException("Item ID cannot be null");
            for (Item item : items) {
                try {
                    if (item.getItem_id().equals(itemId)) return item;
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Item> getPopularItems(int count) {
        try {
            if (count <= 0) throw new IllegalArgumentException("Count must be positive");
            List<Item> sorted = new ArrayList<>(items);
            sorted.sort((a, b) -> Integer.compare(b.getViews(), a.getViews()));
            return sorted.subList(0, Math.min(count, sorted.size()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Item> getNewArrivals(int count) {
        try {
            if (count <= 0) throw new IllegalArgumentException("Count must be positive");
            List<Item> sorted = new ArrayList<>(items);
            sorted.sort((a, b) -> b.getUpload_date().compareTo(a.getUpload_date()));
            return sorted.subList(0, Math.min(count, sorted.size()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Item> getRecommendedItems(User user) {
        try {
            return getNewArrivals(10);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void updateCategories() {
        try {
            categories.clear();
            for (Item item : items) {
                try {
                    Category cat = item.getCategory();
                    categories.put(cat, categories.getOrDefault(cat, 0) + 1);
                } catch (Exception ignored) {}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCatalog() {
        try {
            items.clear();
            categories.clear();
            this.updated_date = new Date();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        try {
            return "Catalog{items=" + items.size() + ", Date: " + getUpdated_date() + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "Catalog{Error retrieving data}";
        }
    }
}
