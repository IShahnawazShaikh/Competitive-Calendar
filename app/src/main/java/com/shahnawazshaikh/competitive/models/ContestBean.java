package com.shahnawazshaikh.competitive.models;

public class ContestBean {
   private String start;
   private String end;
   private String event;
   private String duration;
   private String id;
   private String icon;
   private String contest_site;
    private  String href;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ContestBean(String start, String end, String event, String duration, String id, String href, String icon, String conteset_site) {
        this.start = start;
        this.end = end;
        this.event = event;
        this.duration = duration;
        this.id = id;
        this.icon = icon;
        this.contest_site=conteset_site;
        this.href=href;
    }
    public String getContest_site() {
        return contest_site;
    }

    public void setContest_site(String contest_site) {
        this.contest_site = contest_site;
    }


    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getHref() { return href; }

    public void setHref(String href) { this.href = href; }

}
