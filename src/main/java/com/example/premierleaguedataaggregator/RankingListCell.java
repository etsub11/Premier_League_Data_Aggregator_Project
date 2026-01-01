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

public class RankingListCell extends ListCell<String> {
    private final HBox content;
    private final Label rankLabel;
    private final ImageView playerImage;
    private final Label playerLabel;
    private final Label teamLabel;
    private final Label goalsLabel;
    private final Label assistsLabel;
    private final Label penaltiesLabel;
    private final Label playedLabel;

    public RankingListCell() {
        super();
        
        rankLabel = new Label();
        rankLabel.setMinWidth(30);
        rankLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        rankLabel.setAlignment(Pos.CENTER);

        // Player Image
        playerImage = new ImageView();
        playerImage.setFitHeight(50); // Slightly larger for better visibility
        playerImage.setFitWidth(50);
        playerImage.setPreserveRatio(true);

        playerLabel = new Label();
        playerLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        playerLabel.setMinWidth(150);
        HBox.setHgrow(playerLabel, Priority.ALWAYS);

        teamLabel = new Label();
        teamLabel.setFont(Font.font("System", 12));
        teamLabel.setMinWidth(120);
        teamLabel.setStyle("-fx-text-fill: #666;");

        goalsLabel = createStatLabel("Goals");
        goalsLabel.setStyle("-fx-text-fill: #38003c; -fx-font-weight: bold;");
        
        assistsLabel = createStatLabel("Assists");
        penaltiesLabel = createStatLabel("Pens");
        playedLabel = createStatLabel("Played");

        content = new HBox(15, rankLabel, playerImage, playerLabel, teamLabel, goalsLabel, assistsLabel, penaltiesLabel, playedLabel);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));
    }

    private Label createStatLabel(String tooltip) {
        Label label = new Label();
        label.setMinWidth(50);
        label.setAlignment(Pos.CENTER);
        return label;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            try {
                // Format: Rank|PlayerName|TeamName|Goals|Assists|Penalties|PlayedMatches|ImageURL
                String[] parts = item.split("\\|");
                
                if (parts.length >= 8) {
                    rankLabel.setText(parts[0]);
                    playerLabel.setText(parts[1]);
                    teamLabel.setText(parts[2]);
                    goalsLabel.setText(parts[3] + " G");
                    assistsLabel.setText(parts[4] + " A");
                    penaltiesLabel.setText(parts[5] + " P");
                    playedLabel.setText(parts[6] + " M");
                    
                    String imageUrl = parts[7];
                    if (!"null".equals(imageUrl) && !imageUrl.isEmpty()) {
                        try {
                            playerImage.setImage(new Image(imageUrl, true)); // Background loading
                        } catch (Exception e) {
                            setDefaultImage();
                        }
                    } else {
                        setDefaultImage();
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

    private void setDefaultImage() {
        try {
            playerImage.setImage(new Image("https://cdn-icons-png.flaticon.com/512/1077/1077114.png", true));
        } catch (Exception e) {
            playerImage.setImage(null);
        }
    }
}
