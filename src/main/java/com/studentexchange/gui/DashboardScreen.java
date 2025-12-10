package com.studentexchange.gui;

import com.studentexchange.MainApp;
import com.studentexchange.models.Transaction;
import com.studentexchange.models.User;
import com.studentexchange.models.Item;
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

            HBox topBox = new HBox();
            topBox.setAlignment(Pos.CENTER_LEFT);
            topBox.setSpacing(300);

            VBox infoBox = new VBox(5);
            Label welcome = new Label("Welcome, " + user.getName());
            welcome.setFont(Font.font("Arial", 24));
            welcome.setStyle("-fx-font-weight: bold;");

            Label info = new Label("Credit Points: " + user.getCredit_points() + " | Rating: " + String.format("%.1f", user.getAverage_rating()));
            info.setFont(Font.font("Arial", 14));
            info.setStyle("-fx-text-fill: #555555;");

            infoBox.getChildren().addAll(welcome, info);

            Button logoutBtn = new Button("Logout");
            logoutBtn.setPrefSize(100, 35);
            logoutBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
            logoutBtn.setOnAction(e -> {
                try {
                    MainApp.setCurrentUser(null);
                    mainApp.showWelcomeScreen();
                } catch (Exception ex) {
                    showError("Failed to logout.", ex);
                }
            });

            topBox.getChildren().addAll(infoBox, logoutBtn);

            GridPane buttonGrid = new GridPane();
            buttonGrid.setAlignment(Pos.CENTER);
            buttonGrid.setHgap(20);
            buttonGrid.setVgap(20);
            buttonGrid.setPadding(new Insets(30));

            Button uploadBtn = new Button("Upload Item");
            uploadBtn.setPrefSize(200, 60);
            uploadBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            uploadBtn.setOnAction(e -> {
                try {
                    mainApp.showUploadScreen();
                } catch (Exception ex) {
                    showError("Failed to open upload screen.", ex);
                }
            });

            Button browseBtn = new Button("Browse Items");
            browseBtn.setPrefSize(200, 60);
            browseBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            browseBtn.setOnAction(e -> {
                try {
                    mainApp.showBrowseScreen();
                } catch (Exception ex) {
                    showError("Failed to open browse screen.", ex);
                }
            });

            Button myItemsBtn = new Button("My Uploaded Items");
            myItemsBtn.setPrefSize(200, 60);
            myItemsBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            myItemsBtn.setOnAction(e -> {
                try {
                    showMyItems();
                } catch (Exception ex) {
                    showError("Failed to show uploaded items.", ex);
                }
            });

            Button transBtn = new Button("My Transactions");
            transBtn.setPrefSize(200, 60);
            transBtn.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            transBtn.setOnAction(e -> {
                try {
                    showMyTransactions();
                } catch (Exception ex) {
                    showError("Failed to show transactions.", ex);
                }
            });

            Button profileBtn = new Button("My Profile");
            profileBtn.setPrefSize(200, 60);
            profileBtn.setStyle("-fx-background-color: #00BCD4; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            profileBtn.setOnAction(e -> {
                try {
                    showProfile();
                } catch (Exception ex) {
                    showError("Failed to show profile.", ex);
                }
            });

            Button statsBtn = new Button("Statistics");
            statsBtn.setPrefSize(200, 60);
            statsBtn.setStyle("-fx-background-color: #607D8B; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            statsBtn.setOnAction(e -> {
                try {
                    showStatistics();
                } catch (Exception ex) {
                    showError("Failed to show statistics.", ex);
                }
            });

            buttonGrid.add(uploadBtn, 0, 0);
            buttonGrid.add(browseBtn, 1, 0);
            buttonGrid.add(myItemsBtn, 2, 0);
            buttonGrid.add(transBtn, 0, 1);
            buttonGrid.add(profileBtn, 1, 1);
            buttonGrid.add(statsBtn, 2, 1);

            view.getChildren().addAll(topBox, buttonGrid);
        } catch (Exception e) {
            showError("Failed to create dashboard view.", e);
        }
    }

    private void showMyItems() {
        try {
            User user = MainApp.getCurrentUser();
            List<Item> myItems = MainApp.getSystem().getCatalog().getItemsBySeller(user);

            StringBuilder message = new StringBuilder("Your Uploaded Items:\n\n");
            if (myItems.isEmpty()) {
                message.append("You haven't uploaded any items yet.");
            } else {
                for (Item item : myItems) {
                    message.append("• ").append(item.getTitle())
                            .append(" (").append(item.getCategory()).append(")\n");
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("My Items");
            alert.setHeaderText(null);
            alert.setContentText(message.toString());
            alert.showAndWait();
        } catch (Exception e) {
            showError("Failed to retrieve uploaded items.", e);
        }
    }

    private void showMyTransactions() {
        try {
            User user = MainApp.getCurrentUser();
            List<Transaction> buyerTrans = user.getTransactions_as_buyer();
            List<Transaction> sellerTrans = user.getTransactions_as_seller();

            StringBuilder message = new StringBuilder();
            message.append("=== PURCHASES ===\n");
            if (buyerTrans.isEmpty()) {
                message.append("No purchases yet.\n");
            } else {
                for (Transaction t : buyerTrans) {
                    message.append("• ").append(t.getItem().getTitle())
                            .append(" - Rs.").append(t.getItem().getPrice()).append("\n");
                }
            }

            message.append("\n=== SALES ===\n");
            if (sellerTrans.isEmpty()) {
                message.append("No sales yet.\n");
            } else {
                for (Transaction t : sellerTrans) {
                    message.append("• ").append(t.getItem().getTitle())
                            .append(" - Rs.").append(t.getItem().getPrice()).append("\n");
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("My Transactions");
            alert.setHeaderText(null);
            alert.setContentText(message.toString());
            alert.showAndWait();
        } catch (Exception e) {
            showError("Failed to retrieve transactions.", e);
        }
    }

    private void showProfile() {
        try {
            User user = MainApp.getCurrentUser();

            String profile = "=== YOUR PROFILE ===\n\n" +
                    "Name: " + user.getName() + "\n" +
                    "Email: " + user.getEmail() + "\n" +
                    "Phone: " + user.getPhone() + "\n" +
                    "Address: " + user.getAddress() + "\n" +
                    "Credit Points: " + user.getCredit_points() + "\n" +
                    "Rating: " + user.getAverage_rating() + "\n" +
                    "Total Transactions: " + user.getTotalTransactions();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("My Profile");
            alert.setHeaderText(null);
            alert.setContentText(profile);
            alert.showAndWait();
        } catch (Exception e) {
            showError("Failed to show profile.", e);
        }
    }

    private void showStatistics() {
        try {
            User user = MainApp.getCurrentUser();

            String stats = "=== YOUR STATISTICS ===\n\n" +
                    "Total Spent: Rs." + user.getTotalSpent() + "\n" +
                    "Total Earned: Rs." + user.getTotalEarned() + "\n" +
                    "Total Transactions: " + user.getTotalTransactions() + "\n" +
                    "Buyer Rating: " + user.getBuyerRating() + "\n" +
                    "Seller Rating: " + user.getSellerRating();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Statistics");
            alert.setHeaderText(null);
            alert.setContentText(stats);
            alert.showAndWait();
        } catch (Exception e) {
            showError("Failed to show statistics.", e);
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

    public VBox getView() {
        return view;
    }
}
