package com.example.premierleaguedataaggregator.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PremierLeagueService extends Remote {

    /**
     * Gets the name of the league.
     * @return The league name.
     * @throws RemoteException
     */
    String getLeagueName() throws RemoteException;
    
    /**
     * Fetches a descriptive string for a given data category.
     * @param category The name of the category (e.g., "Matches", "Standings").
     * @return A string containing the data for that category.
     * @throws RemoteException if a communication-related error occurs.
     */
    String getDataForCategory(String category) throws RemoteException;

    /**
     * Legacy method to get a list of team names.
     * @return A list of team names.
     * @throws RemoteException
     */
    List<String> getTeamNames() throws RemoteException;

    /**
     * Legacy method to get details for a specific team.
     * @param teamName The name of the team.
     * @return A string with team details.
     * @throws RemoteException
     */
    String getTeamDetails(String teamName) throws RemoteException;
}