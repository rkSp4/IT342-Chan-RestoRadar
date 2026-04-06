package edu.cit.chan.restoradar.dto;

public class SearchRequest {
    private String query;
    private double lat;
    private double lng;
    private double radiusInKm;
    private String priceRange;
    private int page;
    private int size;

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public double getRadiusInKm() { return radiusInKm; }
    public void setRadiusInKm(double radiusInKm) { this.radiusInKm = radiusInKm; }

    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}
