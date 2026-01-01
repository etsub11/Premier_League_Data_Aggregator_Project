package com.example.premierleaguedataaggregator.api;

public class Team {
    private String name;
    private String crest;
    private Coach coach;
    private String venue; // Stadium name
    private String website;
    private Integer founded;
    private String clubColors;

    public String getName() { return name; }
    public String getCrest() { return crest; }
    public Coach getCoach() { return coach; }
    public String getVenue() { return venue; }
    public String getWebsite() { return website; }
    public Integer getFounded() { return founded; }
    public String getClubColors() { return clubColors; }
}
