package com.studentexchange.gui;

import com.studentexchange.MainApp;
import com.studentexchange.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LoginScreen {
    private MainApp mainApp;
    private VBox view;

    public LoginScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        try {
            createView();
        } catch (Exception e) {
            showError("Failed to initialize login screen.", e);
        }
    }

    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(50));
        view.setStyle("-fx-background-color: #F0F0F5;");


        Label title = new Label("STUDENT BAZAAR");
        title.setFont(Font.font("Arial", 36));
        title.setStyle("-fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);

        Label emailLabel = new Label("Email:");
        emailLabel.setFont(Font.font("Arial", 14));
        TextField emailField = new TextField();
        emailField.setPrefWidth(250);
        emailField.setStyle("-fx-font-size: 14px;");

        Label passLabel = new Label("Password:");
        passLabel.setFont(Font.font("Arial", 14));
        PasswordField passField = new PasswordField();
        passField.setPrefWidth(250);
        passField.setStyle("-fx-font-size: 14px;");

        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Login");
        loginBtn.setPrefSize(120, 35);
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        loginBtn.setOnAction(e -> {
            try {
                String email = emailField.getText();
                String password = passField.getText();

                User user = MainApp.getSystem().login(email, password);
                if (user != null) {
                    MainApp.setCurrentUser(user);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Welcome " + user.getName() + "!");
                    alert.showAndWait();
                    mainApp.showDashboard();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong email or password!");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                showError("Login failed.", ex);
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(120, 35);
        backBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> {
            try {
                mainApp.showWelcomeScreen();
            } catch (Exception ex) {
                showError("Failed to go back.", ex);
            }
        });

        buttonBox.getChildren().addAll(loginBtn, backBtn);


        view.getChildren().addAll(title, grid, buttonBox);
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
