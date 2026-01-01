package com.example.premierleaguedataaggregator;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.Desktop;
import java.net.URI;

public class TeamListCell extends ListCell<String> {
    private final HBox content;
    private final ImageView logo;
    private final Label nameLabel;
    private final Label foundedLabel;
    private final Label colorsLabel;
    private final Hyperlink websiteLink;

    public TeamListCell() {
        super();
        
        logo = new ImageView();
        logo.setFitHeight(50);
        logo.setFitWidth(50);
        logo.setPreserveRatio(true);

        nameLabel = new Label();
        nameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        foundedLabel = new Label();
        foundedLabel.setFont(Font.font("System", 12));
        foundedLabel.setStyle("-fx-text-fill: #666;");

        colorsLabel = new Label();
        colorsLabel.setFont(Font.font("System", 12));
        colorsLabel.setStyle("-fx-text-fill: #666;");

        websiteLink = new Hyperlink("Visit Website");
        websiteLink.setOnAction(e -> {
            if (getItem() != null) {
                try {
                    String[] parts = getItem().split("\\|");
                    if (parts.length >= 5 && !parts[4].isEmpty()) {
                        Desktop.getDesktop().browse(new URI(parts[4]));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox infoBox = new VBox(2, nameLabel, foundedLabel, colorsLabel, websiteLink);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        content = new HBox(15, logo, infoBox);
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
                // Format: Name|Crest|Founded|Colors|Website
                String[] parts = item.split("\\|");
                
                if (parts.length >= 5) {
                    nameLabel.setText(parts[0]);
                    try {
                        logo.setImage(new Image(parts[1], true));
                    } catch (Exception e) { logo.setImage(null); }
                    
                    foundedLabel.setText("Founded: " + parts[2]);
                    colorsLabel.setText("Colors: " + parts[3]);
                    
                    if (parts[4].isEmpty()) {
                        websiteLink.setVisible(false);
                    } else {
                        websiteLink.setVisible(true);
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
