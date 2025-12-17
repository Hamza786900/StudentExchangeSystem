package com.studentexchange.services;

import com.studentexchange.models.*;
import com.studentexchange.enums.*;
import java.util.*;

public class StudentBookExchange {
    private Map<String, User> users = new HashMap<>();
    private Catalog catalog = new Catalog();
    private List<Transaction> transactions = new ArrayList<>();

    public StudentBookExchange() {
        try {
            this.users = new HashMap<>();
            this.catalog = new Catalog();
            this.transactions = new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create StudentBookExchange: " + e.getMessage());
        }
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public Map<String, User> getUsers() {
        return new HashMap<>(users);
    }

    public void adduser(User user) {
        if (user != null) {
            users.put(user.getUser_id(), user);
        }
    }

    public User login(String email, String password) {
        String trimmedEmail = email.trim().toLowerCase();
        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(trimmedEmail) && user.getPassword().equals(password.trim())) {
                return user;
            }
        }
        return null;
    }

    public User registerUser(String name, String cnic, String email, String password, String phone, String address) {
        User newUser = new User(name.trim(), cnic.trim(), email.trim().toLowerCase(), password.trim(), phone.trim(), address.trim());
        users.put(newUser.getUser_id(), newUser);
        return newUser;
    }

    public Book uploadBook(User uploader, String title, String description, Category category, GradeLevel grade, String subject, Condition condition, float market_price, float price, String author, String edition, String publisher, int pages, boolean is_hardcover) {
        Book book = new Book(title.trim(), uploader, description, category, grade, subject, condition, market_price, price, author, edition, publisher, pages, is_hardcover);
        catalog.addItem(book);
        return book;
    }

    public Transaction createTransaction(User buyer, Item item, PaymentMethod method) {
        try {
            if (!(item instanceof ForSaleItem)) {
                throw new IllegalArgumentException("Item is not for sale");
            }
            ForSaleItem forSaleItem = (ForSaleItem) item;
            User seller = item.getUploader();

            Transaction transaction = new Transaction(buyer, seller, forSaleItem, method);
            transaction.completePayment(method);
            transactions.add(transaction);

            buyer.addTransactionAsBuyer(transaction);
            seller.addTransactionAsSeller(transaction);

            return transaction;
        } catch (Exception e) {
            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }
    }

    public List<Item> searchItems(String keyword, Category category, GradeLevel grade, float minPrice, float maxPrice, String subject, Condition condition) {
        return catalog.filterItems(category, grade, minPrice, maxPrice, subject, condition);
    }
}