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

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);


        Circle profileCircle = new Circle(50, Color.GRAY);
        StackPane profilePane = new StackPane(profileCircle);
        profilePane.setAlignment(Pos.CENTER);
        grid.add(profilePane, 0, 0, 3, 1);


        Label nameLabel = new Label("Name");
        Label colon1 = new Label(":");
        Label nameValue = new Label(user.getName());
        grid.add(nameLabel, 0, 1);
        grid.add(colon1, 1, 1);
        grid.add(nameValue, 2, 1);


        Label emailLabel = new Label("Email");
        Label colon2 = new Label(":");
        Label emailValue = new Label(user.getEmail());
        grid.add(emailLabel, 0, 2);
        grid.add(colon2, 1, 2);
        grid.add(emailValue, 2, 2);


        Label cnicLabel = new Label("CNIC");
        Label colon3 = new Label(":");
        Label cnicValue = new Label(user.getCnic());
        grid.add(cnicLabel, 0, 3);
        grid.add(colon3, 1, 3);
        grid.add(cnicValue, 2, 3);


        Label phoneLabel = new Label("Phone");
        Label colon4 = new Label(":");
        Label phoneValue = new Label(user.getPhone());
        grid.add(phoneLabel, 0, 4);
        grid.add(colon4, 1, 4);
        grid.add(phoneValue, 2, 4);


        Label addressLabel = new Label("Address");
        Label colon5 = new Label(":");
        Label addressValue = new Label(user.getAddress());
        grid.add(addressLabel, 0, 5);
        grid.add(colon5, 1, 5);
        grid.add(addressValue, 2, 5);


        Label creditLabel = new Label("Credit Points");
        Label colon6 = new Label(":");
        Label creditValue = new Label(String.valueOf(user.getCredit_points()));
        grid.add(creditLabel, 0, 6);
        grid.add(colon6, 1, 6);
        grid.add(creditValue, 2, 6);


        Label ratingLabel = new Label("Rating");
        Label colon7 = new Label(":");
        Label ratingValue = new Label(String.format("%.1f", user.getAverage_rating()));
        grid.add(ratingLabel, 0, 7);
        grid.add(colon7, 1, 7);
        grid.add(ratingValue, 2, 7);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");


        Label regLabel = new Label("Registration Date");
        Label colon8 = new Label(":");

        Label regValue = new Label(sdf.format(user.getRegistration_date()));

        grid.add(regLabel, 0, 8);
        grid.add(colon8, 1, 8);
        grid.add(regValue, 2, 8);


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


    public BorderPane getroot() {
        return root;
    }
}