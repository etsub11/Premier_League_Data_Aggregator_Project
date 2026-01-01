package com.example.premierleaguedataaggregator;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class InfoListCell extends ListCell<String> {
    private final HBox content;
    private final Label labelText;
    private final Label valueText;
    private final ImageView imageView;

    public InfoListCell() {
        super();
        
        labelText = new Label();
        labelText.setFont(Font.font("System", FontWeight.BOLD, 14));
        labelText.setMinWidth(150);
        labelText.setStyle("-fx-text-fill: #38003c;");

        valueText = new Label();
        valueText.setFont(Font.font("System", 14));
        
        imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new javafx.geometry.Insets(10));
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            try {
                // Format: Label|Value
                String[] parts = item.split("\\|");

                if (parts.length >= 2) {
                    String label = parts[0];
                    String value = parts[1];
                    
                    labelText.setText(label + ":");
                    
                    content.getChildren().clear();
                    content.getChildren().add(labelText);
                    
                    if ("Emblem".equals(label)) {
                        try {
                            imageView.setImage(new Image(value, true));
                            content.getChildren().add(imageView);
                        } catch (Exception e) {
                            valueText.setText("Image not available");
                            content.getChildren().add(valueText);
                        }
                    } else {
                        valueText.setText(value);
                        content.getChildren().add(valueText);
                    }

                    setGraphic(content);
                } else {
                    setText(item);
                    setGraphic(null);
                }
            } catch (Exception e) {
                setText(item);
                setGraphic(null);
            }
        }
    }
}
