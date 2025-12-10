package com.studentexchange.gui;

import com.studentexchange.MainApp;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class WelcomeScreen {
    private MainApp mainApp;
    private VBox view;

    public WelcomeScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        try {
            createView();
        } catch (Exception e) {
            showError("Failed to initialize welcome screen.", e);
        }
    }

    private void createView() {
        view = new VBox(30); // spacing increased for visual separation
        view.setAlignment(Pos.CENTER);
        view.setStyle("-fx-background-color: #F0F0F5;");

        // Heading
        Label title = new Label("STUDENT BAZAAR");
        title.setFont(Font.font("Arial", 40));
        title.setStyle("-fx-font-weight: bold;");

        // Buttons
        Button loginBtn = new Button("Login");
        loginBtn.setPrefSize(200, 40);
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        loginBtn.setOnAction(e -> {
            try {
                mainApp.showLoginScreen();
            } catch (Exception ex) {
                showError("Failed to open login screen.", ex);
            }
        });

        Button registerBtn = new Button("Register");
        registerBtn.setPrefSize(200, 40);
        registerBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        registerBtn.setOnAction(e -> {
            try {
                mainApp.showRegisterScreen();
            } catch (Exception ex) {
                showError("Failed to open register screen.", ex);
            }
        });

        // Add only heading and buttons to the view
        view.getChildren().addAll(title, loginBtn, registerBtn);
    }

    private void showError(String message, Exception e) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message + (e != null ? "\nDetails: " + e.getMessage() : ""));
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}
