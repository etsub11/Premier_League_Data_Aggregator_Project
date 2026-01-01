package com.example.premierleaguedataaggregator.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsService {

    private static final String RSS_URL = "https://www.skysports.com/rss/12040"; // Sky Sports Premier League RSS

    public List<NewsItem> getLatestNews() {
        List<NewsItem> newsList = new ArrayList<>();
        try {
            URL url = new URL(RSS_URL);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(url.openStream());
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("item");

            // Limit to top 10 news items
            int limit = Math.min(nodeList.getLength(), 10);

            for (int i = 0; i < limit; i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    
                    String title = getTagValue("title", element);
                    String description = getTagValue("description", element);
                    String link = getTagValue("link", element);
                    String pubDate = getTagValue("pubDate", element);

                    // Clean up description if it contains HTML
                    if (description != null) {
                        description = description.replaceAll("\\<.*?\\>", "").trim();
                    }

                    newsList.add(new NewsItem(title, description, link, pubDate));
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching news: " + e.getMessage());
            e.printStackTrace();
        }
        return newsList;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = nodeList.item(0);
        return node != null ? node.getNodeValue() : "";
    }
}
