package com.studentexchange.gui;

import com.studentexchange.MainApp;
import com.studentexchange.enums.PaymentMethod;
import com.studentexchange.models.*;
import com.studentexchange.services.CreditSystem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowseScreen {
    private MainApp mainApp;
    private VBox view;
    private ListView<String> itemListView;
    private List<Item> currentItems;
    private Label creditPointsLabel;

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


        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER);

        Label title = new Label("Browse Available Items");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");


        HBox creditBox = new HBox(5);
        creditBox.setAlignment(Pos.CENTER_RIGHT);

        Label creditIcon = new Label("💰");
        creditIcon.setFont(Font.font("Arial", 16));

        creditPointsLabel = new Label("Loading...");
        creditPointsLabel.setFont(Font.font("Arial", 14));
        creditPointsLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #4CAF50;");

        creditBox.getChildren().addAll(creditIcon, creditPointsLabel);


        HBox.setHgrow(title, javafx.scene.layout.Priority.ALWAYS);
        headerBox.getChildren().addAll(title, creditBox);

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
            System.out.println("DEBUG: Search button clicked");
            try {
                String keyword = searchField.getText();
                if (!keyword.isEmpty()) {
                    System.out.println("DEBUG: Searching for: " + keyword);
                    currentItems = MainApp.getSystem().getCatalog().search(keyword);
                    System.out.println("DEBUG: Found " + currentItems.size() + " items");
                    updateItemList();
                } else {
                    System.out.println("DEBUG: Search field empty");
                }
            } catch (Exception ex) {
                System.out.println("DEBUG: Search error: " + ex.getMessage());
                ex.printStackTrace();
                showError("Search failed.", ex);
            }
        });

        Button showAllBtn = new Button("Show All");
        showAllBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        showAllBtn.setOnAction(e -> {
            System.out.println("DEBUG: Show All button clicked");
            try {
                searchField.clear();
                loadAllItems();
            } catch (Exception ex) {
                System.out.println("DEBUG: Show All error: " + ex.getMessage());
                ex.printStackTrace();
                showError("Failed to load all items.", ex);
            }
        });

        searchBox.getChildren().addAll(searchLabel, searchField, searchBtn, showAllBtn);

        itemListView = new ListView<>();
        itemListView.setPrefHeight(250);
        itemListView.setStyle("-fx-font-size: 13px; -fx-font-family: 'Courier New';");

        try {
            System.out.println("DEBUG: Loading initial items");
            loadAllItems();
        } catch (Exception ex) {
            System.out.println("DEBUG: Initial load error: " + ex.getMessage());
            ex.printStackTrace();
            showError("Failed to load items.", ex);
        }

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button viewBtn = new Button("View Details");
        viewBtn.setPrefSize(130, 35);
        viewBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        viewBtn.setOnAction(e -> {
            System.out.println("DEBUG: View Details button clicked");
            try {
                viewItemDetails();
            } catch (Exception ex) {
                System.out.println("DEBUG: View Details error: " + ex.getMessage());
                ex.printStackTrace();
                showError("Failed to view item details.", ex);
            }
        });

        Button buyBtn = new Button("Purchase Item");
        buyBtn.setPrefSize(130, 35);
        buyBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        buyBtn.setOnAction(e -> {
            System.out.println("DEBUG: Purchase Item button clicked");
            try {
                purchaseItem();
            } catch (Exception ex) {
                System.out.println("DEBUG: Purchase error: " + ex.getMessage());
                ex.printStackTrace();
                showError("Purchase failed.", ex);
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(130, 35);
        backBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> {
            System.out.println("DEBUG: Back button clicked");
            try {
                System.out.println("DEBUG: Attempting to show dashboard");
                mainApp.showDashboard();
                System.out.println("DEBUG: Dashboard method completed");
            } catch (Exception ex) {
                System.out.println("DEBUG: Back error: " + ex.getMessage());
                ex.printStackTrace();
                showError("Failed to return to dashboard.", ex);
            }
        });

        buttonBox.getChildren().addAll(viewBtn, buyBtn, backBtn);

        view.getChildren().addAll(headerBox, searchBox, itemListView, buttonBox);

        // Delay credit points update to prevent blocking UI
        javafx.application.Platform.runLater(() -> {
            updateCreditPointsDisplay();
        });

        System.out.println("DEBUG: BrowseScreen view created successfully");
    }

    private void loadAllItems() {
        try {
            System.out.println("DEBUG: Loading all items");
            currentItems = MainApp.getSystem().getCatalog().getAvailableItems();
            System.out.println("DEBUG: Loaded " + currentItems.size() + " items");
            updateItemList();
        } catch (Exception e) {
            System.out.println("DEBUG: Load items error: " + e.getMessage());
            e.printStackTrace();
            showError("Error loading items.", e);
        }
    }

    private void updateItemList() {
        try {
            System.out.println("DEBUG: Updating item list with " + currentItems.size() + " items");
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
            System.out.println("DEBUG: ListView now has " + itemListView.getItems().size() + " items");
        } catch (Exception e) {
            System.out.println("DEBUG: Update list error: " + e.getMessage());
            e.printStackTrace();
            showError("Error updating item list.", e);
        }
    }

    private void viewItemDetails() {
        try {
            System.out.println("DEBUG: viewItemDetails called");
            int selected = itemListView.getSelectionModel().getSelectedIndex();
            System.out.println("DEBUG: Selected index: " + selected);

            if (selected >= 0 && selected < currentItems.size()) {
                Item item = currentItems.get(selected);
                System.out.println("DEBUG: Selected item: " + item.getTitle());

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

                System.out.println("DEBUG: Showing details dialog");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Item Details");
                alert.setHeaderText(null);
                alert.setContentText(details.toString());
                alert.showAndWait();
                System.out.println("DEBUG: Details dialog closed");
            } else {
                System.out.println("DEBUG: No item selected or invalid selection");
                showWarning("Please select an item!");
            }
        } catch (Exception e) {
            System.out.println("DEBUG: viewItemDetails error: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to view item details.", e);
        }
    }

    private void purchaseItem() {
        try {
            System.out.println("DEBUG: purchaseItem called");
            int selected = itemListView.getSelectionModel().getSelectedIndex();
            System.out.println("DEBUG: Selected index for purchase: " + selected);

            if (selected < 0 || selected >= currentItems.size()) {
                System.out.println("DEBUG: Invalid selection for purchase");
                showWarning("Please select an item!");
                return;
            }

            Item item = currentItems.get(selected);
            System.out.println("DEBUG: Purchasing item: " + item.getTitle());

            if (!(item instanceof ForSaleItem)) {
                System.out.println("DEBUG: Item is free, not for sale");
                showInfo("This is a free resource! You can download it directly.");
                return;
            }

            User currentUser = MainApp.getCurrentUser();
            System.out.println("DEBUG: Current user: " + (currentUser != null ? currentUser.getName() : "null"));

            if (currentUser == null) {
                System.out.println("DEBUG: Current user is null!");
                showError("You must be logged in to purchase items!");
                return;
            }

            if (item.getUploader().equals(currentUser)) {
                System.out.println("DEBUG: User trying to purchase own item");
                showError("You cannot purchase your own item!");
                return;
            }

            ForSaleItem forSaleItem = (ForSaleItem) item;


            if (!forSaleItem.isAvailable()) {
                System.out.println("DEBUG: Item is no longer available");
                showError("Sorry, this item has already been sold!");
                loadAllItems();
                return;
            }

            float itemPrice = forSaleItem.getPrice();
            int userCredits = currentUser.getCredit_points();
            CreditSystem creditSystem = MainApp.getSystem().getCredit_system();
            double eligibleDiscount = creditSystem.getEligibleDiscount(currentUser);

            System.out.println("DEBUG: Price: " + itemPrice + ", Credits: " + userCredits + ", Discount: " + eligibleDiscount);


            Alert creditAlert = new Alert(Alert.AlertType.CONFIRMATION);
            creditAlert.setTitle("Use Credit Points");
            creditAlert.setHeaderText("Credit Points Available: " + userCredits);
            creditAlert.setContentText(String.format(
                    "Item Price: Rs. %.2f\n" +
                            "Eligible Discount: Rs. %.2f\n\n" +
                            "Do you want to use your credit points for discount?",
                    itemPrice, eligibleDiscount
            ));

            ButtonType useCreditsBtn = new ButtonType("Use Credits");
            ButtonType skipCreditsBtn = new ButtonType("Skip Credits");
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            creditAlert.getButtonTypes().setAll(useCreditsBtn, skipCreditsBtn, cancelBtn);

            System.out.println("DEBUG: Showing credit alert");
            creditAlert.showAndWait().ifPresent(response -> {
                try {
                    System.out.println("DEBUG: Credit alert response: " + response.getText());

                    if (response == cancelBtn) {
                        System.out.println("DEBUG: Purchase cancelled");
                        return;
                    }

                    final int[] creditsToUse = {0};
                    final float[] finalPrice = {itemPrice};

                    if (response == useCreditsBtn) {
                        System.out.println("DEBUG: User chose to use credits");

                        Map<Integer, Double> availableRedemptions = creditSystem.getAvailableRedemptions(currentUser);
                        System.out.println("DEBUG: Available redemptions: " + availableRedemptions.size());

                        if (availableRedemptions.isEmpty()) {
                            System.out.println("DEBUG: No redemptions available");
                            showInfo("No redemption options available with your current points.");
                        } else {

                            ChoiceDialog<Integer> redemptionDialog = new ChoiceDialog<>();
                            redemptionDialog.setTitle("Select Redemption");
                            redemptionDialog.setHeaderText("Available Redemption Options");
                            redemptionDialog.setContentText("Select points to redeem:");

                            for (Integer points : availableRedemptions.keySet()) {
                                redemptionDialog.getItems().add(points);
                            }

                            System.out.println("DEBUG: Showing redemption dialog");
                            redemptionDialog.showAndWait().ifPresent(selectedPoints -> {
                                try {
                                    System.out.println("DEBUG: Selected points: " + selectedPoints);
                                    if (creditSystem.validateRedemption(currentUser, selectedPoints)) {
                                        double discount = creditSystem.calculateDiscountAmount(selectedPoints);
                                        finalPrice[0] = Math.max(0, itemPrice - (float) discount);
                                        creditsToUse[0] = selectedPoints;

                                        System.out.println("DEBUG: Discount: " + discount + ", Final price: " + finalPrice[0]);

                                        Alert confirmDiscount = new Alert(Alert.AlertType.CONFIRMATION);
                                        confirmDiscount.setTitle("Discount Applied");
                                        confirmDiscount.setHeaderText("Discount Summary");
                                        confirmDiscount.setContentText(String.format(
                                                "Original Price: Rs. %.2f\n" +
                                                        "Discount: Rs. %.2f\n" +
                                                        "Final Price: Rs. %.2f\n\n" +
                                                        "Continue with purchase?",
                                                itemPrice, discount, finalPrice[0]
                                        ));

                                        System.out.println("DEBUG: Showing discount confirmation");
                                        confirmDiscount.showAndWait().ifPresent(confirmation -> {
                                            if (confirmation == ButtonType.OK) {
                                                System.out.println("DEBUG: Proceeding with discount");
                                                proceedWithPayment(forSaleItem, finalPrice[0], creditsToUse[0]);
                                            } else {
                                                System.out.println("DEBUG: Discount cancelled");
                                            }
                                        });
                                    } else {
                                        System.out.println("DEBUG: Redemption validation failed");
                                        showError("Invalid redemption selection!");
                                    }
                                } catch (Exception ex) {
                                    System.out.println("DEBUG: Redemption error: " + ex.getMessage());
                                    ex.printStackTrace();
                                    showError("Failed to apply credits.", ex);
                                }
                            });
                            return;
                        }
                    }


                    System.out.println("DEBUG: Proceeding without credits or after skipping");
                    proceedWithPayment(forSaleItem, finalPrice[0], creditsToUse[0]);

                } catch (Exception ex) {
                    System.out.println("DEBUG: Credit processing error: " + ex.getMessage());
                    ex.printStackTrace();
                    showError("Error in credit processing.", ex);
                }
            });

        } catch (Exception e) {
            System.out.println("DEBUG: Purchase failed: " + e.getMessage());
            e.printStackTrace();
            showError("Purchase failed.", e);
        }
    }

    private void proceedWithPayment(ForSaleItem item, float finalPrice, int creditsToUse) {
        try {
            System.out.println("DEBUG: proceedWithPayment called");
            System.out.println("DEBUG: Item: " + item.getTitle() + ", Price: " + finalPrice + ", Credits: " + creditsToUse);
            System.out.println("DEBUG: Item available? " + item.isAvailable());


            if (!item.isAvailable()) {
                System.out.println("DEBUG: Item is no longer available before payment!");
                showError("Sorry, this item has already been sold or is no longer available!");

                loadAllItems();
                return;
            }

            Alert paymentAlert = new Alert(Alert.AlertType.CONFIRMATION);
            paymentAlert.setTitle("Payment Confirmation");
            paymentAlert.setHeaderText("Purchase Summary");
            paymentAlert.setContentText(String.format(
                    "Item: %s\n" +
                            "Original Price: Rs. %.2f\n" +
                            "Credits Used: %d points\n" +
                            "Final Price: Rs. %.2f\n\n" +
                            "Select payment method:",
                    item.getTitle(), item.getPrice(), creditsToUse, finalPrice
            ));

            ButtonType onlineBtn = new ButtonType("Online Payment");
            ButtonType codBtn = new ButtonType("Cash on Delivery");
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            paymentAlert.getButtonTypes().setAll(onlineBtn, codBtn, cancelBtn);

            System.out.println("DEBUG: Showing payment alert");
            paymentAlert.showAndWait().ifPresent(response -> {
                try {
                    System.out.println("DEBUG: Payment response: " + response.getText());

                    if (response == onlineBtn || response == codBtn) {
                        PaymentMethod method = (response == onlineBtn) ? PaymentMethod.ONLINE : PaymentMethod.CASH_ON_DELIVERY;
                        System.out.println("DEBUG: Payment method selected: " + method);

                        User currentUser = MainApp.getCurrentUser();
                        System.out.println("DEBUG: Creating transaction for user: " + currentUser.getName());


                        if (!item.isAvailable()) {
                            System.out.println("DEBUG: Item is no longer available! Refreshing list...");
                            showError("This item is no longer available for purchase!");

                            loadAllItems();
                            return;
                        }


                        Transaction transaction = MainApp.getSystem().createTransaction(currentUser, item, method);
                        System.out.println("DEBUG: Transaction created: " + (transaction != null ? transaction.getTransaction_id() : "null"));

                        if (transaction != null) {

                            if (creditsToUse > 0) {
                                System.out.println("DEBUG: Applying " + creditsToUse + " credits to transaction");
                                transaction.applyCredits(creditsToUse);
                            }


                            Map<String, String> paymentDetails = new HashMap<>();
                            paymentDetails.put("payment_method", method.name());
                            paymentDetails.put("amount", String.valueOf(finalPrice));
                            paymentDetails.put("reference", "TXN_" + System.currentTimeMillis());

                            System.out.println("DEBUG: Payment details: " + paymentDetails);

                            boolean paymentSuccess = MainApp.getSystem().processPayment(transaction, paymentDetails);
                            System.out.println("DEBUG: Payment success: " + paymentSuccess);

                            if (paymentSuccess) {
                                showInfo("Purchase successful!\n" +
                                        "Transaction ID: " + transaction.getTransaction_id() + "\n" +
                                        "Final Price: Rs. " + String.format("%.2f", finalPrice) + "\n" +
                                        "Credits Used: " + creditsToUse + " points");


                                currentItems.remove(item);
                                updateItemList();


                                updateCreditPointsDisplay();
                            } else {
                                showError("Payment processing failed!");
                            }
                        } else {
                            showError("Transaction creation failed!");
                        }
                    } else {
                        System.out.println("DEBUG: Payment cancelled");
                    }
                } catch (Exception ex) {
                    System.out.println("DEBUG: Transaction error: " + ex.getMessage());
                    ex.printStackTrace();


                    if (ex.getMessage() != null && ex.getMessage().contains("no longer available")) {
                        showError("This item was sold while you were completing the purchase. Please browse other items.");

                        loadAllItems();
                    } else {
                        showError("Error processing transaction.", ex);
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("DEBUG: Payment processing failed: " + e.getMessage());
            e.printStackTrace();
            showError("Payment processing failed.", e);
        }
    }

    private void updateCreditPointsDisplay() {
        try {
            System.out.println("DEBUG: updateCreditPointsDisplay called");
            User currentUser = MainApp.getCurrentUser();
            System.out.println("DEBUG: Current user: " + (currentUser != null ? currentUser.getName() : "null"));

            if (currentUser != null) {
                int credits = currentUser.getCredit_points();
                System.out.println("DEBUG: User credits: " + credits);
                creditPointsLabel.setText("Credits: " + credits + " pts");
            } else {
                System.out.println("DEBUG: Current user is null");
                creditPointsLabel.setText("Credits: N/A");
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Credit display error: " + e.getMessage());
            e.printStackTrace();
            creditPointsLabel.setText("Credits: Error");
        }
    }

    private void showError(String message) {
        showError(message, null);
    }

    private void showError(String message, Exception e) {
        System.out.println("ERROR: " + message + (e != null ? " - " + e.getMessage() : ""));
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message + (e != null ? "\nDetails: " + e.getMessage() : ""));
        alert.showAndWait();
    }

    private void showWarning(String message) {
        System.out.println("WARNING: " + message);
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        System.out.println("INFO: " + message);
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