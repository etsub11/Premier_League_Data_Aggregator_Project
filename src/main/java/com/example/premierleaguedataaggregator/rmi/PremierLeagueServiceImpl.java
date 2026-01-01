package com.example.premierleaguedataaggregator.rmi;

import com.example.premierleaguedataaggregator.api.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class PremierLeagueServiceImpl extends UnicastRemoteObject implements PremierLeagueService {

    private final Map<String, String> teamDetails;
    private final FootballDataService footballDataService;
    private final PlayerImageService playerImageService;
    private final NewsService newsService;

    public PremierLeagueServiceImpl() throws RemoteException {
        super();
        teamDetails = new HashMap<>();
        footballDataService = new FootballDataService();
        playerImageService = new PlayerImageService();
        newsService = new NewsService();
        initializeData();
    }

    private void initializeData() {
        teamDetails.put("Arsenal", "Stadium: Emirates Stadium\nManager: Mikel Arteta\nKey Player: Bukayo Saka");
        teamDetails.put("Manchester City", "Stadium: Etihad Stadium\nManager: Pep Guardiola\nKey Player: Kevin De Bruyne");
        teamDetails.put("Liverpool", "Stadium: Anfield\nManager: Jürgen Klopp\nKey Player: Mohamed Salah");
        teamDetails.put("Manchester United", "Stadium: Old Trafford\nManager: Erik ten Hag\nKey Player: Bruno Fernandes");
        teamDetails.put("Chelsea", "Stadium: Stamford Bridge\nManager: Mauricio Pochettino\nKey Player: Cole Palmer");
    }

    @Override
    public String getLeagueName() throws RemoteException {
        return "English Premier League 2024/25";
    }

    @Override
    public List<String> getTeamNames() throws RemoteException {
        return new ArrayList<>(teamDetails.keySet());
    }

    @Override
    public String getTeamDetails(String teamName) throws RemoteException {
        return teamDetails.getOrDefault(teamName, "Details not found for team: " + teamName);
    }

    @Override
    public String getDataForCategory(String category) throws RemoteException {
        System.out.println("DEBUG: getDataForCategory called for: " + category);
        if ("Matches".equalsIgnoreCase(category)) {
            return getMatchesData();
        } else if ("Standings".equalsIgnoreCase(category)) {
            return getStandingsData();
        } else if ("Info".equalsIgnoreCase(category)) {
            return getCompetitionInfoData();
        } else if ("Ranking".equalsIgnoreCase(category)) {
            return getTopScorersData();
        } else if ("News".equalsIgnoreCase(category)) {
            return getNewsData();
        } else if ("Coaches".equalsIgnoreCase(category)) {
            return getCoachesData();
        } else if ("Referees".equalsIgnoreCase(category)) {
            return getRefereesData();
        } else if ("Teams".equalsIgnoreCase(category)) {
            return getTeamsData();
        } else if ("Transfers".equalsIgnoreCase(category)) {
            return getTransfersData();
        } else if ("Stadiums".equalsIgnoreCase(category)) {
            return getStadiumsData();
        }
        return "Data for category '" + category + "' is not yet available. Please check back later.";
    }

    private String getMatchesData() {
        MatchData matchData = footballDataService.getMatches();
        if (matchData == null || matchData.getMatches() == null) {
            return "Could not retrieve match data.";
        }

        StringBuilder sb = new StringBuilder();
        for (Match match : matchData.getMatches()) {
            sb.append(match.getUtcDate()).append("|");
            sb.append(match.getStatus()).append("|");
            sb.append(match.getHomeTeam().getCrest()).append("|");
            sb.append(match.getHomeTeam().getName()).append("|");
            sb.append(match.getAwayTeam().getCrest()).append("|");
            sb.append(match.getAwayTeam().getName()).append("|");

            if (match.getScore() != null && match.getScore().getFullTime() != null) {
                Integer homeScore = match.getScore().getFullTime().getHome();
                Integer awayScore = match.getScore().getFullTime().getAway();
                sb.append(homeScore != null ? homeScore : "-").append("|");
                sb.append(awayScore != null ? awayScore : "-");
            } else {
                sb.append("-|-");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getStandingsData() {
        StandingsResponse response = footballDataService.getStandings();
        if (response == null || response.getStandings() == null) {
            return "Could not retrieve standings data.";
        }

        List<TableEntry> table = null;
        for (Standing standing : response.getStandings()) {
            if ("TOTAL".equals(standing.getType())) {
                table = standing.getTable();
                break;
            }
        }

        if (table == null) {
            return "No standings table found.";
        }

        StringBuilder sb = new StringBuilder();
        for (TableEntry entry : table) {
            sb.append(entry.getPosition()).append("|");
            sb.append(entry.getTeam().getCrest()).append("|");
            sb.append(entry.getTeam().getName()).append("|");
            sb.append(entry.getPlayedGames()).append("|");
            sb.append(entry.getWon()).append("|");
            sb.append(entry.getDraw()).append("|");
            sb.append(entry.getLost()).append("|");
            sb.append(entry.getPoints()).append("|");
            sb.append(entry.getGoalsFor()).append("|");
            sb.append(entry.getGoalsAgainst()).append("|");
            sb.append(entry.getGoalDifference());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getCompetitionInfoData() {
        CompetitionResponse response = footballDataService.getCompetitionInfo();
        if (response == null) {
            return "Could not retrieve competition info.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Name|").append(response.getName()).append("\n");
        sb.append("Code|").append(response.getCode()).append("\n");
        sb.append("Type|").append(response.getType()).append("\n");
        sb.append("Emblem|").append(response.getEmblem()).append("\n");

        if (response.getCurrentSeason() != null) {
            sb.append("Current Season Start|").append(response.getCurrentSeason().getStartDate()).append("\n");
            sb.append("Current Season End|").append(response.getCurrentSeason().getEndDate()).append("\n");
            sb.append("Current Matchday|").append(response.getCurrentSeason().getCurrentMatchday()).append("\n");
        }

        sb.append("Available Seasons|").append(response.getNumberOfAvailableSeasons()).append("\n");
        return sb.toString();
    }

    private String getTopScorersData() {
        ScorersResponse response = footballDataService.getTopScorers();
        if (response == null || response.getScorers() == null) {
            return "Could not retrieve top scorers data.";
        }

        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (ScorerEntry entry : response.getScorers()) {
            String playerName = entry.getPlayer().getName();
            String imageUrl = playerImageService.getPlayerImageUrl(playerName);
            if (imageUrl == null) imageUrl = "null";

            sb.append(rank++).append("|");
            sb.append(playerName).append("|");
            sb.append(entry.getTeam().getName()).append("|");
            sb.append(entry.getGoals()).append("|");
            sb.append(entry.getAssists() != null ? entry.getAssists() : 0).append("|");
            sb.append(entry.getPenalties() != null ? entry.getPenalties() : 0).append("|");
            sb.append(entry.getPlayedMatches() != null ? entry.getPlayedMatches() : 0).append("|");
            sb.append(imageUrl);
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getNewsData() {
        List<NewsItem> news = newsService.getLatestNews();
        if (news.isEmpty()) {
            return "Could not retrieve news.";
        }

        StringBuilder sb = new StringBuilder();
        for (NewsItem item : news) {
            sb.append(item.getTitle()).append("|");
            sb.append(item.getPubDate()).append("|");
            sb.append(item.getDescription()).append("|");
            sb.append(item.getLink());
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getCoachesData() {
        TeamsResponse response = footballDataService.getTeams();
        if (response == null || response.getTeams() == null) {
            return "Could not retrieve teams/coaches data.";
        }

        StringBuilder sb = new StringBuilder();
        for (Team team : response.getTeams()) {
            if (team.getCoach() != null) {
                sb.append(team.getCoach().getName()).append("|");
                sb.append(team.getCoach().getNationality()).append("|");
                sb.append(team.getName()).append("|");
                sb.append(team.getCrest());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String getRefereesData() {
        // Referees are typically associated with matches.
        // We will fetch recent matches to extract referee information.
        MatchData matchData = footballDataService.getMatches();
        if (matchData == null || matchData.getMatches() == null) {
            return "Could not retrieve referee data.";
        }

        Set<String> uniqueReferees = new HashSet<>();
        StringBuilder sb = new StringBuilder();

        for (Match match : matchData.getMatches()) {
            if (match.getReferees() != null) {
                for (Referee ref : match.getReferees()) {
                    String key = ref.getName() + "|" + ref.getNationality();
                    if (!uniqueReferees.contains(key)) {
                        uniqueReferees.add(key);
                        sb.append(ref.getName()).append("|");
                        sb.append(ref.getNationality() != null ? ref.getNationality() : "Unknown");
                        sb.append("\n");
                    }
                }
            }
        }

        if (sb.length() == 0) {
            return "No referee data available for current matches.";
        }
        return sb.toString();
    }

    private String getTeamsData() {
        TeamsResponse response = footballDataService.getTeams();
        if (response == null || response.getTeams() == null) {
            return "Could not retrieve teams data.";
        }

        StringBuilder sb = new StringBuilder();
        for (Team team : response.getTeams()) {
            // Format: Name|Crest|Founded|Colors|Website
            sb.append(team.getName()).append("|");
            sb.append(team.getCrest()).append("|");
            sb.append(team.getFounded() != null ? team.getFounded() : "Unknown").append("|");
            sb.append(team.getClubColors() != null ? team.getClubColors() : "Unknown").append("|");
            sb.append(team.getWebsite() != null ? team.getWebsite() : "");
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getStadiumsData() {
        TeamsResponse response = footballDataService.getTeams();
        if (response == null || response.getTeams() == null) {
            return "Could not retrieve stadium data.";
        }

        StringBuilder sb = new StringBuilder();
        for (Team team : response.getTeams()) {
            // Format: StadiumName|TeamName|TeamCrest
            if (team.getVenue() != null) {
                sb.append(team.getVenue()).append("|");
                sb.append(team.getName()).append("|");
                sb.append(team.getCrest());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private String getTransfersData() {
        List<NewsItem> news = newsService.getLatestNews();
        if (news.isEmpty()) {
            return "Could not retrieve transfer news.";
        }

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (NewsItem item : news) {
            String title = item.getTitle().toLowerCase();
            if (title.contains("transfer") || title.contains("sign") || title.contains("deal") || title.contains("loan")) {
                sb.append(item.getTitle()).append("|");
                sb.append(item.getPubDate()).append("|");
                sb.append(item.getDescription()).append("|");
                sb.append(item.getLink());
                sb.append("\n");
                found = true;
            }
        }

        if (!found) {
            return "No recent transfer news found.";
        }
        return sb.toString();
    }
}
