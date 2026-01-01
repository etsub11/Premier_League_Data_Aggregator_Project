package com.example.premierleaguedataaggregator.api;

public class Season {
    private int id;
    private String startDate;
    private String endDate;
    private int currentMatchday;
    private Team winner;

    public int getId() { return id; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public int getCurrentMatchday() { return currentMatchday; }
    public Team getWinner() { return winner; }
}
