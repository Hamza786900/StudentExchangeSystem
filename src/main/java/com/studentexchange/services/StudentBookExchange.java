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
        try {
            this.users = new HashMap<>();
            this.catalog = new Catalog();
            this.transactions = new ArrayList<>();
            this.credit_system = new CreditSystem();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create StudentBookExchange: " + e.getMessage());
        }
    }

    public Map<String, User> getUsers() {
        return new HashMap<>(users);
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public CreditSystem getCredit_system() {
        return credit_system;
    }

    public void addTransaction(Transaction transaction) {
        try {
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction cannot be null");
            }

            String transId = transaction.getTransaction_id();
            for (Transaction existingTrans : transactions) {
                if (existingTrans.getTransaction_id().equals(transId)) {
                    throw new IllegalArgumentException("Transaction with ID " + transId + " already exists");
                }
            }

            transactions.add(transaction);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add transaction: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error adding transaction: " + e.getMessage());
        }
    }

    public void adduser(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            String userId = user.getUser_id();
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be null or empty");
            }

            if (users.containsKey(userId)) {
                throw new IllegalArgumentException("User with ID " + userId + " already exists");
            }

            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("User email cannot be null or empty");
            }

            for (User existingUser : users.values()) {
                if (existingUser.getEmail().equalsIgnoreCase(user.getEmail().trim())) {
                    throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
                }
            }

            users.put(userId, user);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add user: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error adding user: " + e.getMessage());
        }
    }

    public void addItemToCatalog(Item item) {
        try {
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }

            // Validate item has required fields
            if (item.getTitle() == null || item.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Item title cannot be null or empty");
            }

            if (item.getUploader() == null) {
                throw new IllegalArgumentException("Item uploader cannot be null");
            }

            // Check if uploader exists in system
            if (!users.containsKey(item.getUploader().getUser_id())) {
                throw new IllegalArgumentException("Item uploader is not a registered user");
            }

            catalog.addItem(item);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to add item to catalog: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error adding item to catalog: " + e.getMessage());
        }
    }

    public User registerUser(String name, String cnic, String email, String password, String phone, String address) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            if (cnic == null || cnic.trim().isEmpty()) {
                throw new IllegalArgumentException("CNIC cannot be null or empty");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone cannot be null or empty");
            }
            if (address == null || address.trim().isEmpty()) {
                throw new IllegalArgumentException("Address cannot be null or empty");
            }

            String trimmedEmail = email.trim().toLowerCase();
            String trimmedCnic = cnic.trim();

            if (!trimmedEmail.contains("@")) {
                throw new IllegalArgumentException("Invalid email format");
            }

            if (!validateCNIC(trimmedCnic)) {
                throw new IllegalArgumentException("Invalid CNIC format. Must be 13 digits or 00000-0000000-0 format");
            }

            if (checkUserExists(trimmedEmail, trimmedCnic)) {
                throw new IllegalArgumentException("User already exists with same email or CNIC");
            }

            if (password.length() < 6) {
                throw new IllegalArgumentException("Password must be at least 6 characters long");
            }

            User newUser = new User(name.trim(), trimmedCnic, trimmedEmail, password.trim(), phone.trim(), address.trim());
            users.put(newUser.getUser_id(), newUser);

            return newUser;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to register user: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error registering user: " + e.getMessage());
        }
    }

    public User login(String email, String password) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }

            String trimmedEmail = email.trim().toLowerCase();
            String trimmedPassword = password.trim();

            for (User user : users.values()) {
                try {
                    if (user.getEmail().equalsIgnoreCase(trimmedEmail) &&
                            user.getPassword().equals(trimmedPassword)) {
                        return user;
                    }
                } catch (Exception e) {
                    System.err.println("Error accessing user data: " + e.getMessage());
                }
            }

            return null;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to login: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during login: " + e.getMessage());
        }
    }

    public Book uploadBook(User uploader, String title, String description, Category category, GradeLevel grade,
                           String subject, Condition condition, float market_price, float price,
                           String author, String edition, String publisher, int pages, boolean is_hardcover) {
        try {
            if (uploader == null) {
                throw new IllegalArgumentException("Uploader cannot be null");
            }

            if (!users.containsKey(uploader.getUser_id())) {
                throw new IllegalArgumentException("Uploader is not a registered user");
            }

            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Book title cannot be null or empty");
            }
            if (category == null) {
                throw new IllegalArgumentException("Category cannot be null");
            }
            if (grade == null) {
                throw new IllegalArgumentException("Grade level cannot be null");
            }
            if (subject == null || subject.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject cannot be null or empty");
            }
            if (condition == null) {
                throw new IllegalArgumentException("Condition cannot be null");
            }
            if (market_price <= 0) {
                throw new IllegalArgumentException("Market price must be positive");
            }
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            if (price > market_price * 2) {
                throw new IllegalArgumentException("Price cannot be more than double the market price");
            }
            if (author == null || author.trim().isEmpty()) {
                throw new IllegalArgumentException("Author cannot be null or empty");
            }
            if (edition == null || edition.trim().isEmpty()) {
                throw new IllegalArgumentException("Edition cannot be null or empty");
            }
            if (publisher == null || publisher.trim().isEmpty()) {
                throw new IllegalArgumentException("Publisher cannot be null or empty");
            }
            if (pages <= 0) {
                throw new IllegalArgumentException("Pages must be positive");
            }

            Book book = new Book(title.trim(), uploader, description != null ? description.trim() : "",
                    category, grade, subject.trim(), condition, market_price, price,
                    author.trim(), edition.trim(), publisher.trim(), pages, is_hardcover);

            catalog.addItem(book);
            return book;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to upload book: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error uploading book: " + e.getMessage());
        }
    }

    public Notes uploadNotes(User uploader, String title, String description, Category category, GradeLevel grade,
                             String subject, Condition condition, float market_price, float price,
                             int pages, String format_type, boolean is_handwritten, boolean is_scanned, String quality) {
        try {
            if (uploader == null) {
                throw new IllegalArgumentException("Uploader cannot be null");
            }

            if (!users.containsKey(uploader.getUser_id())) {
                throw new IllegalArgumentException("Uploader is not a registered user");
            }

            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Notes title cannot be null or empty");
            }
            if (category == null) {
                throw new IllegalArgumentException("Category cannot be null");
            }
            if (grade == null) {
                throw new IllegalArgumentException("Grade level cannot be null");
            }
            if (subject == null || subject.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject cannot be null or empty");
            }
            if (condition == null) {
                throw new IllegalArgumentException("Condition cannot be null");
            }
            if (market_price <= 0) {
                throw new IllegalArgumentException("Market price must be positive");
            }
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            if (pages <= 0) {
                throw new IllegalArgumentException("Pages must be positive");
            }
            if (format_type == null || format_type.trim().isEmpty()) {
                throw new IllegalArgumentException("Format type cannot be null or empty");
            }
            if (quality == null || quality.trim().isEmpty()) {
                throw new IllegalArgumentException("Quality cannot be null or empty");
            }

            String trimmedQuality = quality.trim().toLowerCase();
            if (!trimmedQuality.equals("high") && !trimmedQuality.equals("medium") && !trimmedQuality.equals("low")) {
                throw new IllegalArgumentException("Quality must be 'high', 'medium', or 'low'");
            }

            Notes notes = new Notes(title.trim(), uploader, description != null ? description.trim() : "",
                    category, grade, subject.trim(), condition, market_price, price,
                    pages, format_type.trim(), is_handwritten, is_scanned, trimmedQuality);

            catalog.addItem(notes);
            return notes;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to upload notes: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error uploading notes: " + e.getMessage());
        }
    }

    public PastPaper uploadPastPaper(User uploader, String title, String description, Category category,
                                     GradeLevel grade, String subject, Condition condition, float market_price,
                                     float price, String exam_board, int year, boolean has_answers,
                                     boolean has_model_paper, boolean is_solved, int total_papers,
                                     String subject_code, boolean is_compilation) {
        try {
            if (uploader == null) {
                throw new IllegalArgumentException("Uploader cannot be null");
            }

            if (!users.containsKey(uploader.getUser_id())) {
                throw new IllegalArgumentException("Uploader is not a registered user");
            }

            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Past paper title cannot be null or empty");
            }
            if (category == null) {
                throw new IllegalArgumentException("Category cannot be null");
            }
            if (grade == null) {
                throw new IllegalArgumentException("Grade level cannot be null");
            }
            if (subject == null || subject.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject cannot be null or empty");
            }
            if (condition == null) {
                throw new IllegalArgumentException("Condition cannot be null");
            }
            if (market_price <= 0) {
                throw new IllegalArgumentException("Market price must be positive");
            }
            if (price <= 0) {
                throw new IllegalArgumentException("Price must be positive");
            }
            if (exam_board == null || exam_board.trim().isEmpty()) {
                throw new IllegalArgumentException("Exam board cannot be null or empty");
            }

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1900 || year > currentYear + 1) {
                throw new IllegalArgumentException("Year must be between 1900 and " + (currentYear + 1));
            }

            if (total_papers <= 0) {
                throw new IllegalArgumentException("Total papers must be positive");
            }
            if (subject_code == null || subject_code.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject code cannot be null or empty");
            }

            PastPaper pastPaper = new PastPaper(title.trim(), uploader, description != null ? description.trim() : "",
                    category, grade, subject.trim(), condition, market_price, price,
                    exam_board.trim(), year, has_answers, has_model_paper, is_solved,
                    total_papers, subject_code.trim(), is_compilation);

            catalog.addItem(pastPaper);
            return pastPaper;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to upload past paper: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error uploading past paper: " + e.getMessage());
        }
    }

    public FreeResource uploadFreeResource(User uploader, String title, String description, Category category,
                                           GradeLevel grade, String subject, String file_url,
                                           boolean is_university_paper, String university, String course_code,
                                           int year, String semester, String exam_type, boolean has_solutions,
                                           boolean is_official, float file_size, String file_format) {
        try {
            if (uploader == null) {
                throw new IllegalArgumentException("Uploader cannot be null");
            }

            // Validate uploader exists in system
            if (!users.containsKey(uploader.getUser_id())) {
                throw new IllegalArgumentException("Uploader is not a registered user");
            }

            // Validate all required parameters
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Resource title cannot be null or empty");
            }
            if (category == null) {
                throw new IllegalArgumentException("Category cannot be null");
            }
            if (grade == null) {
                throw new IllegalArgumentException("Grade level cannot be null");
            }
            if (subject == null || subject.trim().isEmpty()) {
                throw new IllegalArgumentException("Subject cannot be null or empty");
            }
            if (file_url == null || file_url.trim().isEmpty()) {
                throw new IllegalArgumentException("File URL cannot be null or empty");
            }
            if (file_format == null || file_format.trim().isEmpty()) {
                throw new IllegalArgumentException("File format cannot be null or empty");
            }

            // Validate URL format
            String trimmedUrl = file_url.trim().toLowerCase();
            if (!(trimmedUrl.startsWith("http://") || trimmedUrl.startsWith("https://") ||
                    trimmedUrl.startsWith("ftp://") || trimmedUrl.startsWith("file://"))) {
                throw new IllegalArgumentException("Invalid file URL format");
            }

            if (file_size <= 0) {
                throw new IllegalArgumentException("File size must be positive");
            }
            if (file_size > 1000) { // 1GB limit
                throw new IllegalArgumentException("File size cannot exceed 1000 MB (1GB)");
            }

            // Validate university paper specific fields
            if (is_university_paper) {
                if (university == null || university.trim().isEmpty()) {
                    throw new IllegalArgumentException("University cannot be null or empty for university papers");
                }
                if (course_code == null || course_code.trim().isEmpty()) {
                    throw new IllegalArgumentException("Course code cannot be null or empty for university papers");
                }
                if (year < 1900 || year > Calendar.getInstance().get(Calendar.YEAR)) {
                    throw new IllegalArgumentException("Invalid year for university paper");
                }
            }

            FreeResource resource = new FreeResource(title.trim(), uploader, description != null ? description.trim() : "",
                    category, grade, subject.trim(), file_url.trim(),
                    is_university_paper,
                    university != null ? university.trim() : null,
                    course_code != null ? course_code.trim() : null,
                    year,
                    semester != null ? semester.trim() : null,
                    exam_type != null ? exam_type.trim() : null,
                    has_solutions, is_official, file_size, file_format.trim());

            catalog.addItem(resource);
            awardUploadCredits(uploader);
            return resource;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to upload free resource: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error uploading free resource: " + e.getMessage());
        }
    }

    public Transaction createTransaction(User buyer, Item item, PaymentMethod method) {
        try {
            if (buyer == null) {
                throw new IllegalArgumentException("Buyer cannot be null");
            }
            if (item == null) {
                throw new IllegalArgumentException("Item cannot be null");
            }
            if (method == null) {
                throw new IllegalArgumentException("Payment method cannot be null");
            }

            // Validate buyer exists in system
            if (!users.containsKey(buyer.getUser_id())) {
                throw new IllegalArgumentException("Buyer is not a registered user");
            }

            if (!(item instanceof ForSaleItem)) {
                throw new IllegalArgumentException("Item is not available for sale");
            }

            ForSaleItem forSaleItem = (ForSaleItem) item;

            User seller = item.getUploader();
            if (!users.containsKey(seller.getUser_id())) {
                throw new IllegalArgumentException("Seller is not a registered user");
            }

            if (!forSaleItem.isAvailable()) {
                throw new IllegalStateException("Item is not available for purchase");
            }

            if (!forSaleItem.canBePurchased()) {
                throw new IllegalStateException("Item cannot be purchased at this time");
            }

            if (buyer.equals(seller)) {
                throw new IllegalArgumentException("Buyer cannot purchase from themselves");
            }

            Transaction transaction = new Transaction(buyer, seller, forSaleItem, method);
            transactions.add(transaction);

            try {
                buyer.addTransactionAsBuyer(transaction);
                seller.addTransactionAsSeller(transaction);
            } catch (Exception e) {
                transactions.remove(transaction);
                throw new IllegalStateException("Failed to update user transaction records: " + e.getMessage());
            }


            return transaction;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to create transaction: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to create transaction: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error creating transaction: " + e.getMessage());
        }
    }

    public List<Item> searchItems(String keyword, Category category, GradeLevel grade,
                                  float minPrice, float maxPrice, String subject, Condition condition) {
        try {
            List<Item> results;

            if (keyword != null && !keyword.trim().isEmpty()) {
                results = catalog.search(keyword.trim());
            } else {
                results = catalog.getItems();
            }

            List<Item> filteredResults = catalog.filterItems(category, grade, minPrice, maxPrice, subject, condition);

            if (keyword != null && !keyword.trim().isEmpty()) {
                Set<Item> keywordSet = new HashSet<>(results);
                filteredResults.removeIf(item -> !keywordSet.contains(item));
            }

            return filteredResults;

        } catch (Exception e) {
            throw new RuntimeException("Failed to search items: " + e.getMessage());
        }
    }

    public User getUserById(String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be null or empty");
            }

            String trimmedId = userId.trim();
            User user = users.get(trimmedId);

            if (user == null) {
                throw new IllegalArgumentException("User with ID " + trimmedId + " not found");
            }

            return user;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get user by ID: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting user by ID: " + e.getMessage());
        }
    }

    public Item getItemById(String itemId) {
        try {
            if (itemId == null || itemId.trim().isEmpty()) {
                throw new IllegalArgumentException("Item ID cannot be null or empty");
            }

            Item item = catalog.getItemById(itemId.trim());

            if (item == null) {
                throw new IllegalArgumentException("Item with ID " + itemId + " not found");
            }

            return item;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get item by ID: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting item by ID: " + e.getMessage());
        }
    }

    public boolean processPayment(Transaction transaction, Map<String, String> paymentDetails) {
        try {
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction cannot be null");
            }

            if (paymentDetails == null || paymentDetails.isEmpty()) {
                throw new IllegalArgumentException("Payment details cannot be null or empty");
            }

            if (!paymentDetails.containsKey("payment_method") ||
                    !paymentDetails.containsKey("amount") ||
                    !paymentDetails.containsKey("reference")) {
                throw new IllegalArgumentException("Payment details must include payment_method, amount, and reference");
            }

            try {
                float amount = Float.parseFloat(paymentDetails.get("amount"));
                float transactionTotal = transaction.calculateTotal();

                if (Math.abs(amount - transactionTotal) > 0.01) {
                    throw new IllegalArgumentException("Payment amount does not match transaction total. Expected: " +
                            transactionTotal + ", Received: " + amount);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid payment amount format");
            }


            if (transaction.isPaid()) {
                throw new IllegalStateException("Transaction has already been paid");
            }

            transaction.completePayment(transaction.getPayment_method());
            transaction.updateShippingStatus(ShippingStatus.NOT_SHIPPED);

            ForSaleItem item = transaction.getItem();
            if (item.isAvailable()) {
                item.markAsSold(transaction.getBuyer(), new Date());
            } else {
                if (!item.getBuyer().equals(transaction.getBuyer())) {
                    throw new IllegalStateException("Item is no longer available for purchase");
                }
            }

            credit_system.awardPointsForTransaction(transaction.getBuyer(), transaction.calculateTotal());

            return true;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to process payment: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to process payment: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error processing payment: " + e.getMessage());
        }
    }

    public boolean confirmDelivery(Transaction transaction) {
        try {
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction cannot be null");
            }

            if (!transaction.isPaid()) {
                throw new IllegalStateException("Cannot confirm delivery for unpaid transaction");
            }

            if (transaction.isDelivered()) {
                throw new IllegalStateException("Delivery already confirmed for this transaction");
            }

            transaction.confirmDelivery();

            credit_system.addBonusPoints(transaction.getBuyer(), "DELIVERY_COMPLETION");
            credit_system.addBonusPoints(transaction.getSeller(), "SALE_COMPLETED");

            return true;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to confirm delivery: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to confirm delivery: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error confirming delivery: " + e.getMessage());
        }
    }

    public Review submitReview(Transaction transaction, User reviewer, int rating, String comment) {
        try {
            if (transaction == null) {
                throw new IllegalArgumentException("Transaction cannot be null");
            }
            if (reviewer == null) {
                throw new IllegalArgumentException("Reviewer cannot be null");
            }

            if (!reviewer.equals(transaction.getBuyer()) && !reviewer.equals(transaction.getSeller())) {
                throw new IllegalArgumentException("Reviewer must be either buyer or seller in the transaction");
            }

            if (!transaction.canSubmitReview(reviewer)) {
                throw new IllegalStateException("Reviewer cannot submit review at this time");
            }

            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }

            if (comment == null || comment.trim().isEmpty()) {
                throw new IllegalArgumentException("Comment cannot be null or empty");
            }

            User reviewedUser = reviewer.equals(transaction.getBuyer())
                    ? transaction.getSeller()
                    : transaction.getBuyer();

            Review review = new Review(rating, comment.trim(), reviewedUser, reviewer, transaction);

            if (reviewer.equals(transaction.getBuyer())) {
                transaction.addBuyerReview(review);
            } else {
                transaction.addSellerReview(review);
            }

            reviewedUser.calculateAverageRating();

            return review;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to submit review: " + e.getMessage());
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Failed to submit review: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error submitting review: " + e.getMessage());
        }
    }


    public void awardUploadCredits(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            if (!users.containsKey(user.getUser_id())) {
                throw new IllegalArgumentException("User is not registered");
            }

            int credits = credit_system.getUploadCredits();
            user.addCreditPoints(credits);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to award upload credits: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error awarding upload credits: " + e.getMessage());
        }
    }

    public Map<String, Object> getUserStats(User user) {
        try {
            if (user == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            if (!users.containsKey(user.getUser_id())) {
                throw new IllegalArgumentException("User is not registered");
            }

            Map<String, Object> stats = new HashMap<>();

            int totalTransactions = user.getTotalTransactions();
            float totalSpent = user.getTotalSpent();
            float totalEarned = user.getTotalEarned();
            int creditPoints = user.getCredit_points();
            float averageRating = user.getAverage_rating();
            boolean isVerified = user.isIs_verified();
            float buyerRating = user.getBuyerRating();
            float sellerRating = user.getSellerRating();

            stats.put("Total Transactions", totalTransactions);
            stats.put("Total Spent", String.format("Rs. %.2f", totalSpent));
            stats.put("Total Earned", String.format("Rs. %.2f", totalEarned));
            stats.put("Credit Points", creditPoints);
            stats.put("Credit Value", String.format("Rs. %.2f", credit_system.getEligibleDiscount(user)));
            stats.put("Average Rating", String.format("%.1f/5", averageRating));
            stats.put("Buyer Rating", String.format("%.1f/5", buyerRating));
            stats.put("Seller Rating", String.format("%.1f/5", sellerRating));
            stats.put("Verified", isVerified ? "Yes ✓" : "No");
            stats.put("Member Since", user.getRegistration_date());

            return stats;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to get user stats: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error getting user stats: " + e.getMessage());
        }
    }



    public boolean validateCNIC(String cnic) {
        try {
            if (cnic == null || cnic.trim().isEmpty()) {
                return false;
            }

            String trimmedCnic = cnic.trim();

            String cleanCnic = trimmedCnic.replaceAll("-", "");

            if (!cleanCnic.matches("\\d{13}")) {
                return false;
            }

            return trimmedCnic.matches("\\d{5}-\\d{7}-\\d") ||
                    trimmedCnic.matches("\\d{13}");

        } catch (Exception e) {
            throw new RuntimeException("Failed to validate CNIC: " + e.getMessage());
        }
    }

    public boolean checkUserExists(String email, String cnic) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (cnic == null || cnic.trim().isEmpty()) {
                throw new IllegalArgumentException("CNIC cannot be null or empty");
            }

            String trimmedEmail = email.trim().toLowerCase();
            String trimmedCnic = cnic.trim();

            for (User user : users.values()) {
                try {
                    if (user.getEmail().equalsIgnoreCase(trimmedEmail) ||
                            user.getCnic().equals(trimmedCnic)) {
                        return true;
                    }
                } catch (Exception e) {
                    System.err.println("Error checking user existence: " + e.getMessage());
                }
            }

            return false;

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to check user existence: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error checking user existence: " + e.getMessage());
        }
    }



    public float getTotalRevenue() {
        try {
            float total = 0;
            int paidTransactions = 0;

            for (Transaction transaction : transactions) {
                try {
                    if (transaction.isPaid()) {
                        total += transaction.calculateTotal();
                        paidTransactions++;
                    }
                } catch (Exception e) {
                    System.err.println("Error calculating transaction revenue: " + e.getMessage());
                }
            }

            if (Float.isNaN(total) || Float.isInfinite(total)) {
                throw new ArithmeticException("Invalid total revenue calculation");
            }

            return total;

        } catch (ArithmeticException e) {
            throw new ArithmeticException("Failed to calculate total revenue: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error calculating total revenue: " + e.getMessage());
        }
    }


    @Override
    public String toString() {
        return "Users: " + users.size() +
                " Items: " + catalog.getItems().size() +
                " Transactions: " + transactions.size();
    }
}