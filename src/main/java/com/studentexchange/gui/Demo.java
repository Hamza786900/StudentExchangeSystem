package com.studentexchange.gui;


import com.studentexchange.Main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Demo  {
    Main main;
    BorderPane root;
    public Demo (Main main) {
        this.main = main;
        createview();
    }
    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // ===== Title =====
        Label title = new Label("Student Bazaar");
        title.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;"
        );

        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 0, 0)); // 👈 moves title DOWN

        root.setTop(topBox);


        // ===== Buttons =====
        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register");

        loginBtn.setPrefSize(140, 40);
        registerBtn.setPrefSize(140, 40);

        loginBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        registerBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        loginBtn.setOnAction(e -> {main.showLoginScreen();});
        registerBtn.setOnAction(e -> {main.showRegisterScreen();});

        VBox centerBox = new VBox(15, loginBtn, registerBtn);
        centerBox.setAlignment(Pos.CENTER);

        root.setCenter(centerBox);




    }
    public BorderPane getroot() {
        return root;
    }
}
