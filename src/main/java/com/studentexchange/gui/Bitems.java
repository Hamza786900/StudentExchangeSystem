package com.studentexchange.gui;

import com.studentexchange.Main;
import com.studentexchange.models.Item;
import com.studentexchange.models.ForSaleItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class Bitems {
    Main main;
    BorderPane root;
    private VBox itemsBox;
    private Item selectedItem;

    public Bitems(Main main) {
        this.main = main;
        createview();
    }

    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        Label heading = new Label("Browse Items");
        heading.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        VBox topBox = new VBox(heading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 15, 0));
        root.setTop(topBox);

        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.TOP_CENTER);

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        TextField searchField = new TextField();
        searchField.setPromptText("Search items...");
        searchField.setPrefWidth(200);
        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        searchBtn.setPrefSize(80, 30);

        searchBtn.setOnAction(e -> {
            String keyword = searchField.getText().trim();
            loadItems(keyword);
        });

        searchBox.getChildren().addAll(searchField, searchBtn);
        centerBox.getChildren().add(searchBox);


        itemsBox = new VBox(15);
        itemsBox.setPadding(new Insets(10));
        itemsBox.setStyle("-fx-background-color: #1c1c1c;");

        ScrollPane scrollPane = new ScrollPane(itemsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(300);

        centerBox.getChildren().add(scrollPane);

        loadItems("");

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        Button detailsBtn = new Button("Details");
        detailsBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        detailsBtn.setPrefSize(100, 35);
        detailsBtn.setOnAction(e -> showItemDetails());

        Button purchaseBtn = new Button("Purchase");
        purchaseBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        purchaseBtn.setPrefSize(100, 35);
        purchaseBtn.setOnAction(e -> confirmPurchase());

        buttonBox.getChildren().addAll(detailsBtn, purchaseBtn);
        centerBox.getChildren().add(buttonBox);

        root.setCenter(centerBox);
    }

    private void loadItems(String keyword) {
        itemsBox.getChildren().clear();

        List<Item> items;
        if (keyword.isEmpty()) {
            items = main.getSystem().getCatalog().getItems();
        } else {
            items = main.getSystem().searchItems(keyword, null, null, 0, Float.MAX_VALUE, null, null);
        }

        if (items.isEmpty()) {
            Label noItems = new Label("No items found");
            noItems.setStyle("-fx-text-fill: white;");
            itemsBox.getChildren().add(noItems);
        } else {
            for (Item item : items) {
                if (item instanceof ForSaleItem) {
                    ForSaleItem forSaleItem = (ForSaleItem) item;
                    if (!forSaleItem.isIs_sold()) {
                        Button itemBtn = new Button(item.getTitle() + " - Rs." + forSaleItem.getPrice());
                        itemBtn.setStyle("-fx-text-fill: white; -fx-background-color: #2c2c2c;");
                        itemBtn.setPrefWidth(400);
                        itemBtn.setAlignment(Pos.CENTER_LEFT);
                        itemBtn.setOnAction(e -> {
                            selectedItem = item;
                            highlightSelection(itemBtn);
                        });
                        itemsBox.getChildren().add(itemBtn);
                    }
                }
            }
        }
    }

    private void highlightSelection(Button selected) {
        for (var node : itemsBox.getChildren()) {
            if (node instanceof Button) {
                ((Button) node).setStyle("-fx-text-fill: white; -fx-background-color: #2c2c2c;");
            }
        }
        selected.setStyle("-fx-text-fill: white; -fx-background-color: teal;");
    }

    private void showItemDetails() {
        if (selectedItem == null) {
            showAlert("Error", "Please select an item first");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Item Details");
        alert.setHeaderText(selectedItem.getTitle());
        alert.setContentText(selectedItem.getDetails());
        alert.showAndWait();
    }

    private void confirmPurchase() {
        if (selectedItem == null) {
            showAlert("Error", "Please select an item first");
            return;
        }

        if (!(selectedItem instanceof ForSaleItem)) {
            showAlert("Error", "This item is not for sale");
            return;
        }

        ForSaleItem forSaleItem = (ForSaleItem) selectedItem;

        if (main.getCurrentUser() == null) {
            showAlert("Error", "You must be logged in to purchase an item.");
            return;
        }

        if (forSaleItem.getUploader().equals(main.getCurrentUser())) {
            showAlert("Error", "You cannot purchase your own item");
            return;
        }

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Confirm Purchase");

        Label question = new Label("Are you sure you want to purchase this item?");
        question.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");
        question.setWrapText(true);

        Button yesBtn = new Button("Yes");
        yesBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        yesBtn.setPrefSize(70, 30);
        yesBtn.setOnAction(ev -> {
            popup.close();
            main.showPurchase();
        });

        Button noBtn = new Button("No");
        noBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        noBtn.setPrefSize(70, 30);
        noBtn.setOnAction(ev -> popup.close());

        HBox btnBox = new HBox(20, yesBtn, noBtn);
        btnBox.setAlignment(Pos.CENTER);

        VBox popupVBox = new VBox(20, question, btnBox);
        popupVBox.setAlignment(Pos.CENTER);
        popupVBox.setPadding(new Insets(15));
        popupVBox.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px;");

        Scene popupScene = new Scene(popupVBox, 300, 150);
        popup.setScene(popupScene);
        popup.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Item getSelectedItem() {
        return selectedItem;
    }

    public BorderPane getroot() {
        return root;
    }
}