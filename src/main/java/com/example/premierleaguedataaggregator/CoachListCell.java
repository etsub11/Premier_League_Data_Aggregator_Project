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

public class CoachListCell extends ListCell<String> {
    private final HBox content;
    private final ImageView teamLogo;
    private final Label coachNameLabel;
    private final Label nationalityLabel;
    private final Label teamNameLabel;

    public CoachListCell() {
        super();
        
        teamLogo = new ImageView();
        teamLogo.setFitHeight(40);
        teamLogo.setFitWidth(40);
        teamLogo.setPreserveRatio(true);

        coachNameLabel = new Label();
        coachNameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        nationalityLabel = new Label();
        nationalityLabel.setFont(Font.font("System", 12));
        nationalityLabel.setStyle("-fx-text-fill: #666;");

        VBox coachInfoBox = new VBox(2, coachNameLabel, nationalityLabel);
        HBox.setHgrow(coachInfoBox, Priority.ALWAYS);

        teamNameLabel = new Label();
        teamNameLabel.setFont(Font.font("System", 14));
        teamNameLabel.setMinWidth(150);
        teamNameLabel.setAlignment(Pos.CENTER_RIGHT);

        content = new HBox(15, coachInfoBox, teamNameLabel, teamLogo);
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
                // Format: CoachName|Nationality|TeamName|TeamLogo
                String[] parts = item.split("\\|");
                
                if (parts.length >= 4) {
                    coachNameLabel.setText(parts[0]);
                    nationalityLabel.setText(parts[1]);
                    teamNameLabel.setText(parts[2]);
                    
                    try {
                        teamLogo.setImage(new Image(parts[3], true));
                    } catch (Exception e) {
                        teamLogo.setImage(null);
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
