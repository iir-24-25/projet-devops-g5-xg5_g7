package com.example.pharmacie;



public class StatItem {
    private String title;
    private String value;
    private int iconRes;
    private int color;

    public StatItem(String title, String value, int iconRes, int color) {
        this.title = title;
        this.value = value;
        this.iconRes = iconRes;
        this.color = color;
    }

    // Getters
    public String getTitle() { return title; }
    public String getValue() { return value; }
    public int getIconRes() { return iconRes; }
    public int getColor() { return color; }
}
