package com.studentexchange.gui;

import com.studentexchange.Main;
import com.studentexchange.enums.*;
import com.studentexchange.models.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Uitems {
    Main main;
    BorderPane root;

    public Uitems(Main main) {
        this.main = main;
        createview();
    }

    public void createview() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: black;");

        // ===== TOP: Heading =====
        Label heading = new Label("Upload Item");
        heading.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");
        VBox topBox = new VBox(heading);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 15, 0));
        root.setTop(topBox);

        // ===== CENTER: Form =====
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);

        // Category
        Label categoryLabel = new Label("Category:");
        categoryLabel.setStyle("-fx-text-fill: white;");
        ComboBox<Category> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(Category.values());
        categoryBox.setPrefWidth(200);

        // Grade Level
        Label gradeLabel = new Label("Grade:");
        gradeLabel.setStyle("-fx-text-fill: white;");
        ComboBox<GradeLevel> gradeBox = new ComboBox<>();
        gradeBox.getItems().addAll(GradeLevel.values());
        gradeBox.setPrefWidth(200);

        // Title
        Label titleLabel = new Label("Title:");
        titleLabel.setStyle("-fx-text-fill: white;");
        TextField titleField = new TextField();

        // Subject
        Label subjectLabel = new Label("Subject:");
        subjectLabel.setStyle("-fx-text-fill: white;");
        TextField subjectField = new TextField();

        // Description
        Label descLabel = new Label("Description:");
        descLabel.setStyle("-fx-text-fill: white;");
        TextArea descArea = new TextArea();
        descArea.setPrefRowCount(3);
        descArea.setPrefWidth(200);

        // Condition
        Label conditionLabel = new Label("Condition:");
        conditionLabel.setStyle("-fx-text-fill: white;");
        ComboBox<Condition> conditionBox = new ComboBox<>();
        conditionBox.getItems().addAll(Condition.values());
        conditionBox.setPrefWidth(200);

        // Market Price
        Label marketPriceLabel = new Label("Market Price:");
        marketPriceLabel.setStyle("-fx-text-fill: white;");
        TextField marketPriceField = new TextField();
        marketPriceField.setPromptText("0.00");

        // Selling Price
        Label priceLabel = new Label("Selling Price:");
        priceLabel.setStyle("-fx-text-fill: white;");
        TextField priceField = new TextField();
        priceField.setPromptText("0.00");

        // Add to grid
        grid.add(categoryLabel, 0, 0);
        grid.add(categoryBox, 1, 0);
        grid.add(gradeLabel, 0, 1);
        grid.add(gradeBox, 1, 1);
        grid.add(titleLabel, 0, 2);
        grid.add(titleField, 1, 2);
        grid.add(subjectLabel, 0, 3);
        grid.add(subjectField, 1, 3);
        grid.add(descLabel, 0, 4);
        grid.add(descArea, 1, 4);
        grid.add(conditionLabel, 0, 5);
        grid.add(conditionBox, 1, 5);
        grid.add(marketPriceLabel, 0, 6);
        grid.add(marketPriceField, 1, 6);
        grid.add(priceLabel, 0, 7);
        grid.add(priceField, 1, 7);

        root.setCenter(grid);

        // ===== BOTTOM: Upload Button =====
        Button uploadBtn = new Button("Upload");
        uploadBtn.setStyle("-fx-background-color: teal; -fx-text-fill: black;");
        uploadBtn.setPrefSize(100, 35);
        uploadBtn.setOnAction(e -> {
            try {
                // Validate inputs
                if (categoryBox.getValue() == null || gradeBox.getValue() == null ||
                        titleField.getText().trim().isEmpty() || subjectField.getText().trim().isEmpty() ||
                        conditionBox.getValue() == null || marketPriceField.getText().trim().isEmpty() ||
                        priceField.getText().trim().isEmpty()) {
                    showAlert("Error", "Please fill in all required fields");
                    return;
                }

                float marketPrice = Float.parseFloat(marketPriceField.getText().trim());
                float price = Float.parseFloat(priceField.getText().trim());

                if (marketPrice < 0 || price < 0) {
                    showAlert("Error", "Prices cannot be negative");
                    return;
                }

                // Create a simple book item (you can expand this to handle different types)
                Book book = main.getSystem().uploadBook(
                        main.getCurrentUser(),
                        titleField.getText().trim(),
                        descArea.getText().trim(),
                        categoryBox.getValue(),
                        gradeBox.getValue(),
                        subjectField.getText().trim(),
                        conditionBox.getValue(),
                        marketPrice,
                        price,
                        "Unknown Author", // Default values for Book-specific fields
                        "1st Edition",
                        "Unknown Publisher",
                        100,
                        false
                );

                showAlert("Success", "Item uploaded successfully!");

                // Clear form
                categoryBox.setValue(null);
                gradeBox.setValue(null);
                titleField.clear();
                subjectField.clear();
                descArea.clear();
                conditionBox.setValue(null);
                marketPriceField.clear();
                priceField.clear();

            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter valid prices");
            } catch (Exception ex) {
                showAlert("Error", "Upload failed: " + ex.getMessage());
            }
        });

        HBox bottomBox = new HBox(uploadBtn);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(15, 0, 15, 0));

        root.setBottom(bottomBox);
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