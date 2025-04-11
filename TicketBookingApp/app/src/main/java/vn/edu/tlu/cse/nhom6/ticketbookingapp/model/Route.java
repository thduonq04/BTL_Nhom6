package vn.edu.tlu.cse.nhom6.ticketbookingapp.model;

public class Route {
    private int route_id;
    private String start_location;
    private String end_location;
    private int distance;

    public Route(int route_id, String start_location, String end_location, int distance) {
        this.route_id = route_id;
        this.start_location = start_location;
        this.end_location = end_location;
        this.distance = distance;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getStart_location() {
        return start_location;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public String getEnd_location() {
        return end_location;
    }

    public void setEnd_location(String end_location) {
        this.end_location = end_location;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
