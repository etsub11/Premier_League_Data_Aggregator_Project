package com.example.premierleaguedataaggregator.api;

import com.google.gson.Gson;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;

public class FootballDataService {

    private String apiToken;

    public FootballDataService() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("DEBUG: config.properties file not found in classpath.");
                this.apiToken = null;
                return;
            }
            props.load(input);
            this.apiToken = props.getProperty("api.token");
            System.out.println("DEBUG: API Token loaded: " + (this.apiToken != null ? "Yes" : "No"));
        } catch (Exception e) {
            System.out.println("DEBUG: Exception loading config.properties");
            e.printStackTrace();
            this.apiToken = null;
        }
    }

    public MatchData getMatches() {
        return fetchData("https://api.football-data.org/v4/competitions/PL/matches", MatchData.class);
    }

    public StandingsResponse getStandings() {
        return fetchData("https://api.football-data.org/v4/competitions/PL/standings", StandingsResponse.class);
    }

    public CompetitionResponse getCompetitionInfo() {
        return fetchData("https://api.football-data.org/v4/competitions/PL", CompetitionResponse.class);
    }

    public ScorersResponse getTopScorers() {
        return fetchData("https://api.football-data.org/v4/competitions/PL/scorers", ScorersResponse.class);
    }

    public TeamsResponse getTeams() {
        return fetchData("https://api.football-data.org/v4/competitions/PL/teams", TeamsResponse.class);
    }

    private <T> T fetchData(String urlString, Class<T> responseType) {
        if (apiToken == null) {
            System.out.println("DEBUG: API token is null. Cannot make request.");
            return null;
        }

        try {
            URL url = new URL(urlString);
            System.out.println("DEBUG: Requesting URL: " + url);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Auth-Token", apiToken);

            int responseCode = conn.getResponseCode();
            System.out.println("DEBUG: API Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(conn.getInputStream());
                String jsonResponse = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                Gson gson = new Gson();
                return gson.fromJson(jsonResponse, responseType);
            } else {
                System.out.println("DEBUG: Failed to get data. Response message: " + conn.getResponseMessage());
                return null;
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Exception during API request");
            e.printStackTrace();
            return null;
        }
    }
}
