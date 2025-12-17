package com.studentexchange;

import com.studentexchange.gui.*;
import com.studentexchange.models.*;
import com.studentexchange.services.StudentBookExchange;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage primaryStage;
    private DashboardWrapper dashboardWrapper;
    private StudentBookExchange system = new StudentBookExchange();
    private User currentUser;
    private Item selectedItem;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showDemo();
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
        if (dashboardWrapper != null) {
            dashboardWrapper.updateUsername();
        }
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Item item) {
        this.selectedItem = item;
    }

    public void showDemo() {
        Demo welcomeScreen = new Demo(this);
        Scene scene = new Scene(welcomeScreen.getroot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showLoginScreen() {
        Demo1 login = new Demo1(this);
        Scene scene = new Scene(login.getroot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showRegisterScreen() {
        Demo2 register = new Demo2(this);
        Scene scene = new Scene(register.getroot(), 800, 500);
        primaryStage.setScene(scene);
    }

    public void showDashboardScreen() {
        dashboardWrapper = new DashboardWrapper(this);
        showBrowse();
    }

    public void showBrowse() {
        if (dashboardWrapper == null)
            dashboardWrapper = new DashboardWrapper(this);
        Bitems browse = new Bitems(this);
        dashboardWrapper.setContent(browse.getroot());
        primaryStage.setScene(new Scene(dashboardWrapper.getRoot(), 800, 500));
    }

    public void showPurchase() {
        if (dashboardWrapper == null) dashboardWrapper = new DashboardWrapper(this);
        purchase purchaseScreen = new purchase(this);
        dashboardWrapper.setContent(purchaseScreen.getroot());
        primaryStage.setScene(new Scene(dashboardWrapper.getRoot(), 800, 500));
    }

    public void showTransactions() {
        if (dashboardWrapper == null) dashboardWrapper = new DashboardWrapper(this);
        Tran transactions = new Tran(this);
        dashboardWrapper.setContent(transactions.getroot());
        primaryStage.setScene(new Scene(dashboardWrapper.getRoot(), 800, 500));
    }

    public void showMyUploads() {
        if (dashboardWrapper == null) dashboardWrapper = new DashboardWrapper(this);
        MUitems myUploads = new MUitems(this);
        dashboardWrapper.setContent(myUploads.getroot());
        primaryStage.setScene(new Scene(dashboardWrapper.getRoot(), 800, 500));
    }

    public void showUploadedItems() {
        if (dashboardWrapper == null) dashboardWrapper = new DashboardWrapper(this);
        Uitems upload = new Uitems(this);
        dashboardWrapper.setContent(upload.getroot());
        primaryStage.setScene(new Scene(dashboardWrapper.getRoot(), 800, 500));
    }

    public void showProfileScreen() {
        if (dashboardWrapper == null) dashboardWrapper = new DashboardWrapper(this);
        Prof profile = new Prof(this);
        dashboardWrapper.setContent(profile.getroot());
        primaryStage.setScene(new Scene(dashboardWrapper.getRoot(), 800, 500));
    }

    public void showUpdateProfile() {
        if (dashboardWrapper == null) dashboardWrapper = new DashboardWrapper(this);
        updateprof update = new updateprof(this);
        dashboardWrapper.setContent(update.getroot());
        primaryStage.setScene(new Scene(dashboardWrapper.getRoot(), 800, 500));
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