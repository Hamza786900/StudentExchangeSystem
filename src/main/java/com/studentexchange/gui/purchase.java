package com.studentexchange.gui;

import com.studentexchange.Main;
import com.studentexchange.enums.PaymentMethod;
import com.studentexchange.models.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class purchase {
    Main main;
    BorderPane root;
    private Transaction currentTransaction;
    private Label discountLabel;
    private Label finalPriceLabel;
    private int creditsToUse = 0;

    public purchase(Main main) {
        this.main = main;
        createview();
    }

    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // ===== TOP: Heading =====
        Label heading = new Label("Purchase Item");
        heading.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        VBox topBox = new VBox(heading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 15, 0));
        root.setTop(topBox);

        // ===== CENTER: Form =====
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);

        User currentUser = main.getCurrentUser();
        // Fallback for null user (shouldn't happen if flow is followed)
        if (currentUser == null) {
            root.setCenter(new Label("User not logged in. Cannot proceed to purchase."));
            return;
        }

        // Name, Email, Phone, Address fields initialized with user data (Code unchanged)
        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-text-fill: white;");
        TextField nameField = new TextField(currentUser.getName());

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-text-fill: white;");
        TextField emailField = new TextField(currentUser.getEmail());

        Label phoneLabel = new Label("Phone:");
        phoneLabel.setStyle("-fx-text-fill: white;");
        TextField phoneField = new TextField(currentUser.getPhone());

        Label addressLabel = new Label("Address:");
        addressLabel.setStyle("-fx-text-fill: white;");
        TextField addressField = new TextField(currentUser.getAddress());

        // Payment Method
        Label paymentLabel = new Label("Payment Method:");
        paymentLabel.setStyle("-fx-text-fill: white;");
        ComboBox<PaymentMethod> paymentBox = new ComboBox<>();
        paymentBox.getItems().addAll(PaymentMethod.values());
        paymentBox.setValue(PaymentMethod.CASH_ON_DELIVERY);

        // Credit points option
        Label creditLabel = new Label("Use Credit Points for Discount?");
        creditLabel.setStyle("-fx-text-fill: white;");
        ToggleGroup creditGroup = new ToggleGroup();
        RadioButton yesBtn = new RadioButton("Yes");
        yesBtn.setStyle("-fx-text-fill: white;");
        yesBtn.setToggleGroup(creditGroup);
        RadioButton noBtn = new RadioButton("No");
        noBtn.setStyle("-fx-text-fill: white;");
        noBtn.setToggleGroup(creditGroup);
        noBtn.setSelected(true);

        HBox creditBox = new HBox(20, yesBtn, noBtn);
        creditBox.setAlignment(Pos.CENTER_LEFT);

        // Available credits
        Label availableCreditsLabel = new Label("Available Credits: " + currentUser.getCredit_points());
        availableCreditsLabel.setStyle("-fx-text-fill: white;");

        // Discount and final price labels
        discountLabel = new Label("Discount Applied: Rs. 0");
        discountLabel.setStyle("-fx-text-fill: white;");
        finalPriceLabel = new Label("Final Price: Rs. 0");
        finalPriceLabel.setStyle("-fx-text-fill: white;");

        // Update prices when credit option changes
        yesBtn.setOnAction(e -> calculateDiscount(true));
        noBtn.setOnAction(e -> calculateDiscount(false));

        // Add elements to grid (Code unchanged)
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(emailLabel, 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(phoneLabel, 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(addressLabel, 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(paymentLabel, 0, 4);
        grid.add(paymentBox, 1, 4);
        grid.add(creditLabel, 0, 5);
        grid.add(creditBox, 1, 5);
        grid.add(availableCreditsLabel, 0, 6, 2, 1);
        grid.add(discountLabel, 0, 7, 2, 1);
        grid.add(finalPriceLabel, 0, 8, 2, 1);

        root.setCenter(grid);

        // Initialize price display
        calculateDiscount(false);

        // ===== BOTTOM: Checkout Button =====
        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        checkoutBtn.setPrefSize(100, 35);
        checkoutBtn.setOnAction(e -> {
            try {
                if (paymentBox.getValue() == null) {
                    showAlert("Error", "Please select a payment method");
                    return;
                }

                showAlert("Success", "Purchase initiated successfully! (Transaction details skipped for simplicity)");

                // --- LINKING ---
                main.showDashboardScreen();
            } catch (Exception ex) {
                showAlert("Error", "Purchase failed: " + ex.getMessage());
            }
        });

        HBox bottomBox = new HBox(checkoutBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15, 0, 15, 0));

        root.setBottom(bottomBox);
    }

    private void calculateDiscount(boolean useCredits) {
        float itemPrice = 1000.0f;
        float discount = 0.0f;

        if (useCredits) {
            User user = main.getCurrentUser();
            int availableCredits = user.getCredit_points();

            float maxDiscount = availableCredits * 10.0f;
            discount = Math.min(maxDiscount, itemPrice);
            creditsToUse = (int)(discount / 10.0f);
        } else {
            creditsToUse = 0;
        }

        float finalPrice = itemPrice - discount;

        discountLabel.setText(String.format("Discount Applied: Rs. %.2f", discount));
        finalPriceLabel.setText(String.format("Final Price: Rs. %.2f", finalPrice));
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