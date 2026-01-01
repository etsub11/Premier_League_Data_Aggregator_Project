package com.example.premierleaguedataaggregator;

import javafx.scene.control.ListCell;
import javafx.scene.text.Text;

public class StatusMessageListCell extends ListCell<String> {
    private final Text text;

    public StatusMessageListCell() {
        this.text = new Text();
        getStyleClass().add("option-response-text");
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            text.setText(item);
            setGraphic(text);
        }
    }
}
