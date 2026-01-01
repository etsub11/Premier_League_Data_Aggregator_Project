package com.example.premierleaguedataaggregator.api;

public class ScorerEntry {
    private Player player;
    private Team team;
    private int goals;
    private Integer assists; // Can be null
    private Integer penalties; // Can be null
    private Integer playedMatches; // Added field

    public Player getPlayer() { return player; }
    public Team getTeam() { return team; }
    public int getGoals() { return goals; }
    public Integer getAssists() { return assists; }
    public Integer getPenalties() { return penalties; }
    public Integer getPlayedMatches() { return playedMatches; }
}
