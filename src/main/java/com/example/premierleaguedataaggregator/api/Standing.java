package com.example.premierleaguedataaggregator.api;

import java.util.List;

public class Standing {
    private String type; // e.g., "TOTAL"
    private List<TableEntry> table;

    public String getType() {
        return type;
    }

    public List<TableEntry> getTable() {
        return table;
    }
}
