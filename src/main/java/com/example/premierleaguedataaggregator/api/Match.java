package com.example.premierleaguedataaggregator.api;

import java.util.List;

public class Match {
    private Team homeTeam;
    private Team awayTeam;
    private Score score;
    private String status;
    private String utcDate;
    private List<Referee> referees; // Added referees list

    public Team getHomeTeam() { return homeTeam; }
    public Team getAwayTeam() { return awayTeam; }
    public Score getScore() { return score; }
    public String getStatus() { return status; }
    public String getUtcDate() { return utcDate; }
    public List<Referee> getReferees() { return referees; }
}
