package com.muhamad_galal.earthquake.earthquakewatcher.Model;

import java.io.Serializable;

public class EarthQuake implements Serializable {

    private final static long serializable =10L;
    private String place;
    private double magnitude;
    private String detailLink;
    private String type;
    private long time;
    private double lat;
    private double lon;

    public EarthQuake(String place, double magnitude, String detailLink, String type, long time, double lat, double lon) {
        this.place = place;
        this.magnitude = magnitude;
        this.detailLink = detailLink;
        this.type = type;
        this.time = time;
        this.lat = lat;
        this.lon = lon;
    }

    public EarthQuake() {
    }

    public static long getSerializable() {
        return serializable;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}