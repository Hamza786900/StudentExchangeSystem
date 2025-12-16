package com.studentexchange;

import com.studentexchange.gui.*;
import com.studentexchange.models.User;
import com.studentexchange.services.StudentBookExchange;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primaryStage;
    private DashboardWrapper dashboardWrapper;
    private StudentBookExchange system;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.system = new StudentBookExchange();
        showDemo();
        primaryStage.setTitle("Student Bazaar");
        primaryStage.show();
    }

    public StudentBookExchange getSystem() {
        return system;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void showDemo() {
        currentUser = null;
        Demo welcomeScreen = new Demo(this);
        Scene scene = new Scene(welcomeScreen.getroot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showLoginScreen() {
        Demo1 Login = new Demo1(this);
        Scene scene = new Scene(Login.getroot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showRegisterScreen() {
        Demo2 Register = new Demo2(this);
        Scene scene = new Scene(Register.getroot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showDashboardScreen() {
        dashboardWrapper = new DashboardWrapper(this);
        Scene scene = new Scene(dashboardWrapper.getRoot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showProfileScreen() {
        if (dashboardWrapper == null) {
            dashboardWrapper = new DashboardWrapper(this);
        }
        Prof Profile = new Prof(this);
        dashboardWrapper.setContent(Profile.getroot());
        Scene scene = new Scene(dashboardWrapper.getRoot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showBrowse() {
        if (dashboardWrapper == null) {
            dashboardWrapper = new DashboardWrapper(this);
        }
        Bitems Browse = new Bitems(this);
        dashboardWrapper.setContent(Browse.getroot());
        Scene scene = new Scene(dashboardWrapper.getRoot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showUploadedItems() {
        if (dashboardWrapper == null) {
            dashboardWrapper = new DashboardWrapper(this);
        }
        Uitems Uploaded = new Uitems(this);
        dashboardWrapper.setContent(Uploaded.getroot());
        Scene scene = new Scene(dashboardWrapper.getRoot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showMyUploads() {
        if (dashboardWrapper == null) {
            dashboardWrapper = new DashboardWrapper(this);
        }
        MUitems MyUploads = new MUitems(this);
        dashboardWrapper.setContent(MyUploads.getroot());
        Scene scene = new Scene(dashboardWrapper.getRoot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showTransactions() {
        if (dashboardWrapper == null) {
            dashboardWrapper = new DashboardWrapper(this);
        }
        Tran transactions = new Tran(this);
        dashboardWrapper.setContent(transactions.getroot());
        Scene scene = new Scene(dashboardWrapper.getRoot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showUpdateProfile() {
        if (dashboardWrapper == null) {
            dashboardWrapper = new DashboardWrapper(this);
        }
        updateprof update = new updateprof(this);
        dashboardWrapper.setContent(update.getroot());
        Scene scene = new Scene(dashboardWrapper.getRoot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showPurchase() {
        if (dashboardWrapper == null) {
            dashboardWrapper = new DashboardWrapper(this);
        }
        purchase purchase = new purchase(this);
        dashboardWrapper.setContent(purchase.getroot());
        Scene scene = new Scene(dashboardWrapper.getRoot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showForgotPassword() {
        Forgot forgot = new Forgot(this);
        Scene scene = new Scene(forgot.getroot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}