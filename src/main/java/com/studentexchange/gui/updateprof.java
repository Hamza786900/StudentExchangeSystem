package com.studentexchange.gui;

import com.studentexchange.Main;
import com.studentexchange.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class updateprof {
    Main main;
    BorderPane root;

    public updateprof(Main main) {
        this.main = main;
        createview();
    }

    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // ===== TOP: Heading =====
        Label heading = new Label("Update Profile");
        heading.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        VBox topBox = new VBox(heading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 20, 0));
        root.setTop(topBox);

        User user = main.getCurrentUser();
        if (user == null) {
            Label errorLabel = new Label("No user logged in");
            errorLabel.setStyle("-fx-text-fill: white;");
            root.setCenter(errorLabel);
            return;
        }

        // ===== CENTER: Update Form =====
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.TOP_CENTER);

        // Profile Circle
        Circle profileCircle = new Circle(50, Color.GRAY);
        StackPane profilePane = new StackPane(profileCircle);
        profilePane.setAlignment(Pos.CENTER);
        centerBox.getChildren().add(profilePane);

        // GridPane for labels and text fields
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);

        // Name
        Label nameLabel = new Label("Name");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label nameColon = new Label(":");
        nameColon.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        TextField nameField = new TextField(user.getName());
        grid.add(nameLabel, 0, 0);
        grid.add(nameColon, 1, 0);
        grid.add(nameField, 2, 0);

        // Email
        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label emailColon = new Label(":");
        emailColon.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        TextField emailField = new TextField(user.getEmail());
        grid.add(emailLabel, 0, 1);
        grid.add(emailColon, 1, 1);
        grid.add(emailField, 2, 1);

        // Phone
        Label phoneLabel = new Label("Phone");
        phoneLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label phoneColon = new Label(":");
        phoneColon.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        TextField phoneField = new TextField(user.getPhone());
        grid.add(phoneLabel, 0, 2);
        grid.add(phoneColon, 1, 2);
        grid.add(phoneField, 2, 2);

        // Address
        Label addressLabel = new Label("Address");
        addressLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label addressColon = new Label(":");
        addressColon.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        TextField addressField = new TextField(user.getAddress());
        grid.add(addressLabel, 0, 3);
        grid.add(addressColon, 1, 3);
        grid.add(addressField, 2, 3);

        centerBox.getChildren().add(grid);

        // Confirm Button
        Button confirmBtn = new Button("Confirm");
        confirmBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        confirmBtn.setPrefSize(120, 35);
        confirmBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                showAlert("Error", "All fields must be filled");
                return;
            }

            try {
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setAddress(address);

                showAlert("Success", "Profile updated successfully!");
                main.showProfileScreen();
            } catch (Exception ex) {
                showAlert("Error", "Failed to update profile: " + ex.getMessage());
            }
        });

        HBox buttonBox = new HBox(confirmBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        centerBox.getChildren().add(buttonBox);
        root.setCenter(centerBox);
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