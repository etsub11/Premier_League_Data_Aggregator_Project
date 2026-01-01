package com.example.premierleaguedataaggregator;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Match {
    private final String homeTeam;
    private final String awayTeam;
    private final LocalDateTime dateTime;
    private final String stadium; // This might not be available directly from the new format
    private final String score;
    private final String homeTeamLogoUrl;
    private final String awayTeamLogoUrl;
    private final String status;

    public Match(String homeTeam, String awayTeam, LocalDateTime dateTime, String stadium, String score, String homeTeamLogoUrl, String awayTeamLogoUrl, String status) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.dateTime = dateTime;
        this.stadium = stadium;
        this.score = score;
        this.homeTeamLogoUrl = homeTeamLogoUrl;
        this.awayTeamLogoUrl = awayTeamLogoUrl;
        this.status = status;
    }

    public static Match fromString(String matchString) {
        String[] parts = matchString.split("\\|");
        if (parts.length < 8) {
            return null; // Or handle error appropriately
        }

        String utcDate = parts[0];
        String status = parts[1];
        String homeLogo = parts[2];
        String homeTeam = parts[3];
        String awayLogo = parts[4];
        String awayTeam = parts[5];
        String homeScore = parts[6];
        String awayScore = parts[7];

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dateTime = ZonedDateTime.parse(utcDate, formatter).toLocalDateTime();

        String score = homeScore + " - " + awayScore;
        String stadium = "Stadium info not available"; // Placeholder

        return new Match(homeTeam, awayTeam, dateTime, stadium, score, homeLogo, awayLogo, status);
    }

    // Getters
    public String getHomeTeam() { return homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getStadium() { return stadium; }
    public String getScore() { return score; }
    public String getHomeTeamLogoUrl() { return homeTeamLogoUrl; }
    public String getAwayTeamLogoUrl() { return awayTeamLogoUrl; }
    public String getStatus() { return status; }

    public String getMonthYear() {
        return dateTime.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    }
}
