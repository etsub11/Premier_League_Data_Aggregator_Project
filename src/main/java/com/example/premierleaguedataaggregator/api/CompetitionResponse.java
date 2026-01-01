package com.example.premierleaguedataaggregator.api;

public class CompetitionResponse {
    private String name;
    private String code;
    private String type;
    private String emblem;
    private Season currentSeason;
    private int numberOfAvailableSeasons;

    public String getName() { return name; }
    public String getCode() { return code; }
    public String getType() { return type; }
    public String getEmblem() { return emblem; }
    public Season getCurrentSeason() { return currentSeason; }
    public int getNumberOfAvailableSeasons() { return numberOfAvailableSeasons; }
}
