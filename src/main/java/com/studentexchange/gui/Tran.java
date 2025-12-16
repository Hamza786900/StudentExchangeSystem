package com.studentexchange.gui;

import com.studentexchange.Main;
import com.studentexchange.models.Transaction;
import com.studentexchange.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Map;

public class Tran {
    Main main;
    BorderPane root;
    private VBox transactionBox;
    private Transaction selectedTransaction;

    public Tran(Main main) {
        this.main = main;
        createview();
    }

    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // ===== TOP: Heading =====
        Label heading = new Label("Transactions");
        heading.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        VBox topBox = new VBox(heading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 15, 0));
        root.setTop(topBox);

        // ===== CENTER: Scrollable Transaction List =====
        transactionBox = new VBox(10);
        transactionBox.setPadding(new Insets(10));
        transactionBox.setStyle("-fx-background-color: #222;");
        transactionBox.setAlignment(Pos.TOP_LEFT);

        loadTransactions();

        // ScrollPane for transactions
        ScrollPane scrollPane = new ScrollPane(transactionBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #222; -fx-border-color: #333;");

        root.setCenter(scrollPane);

        // ===== BOTTOM: Details Button =====
        Button detailsBtn = new Button("Details");
        detailsBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        detailsBtn.setPrefSize(100, 35);
        detailsBtn.setOnAction(e -> showTransactionDetails());

        HBox bottomBox = new HBox(detailsBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15, 0, 15, 0));
        root.setBottom(bottomBox);
    }

    private void loadTransactions() {
        transactionBox.getChildren().clear();

        User currentUser = main.getCurrentUser();
        List<Transaction> buyerTransactions = currentUser.getTransactionsAsBuyer();
        List<Transaction> sellerTransactions = currentUser.getTransactionsAsSeller();

        if (buyerTransactions.isEmpty() && sellerTransactions.isEmpty()) {
            Label noTransactions = new Label("No transactions yet");
            noTransactions.setStyle("-fx-text-fill: white; -fx-padding: 5px;");
            transactionBox.getChildren().add(noTransactions);
        } else {
            // Add buyer transactions
            if (!buyerTransactions.isEmpty()) {
                Label buyerLabel = new Label("Purchases:");
                buyerLabel.setStyle("-fx-text-fill: teal; -fx-font-weight: bold; -fx-padding: 5px;");
                transactionBox.getChildren().add(buyerLabel);

                for (Transaction t : buyerTransactions) {
                    addTransactionButton(t, "Bought");
                }
            }

            // Add seller transactions
            if (!sellerTransactions.isEmpty()) {
                Label sellerLabel = new Label("Sales:");
                sellerLabel.setStyle("-fx-text-fill: teal; -fx-font-weight: bold; -fx-padding: 5px;");
                transactionBox.getChildren().add(sellerLabel);

                for (Transaction t : sellerTransactions) {
                    addTransactionButton(t, "Sold");
                }
            }
        }
    }

    private void addTransactionButton(Transaction t, String type) {
        String itemTitle = t.getItem().getTitle();
        String status = t.getPayment_status().name();

        Button transBtn = new Button(String.format("%s: %s - %s", type, itemTitle, status));
        transBtn.setStyle("-fx-text-fill: white; -fx-background-color: #2c2c2c;");
        transBtn.setPrefWidth(400);
        transBtn.setAlignment(Pos.CENTER_LEFT);
        transBtn.setOnAction(e -> {
            selectedTransaction = t;
            highlightSelection(transBtn);
        });
        transactionBox.getChildren().add(transBtn);
    }

    private void highlightSelection(Button selected) {
        for (var node : transactionBox.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-text-fill: white; -fx-background-color: #2c2c2c;");
            }
        }
        selected.setStyle("-fx-text-fill: white; -fx-background-color: teal;");
    }

    private void showTransactionDetails() {
        if (selectedTransaction == null) {
            showAlert("Error", "Please select a transaction first");
            return;
        }

        Map<String, String> status = selectedTransaction.getTransactionStatus();
        StringBuilder details = new StringBuilder();

        for (Map.Entry<String, String> entry : status.entrySet()) {
            details.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transaction Details");
        alert.setHeaderText(selectedTransaction.getTransaction_id());
        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public BorderPane getroot() {
        return root;
    }
}