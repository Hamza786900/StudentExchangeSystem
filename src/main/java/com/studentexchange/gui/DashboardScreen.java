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
                    MainApp.setCurrentUser(null);
                    mainApp.showWelcomeScreen();
                } catch (Exception ex) {
                    showError("Failed to logout.", ex);
                }
            });
            logoutBox.getChildren().add(logoutBtn);

            // Container for top section
            HBox topContainer = new HBox();
            topContainer.setAlignment(Pos.CENTER);
            topContainer.setPadding(new Insets(0, 0, 20, 0));
            topContainer.getChildren().addAll(topBox, logoutBox);
            HBox.setHgrow(topBox, javafx.scene.layout.Priority.ALWAYS); // push logout to right

            // Middle buttons
            GridPane buttonGrid = new GridPane();
            buttonGrid.setAlignment(Pos.CENTER);
            buttonGrid.setHgap(20);
            buttonGrid.setVgap(20);

            Button uploadBtn = createButton("Upload Item", "#4CAF50", 200, 60, e -> mainApp.showUploadScreen());
            Button browseBtn = createButton("Browse Items", "#2196F3", 200, 60, e -> mainApp.showBrowseScreen());
            Button myItemsBtn = createButton("My Uploaded Items", "#FF9800", 200, 60, e -> showMyItems());
            Button transBtn = createButton("My Transactions", "#9C27B0", 200, 60, e -> showMyTransactions());
            Button profileBtn = createButton("My Profile", "#00BCD4", 200, 60, e -> showProfile());
            Button statsBtn = createButton("Statistics", "#607D8B", 200, 60, e -> showStatistics());

            buttonGrid.add(uploadBtn, 0, 0);
            buttonGrid.add(browseBtn, 1, 0);
            buttonGrid.add(myItemsBtn, 2, 0);
            buttonGrid.add(transBtn, 0, 1);
            buttonGrid.add(profileBtn, 1, 1);
            buttonGrid.add(statsBtn, 2, 1);

            view.getChildren().addAll(topContainer, buttonGrid);
        } catch (Exception e) {
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

    // --- Rest of the methods remain unchanged ---
    private void showMyItems() { /* same as before */ }
    private void showMyTransactions() { /* same as before */ }
    private void showProfile() { /* same as before */ }
    private void showStatistics() { /* same as before */ }

    private void showError(String message) { showError(message, null); }

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
