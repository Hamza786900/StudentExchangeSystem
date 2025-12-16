package com.studentexchange.gui;

import com.studentexchange.Main;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class DashboardWrapper {
    private Main main;
    private BorderPane dashboardRoot;
    private Text usernameText;

    public DashboardWrapper(Main main) {
        this.main = main;
        createDashboard();
    }

    private void createDashboard() {
        dashboardRoot = new BorderPane();
        dashboardRoot.setStyle("-fx-background-color: #000000;");

        // ===== LEFT MENU =====
        VBox menu = new VBox(20);
        menu.setPadding(new Insets(20, 10, 20, 10));
        menu.setStyle("-fx-background-color: #1c1c1c;");
        menu.setPrefWidth(200);

        // Profile button (circle + username)
        Circle avatar = new Circle(20, Color.WHITE);
        usernameText = new Text(main.getCurrentUser() != null ?
                main.getCurrentUser().getName() : "User");
        usernameText.setFill(Color.WHITE);
        HBox profileBox = new HBox(10, avatar, usernameText);
        profileBox.setAlignment(Pos.CENTER_LEFT);

        Button profileBtn = new Button();
        profileBtn.setGraphic(profileBox);
        profileBtn.setStyle("-fx-background-color: transparent;");
        profileBtn.setPrefWidth(Double.MAX_VALUE);
        profileBtn.setOnAction(e -> main.showProfileScreen());

        // Menu buttons
        Button browseBtn = new Button("Browse Items");
        browseBtn.setPrefWidth(Double.MAX_VALUE);
        browseBtn.setStyle("-fx-background-color: #1c1c1c; -fx-text-fill: white;");
        browseBtn.setAlignment(Pos.CENTER_LEFT);
        browseBtn.setOnAction(e -> main.showBrowse());

        Button uploadBtn = new Button("Upload Items");
        uploadBtn.setPrefWidth(Double.MAX_VALUE);
        uploadBtn.setStyle("-fx-background-color: #1c1c1c; -fx-text-fill: white;");
        uploadBtn.setAlignment(Pos.CENTER_LEFT);
        uploadBtn.setOnAction(e -> main.showUploadedItems());

        Button transactionsBtn = new Button("Transactions");
        transactionsBtn.setPrefWidth(Double.MAX_VALUE);
        transactionsBtn.setStyle("-fx-background-color: #1c1c1c; -fx-text-fill: white;");
        transactionsBtn.setAlignment(Pos.CENTER_LEFT);
        transactionsBtn.setOnAction(e -> main.showTransactions());

        Button myUploadsBtn = new Button("My Uploaded Items");
        myUploadsBtn.setPrefWidth(Double.MAX_VALUE);
        myUploadsBtn.setStyle("-fx-background-color: #1c1c1c; -fx-text-fill: white;");
        myUploadsBtn.setAlignment(Pos.CENTER_LEFT);
        myUploadsBtn.setOnAction(e -> main.showMyUploads());

        // Logout button at bottom
        Button logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(Double.MAX_VALUE);
        logoutBtn.setStyle("-fx-background-color: #102d36; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> main.showDemo());

        // VBox to separate buttons from logout
        VBox menuButtonsBox = new VBox(10, browseBtn, uploadBtn, transactionsBtn, myUploadsBtn);
        menuButtonsBox.setAlignment(Pos.TOP_LEFT);

        // Spacer between menu buttons and logout
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        menu.getChildren().addAll(profileBtn, menuButtonsBox, spacer, logoutBtn);
        dashboardRoot.setLeft(menu);
    }

    public void updateUsername() {
        if (main.getCurrentUser() != null) {
            usernameText.setText(main.getCurrentUser().getName());
        }
    }

    public void setContent(BorderPane content) {
        dashboardRoot.setCenter(content);
    }

    public BorderPane getRoot() {
        return dashboardRoot;
    }
}