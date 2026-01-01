package com.example.premierleaguedataaggregator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    protected void onEnterButtonClick(ActionEvent event) throws IOException {
        // Ensure this loads the correct dashboard view
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700); // Adjusted size for the new dashboard
        
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Premier League Dashboard");
        stage.show();
    }
}