package com.studentexchange.services;

import com.studentexchange.enums.*;
import com.studentexchange.models.*;

import java.util.*;

public class StudentBookExchange {
    private Map<String, User> users;
    private Catalog catalog;
    private List<Transaction> transactions;
    private CreditSystem credit_system;

    public StudentBookExchange() {
        this.users = new HashMap<>();
        this.catalog = new Catalog();
        this.transactions = new ArrayList<>();
        this.credit_system = new CreditSystem();
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public CreditSystem getCredit_system() {
        return credit_system;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction == null)
            throw new IllegalArgumentException("Transaction cannot be null");
        transactions.add(transaction);
    }

    public void adduser(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        users.put(user.getUser_id(), user);
    }

    public void addItemToCatalog(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item cannot be null");
        catalog.addItem(item);
    }

    public User registerUser(String name, String cnic, String email, String password, String phone, String address) {
        if (email == null || password == null)
            throw new IllegalArgumentException("Email and password cannot be null");

        if (checkUserExists(email, cnic)) {
            throw new IllegalArgumentException("User already exists with same email or CNIC");
        }

        if (!validateCNIC(cnic)) {
            throw new IllegalArgumentException("Invalid CNIC format");
        }

        User newUser = new User(name, cnic, email, password, phone, address);
        users.put(newUser.getUser_id(), newUser);
        return newUser;
    }

    public User login(String email, String password) {
        if (email == null || password == null)
            throw new IllegalArgumentException("Email and password cannot be null");

        for (User user : users.values()) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public Book uploadBook(User uploader, String title, String description, Category category, GradeLevel grade,
                           String subject, Condition condition, float market_price, float price,
                           String author, String edition, String publisher, int pages, boolean is_hardcover) {

        if (uploader == null)
            throw new NullPointerException("Uploader cannot be null");

        Book book = new Book(title, uploader, description, category, grade, subject,
                condition, market_price, price, author, edition, publisher, pages, is_hardcover);
        catalog.addItem(book);
        return book;
    }

    public Notes uploadNotes(User uploader, String title, String description, Category category, GradeLevel grade,
                             String subject, Condition condition, float market_price, float price,
                             int pages, String format_type, boolean is_handwritten, boolean is_scanned, String quality) {

        if (uploader == null)
            throw new NullPointerException("Uploader cannot be null");

        Notes notes = new Notes(title, uploader, description, category, grade, subject,
                condition, market_price, price, pages, format_type, is_handwritten, is_scanned, quality);
        catalog.addItem(notes);
        return notes;
    }

    public PastPaper uploadPastPaper(User uploader, String title, String description, Category category,
                                     GradeLevel grade, String subject, Condition condition, float market_price,
                                     float price, String exam_board, int year, boolean has_answers,
                                     boolean has_model_paper, boolean is_solved, int total_papers,
                                     String subject_code, boolean is_compilation) {

        if (uploader == null)
            throw new NullPointerException("Uploader cannot be null");

        PastPaper pastPaper = new PastPaper(title, uploader, description, category, grade, subject,
                condition, market_price, price, exam_board, year, has_answers, has_model_paper,
                is_solved, total_papers, subject_code, is_compilation);
        catalog.addItem(pastPaper);
        return pastPaper;
    }

    public FreeResource uploadFreeResource(User uploader, String title, String description, Category category,
                                           GradeLevel grade, String subject, String file_url,
                                           boolean is_university_paper, String university, String course_code,
                                           int year, String semester, String exam_type, boolean has_solutions,
                                           boolean is_official, float file_size, String file_format) {

        if (uploader == null)
            throw new NullPointerException("Uploader cannot be null");

        FreeResource resource = new FreeResource(title, uploader, description, category, grade, subject,
                file_url, is_university_paper, university, course_code, year, semester, exam_type,
                has_solutions, is_official, file_size, file_format);
        catalog.addItem(resource);
        awardUploadCredits(uploader);
        return resource;
    }

    public Transaction createTransaction(User buyer, Item item, PaymentMethod method) {
        if (buyer == null || item == null || method == null)
            throw new IllegalArgumentException("Buyer, Item & Payment Method cannot be null");

        if (!(item instanceof ForSaleItem)) {
            return null;
        }
        ForSaleItem forSaleItem = (ForSaleItem) item;

        if (!forSaleItem.isAvailable()) {
            throw new IllegalStateException("Item is not available");
        }

        Transaction transaction = new Transaction(buyer, item.getUploader(), forSaleItem, method);
        transactions.add(transaction);
        buyer.addTransactionAsBuyer(transaction);
        item.getUploader().addTransactionAsSeller(transaction);
        return transaction;
    }

    public List<Item> searchItems(String keyword, Category category, GradeLevel grade,
                                  Double minPrice, Double maxPrice, String subject, Condition condition) {
        return catalog.search(keyword); // filtering already handled elsewhere
    }

    public User getUserById(String userId) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        return users.get(userId);
    }

