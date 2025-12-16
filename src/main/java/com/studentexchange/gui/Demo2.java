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

public class Demo2 {
    Main main;
    BorderPane root;

    public Demo2(Main main) {
        this.main = main;
        createview();
    }

    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // ===== TOP: Headings =====
        Label mainHeading = new Label("Student Bazaar");
        mainHeading.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label subHeading = new Label("Register");
        subHeading.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

        VBox topBox = new VBox(8, mainHeading, subHeading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(25, 0, 15, 0));
        root.setTop(topBox);

        // ===== CENTER: Register Form =====
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(12);

        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-text-fill: white;");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter name");

        Label emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-text-fill: white;");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter email");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: white;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Label phoneLabel = new Label("Phone:");
        phoneLabel.setStyle("-fx-text-fill: white;");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter phone");

        Label cnicLabel = new Label("CNIC:");
        cnicLabel.setStyle("-fx-text-fill: white;");
        TextField cnicField = new TextField();
        cnicField.setPromptText("Enter CNIC (13 digits)");

        Label addressLabel = new Label("Address:");
        addressLabel.setStyle("-fx-text-fill: white;");
        TextField addressField = new TextField();
        addressField.setPromptText("Enter address");

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(emailLabel, 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(passwordLabel, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(phoneLabel, 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(cnicLabel, 0, 4);
        grid.add(cnicField, 1, 4);
        grid.add(addressLabel, 0, 5);
        grid.add(addressField, 1, 5);

        root.setCenter(grid);

        // ===== BOTTOM: Buttons =====
        Button registerBtn = new Button("Register");
        registerBtn.setPrefSize(90, 35);
        registerBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        registerBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String phone = phoneField.getText().trim();
            String cnic = cnicField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                    phone.isEmpty() || cnic.isEmpty() || address.isEmpty()) {
                showAlert("Error", "Please fill in all fields");
                return;
            }

            if (!main.getSystem().validateCNIC(cnic)) {
                showAlert("Error", "Invalid CNIC format. Please enter 13 digits.");
                return;
            }

            try {
                User newUser = main.getSystem().registerUser(name, cnic, email, password, phone, address);
                showAlert("Success", "Registration successful! Please login.");
                main.showLoginScreen();
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(90, 35);
        backBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        backBtn.setOnAction(e -> main.showDemo());

        HBox buttonBox = new HBox(15, registerBtn, backBtn);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.setPadding(new Insets(0, 20, 20, 0));

        root.setBottom(buttonBox);
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