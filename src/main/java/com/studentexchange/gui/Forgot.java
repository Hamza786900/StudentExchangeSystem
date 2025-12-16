package com.studentexchange.gui;


import com.studentexchange.Main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Forgot {

    Main main;
    BorderPane root;
    public Forgot(Main main){
        this.main=main;
        createview();
    }
    private void createview(){
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // ===== TOP: Headings =====
        Label mainHeading = new Label("Student Bazaar");
        mainHeading.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        Label subHeading = new Label("Reset Password");
        subHeading.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

        VBox topBox = new VBox(8, mainHeading, subHeading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(25, 0, 15, 0));

        root.setTop(topBox);

        // ===== CENTER: New Password Form =====
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);

        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setStyle("-fx-text-fill: white;");

        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");

        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordLabel.setStyle("-fx-text-fill: white;");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");

        grid.add(newPasswordLabel, 0, 0);
        grid.add(newPasswordField, 1, 0);
        grid.add(confirmPasswordLabel, 0, 1);
        grid.add(confirmPasswordField, 1, 1);

        // ===== Buttons =====
        Button submitBtn = new Button("Submit");
        submitBtn.setPrefSize(90, 35);
        submitBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        submitBtn.setOnAction(e -> {main.showLoginScreen();});

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(90, 35);
        backBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        backBtn.setOnAction(e -> {main.showLoginScreen();});

        HBox buttonBox = new HBox(15, submitBtn, backBtn);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        grid.add(buttonBox, 1, 2);

        root.setCenter(grid);


    }
    public BorderPane getroot(){
        return root;
    }
}
