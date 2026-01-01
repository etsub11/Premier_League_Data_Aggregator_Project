package com.example.premierleaguedataaggregator;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StadiumListCell extends ListCell<String> {
    private final HBox content;
    private final ImageView teamLogo;
    private final Label stadiumNameLabel;
    private final Label teamNameLabel;

    public StadiumListCell() {
        super();
        
        teamLogo = new ImageView();
        teamLogo.setFitHeight(40);
        teamLogo.setFitWidth(40);
        teamLogo.setPreserveRatio(true);

        stadiumNameLabel = new Label();
        stadiumNameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        teamNameLabel = new Label();
        teamNameLabel.setFont(Font.font("System", 14));
        teamNameLabel.setStyle("-fx-text-fill: #666;");

        VBox infoBox = new VBox(2, stadiumNameLabel, teamNameLabel);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        content = new HBox(15, teamLogo, infoBox);
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
                // Format: StadiumName|TeamName|TeamCrest
                String[] parts = item.split("\\|");
                
                if (parts.length >= 3) {
                    stadiumNameLabel.setText(parts[0]);
                    teamNameLabel.setText("Home of " + parts[1]);
                    try {
                        teamLogo.setImage(new Image(parts[2], true));
                    } catch (Exception e) { teamLogo.setImage(null); }
                    
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
