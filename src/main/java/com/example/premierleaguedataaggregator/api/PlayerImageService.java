package com.example.premierleaguedataaggregator.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PlayerImageService {
    
    // Simple in-memory cache to avoid repeated API calls
    private final Map<String, String> imageCache = new HashMap<>();

    public String getPlayerImageUrl(String playerName) {
        // Check cache first
        if (imageCache.containsKey(playerName)) {
            return imageCache.get(playerName);
        }

        String imageUrl = fetchFromApi(playerName);
        // Cache the result (even if null, to avoid re-fetching failed lookups)
        imageCache.put(playerName, imageUrl);
        return imageUrl;
    }

    private String fetchFromApi(String playerName) {
        try {
            // Using TheSportsDB free test API key "3"
            String encodedName = URLEncoder.encode(playerName, StandardCharsets.UTF_8);
            URL url = new URL("https://www.thesportsdb.com/api/v1/json/3/searchplayers.php?p=" + encodedName);
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Scanner scanner = new Scanner(conn.getInputStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();

                Gson gson = new Gson();
                JsonObject json = gson.fromJson(response, JsonObject.class);
                
                if (json.has("player") && !json.get("player").isJsonNull()) {
                    JsonArray players = json.getAsJsonArray("player");
                    if (players.size() > 0) {
                        JsonObject player = players.get(0).getAsJsonObject();
                        // Try thumbnail first
                        if (player.has("strThumb") && !player.get("strThumb").isJsonNull()) {
                            String thumb = player.get("strThumb").getAsString();
                            if (!thumb.isEmpty()) return thumb;
                        }
                        // Fallback to cutout
                        if (player.has("strCutout") && !player.get("strCutout").isJsonNull()) {
                            String cutout = player.get("strCutout").getAsString();
                            if (!cutout.isEmpty()) return cutout;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Failed to fetch image for " + playerName);
            e.printStackTrace();
        }
        return null; // No image found
    }
}