    public Item getItemById(String itemId) {
        if (itemId == null) throw new IllegalArgumentException("Item ID cannot be null");
        return catalog.getItemById(itemId);
    }

    public boolean processPayment(Transaction transaction, Map<String, String> paymentDetails) {
        if (transaction == null)
            throw new IllegalArgumentException("Transaction cannot be null");

        transaction.completePayment(transaction.getPayment_method());
        transaction.updateShippingStatus(ShippingStatus.NOT_SHIPPED);
        return true;
    }

    public boolean confirmDelivery(Transaction transaction) {
        if (transaction == null)
            throw new IllegalArgumentException("Transaction cannot be null");

        transaction.confirmDelivery();
        transaction.getItem().markAsSold(transaction.getBuyer(), new Date());
        return true;
    }

    public Review submitReview(Transaction transaction, User reviewer, int rating, String comment) {
        if (transaction == null || reviewer == null)
            throw new IllegalArgumentException("Reviewer or transaction cannot be null");

        if (!transaction.canSubmitReview(reviewer)) {
            return null;
        }
        User reviewedUser = reviewer.equals(transaction.getBuyer())
                ? transaction.getSeller()
                : transaction.getBuyer();

        Review review = new Review(rating, comment, reviewedUser, reviewer, transaction);

        if (reviewer.equals(transaction.getBuyer())) {
            transaction.addBuyerReview(review);
        } else {
            transaction.addSellerReview(review);
        }

        reviewedUser.calculateAverageRating();
        return review;
    }

    public boolean downloadFreeResource(String resourceId, User user) {
        if (resourceId == null)
            throw new IllegalArgumentException("Resource ID cannot be null");

        Item item = catalog.getItemById(resourceId);

        if (item instanceof FreeResource) {
            FreeResource resource = (FreeResource) item;
            resource.incrementDownload();
            resource.incrementViews();
            return true;
        }
        return false;
    }

    public void awardUploadCredits(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        int credits = credit_system.getUploadCredits();
        user.addCreditPoints(credits);
    }

    public Map<String, Object> getUserStats(User user) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        Map<String, Object> stats = new HashMap<>();
        stats.put("Total Transactions", user.getTotalTransactions());
        stats.put("Total Spent", user.getTotalSpent());
        stats.put("Total Earned", user.getTotalEarned());
        stats.put("Credit Points", user.getCredit_points());
        stats.put("Average Rating", user.getAverage_rating());
        stats.put("Verified", user.isIs_verified());
        return stats;
    }

    public Map<String, Object> generateSystemReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("Total Users", users.size());
        report.put("Total Items", catalog.getItems().size());
        report.put("Total Transactions", transactions.size());
        report.put("Available Items", catalog.getAvailableItems().size());
        report.put("Total Revenue", getTotalRevenue());
        return report;
    }

    public boolean validateCNIC(String cnic) {
        if (cnic == null) return false;
        return cnic.matches("\\d{5}-\\d{7}-\\d") || cnic.matches("\\d{13}");
    }

    public boolean checkUserExists(String email, String cnic) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email) || user.getCnic().equals(cnic)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getActiveUsers() {
        List<User> activeUsers = new ArrayList<>();
        for (User user : users.values()) {
            if (user.hasTransactionHistory()) {
                activeUsers.add(user);
            }
        }
        return activeUsers;
    }

    public float getTotalRevenue() {
        float total = 0;
        for (Transaction transaction : transactions) {
            if (transaction.isPaid()) {
                total += transaction.calculateTotal();
            }
        }
        return total;
    }

    public List<User> getTopSellers() {
        List<User> sellers = new ArrayList<>(users.values());
        sellers.sort((a, b) -> Float.compare(b.getTotalEarned(), a.getTotalEarned()));
        return sellers.subList(0, Math.min(10, sellers.size()));
    }

    public List<FreeResource> getMostDownloadedResources() {
        List<FreeResource> resources = catalog.getFreeResources();
        resources.sort((a, b) -> Integer.compare(b.getDownload_count(), a.getDownload_count()));
        return resources.subList(0, Math.min(10, resources.size()));
    }

    public boolean backupData() {
        return true;
    }

    public boolean restoreData() {
        return true;
    }

    @Override
    public String toString() {
        return "Users: " + users.size() + " Items: " + catalog.getItems().size() + " Transactions: " + transactions.size();
    }
}
