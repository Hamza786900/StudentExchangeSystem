package com.studentexchange.gui;

import com.studentexchange.Main;
import com.studentexchange.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Demo1 {
    Main main;
    BorderPane root;

    public Demo1(Main main) {
        this.main = main;
        createview();
    }

    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        Label mainHeading = new Label("Student Bazaar");
        mainHeading.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        Label subHeading = new Label("Login");
        subHeading.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: bold;");

        VBox topBox = new VBox(8, mainHeading, subHeading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(25, 0, 15, 0));
        root.setTop(topBox);

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-text-fill: white;");

        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: white;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Button loginBtn = new Button("Login");
        loginBtn.setPrefSize(90, 35);
        loginBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Please fill in all fields");
                return;
            }

            try {
                User user = main.getSystem().login(email, password);
                if (user != null) {
                    main.setCurrentUser(user);
                    showAlert("Success", "Login successful!");
                    main.showDashboardScreen();
                } else {
                    showAlert("Error", "Invalid email or password");
                }
            } catch (Exception ex) {
                showAlert("Error", "Login failed: " + ex.getMessage());
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(90, 35);
        backBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        backBtn.setOnAction(e -> main.showDemo());

        Button forgotBtn = new Button("Forgot Password?");
        forgotBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: teal;");
        forgotBtn.setOnAction(e -> main.showForgotPassword());

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);

        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);

        HBox buttonBox = new HBox(15, loginBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(buttonBox, 1, 2);

        HBox forgotBox = new HBox(forgotBtn);
        forgotBox.setAlignment(Pos.CENTER);
        grid.add(forgotBox, 1, 3);

        root.setCenter(grid);
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