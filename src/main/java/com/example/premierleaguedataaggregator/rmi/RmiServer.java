package com.example.premierleaguedataaggregator.rmi;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;

public class RmiServer {
    public static void main(String[] args) {
        try {
            // Get the server's local IP address
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            
            // Set the hostname property for RMI
            System.setProperty("java.rmi.server.hostname", ipAddress);
            
            System.out.println("Starting RMI Server on IP: " + ipAddress + ", Port: 1099...");
            
            Registry registry = LocateRegistry.createRegistry(1099);
            
            PremierLeagueService service = new PremierLeagueServiceImpl();
            
            registry.rebind("PremierLeagueService", service);
            
            System.out.println("RMI Server is running and service is bound.");
            System.out.println("Client can connect using IP: " + ipAddress);
            
        } catch (ExportException e) {
            System.err.println("FATAL: Port 1099 is already in use.");
            System.err.println("Another RMI registry or a previous instance of this server is likely running.");
            System.err.println("Please stop the other process. In your IDE, you can usually stop all running processes from the 'Run' or 'Debug' tabs.");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred on the RMI Server:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
