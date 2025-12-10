package com.studentexchange.gui;

import com.studentexchange.MainApp;
import com.studentexchange.models.Transaction;
import com.studentexchange.models.User;
import com.studentexchange.models.Item;
import com.studentexchange.models.ForSaleItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.List;
import java.util.ArrayList;

public class DashboardScreen {
    private MainApp mainApp;
    private VBox view;

    public DashboardScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        try {
            createView();
        } catch (Exception e) {
            showError("Failed to initialize dashboard screen.", e);
        }
    }

    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #F0F0F5;");

        try {
            User user = MainApp.getCurrentUser();
            System.out.println("DEBUG: DashboardScreen - Current user: " + (user != null ? user.getName() : "null"));

            // Top HBox for welcome label
            HBox topBox = new HBox();
            topBox.setAlignment(Pos.CENTER_LEFT);
            topBox.setPadding(new Insets(0, 0, 20, 0));

            Label welcome = new Label("Welcome, " + user.getName());
            welcome.setFont(Font.font("Arial", 24));
            welcome.setStyle("-fx-font-weight: bold;");

            topBox.getChildren().add(welcome);

            // Logout button at top-right
            HBox logoutBox = new HBox();
            logoutBox.setAlignment(Pos.TOP_RIGHT);
            Button logoutBtn = new Button("Logout");
            logoutBtn.setPrefSize(100, 35);
            logoutBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
            logoutBtn.setOnAction(e -> {
                try {
                    System.out.println("DEBUG: Logout button clicked");
                    MainApp.setCurrentUser(null);
                    mainApp.showWelcomeScreen();
                } catch (Exception ex) {
                    System.out.println("DEBUG: Logout error: " + ex.getMessage());
                    ex.printStackTrace();
                    showError("Failed to logout.", ex);
                }
            });
            logoutBox.getChildren().add(logoutBtn);

            // Container for top section
            HBox topContainer = new HBox();
            topContainer.setAlignment(Pos.CENTER);
            topContainer.setPadding(new Insets(0, 0, 20, 0));
            topContainer.getChildren().addAll(topBox, logoutBox);
            HBox.setHgrow(topBox, javafx.scene.layout.Priority.ALWAYS);

            // Middle buttons
            GridPane buttonGrid = new GridPane();
            buttonGrid.setAlignment(Pos.CENTER);
            buttonGrid.setHgap(20);
            buttonGrid.setVgap(20);

            // Upload and Browse buttons
            Button uploadBtn = createButton("Upload Item", "#4CAF50", 200, 60, e -> {
                System.out.println("DEBUG: Upload Item button clicked");
                try {
                    mainApp.showUploadScreen();
                } catch (Exception ex) {
                    System.out.println("DEBUG: Upload screen error: " + ex.getMessage());
                    ex.printStackTrace();
                    showError("Failed to open upload screen.", ex);
                }
            });

            Button browseBtn = createButton("Browse Items", "#2196F3", 200, 60, e -> {
                System.out.println("DEBUG: Browse Items button clicked");
                try {
                    mainApp.showBrowseScreen();
                } catch (Exception ex) {
                    System.out.println("DEBUG: Browse screen error: " + ex.getMessage());
                    ex.printStackTrace();
                    showError("Failed to open browse screen.", ex);
                }
            });

            // Fixed buttons - using existing methods
            Button myItemsBtn = createButton("My Uploaded Items", "#FF9800", 200, 60, e -> {
                System.out.println("DEBUG: My Uploaded Items button clicked");
                try {
                    showMyItems();
                } catch (Exception ex) {
                    System.out.println("DEBUG: My Items error: " + ex.getMessage());
                    ex.printStackTrace();
                    showError("Failed to show your uploaded items.", ex);
                }
            });

            Button transBtn = createButton("My Transactions", "#9C27B0", 200, 60, e -> {
                System.out.println("DEBUG: My Transactions button clicked");
                try {
                    showMyTransactions();
                } catch (Exception ex) {
                    System.out.println("DEBUG: Transactions error: " + ex.getMessage());
                    ex.printStackTrace();
                    showError("Failed to show your transactions.", ex);
                }
            });

            Button profileBtn = createButton("My Profile", "#00BCD4", 200, 60, e -> {
                System.out.println("DEBUG: My Profile button clicked");
                try {
                    showProfile();
                } catch (Exception ex) {
                    System.out.println("DEBUG: Profile error: " + ex.getMessage());
                    ex.printStackTrace();
                    showError("Failed to show profile.", ex);
                }
            });

            Button statsBtn = createButton("Statistics", "#607D8B", 200, 60, e -> {
                System.out.println("DEBUG: Statistics button clicked");
                try {
                    showStatistics();
                } catch (Exception ex) {
                    System.out.println("DEBUG: Statistics error: " + ex.getMessage());
                    ex.printStackTrace();
                    showError("Failed to show statistics.", ex);
                }
            });

            buttonGrid.add(uploadBtn, 0, 0);
            buttonGrid.add(browseBtn, 1, 0);
            buttonGrid.add(myItemsBtn, 2, 0);
            buttonGrid.add(transBtn, 0, 1);
            buttonGrid.add(profileBtn, 1, 1);
            buttonGrid.add(statsBtn, 2, 1);

            view.getChildren().addAll(topContainer, buttonGrid);
        } catch (Exception e) {
            System.out.println("DEBUG: Dashboard creation error: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to create dashboard view.", e);
        }
    }

    private Button createButton(String text, String color, int width, int height, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button btn = new Button(text);
        btn.setPrefSize(width, height);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        btn.setOnAction(handler);
        return btn;
    }

    private void showMyItems() {
        System.out.println("DEBUG: showMyItems method called");
        try {
            User currentUser = MainApp.getCurrentUser();
            if (currentUser == null) {
                showError("You must be logged in!");
                return;
            }

            // Get user's uploaded items using Catalog's getItemsBySeller method
            List<Item> myItems = MainApp.getSystem().getCatalog().getItemsBySeller(currentUser);

            if (myItems.isEmpty()) {
                showInfo("You haven't uploaded any items yet.");
            } else {
                StringBuilder itemsList = new StringBuilder();
                itemsList.append("Your Uploaded Items:\n\n");
                for (Item item : myItems) {
                    itemsList.append("• ").append(item.getTitle())
                            .append(" (").append(item.getSubject()).append(")\n");
                    if (item instanceof ForSaleItem) {
                        ForSaleItem forSale = (ForSaleItem) item;
                        itemsList.append("  Price: Rs.").append(String.format("%.2f", forSale.getPrice()))
                                .append(" | Views: ").append(item.getViews())
                                .append(" | Status: ").append(forSale.isIs_sold() ? "SOLD" : "AVAILABLE").append("\n");
                    } else {
                        itemsList.append("  Free | Views: ").append(item.getViews()).append("\n");
                    }
                    itemsList.append("\n");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("My Uploaded Items");
                alert.setHeaderText(null);
                alert.setContentText(itemsList.toString());
                alert.setWidth(400);
                alert.setHeight(300);
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("DEBUG: showMyItems error: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to load your uploaded items.", e);
        }
    }

    private void showMyTransactions() {
        System.out.println("DEBUG: showMyTransactions method called");
        try {
            User currentUser = MainApp.getCurrentUser();
            if (currentUser == null) {
                showError("You must be logged in!");
                return;
            }

            // Get all transactions from system
            List<Transaction> allTransactions = MainApp.getSystem().getTransactions();
            List<Transaction> userTransactions = new ArrayList<>();

            // Filter transactions where user is buyer or seller
            for (Transaction trans : allTransactions) {
                if (trans.getBuyer().equals(currentUser) || trans.getSeller().equals(currentUser)) {
                    userTransactions.add(trans);
                }
            }

            if (userTransactions.isEmpty()) {
                showInfo("You don't have any transactions yet.");
            } else {
                StringBuilder transList = new StringBuilder();
                transList.append("Your Transactions:\n\n");
                for (Transaction trans : userTransactions) {
                    transList.append("• Transaction ID: ").append(trans.getTransaction_id()).append("\n");
                    transList.append("  Item: ").append(trans.getItem().getTitle()).append("\n");
                    transList.append("  Amount: Rs.").append(String.format("%.2f", trans.calculateTotal())).append("\n");
                    transList.append("  Role: ").append(trans.getBuyer().equals(currentUser) ? "Buyer" : "Seller").append("\n");
                    transList.append("  Date: ").append(trans.getTransaction_date()).append("\n");
                    transList.append("  Status: ").append(trans.getPayment_status()).append("\n\n");
                }

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("My Transactions");
                alert.setHeaderText(null);
                alert.setContentText(transList.toString());
                alert.setWidth(500);
                alert.setHeight(400);
                alert.showAndWait();
            }
        } catch (Exception e) {
            System.out.println("DEBUG: showMyTransactions error: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to load your transactions.", e);
        }
    }

    private void showProfile() {
        System.out.println("DEBUG: showProfile method called");
        try {
            User user = MainApp.getCurrentUser();
            if (user == null) {
                showError("You must be logged in!");
                return;
            }

            StringBuilder profile = new StringBuilder();
            profile.append("Your Profile:\n\n");
            profile.append("Name: ").append(user.getName()).append("\n");
            profile.append("Email: ").append(user.getEmail()).append("\n");
            profile.append("User ID: ").append(user.getUser_id()).append("\n");
            profile.append("Credit Points: ").append(user.getCredit_points()).append("\n");
            profile.append("Member Since: ").append(user.getRegistration_date()).append("\n");
            profile.append("Phone: ").append(user.getPhone()).append("\n");
            profile.append("Address: ").append(user.getAddress()).append("\n");
            profile.append("Verified: ").append(user.isIs_verified() ? "Yes" : "No").append("\n");
            profile.append("Average Rating: ").append(String.format("%.1f", user.getAverage_rating())).append("/5.0").append("\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("My Profile");
            alert.setHeaderText(null);
            alert.setContentText(profile.toString());
            alert.showAndWait();
        } catch (Exception e) {
            System.out.println("DEBUG: showProfile error: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to load profile.", e);
        }
    }

    private void showStatistics() {
        System.out.println("DEBUG: showStatistics method called");
        try {
            User user = MainApp.getCurrentUser();
            if (user == null) {
                showError("You must be logged in!");
                return;
            }

            // Get statistics using existing methods
            int totalItems = MainApp.getSystem().getCatalog().getItemCount();
            List<Item> myUploads = MainApp.getSystem().getCatalog().getItemsBySeller(user);
            int myUploadsCount = myUploads.size();

            // Count total views for user's items
            int totalViews = 0;
            for (Item item : myUploads) {
                totalViews += item.getViews();
            }

            // Count user's transactions
            List<Transaction> allTransactions = MainApp.getSystem().getTransactions();
            int myPurchases = 0;
            for (Transaction trans : allTransactions) {
                if (trans.getBuyer().equals(user)) {
                    myPurchases++;
                }
            }

            StringBuilder stats = new StringBuilder();
            stats.append("Statistics:\n\n");
            stats.append("Total Items in Catalog: ").append(totalItems).append("\n");
            stats.append("Your Uploaded Items: ").append(myUploadsCount).append("\n");
            stats.append("Your Purchases: ").append(myPurchases).append("\n");
            stats.append("Total Views on Your Items: ").append(totalViews).append("\n");
            stats.append("Your Credit Points: ").append(user.getCredit_points()).append("\n");
            stats.append("Average Rating: ").append(String.format("%.1f", user.getAverage_rating())).append("/5.0").append("\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Statistics");
            alert.setHeaderText(null);
            alert.setContentText(stats.toString());
            alert.showAndWait();
        } catch (Exception e) {
            System.out.println("DEBUG: showStatistics error: " + e.getMessage());
            e.printStackTrace();
            showError("Failed to load statistics.", e);
        }
    }

    private void showError(String message) {
        showError(message, null);
    }

    private void showError(String message, Exception e) {
        System.out.println("ERROR in DashboardScreen: " + message + (e != null ? " - " + e.getMessage() : ""));
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message + (e != null ? "\nDetails: " + e.getMessage() : ""));
        alert.showAndWait();
    }

    private void showInfo(String message) {
        System.out.println("INFO in DashboardScreen: " + message);
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