package com.studentexchange.gui;

import com.studentexchange.Main;
import com.studentexchange.models.Item;
import com.studentexchange.models.ForSaleItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;

public class MUitems {
    Main main;
    BorderPane root;
    private VBox uploadedBox;
    private Item selectedItem;

    public MUitems(Main main) {
        this.main = main;
        createview();
    }

    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // ===== TOP: Heading =====
        Label heading = new Label("My Uploaded Items");
        heading.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        VBox topBox = new VBox(heading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 15, 0));
        root.setTop(topBox);

        // ===== CENTER: Scrollable Uploaded Items List =====
        uploadedBox = new VBox(10);
        uploadedBox.setPadding(new Insets(10));
        uploadedBox.setStyle("-fx-background-color: #222;");
        uploadedBox.setAlignment(Pos.TOP_LEFT);

        loadMyItems();

        // ScrollPane for uploaded items
        ScrollPane scrollPane = new ScrollPane(uploadedBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #222; -fx-border-color: #333;");

        root.setCenter(scrollPane);

        // ===== BOTTOM: Details Button =====
        Button detailsBtn = new Button("Details");
        detailsBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        detailsBtn.setPrefSize(100, 35);
        detailsBtn.setOnAction(e -> showItemDetails());

        HBox bottomBox = new HBox(detailsBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15, 0, 15, 0));
        root.setBottom(bottomBox);
    }

    private void loadMyItems() {
        uploadedBox.getChildren().clear();

        List<Item> myItems = main.getSystem().getCatalog().getItemsBySeller(main.getCurrentUser());

        if (myItems.isEmpty()) {
            Label noItems = new Label("You haven't uploaded any items yet");
            noItems.setStyle("-fx-text-fill: white; -fx-padding: 5px;");
            uploadedBox.getChildren().add(noItems);
        } else {
            for (Item item : myItems) {
                String status = "";
                if (item instanceof ForSaleItem) {
                    ForSaleItem forSale = (ForSaleItem) item;
                    status = forSale.isIs_sold() ? " (SOLD)" : " (AVAILABLE)";
                }

                Button itemBtn = new Button(item.getTitle() + status);
                itemBtn.setStyle("-fx-text-fill: white; -fx-background-color: #2c2c2c;");
                itemBtn.setPrefWidth(400);
                itemBtn.setAlignment(Pos.CENTER_LEFT);
                itemBtn.setOnAction(e -> {
                    selectedItem = item;
                    highlightSelection(itemBtn);
                });
                uploadedBox.getChildren().add(itemBtn);
            }
        }
    }

    private void highlightSelection(Button selected) {
        for (var node : uploadedBox.getChildren()) {
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public BorderPane getroot() {
        return root;
    }
}