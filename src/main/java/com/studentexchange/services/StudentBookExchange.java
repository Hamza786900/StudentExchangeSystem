package com.studentexchange.services;

import com.studentexchange.enums.*;
import com.studentexchange.models.*;
import java.util.*;

public class StudentBookExchange {

    private Map<String, User> users = new HashMap<>();
    private Catalog catalog = new Catalog();
    private List<Transaction> transactions = new ArrayList<>();

    public User registerUser(User user) {
        users.put(user.getUser_id(), user);
        return user;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public Transaction createTransaction(User buyer, Item item,
                                         PaymentMethod method) {

        ForSaleItem saleItem = (ForSaleItem) item;
        Transaction tx = new Transaction(
                buyer, item.getUploader(), saleItem, method);

        transactions.add(tx);
        return tx;
    }
}
