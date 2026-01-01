package com.example.premierleaguedataaggregator.api;

public class Score {
    private ScoreDetail fullTime;

    public ScoreDetail getFullTime() {
        return fullTime;
    }

    public static class ScoreDetail {
        // Updated to match v4 API structure
        private Integer home;
        private Integer away;

        public Integer getHome() { return home; }
        public Integer getAway() { return away; }
    }
}
