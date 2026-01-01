module com.example.premierleaguedataaggregator {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web; // Added for WebView
    requires java.rmi;
    requires com.google.gson;
    requires java.xml; // Added for XML parsing (NewsService)
    requires java.desktop; // Added for Desktop.getDesktop().browse() (NewsListCell)

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.example.premierleaguedataaggregator to javafx.fxml;
    opens com.example.premierleaguedataaggregator.api to com.google.gson;

    exports com.example.premierleaguedataaggregator;
    exports com.example.premierleaguedataaggregator.rmi;
}