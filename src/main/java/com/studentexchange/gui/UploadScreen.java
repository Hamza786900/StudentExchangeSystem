package com.studentexchange.gui;

import com.studentexchange.MainApp;
import com.studentexchange.enums.*;
import com.studentexchange.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class UploadScreen {
    private MainApp mainApp;
    private VBox view;

    public UploadScreen(MainApp mainApp) {
        this.mainApp = mainApp;
        try {
            createView();
        } catch (Exception e) {
            showError("Failed to initialize upload screen.", e);
        }
    }

    private void createView() {
        view = new VBox(20);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(30));
        view.setStyle("-fx-background-color: #F0F0F5;");

        Label title = new Label("Upload New Item");
        title.setFont(Font.font("Arial", 28));
        title.setStyle("-fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);

        Label typeLabel = new Label("Item Type:");
        typeLabel.setFont(Font.font("Arial", 14));
        String[] types = {"Book", "Notes", "Past Paper", "Free Resource"};
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(types);
        typeBox.setValue("Book");
        typeBox.setPrefWidth(250);
        typeBox.setStyle("-fx-font-size: 14px;");

        Label titleLabel = new Label("Title:");
        titleLabel.setFont(Font.font("Arial", 14));
        TextField titleField = new TextField();
        titleField.setPrefWidth(250);
        titleField.setStyle("-fx-font-size: 14px;");

        Label subjectLabel = new Label("Subject:");
        subjectLabel.setFont(Font.font("Arial", 14));
        TextField subjectField = new TextField();
        subjectField.setPrefWidth(250);
        subjectField.setStyle("-fx-font-size: 14px;");

        Label gradeLabel = new Label("Grade:");
        gradeLabel.setFont(Font.font("Arial", 14));
        ComboBox<GradeLevel> gradeBox = new ComboBox<>();
        gradeBox.getItems().addAll(GradeLevel.values());
        gradeBox.setValue(GradeLevel.GRADE_10);
        gradeBox.setPrefWidth(250);
        gradeBox.setStyle("-fx-font-size: 14px;");

        Label priceLabel = new Label("Price (Rs):");
        priceLabel.setFont(Font.font("Arial", 14));
        TextField priceField = new TextField();
        priceField.setPrefWidth(250);
        priceField.setStyle("-fx-font-size: 14px;");

        Label marketPriceLabel = new Label("Market Price (Rs):");
        marketPriceLabel.setFont(Font.font("Arial", 14));
        TextField marketPriceField = new TextField();
        marketPriceField.setPrefWidth(250);
        marketPriceField.setStyle("-fx-font-size: 14px;");

        grid.add(typeLabel, 0, 0);
        grid.add(typeBox, 1, 0);
        grid.add(titleLabel, 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(subjectLabel, 0, 2);
        grid.add(subjectField, 1, 2);
        grid.add(gradeLabel, 0, 3);
        grid.add(gradeBox, 1, 3);
        grid.add(priceLabel, 0, 4);
        grid.add(priceField, 1, 4);
        grid.add(marketPriceLabel, 0, 5);
        grid.add(marketPriceField, 1, 5);

        typeBox.setOnAction(e -> {
            try {
                if (typeBox.getValue().equals("Free Resource")) {
                    priceField.setDisable(true);
                    marketPriceField.setDisable(true);
                    priceField.setText("0");
                    marketPriceField.setText("0");
                } else {
                    priceField.setDisable(false);
                    marketPriceField.setDisable(false);
                    priceField.setText("");
                    marketPriceField.setText("");
                }
            } catch (Exception ex) {
                showError("Failed to handle type selection.", ex);
            }
        });

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button uploadBtn = new Button("Upload");
        uploadBtn.setPrefSize(120, 35);
        uploadBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        uploadBtn.setOnAction(e -> {
            try {
                String itemType = typeBox.getValue();
                String itemTitle = titleField.getText();
                String subject = subjectField.getText();
                GradeLevel grade = gradeBox.getValue();

                if (itemTitle.isEmpty() || subject.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all required fields!");
                    alert.showAndWait();
                    return;
                }

                User user = MainApp.getCurrentUser();

                if (itemType.equals("Free Resource")) {
                    MainApp.getSystem().uploadFreeResource(user, itemTitle, "Sample description",
                            Category.RESOURCE, grade, subject, "http://example.com/file.pdf",
                            true, "Sample University", "CS-101", 2024, "Fall", "Mid", false, true, 2.5f, "PDF");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Free resource uploaded! You earned 10 credit points!");
                    alert.showAndWait();
                } else {
                    float price = Float.parseFloat(priceField.getText());
                    float marketPrice = Float.parseFloat(marketPriceField.getText());

                    if (itemType.equals("Book")) {
                        MainApp.getSystem().uploadBook(user, itemTitle, "Sample description", Category.BOOK,
                                grade, subject, Condition.GOOD, marketPrice, price, "Author Name", "2024",
                                "Publisher", 300, false);
                    } else if (itemType.equals("Notes")) {
                        MainApp.getSystem().uploadNotes(user, itemTitle, "Sample description", Category.NOTES,
                                grade, subject, Condition.GOOD, marketPrice, price, 100, "PDF", true, true, "High");
                    } else if (itemType.equals("Past Paper")) {
                        MainApp.getSystem().uploadPastPaper(user, itemTitle, "Sample description", Category.PAST_PAPERS,
                                grade, subject, Condition.NEW, marketPrice, price, "Federal Board", 2024,
                                true, true, true, 1, subject, false);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Item uploaded successfully!");
                    alert.showAndWait();
                }

                mainApp.showDashboard();

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please enter valid prices!");
                alert.showAndWait();
            } catch (Exception ex) {
                showError("Upload failed due to unexpected error.", ex);
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setPrefSize(120, 35);
        backBtn.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> {
            try {
                mainApp.showDashboard();
            } catch (Exception ex) {
                showError("Failed to go back.", ex);
            }
        });

        buttonBox.getChildren().addAll(uploadBtn, backBtn);
        view.getChildren().addAll(title, grid, buttonBox);
    }

    private void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message + (e != null ? "\nDetails: " + e.getMessage() : ""));
        alert.showAndWait();
    }

    public VBox getView() {
        return view;
    }
}
