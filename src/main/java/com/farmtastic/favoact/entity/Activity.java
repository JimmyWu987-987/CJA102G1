package com.farmtastic.favoact.entity;

public class Activity {
    private Long id;
    private String name;
    private String date;
    private String icon;

    public Activity() {}
    public Activity(Long id, String name, String date, String icon){
        this.id = id;
        this.name = name;
        this.date = date;
        this.icon = icon;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
}
