package com.example.premierleaguedataaggregator;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StandingListCell extends ListCell<String> {
    private final HBox content;
    private final Label posLabel;
    private final ImageView logo;
    private final Label nameLabel;
    private final Label playedLabel;
    private final Label wonLabel;
    private final Label drawLabel;
    private final Label lostLabel;
    private final Label pointsLabel;

    public StandingListCell() {
        super();
        
        posLabel = new Label();
        posLabel.setMinWidth(30);
        posLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        posLabel.setAlignment(Pos.CENTER);

        logo = new ImageView();
        logo.setFitHeight(24);
        logo.setFitWidth(24);
        logo.setPreserveRatio(true);

        nameLabel = new Label();
        nameLabel.setFont(Font.font("System", 14));
        nameLabel.setMinWidth(150);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        playedLabel = createStatLabel();
        wonLabel = createStatLabel();
        drawLabel = createStatLabel();
        lostLabel = createStatLabel();
        
        pointsLabel = new Label();
        pointsLabel.setMinWidth(40);
        pointsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        pointsLabel.setAlignment(Pos.CENTER);
        pointsLabel.setStyle("-fx-text-fill: #38003c;");

        content = new HBox(10, posLabel, logo, nameLabel, playedLabel, wonLabel, drawLabel, lostLabel, pointsLabel);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
    }

    private Label createStatLabel() {
        Label label = new Label();
        label.setMinWidth(30);
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-text-fill: #666;");
        return label;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            try {
                // Format: Pos|Logo|Team|Played|Won|Draw|Lost|Points|GF|GA|GD
                String[] parts = item.split("\\|");

                if (parts.length >= 8) {
                    posLabel.setText(parts[0]);
                    try {
                        logo.setImage(new Image(parts[1], true));
                    } catch (Exception e) { logo.setImage(null); }
                    nameLabel.setText(parts[2]);
                    playedLabel.setText(parts[3]);
                    wonLabel.setText(parts[4]);
                    drawLabel.setText(parts[5]);
                    lostLabel.setText(parts[6]);
                    pointsLabel.setText(parts[7]);

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
