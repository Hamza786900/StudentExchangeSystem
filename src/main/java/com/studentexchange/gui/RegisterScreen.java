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

public class RegisterScreen {
    private MainApp mainApp;
    private VBox view;

    public RegisterScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        createView();
    }

    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #F0F0F5;");

        Label title = new Label("Create New Account");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(12);

        Label nameLabel = new Label("Name:");
        nameLabel.setFont(Font.font("Arial", 14));
        TextField nameField = new TextField();
        nameField.setPrefWidth(250);
        nameField.setStyle("-fx-font-size: 14px;");

        Label cnicLabel = new Label("CNIC:");
        cnicLabel.setFont(Font.font("Arial", 14));
        TextField cnicField = new TextField();
        cnicField.setPromptText("xxxxx-xxxxxxx-x");
        cnicField.setPrefWidth(250);
        cnicField.setStyle("-fx-font-size: 14px;");

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

        Label phoneLabel = new Label("Phone:");
        phoneLabel.setFont(Font.font("Arial", 14));
        TextField phoneField = new TextField();
        phoneField.setPromptText("03xx-xxxxxxx");
        phoneField.setPrefWidth(250);
        phoneField.setStyle("-fx-font-size: 14px;");

        Label addressLabel = new Label("Address:");
        addressLabel.setFont(Font.font("Arial", 14));
        TextField addressField = new TextField();
        addressField.setPrefWidth(250);
        addressField.setStyle("-fx-font-size: 14px;");

        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(cnicLabel, 0, 1);
        grid.add(cnicField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(passLabel, 0, 3);
        grid.add(passField, 1, 3);
        grid.add(phoneLabel, 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(addressLabel, 0, 5);
        grid.add(addressField, 1, 5);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button registerBtn = new Button("Register");
        registerBtn.setPrefSize(120, 35);
        registerBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        registerBtn.setOnAction(e -> {
            String name = nameField.getText();
            String cnic = cnicField.getText();
            String email = emailField.getText();
            String password = passField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();

            if (name.isEmpty() || cnic.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all required fields!");
                alert.showAndWait();
                return;
            }

            User user = MainApp.getSystem().registerUser(name, cnic, email, password, phone, address);
            if (user != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Registration successful! Please login.");
                alert.showAndWait();
                mainApp.showLoginScreen();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Registration failed! Email or CNIC already exists.");
                alert.showAndWait();
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(120, 35);
        backBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> mainApp.showWelcomeScreen());

        buttonBox.getChildren().addAll(registerBtn, backBtn);

        view.getChildren().addAll(title, grid, buttonBox);
    }

    public VBox getView() {
        return view;
    }
}