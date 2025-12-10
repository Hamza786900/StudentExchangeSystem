package com.studentexchange.gui;

import com.studentexchange.MainApp;
import com.studentexchange.enums.PaymentMethod;
import com.studentexchange.models.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.List;

public class BrowseScreen {
    private MainApp mainApp;
    private VBox view;
    private ListView<String> itemListView;
    private List<Item> currentItems;

    public BrowseScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        try {
            createView();
        } catch (Exception e) {
            showError("Failed to initialize browse screen.", e);
        }
    }

    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #F0F0F5;");

        Label title = new Label("Browse Available Items");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);

        Label searchLabel = new Label("Search:");
        searchLabel.setFont(Font.font("Arial", 14));

        TextField searchField = new TextField();
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-font-size: 14px;");

        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        searchBtn.setOnAction(e -> {
            try {
                String keyword = searchField.getText();
                if (!keyword.isEmpty()) {
                    currentItems = MainApp.getSystem().getCatalog().search(keyword);
                    updateItemList();
                }
            } catch (Exception ex) {
                showError("Search failed.", ex);
            }
        });

        Button showAllBtn = new Button("Show All");
        showAllBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        showAllBtn.setOnAction(e -> {
            try {
                searchField.clear();
                loadAllItems();
            } catch (Exception ex) {
                showError("Failed to load all items.", ex);
            }
        });

        searchBox.getChildren().addAll(searchLabel, searchField, searchBtn, showAllBtn);

        itemListView = new ListView<>();
        itemListView.setPrefHeight(250);
        itemListView.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New';");

        try {
            loadAllItems();
        } catch (Exception ex) {
            showError("Failed to load items.", ex);
        }

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button viewBtn = new Button("View Details");
        viewBtn.setPrefSize(130, 35);
        viewBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        viewBtn.setOnAction(e -> {
            try {
                viewItemDetails();
            } catch (Exception ex) {
                showError("Failed to view item details.", ex);
            }
        });

        Button buyBtn = new Button("Purchase Item");
        buyBtn.setPrefSize(130, 35);
        buyBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        buyBtn.setOnAction(e -> {
            try {
                purchaseItem();
            } catch (Exception ex) {
                showError("Purchase failed.", ex);
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(130, 35);
        backBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> {
            try {
                mainApp.showDashboard();
            } catch (Exception ex) {
                showError("Failed to return to dashboard.", ex);
            }
        });

        buttonBox.getChildren().addAll(viewBtn, buyBtn, backBtn);

        view.getChildren().addAll(title, searchBox, itemListView, buttonBox);
    }

    private void loadAllItems() {
        try {
            currentItems = MainApp.getSystem().getCatalog().getAvailableItems();
            updateItemList();
        } catch (Exception e) {
            showError("Error loading items.", e);
        }
    }

    private void updateItemList() {
        try {
            itemListView.getItems().clear();
            for (Item item : currentItems) {
                String itemStr;
                if (item instanceof ForSaleItem) {
                    ForSaleItem forSale = (ForSaleItem) item;
                    itemStr = String.format("[%s] %-30s Rs.%-6.0f Grade: %s",
                            item.getItem_id(), item.getTitle(), forSale.getPrice(), item.getGrade());
                } else {
                    itemStr = String.format("[%s] %-30s FREE       Grade: %s",
                            item.getItem_id(), item.getTitle(), item.getGrade());
                }
                itemListView.getItems().add(itemStr);
            }
        } catch (Exception e) {
            showError("Error updating item list.", e);
        }
    }

    private void viewItemDetails() {
        try {
            int selected = itemListView.getSelectionModel().getSelectedIndex();
            if (selected >= 0 && selected < currentItems.size()) {
                Item item = currentItems.get(selected);
                StringBuilder details = new StringBuilder();
                details.append("Item ID: ").append(item.getItem_id()).append("\n");
                details.append("Title: ").append(item.getTitle()).append("\n");
                details.append("Subject: ").append(item.getSubject()).append("\n");
                details.append("Grade: ").append(item.getGrade()).append("\n");
                details.append("Category: ").append(item.getCategory()).append("\n");
                details.append("Seller: ").append(item.getUploader().getName()).append("\n");
                details.append("Views: ").append(item.getViews()).append("\n");

                if (item instanceof ForSaleItem) {
                    ForSaleItem forSale = (ForSaleItem) item;
                    details.append("Price: Rs.").append(forSale.getPrice()).append("\n");
                    details.append("Market Price: Rs.").append(forSale.getMarket_price()).append("\n");
                    details.append("Discount: ").append(String.format("%.1f", forSale.getDiscount_percentage())).append("%\n");
                    details.append("Condition: ").append(forSale.getCondition()).append("\n");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Item Details");
                alert.setHeaderText(null);
                alert.setContentText(details.toString());
                alert.showAndWait();
            } else {
                showWarning("Please select an item!");
            }
        } catch (Exception e) {
            showError("Failed to view item details.", e);
        }
    }

    private void purchaseItem() {
        try {
            int selected = itemListView.getSelectionModel().getSelectedIndex();

            if (selected < 0 || selected >= currentItems.size()) {
                showWarning("Please select an item!");
                return;
            }

            Item item = currentItems.get(selected);

            if (!(item instanceof ForSaleItem)) {
                showInfo("This is a free resource! You can download it directly.");
                return;
            }

            User currentUser = MainApp.getCurrentUser();
            if (item.getUploader().equals(currentUser)) {
                showError("You cannot purchase your own item!");
                return;
            }

            Alert paymentAlert = new Alert(Alert.AlertType.CONFIRMATION);
            paymentAlert.setTitle("Payment Method");
            paymentAlert.setHeaderText("Select Payment Method");
            paymentAlert.setContentText("Choose your payment method:");

            ButtonType onlineBtn = new ButtonType("Online Payment");
            ButtonType codBtn = new ButtonType("Cash on Delivery");
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            paymentAlert.getButtonTypes().setAll(onlineBtn, codBtn, cancelBtn);

            paymentAlert.showAndWait().ifPresent(response -> {
                try {
                    if (response == onlineBtn || response == codBtn) {
                        PaymentMethod method = (response == onlineBtn) ? PaymentMethod.ONLINE : PaymentMethod.CASH_ON_DELIVERY;
                        Transaction transaction = MainApp.getSystem().createTransaction(currentUser, item, method);
                        if (transaction != null) {
                            MainApp.getSystem().processPayment(transaction, null);
                            showInfo("Purchase successful!\nTransaction ID: " + transaction.getTransaction_id());

                            // --- Remove sold item from browse list ---
                            currentItems.remove(item);
                            updateItemList();
                        } else {
                            showError("Purchase failed!");
                        }
                    }
                } catch (Exception ex) {
                    showError("Error processing transaction.", ex);
                }
            });

        } catch (Exception e) {
            showError("Purchase failed.", e);
        }
    }

    private void showError(String message) {
        showError(message, null);
    }

    private void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message + (e != null ? "\nDetails: " + e.getMessage() : ""));
        alert.showAndWait();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}
