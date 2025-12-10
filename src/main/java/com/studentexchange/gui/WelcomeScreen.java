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
        createView();
    }

    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setStyle("-fx-background-color: #F0F0F5;");

        Label title = new Label("BAZAAR");
        title.setFont(Font.font("Arial", 40));
        title.setStyle("-fx-font-weight: bold;");

        Label subtitle = new Label("Student Book Exchange Platform");
        subtitle.setFont(Font.font("Arial", 18));

        Label desc1 = new Label("Buy and sell textbooks at affordable prices");
        desc1.setFont(Font.font("Arial", 14));

        Label desc2 = new Label("Share notes and past papers");
        desc2.setFont(Font.font("Arial", 14));

        Label desc3 = new Label("Earn credit points for contributions");
        desc3.setFont(Font.font("Arial", 14));

        Button loginBtn = new Button("Login");
        loginBtn.setPrefSize(200, 40);
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        loginBtn.setOnAction(e -> mainApp.showLoginScreen());

        Button registerBtn = new Button("Register");
        registerBtn.setPrefSize(200, 40);
        registerBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        registerBtn.setOnAction(e -> mainApp.showRegisterScreen());

        view.getChildren().addAll(title, subtitle, desc1, desc2, desc3, loginBtn, registerBtn);
    }

    public VBox getView() {
        return view;
    }
}