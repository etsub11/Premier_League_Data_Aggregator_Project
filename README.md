# Premier League Data Aggregator

A simple JavaFX + RMI application that aggregates English Premier League data (matches, standings, news, teams, coaches, referees, transfers, stadiums, rankings) by calling the football-data API and some public sources. The project includes:

- A Java RMI server that collects and formats data: [`com.example.premierleaguedataaggregator.rmi.PremierLeagueServiceImpl`](src/main/java/com/example/premierleaguedataaggregator/rmi/PremierLeagueServiceImpl.java)
- A JavaFX client UI that connects to the RMI server: [`com.example.premierleaguedataaggregator.HelloApplication`](src/main/java/com/example/premierleaguedataaggregator/HelloApplication.java) and [`com.example.premierleaguedataaggregator.DashboardController`](src/main/java/com/example/premierleaguedataaggregator/DashboardController.java)

Quick links:

- App entry (client): [`com.example.premierleaguedataaggregator.HelloApplication`](src/main/java/com/example/premierleaguedataaggregator/HelloApplication.java)
- RMI server: [`com.example.premierleaguedataaggregator.rmi.RmiServer`](src/main/java/com/example/premierleaguedataaggregator/rmi/RmiServer.java)
- RMI interface: [`com.example.premierleaguedataaggregator.rmi.PremierLeagueService`](src/main/java/com/example/premierleaguedataaggregator/rmi/PremierLeagueService.java)
- Data fetcher: [`com.example.premierleaguedataaggregator.api.FootballDataService`](src/main/java/com/example/premierleaguedataaggregator/api/FootballDataService.java)
- Player image helper: [`com.example.premierleaguedataaggregator.api.PlayerImageService`](src/main/java/com/example/premierleaguedataaggregator/api/PlayerImageService.java)
- News fetcher: [`com.example.premierleaguedataaggregator.api.NewsService`](src/main/java/com/example/premierleaguedataaggregator/api/NewsService.java)
- UI layouts / styles:
  - [`src/main/resources/com/example/premierleaguedataaggregator/home-view.fxml`](src/main/resources/com/example/premierleaguedataaggregator/home-view.fxml)
  - [`src/main/resources/com/example/premierleaguedataaggregator/dashboard-view.fxml`](src/main/resources/com/example/premierleaguedataaggregator/dashboard-view.fxml)
  - [`src/main/resources/com/example/premierleaguedataaggregator/styles.css`](src/main/resources/com/example/premierleaguedataaggregator/styles.css)
- Configuration: [`src/main/resources/config.properties`](src/main/resources/config.properties)
- Build file: [`pom.xml`](pom.xml)

What it does (overview)

- The RMI server (`RmiServer`) starts an RMI registry on port 1099 and binds an implementation of `PremierLeagueService`. See [`com.example.premierleaguedataaggregator.rmi.RmiServer`](src/main/java/com/example/premierleaguedataaggregator/rmi/RmiServer.java).
- The server implementation (`PremierLeagueServiceImpl`) uses `FootballDataService` to call the football-data API, uses `PlayerImageService` to fetch player images from TheSportsDB, and `NewsService` to parse RSS news. See [`PremierLeagueServiceImpl`](src/main/java/com/example/premierleaguedataaggregator/rmi/PremierLeagueServiceImpl.java).
- The JavaFX client (started by `HelloApplication`) shows a home screen, then opens the dashboard (`DashboardController`). The dashboard prompts for the RMI server IP, connects to the service, and requests data categories (Matches, Standings, Info, Ranking, News, Coaches, Referees, Teams, Transfers, Stadiums). See [`DashboardController`](src/main/java/com/example/premierleaguedataaggregator/DashboardController.java).
- Matches data are returned as pipe-delimited lines. The client parses these strings into `Match` objects using `Match.fromString` and shows them in card-style UI. See [`Match`](src/main/java/com/example/premierleaguedataaggregator/Match.java).

How to run (developer-friendly)

1. Requirements

   - JDK and JAVA_HOME set (JDK 17+ recommended for JavaFX 21).
   - Maven installed.

2. Start the RMI server

   - In your IDE: run the main class [`com.example.premierleaguedataaggregator.rmi.RmiServer`](src/main/java/com/example/premierleaguedataaggregator/rmi/RmiServer.java).
   - The server prints the machine IP and binds the service on port 1099. If port 1099 is already in use you will see an error message in `RmiServer` (check the console). See the RMI server code: [`RmiServer`](src/main/java/com/example/premierleaguedataaggregator/rmi/RmiServer.java).

3. Run the JavaFX client
   - From the project root use the JavaFX Maven plugin:
     ```
     mvn clean javafx:run
     ```
     This launches [com.example.premierleaguedataaggregator.HelloApplication](http://_vscodecontentref_/0).
   - When the dashboard opens you are asked for the RMI server IP (use `localhost` when running server locally). The UI will connect to the RMI service and load categories.

Configuration

- API token for football-data.org is read from [config.properties](http://_vscodecontentref_/1) by [com.example.premierleaguedataaggregator.api.FootballDataService](http://_vscodecontentref_/2). If the token is missing or invalid the service will not return API results.
- News is fetched from Sky Sports RSS by [com.example.premierleaguedataaggregator.api.NewsService](http://_vscodecontentref_/3).
- Player images are looked up via TheSportsDB by [com.example.premierleaguedataaggregator.api.PlayerImageService](http://_vscodecontentref_/4).

Key files and responsibilities

- [com.example.premierleaguedataaggregator.rmi.PremierLeagueServiceImpl](http://_vscodecontentref_/5) — server implementation, formats data into pipe-delimited strings per category.
- [com.example.premierleaguedataaggregator.rmi.PremierLeagueService](http://_vscodecontentref_/6) — RMI interface used by client and server.
- [com.example.premierleaguedataaggregator.api.FootballDataService](http://_vscodecontentref_/7) — HTTP calls to football-data API.
- [com.example.premierleaguedataaggregator.DashboardController](http://_vscodecontentref_/8) — client logic: connects to RMI, requests data, parses and renders UI.
- UI cell renderers (e.g., [`RankingListCell[](src/main/java/com/example/premierleaguedataaggregator/RankingListCell.java), [](http://_vscodecontentref_/9)StandingListCell`](src/main/java/com/example/premierleaguedataaggregator/StandingListCell.java), etc.) convert pipe-delimited strings into styled list items.

Troubleshooting

- If the client cannot connect: ensure the RMI server is running and reachable at the IP you enter. Check firewall rules and that port 1099 is free.
- If match/standings data are missing: confirm your API token is present in [config.properties](http://_vscodecontentref_/10). The token is read by [FootballDataService](http://_vscodecontentref_/11).
- UI images may fail to load if external image URLs are unreachable; the UI classes try to fall back to defaults where available.

Notes & improvements

- Data formatting between server and client currently uses simple pipe-delimited strings. This is easy to parse but brittle; switching to a structured format (JSON) would be more robust.
- The RMI server fetches live data synchronously. Consider caching or background refresh for better performance.
- The project uses Java modules ([module-info.java](http://_vscodecontentref_/12)). If you change dependencies, update module declarations.

License

- This repository contains example code. No license file included.
