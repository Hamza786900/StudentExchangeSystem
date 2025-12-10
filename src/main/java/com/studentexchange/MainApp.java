package com.studentexchange;

import com.studentexchange.gui.*;
import com.studentexchange.services.StudentBookExchange;
import com.studentexchange.models.User;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private static StudentBookExchange system = new StudentBookExchange();
    private static User currentUser = null;
    private Stage primaryStage;

    public static StudentBookExchange getSystem() {
        return system;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("BAZAAR - Student Book Exchange");

        showWelcomeScreen();

        primaryStage.show();
    }

    public void showWelcomeScreen() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(this);
        Scene scene = new Scene(welcomeScreen.getView(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(this);
        Scene scene = new Scene(loginScreen.getView(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showRegisterScreen() {
        RegisterScreen registerScreen = new RegisterScreen(this);
        Scene scene = new Scene(registerScreen.getView(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showDashboard() {
        DashboardScreen dashboardScreen = new DashboardScreen(this);
        Scene scene = new Scene(dashboardScreen.getView(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showUploadScreen() {
        UploadScreen uploadScreen = new UploadScreen(this);
        Scene scene = new Scene(uploadScreen.getView(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showBrowseScreen() {
        BrowseScreen browseScreen = new BrowseScreen(this);
        Scene scene = new Scene(browseScreen.getView(), 800, 500);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}