package com.example.premierleaguedataaggregator;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.awt.Desktop;
import java.net.URI;

public class NewsListCell extends ListCell<String> {
    private final VBox content;
    private final Label titleLabel;
    private final Label dateLabel;
    private final Text descriptionText;
    private final Hyperlink link;

    public NewsListCell() {
        super();
        
        titleLabel = new Label();
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-text-fill: #38003c;");

        dateLabel = new Label();
        dateLabel.setFont(Font.font("System", 12));
        dateLabel.setStyle("-fx-text-fill: #666;");

        descriptionText = new Text();
        descriptionText.setFont(Font.font("System", 14));
        descriptionText.setWrappingWidth(600); // Adjust based on your UI width

        link = new Hyperlink("Read more on Sky Sports");
        link.setOnAction(e -> {
            if (getItem() != null) {
                try {
                    String[] parts = getItem().split("\\|");
                    if (parts.length >= 4) {
                        Desktop.getDesktop().browse(new URI(parts[3]));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        content = new VBox(5, titleLabel, dateLabel, descriptionText, link);
        content.setPadding(new javafx.geometry.Insets(10));
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            try {
                // Format: Title|Date|Description|Link
                String[] parts = item.split("\\|");
                
                if (parts.length >= 4) {
                    titleLabel.setText(parts[0]);
                    dateLabel.setText(parts[1]);
                    descriptionText.setText(parts[2]);
                    // Link action is handled in constructor

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
