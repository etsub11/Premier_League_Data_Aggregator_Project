package com.example.premierleaguedataaggregator;

import com.example.premierleaguedataaggregator.rmi.PremierLeagueService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private ListView<String> categoryListView;
    @FXML
    private ListView<String> detailsListView;
    @FXML
    private VBox detailsPane;
    @FXML
    private HBox matchFilterContainer;
    @FXML
    private ScrollPane matchesScrollPane;
    @FXML
    private VBox matchesContainer;

    private PremierLeagueService service;
    private List<Match> allMatches = new ArrayList<>();

    @FXML
    public void initialize() {
        ObservableList<String> categories = FXCollections.observableArrayList(
                "Matches", "Standings", "Info", "Ranking", "News",
                "Coaches", "Referees", "Teams", "Transfers", "Stadiums"
        );
        categoryListView.setItems(categories);
        
        // Defer connection until the view is shown
        Platform.runLater(this::showIpAddressPrompt);

        categoryListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) loadDataForCategory(newVal);
        });
        matchFilterContainer.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setOnAction(event -> filterMatches(button.getText()));
            }
        });
    }

    private void showIpAddressPrompt() {
        TextInputDialog dialog = new TextInputDialog("localhost");
        dialog.setTitle("Connect to Server");
        dialog.setHeaderText("Enter the RMI Server IP Address");
        dialog.setContentText("IP Address:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresentOrElse(
            ipAddress -> connectToServer(ipAddress),
            () -> {
                showErrorAlert("Connection Canceled", "You must connect to a server to use this application.");
                Platform.exit();
            }
        );
    }

    private void connectToServer(String ipAddress) {
        new Thread(() -> {
            try {
                Registry registry = LocateRegistry.getRegistry(ipAddress, 1099);
                service = (PremierLeagueService) registry.lookup("PremierLeagueService");
                Platform.runLater(() -> System.out.println("Successfully connected to RMI Server at " + ipAddress));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    showErrorAlert("Connection Error", "Could not connect to RMI Server at " + ipAddress + ".");
                    showIpAddressPrompt(); // Let the user try again
                });
            }
        }).start();
    }

    private void loadDataForCategory(String categoryName) {
        if (service == null) {
            showErrorAlert("Service Not Available", "The RMI service is not connected.");
            return;
        }

        boolean isMatchCategory = "Matches".equals(categoryName);
        detailsListView.setVisible(!isMatchCategory);
        detailsListView.setManaged(!isMatchCategory);
        matchesScrollPane.setVisible(isMatchCategory);
        matchesScrollPane.setManaged(isMatchCategory);
        matchFilterContainer.setVisible(isMatchCategory);
        matchFilterContainer.setManaged(isMatchCategory);

        if (isMatchCategory) {
            loadAndDisplayMatches();
        } else {
            detailsListView.setCellFactory(listView -> new StatusMessageListCell());
            detailsListView.setItems(FXCollections.observableArrayList("Loading data for " + categoryName + "..."));
            new Thread(() -> {
                try {
                    String data = service.getDataForCategory(categoryName);
                    Platform.runLater(() -> {
                        setCellFactoryForCategory(categoryName);
                        detailsListView.setItems(FXCollections.observableArrayList(data.split("\n")));
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> detailsListView.setItems(FXCollections.observableArrayList("Failed to load data.")));
                }
            }).start();
        }
    }

    private void loadAndDisplayMatches() {
        setLoadingState("Loading matches...");
        new Thread(() -> {
            try {
                String data = service.getDataForCategory("Matches");
                allMatches = parseMatches(data);
                processAndRenderMatches(allMatches);
            } catch (Exception e) {
                e.printStackTrace();
                setErrorState("Failed to load matches.");
            }
        }).start();
    }

    private void filterMatches(String filter) {
        setLoadingState("Filtering matches...");
        new Thread(() -> {
            List<Match> filteredMatches;
            LocalDate today = LocalDate.now();
            switch (filter) {
                case "Upcoming":
                    filteredMatches = allMatches.stream().filter(m -> m.getDateTime().toLocalDate().isAfter(today)).collect(Collectors.toList());
                    break;
                case "Completed":
                    filteredMatches = allMatches.stream().filter(m -> m.getDateTime().toLocalDate().isBefore(today)).collect(Collectors.toList());
                    break;
                case "Today":
                    filteredMatches = allMatches.stream().filter(m -> m.getDateTime().toLocalDate().isEqual(today)).collect(Collectors.toList());
                    break;
                default:
                    filteredMatches = new ArrayList<>(allMatches);
                    break;
            }
            processAndRenderMatches(filteredMatches);
        }).start();

        Platform.runLater(() -> {
            for (Node node : matchFilterContainer.getChildren()) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.getStyleClass().remove("filter-button-active");
                    if (button.getText().equals(filter)) {
                        button.getStyleClass().add("filter-button-active");
                    }
                }
            }
        });
    }

    private void processAndRenderMatches(List<Match> matches) {
        try {
            Map<String, List<Match>> matchesByMonth = matches.stream()
                    .sorted(Comparator.comparing(Match::getDateTime))
                    .collect(Collectors.groupingBy(Match::getMonthYear, Collectors.toList()));

            List<String> sortedMonths = matchesByMonth.keySet().stream()
                    .sorted(Comparator.comparing(month -> LocalDate.parse("01 " + month, DateTimeFormatter.ofPattern("dd MMMM yyyy"))))
                    .collect(Collectors.toList());

            Platform.runLater(() -> matchesContainer.getChildren().clear());

            if (matches.isEmpty()) {
                setInfoState("No matches found for this filter.");
                return;
            }

            for (String monthYear : sortedMonths) {
                Label monthHeader = new Label(monthYear);
                monthHeader.getStyleClass().add("month-header-label");

                HBox monthRow = new HBox(15);
                monthRow.getStyleClass().add("matches-row-container");
                matchesByMonth.get(monthYear).forEach(match -> monthRow.getChildren().add(createMatchCard(match)));

                ScrollPane horizontalScrollPane = new ScrollPane(monthRow);
                horizontalScrollPane.setFitToHeight(true);
                horizontalScrollPane.getStyleClass().add("matches-scroll-pane");

                Platform.runLater(() -> {
                    matchesContainer.getChildren().addAll(monthHeader, horizontalScrollPane);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            setErrorState("Error processing match data.");
        }
    }

    private List<Match> parseMatches(String data) {
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }
        return data.lines()
                .map(Match::fromString)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Node createMatchCard(Match match) {
        VBox card = new VBox(5);
        card.getStyleClass().add("match-card");

        Image homeLogoImg = new Image(match.getHomeTeamLogoUrl(), 30, 30, true, true, true);
        ImageView homeLogo = new ImageView(homeLogoImg);

        Label teamNames = new Label(match.getHomeTeam() + " vs " + match.getAwayTeam());
        teamNames.getStyleClass().add("team-label");

        Image awayLogoImg = new Image(match.getAwayTeamLogoUrl(), 30, 30, true, true, true);
        ImageView awayLogo = new ImageView(awayLogoImg);

        HBox teams = new HBox(10, homeLogo, teamNames, awayLogo);
        teams.setAlignment(Pos.CENTER);

        Label scoreLabel = new Label(match.getScore());
        scoreLabel.getStyleClass().add("score-label");

        Label detailsLabel = new Label(match.getDateTime().format(DateTimeFormatter.ofPattern("dd MMM, HH:mm")) + "\n" + match.getStadium());
        detailsLabel.getStyleClass().add("match-details-label");

        card.getChildren().addAll(teams, scoreLabel, detailsLabel);

        if (!match.getStatus().equalsIgnoreCase("FINISHED") && !match.getStatus().equalsIgnoreCase("SCHEDULED")) {
            Label statusBadge = new Label(match.getStatus());
            statusBadge.getStyleClass().add("status-badge");
            StackPane.setAlignment(statusBadge, Pos.TOP_RIGHT);
            return new StackPane(card, statusBadge);
        }
        return card;
    }

    private void setCellFactoryForCategory(String categoryName) {
        switch (categoryName) {
            case "Standings": detailsListView.setCellFactory(lv -> new StandingListCell()); break;
            case "Info": detailsListView.setCellFactory(lv -> new InfoListCell()); break;
            case "Ranking": detailsListView.setCellFactory(lv -> new RankingListCell()); break;
            case "News": case "Transfers": detailsListView.setCellFactory(lv -> new NewsListCell()); break;
            case "Coaches": detailsListView.setCellFactory(lv -> new CoachListCell()); break;
            case "Referees": detailsListView.setCellFactory(lv -> new RefereeListCell()); break;
            case "Teams": detailsListView.setCellFactory(lv -> new TeamListCell()); break;
            case "Stadiums": detailsListView.setCellFactory(lv -> new StadiumListCell()); break;
            default: detailsListView.setCellFactory(null); break;
        }
    }

    private void setLoadingState(String message) {
        Platform.runLater(() -> {
            matchesContainer.getChildren().clear();
            Label loadingLabel = new Label(message);
            loadingLabel.getStyleClass().add("option-response-text");
            matchesContainer.getChildren().add(loadingLabel);
        });
    }

    private void setErrorState(String message) {
        Platform.runLater(() -> {
            matchesContainer.getChildren().clear();
            Label errorLabel = new Label(message);
            errorLabel.getStyleClass().add("option-response-text");
            matchesContainer.getChildren().add(errorLabel);
        });
    }
    
    private void setInfoState(String message) {
        Platform.runLater(() -> {
            matchesContainer.getChildren().clear();
            Label infoLabel = new Label(message);
            infoLabel.getStyleClass().add("option-response-text");
            matchesContainer.getChildren().add(infoLabel);
        });
    }

    @FXML
    protected void onBackButtonClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Premier League Data Aggregator");
        stage.show();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
