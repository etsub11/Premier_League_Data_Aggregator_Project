package com.example.premierleaguedataaggregator;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MatchListCell extends ListCell<String> {
    private final HBox content;
    private final Label dateLabel;
    private final Label statusLabel;
    private final ImageView homeLogo;
    private final Label homeNameLabel;
    private final Label scoreLabel;
    private final Label awayNameLabel;
    private final ImageView awayLogo;

    public MatchListCell() {
        super();
        
        // Date and Status
        dateLabel = new Label();
        dateLabel.setFont(Font.font("System", 10));
        dateLabel.setStyle("-fx-text-fill: #666;");
        
        statusLabel = new Label();
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 10));
        
        VBox metaBox = new VBox(2, dateLabel, statusLabel);
        metaBox.setAlignment(Pos.CENTER_LEFT);
        metaBox.setMinWidth(80);

        // Home Team
        homeLogo = new ImageView();
        homeLogo.setFitHeight(24);
        homeLogo.setFitWidth(24);
        homeLogo.setPreserveRatio(true);
        
        homeNameLabel = new Label();
        homeNameLabel.setFont(Font.font("System", 14));
        homeNameLabel.setAlignment(Pos.CENTER_RIGHT);
        
        HBox homeBox = new HBox(10, homeNameLabel, homeLogo);
        homeBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(homeBox, Priority.ALWAYS);

        // Score
        scoreLabel = new Label();
        scoreLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        scoreLabel.setMinWidth(60);
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5; -fx-padding: 2 5;");

        // Away Team
        awayLogo = new ImageView();
        awayLogo.setFitHeight(24);
        awayLogo.setFitWidth(24);
        awayLogo.setPreserveRatio(true);
        
        awayNameLabel = new Label();
        awayNameLabel.setFont(Font.font("System", 14));
        
        HBox awayBox = new HBox(10, awayLogo, awayNameLabel);
        awayBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(awayBox, Priority.ALWAYS);

        content = new HBox(15, metaBox, homeBox, scoreLabel, awayBox);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            try {
                // Parse the pipe-delimited string
                // Format: Date|Status|HomeLogo|HomeName|AwayLogo|AwayName|HomeScore|AwayScore
                String[] parts = item.split("\\|");
                
                if (parts.length >= 8) {
                    // Format Date (simple substring for now, e.g., 2023-12-28)
                    String date = parts[0].length() > 10 ? parts[0].substring(0, 10) : parts[0];
                    dateLabel.setText(date);
                    statusLabel.setText(parts[1]);
                    
                    // Home Team
                    try {
                        homeLogo.setImage(new Image(parts[2], true));
                    } catch (Exception e) { homeLogo.setImage(null); }
                    homeNameLabel.setText(parts[3]);
                    
                    // Away Team
                    try {
                        awayLogo.setImage(new Image(parts[4], true));
                    } catch (Exception e) { awayLogo.setImage(null); }
                    awayNameLabel.setText(parts[5]);
                    
                    // Score
                    scoreLabel.setText(parts[6] + " - " + parts[7]);
                    
                    setGraphic(content);
                } else {
                    // Fallback for simple messages like "Loading..."
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
