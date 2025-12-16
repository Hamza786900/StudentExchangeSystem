package com.studentexchange.gui;

import com.studentexchange.Main;
import com.studentexchange.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.text.SimpleDateFormat;

public class Prof {
    Main main;
    BorderPane root;

    public Prof(Main main) {
        this.main = main;
        createview();
    }

    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // ===== TOP: Heading =====
        Label heading = new Label("Profile");
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

        // ===== CENTER: Profile Info =====
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);

        // Profile Circle
        Circle profileCircle = new Circle(50, Color.GRAY);
        StackPane profilePane = new StackPane(profileCircle);
        profilePane.setAlignment(Pos.CENTER);
        grid.add(profilePane, 0, 0, 3, 1);

        // Name
        addProfileRow(grid, "Name", user.getName(), 1);

        // Email
        addProfileRow(grid, "Email", user.getEmail(), 2);

        // CNIC
        addProfileRow(grid, "CNIC", user.getCnic(), 3);

        // Phone
        addProfileRow(grid, "Phone", user.getPhone(), 4);

        // Address
        addProfileRow(grid, "Address", user.getAddress(), 5);

        // Credit Points
        addProfileRow(grid, "Credit Points", String.valueOf(user.getCredit_points()), 6);

        // Rating
        addProfileRow(grid, "Rating", String.format("%.1f", user.getAverage_rating()), 7);

        // Registration Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        addProfileRow(grid, "Registration Date", sdf.format(user.getRegistration_date()), 8);

        // Update Profile Button
        Button updateBtn = new Button("Update Profile");
        updateBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        updateBtn.setPrefSize(150, 35);
        updateBtn.setOnAction(e -> main.showUpdateProfile());
        HBox buttonBox = new HBox(updateBtn);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        VBox centerBox = new VBox(10, grid, buttonBox);
        centerBox.setAlignment(Pos.CENTER);
        root.setCenter(centerBox);
    }

    private void addProfileRow(GridPane grid, String label, String value, int row) {
        Label fieldLabel = new Label(label);
        fieldLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label colon = new Label(":");
        colon.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: white;");

        grid.add(fieldLabel, 0, row);
        grid.add(colon, 1, row);
        grid.add(valueLabel, 2, row);
    }

    public BorderPane getroot() {
        return root;
    }
}