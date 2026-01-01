package com.example.premierleaguedataaggregator;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RefereeListCell extends ListCell<String> {
    private final HBox content;
    private final Label nameLabel;
    private final Label nationalityLabel;

    public RefereeListCell() {
        super();
        
        nameLabel = new Label();
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        
        nationalityLabel = new Label();
        nationalityLabel.setFont(Font.font("System", 14));
        nationalityLabel.setStyle("-fx-text-fill: #666;");

        content = new HBox(15, nameLabel, nationalityLabel);
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
                // Format: Name|Nationality
                String[] parts = item.split("\\|");
                
                if (parts.length >= 2) {
                    nameLabel.setText(parts[0]);
                    nationalityLabel.setText(parts[1]);
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
